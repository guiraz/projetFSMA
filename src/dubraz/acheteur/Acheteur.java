package dubraz.acheteur;

import java.util.ArrayList;
import java.util.List;

import utilities.*;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.ThreadedBehaviourFactory;

public class Acheteur extends Agent {

	private static final long serialVersionUID = 1L;
	
	private boolean _automatique, _pay, _give;
	private Float _defaultAmount;
	private List<Offer> _offers;
	private final String _marcketName = "marche";
	private Offer _offerBid;
	private ThreadedBehaviourFactory _tbf;
	
	private AcheteurInterface _gui;
	
	public void setup() {
		System.out.println("Hello! Acheteur-agent "+getAID().getName()+" is ready.");
		
		_offers = new ArrayList<Offer>();
		_offerBid = null;
		
		_gui = new AcheteurInterface(this);
		
		_automatique = _gui.getBidProcess();
		_pay = false;
		_give = false;
		
		if(_automatique){
			_defaultAmount = _gui.getDefaultAmount();
			_gui.setLabelAmount();
		}
		
		String[] receivers = new String[] {_marcketName};
		addBehaviour(new OneMessageBehaviour(this, receivers, Protocol.TO_CREATE, "client"));
		
		_tbf = new ThreadedBehaviourFactory();
		addBehaviour(_tbf.wrap(new ReceiveAcheteurBehaviour(this)));
	}

	protected void takeDown() {
		_gui.dispose();
        System.out.println("Acheteur-agent "+getAID().getName()+" terminating.");
        super.takeDown();
    }
	
	public void stop() {
		String[] receivers = new String[] {_marcketName};
		addBehaviour(_tbf.wrap(new OneMessageBehaviour(this, receivers, Protocol.TO_KILL, "client")));
		_tbf.interrupt();
		doDelete();
	}
	
	public boolean isAutomatique() {
		return _automatique;
	}
	
	public List<Offer> getOffers() {
		return _offers;
	}
	
	public void addOffer(ACLMessage msg) {
		Offer offer = Offer.fromACLMessage(msg.getContent());
		
		if(_offers.contains(offer)) {
			int index = _offers.indexOf(offer);
			if(offer.getAmount() < 0)
				_offers.remove(index);
			else
				_offers.get(index).setAmount(offer.getAmount());
		}
		else {
			_offers.add(offer);
		}
		_gui.ressourcesUpdated();
		
		if(_automatique)
			autoBid();
	}
	
	public Offer getOfferBid() {
		return _offerBid;
	}
	
	public void setOfferBid(Offer offer) {
		_offerBid = offer;
	}
	
	public Float getDefaultAmount() {
		return _defaultAmount;
	}

	public void give(String seller) {
		_gui.InfoMessage("Vous avez reçu un bien de "+ seller + ".");
		_give = true;
		bidFinished();
	}

	public void decline() {
		_offerBid = null;
		if(!_automatique)
			_gui.InfoMessage("Votre offre a été décliné.");
		_gui.ressourcesUpdated();
	}

	public void attribute(String seller) {
		_gui.InfoMessage("Vous avez gagné l'enchère de " + seller + ".");
		String[] receiver = new String[] {_marcketName};
		String mess = _offerBid.toACLMessage();
		addBehaviour(_tbf.wrap(new OneMessageBehaviour(this, receiver, Protocol.TO_PAY, mess)));
		_pay = true;
		bidFinished();
	}

	public void bid() {
		String[] receiver = new String[] {_marcketName};
		String mess = _offerBid.toACLMessage();
		addBehaviour(new OneMessageBehaviour(this, receiver, Protocol.TO_BID, mess));
	}

	private void bidFinished() {
		if(_pay && _give)
			stop();
	}
	
	private void autoBid(){
		if(_offerBid == null) {
			int i=0;
			while(i<_offers.size() && _offerBid == null){
				if(_offers.get(i).getAmount() <= _defaultAmount)
					_offerBid = _offers.get(i);
				else
					i++;
			}
			if(_offerBid != null)
				bid();
		}
	}
}
