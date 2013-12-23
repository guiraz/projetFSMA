package dubraz.acheteur;

import java.util.ArrayList;
import java.util.List;

import utilities.OneMessageBehaviour;
import utilities.Protocol;

import jade.core.Agent;
import jade.core.behaviours.ThreadedBehaviourFactory;

public class Acheteur extends Agent {

	private static final long serialVersionUID = 1L;
	
	private boolean _automatique, _pay, _give;
	private Float _defaultAmount;
	private List<String> _sellersNames;
	private List<Float> _amounts;
	private final String _marcketName = "marche";
	private String _sellerBid;
	private ThreadedBehaviourFactory _tbf;
	
	private AcheteurInterface _gui;
	
	public void setup() {
		System.out.println("Hello! Acheteur-agent "+getAID().getName()+" is ready.");
		
		_sellersNames = new ArrayList<String>();
		_amounts = new ArrayList<Float>();
		_sellerBid = null;
		
		_gui = new AcheteurInterface(this);
		
		_automatique = _gui.getBidProcess();
		_pay = false;
		_give = false;
		
		if(_automatique)
			_defaultAmount = _gui.getDefaultAmount();
		
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
		_tbf.interrupt();
		String[] receivers = new String[] {_marcketName};
		addBehaviour(new OneMessageBehaviour(this, receivers, Protocol.TO_KILL, "client"));
		doDelete();
	}
	
	public boolean isAutomatique() {
		return _automatique;
	}
	
	public List<String> getSellersNames() {
		return _sellersNames;
	}
	
	public List<Float> getAmounts() {
		return _amounts;
	}
	
	public void addOffer(String v, Float a) {
		if(_sellersNames.contains(v)) {
			_amounts.set(_sellersNames.indexOf(v), a);
		}
		else {
			_sellersNames.add(v);
			_amounts.add(a);
		}
		_gui.ressourcesUpdated();
	}
	
	public String getSellerBid() {
		return _sellerBid;
	}
	
	public void setSellerBid(String sb) {
		_sellerBid = new String(sb);
	}

	public void give(String seller) {
		_gui.InfoMessage("Vous avez reçu un bien de "+ seller + ".");
		_give = true;
		bidFinished();
	}

	public void decline() {
		if(_automatique) {
		}
		else {
			_gui.InfoMessage("Votre offre a été décliné.");
		}
		_sellerBid = null;
		_gui.ressourcesUpdated();
	}

	public void attribute(String seller) {
		_gui.InfoMessage("Vous avez gagné l'enchère de " + seller + ".");
		String[] receiver = new String[] {_marcketName};
		addBehaviour(_tbf.wrap(new OneMessageBehaviour(this, receiver, Protocol.TO_PAY, _sellerBid)));
		_pay = true;
		bidFinished();
	}

	public void bid() {
		String[] receiver = new String[] {_marcketName};
		addBehaviour(new OneMessageBehaviour(this, receiver, Protocol.TO_BID, _sellerBid));
	}

	private void bidFinished() {
		if(_pay && _give)
			stop();
	}
}
