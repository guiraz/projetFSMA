package dubraz.acheteur;

import java.util.ArrayList;
import java.util.List;

import utilities.*;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.ThreadedBehaviourFactory;

//buying agent implementation
public class Acheteur extends Agent {

	private static final long serialVersionUID = 1L;
	
	//boolean which defines the type of bider (manuel, auto)
	//boolean which defines if the payment and the goods have been sent/receive
	private boolean _automatique, _pay, _give;
	//defines the maximum amount for the automatic bider
	private Float _defaultAmount;
	//holds all the offers pending
	private List<Offer> _offers;
	//holds the name of the market
	private final String _marketName = "marche";
	//holds the offer which the current agent is bidding on
	private Offer _offerBid;
	//the threaded behaviour factory
	private ThreadedBehaviourFactory _tbf;
	
	//parent agent
	private AcheteurInterface _gui;
	
	public void setup() {
		System.out.println("Hello! Acheteur-agent "+getAID().getName()+" is ready.");
		
		_offers = new ArrayList<Offer>();
		_offerBid = null;
		
		_gui = new AcheteurInterface(this);
		
		//asking the user the type of bider (manuel / auto)
		_automatique = _gui.getBidProcess();
		_pay = false;
		_give = false;
		
		//get the maximum amount for the automatic bider
		if(_automatique){
			_defaultAmount = _gui.getDefaultAmount();
			_gui.setLabelAmount();
		}
		
		//subscribe to the marcket
		String[] receivers = new String[] {_marketName};
		addBehaviour(new OneMessageBehaviour(this, receivers, Protocol.TO_CREATE, "client"));
		
		//creating the TBF and launch the message receival behaviour
		_tbf = new ThreadedBehaviourFactory();
		addBehaviour(_tbf.wrap(new ReceiveAcheteurBehaviour(this)));
	}

	protected void takeDown() {
		_gui.dispose();
        System.out.println("Acheteur-agent "+getAID().getName()+" terminating.");
        super.takeDown();
    }
	
	//unsubscribe to the marcket, interrupt the TBF and kill the agent
	public void stop() {
		String[] receivers = new String[] {_marketName};
		addBehaviour(_tbf.wrap(new OneMessageBehaviour(this, receivers, Protocol.TO_KILL, "client")));
		_tbf.interrupt();
		doDelete();
	}
	
	//return the type of bider
	public boolean isAutomatique() {
		return _automatique;
	}
	
	//get all the offers
	public List<Offer> getOffers() {
		return _offers;
	}
	
	//add an offer to the list of all offers
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
	
	//get the current offer the agent is bidding on
	public Offer getOfferBid() {
		return _offerBid;
	}
	
	//set the current offer the agent is bidding on
	public void setOfferBid(Offer offer) {
		_offerBid = offer;
	}
	
	//get the maximum amount of the automatic bider
	public Float getDefaultAmount() {
		return _defaultAmount;
	}

	//give performative action
	public void give(String seller) {
		_gui.InfoMessage("Vous avez reçu un bien de "+ seller + ".");
		_give = true;
		bidFinished();
	}

	//decline performative action
	public void decline() {
		_offerBid = null;
		if(!_automatique)
			_gui.InfoMessage("Votre offre a été décliné.");
		_gui.ressourcesUpdated();
	}

	//attribute performative action
	public void attribute(String seller) {
		_gui.InfoMessage("Vous avez gagné l'enchère de " + seller + ".");
		String[] receiver = new String[] {_marketName};
		String mess = _offerBid.toACLMessage();
		addBehaviour(_tbf.wrap(new OneMessageBehaviour(this, receiver, Protocol.TO_PAY, mess)));
		_pay = true;
		bidFinished();
	}

	//bid performative action
	public void bid() {
		String[] receiver = new String[] {_marketName};
		String mess = _offerBid.toACLMessage();
		addBehaviour(new OneMessageBehaviour(this, receiver, Protocol.TO_BID, mess));
	}

	//if payment and goods are sent/receive stop the bid
	private void bidFinished() {
		if(_pay && _give)
			stop();
	}
	
	//action of the automatic bidder
	//if no bid and one offer's value lower then defaultValue bid on this offer 
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
