package dubraz.vendeur;

import java.util.ArrayList;
import java.util.List;

import utilities.OneMessageBehaviour;
import utilities.Protocol;

import jade.core.Agent;

public class Vendeur extends Agent {

	private static final long serialVersionUID = 1L;
	private Float _amount;
	private Float _minAmount;
	private Float _stepAmount;
	private Long _timer;
	private List<String> _namesClients;
	private final String _marcketName = "marche";
	private VendeurInterface _gui;
	private boolean _announcing;
	
	public Float getAmount() {
		return _amount;
	}
	
	public Float getMinAmount() {
		return _minAmount;
	}
	
	public Float getStepAmount() {
		return _stepAmount;
	}
	
	public Long getTimer() {
		return _timer;
	}
	
	public void setAmount(Float a) {
		_amount = a;
	}
	
	public void setMinAmount(Float a) {
		_minAmount = a;
	}
	
	public void setStepAmount(Float a) {
		_stepAmount = a;
	}
	
	public void setTimer(Long t) {
		_timer = t;
	}
	
	public void addClient(String cl) {
		if(!_namesClients.contains(cl))
			_namesClients.add(cl);
		_gui.update();
	}
	
	public String getClient(int i) {
		if(getNbClients()>0 && i<getNbClients() && i>=0)
			return _namesClients.get(i);
		else
			return null;
	}
	
	public int getNbClients(){
		return _namesClients.size();
	}
	
	public String getMarcket() {
		return _marcketName;
	}
	
	public void setAnnouncing(boolean a) {
		_announcing = a;
	}
	
	public boolean getAnnouncing() {
		return _announcing;
	}
	
	//Constructeur de l'agent
	protected void setup() {
		System.out.println("Hello! Vendeur-agent "+getAID().getName()+" is ready.");
		
		_amount = new Float(0);
		_timer = new Long(0);
		_announcing = false;
		_namesClients = new ArrayList<String>();
		_gui = new VendeurInterface(this);

		//Requête de souscription au marché
		String[] receiver = new String[] {_marcketName};
		addBehaviour(new OneMessageBehaviour(this, receiver, Protocol.TO_CREATE, "vendeur"));
		
		//Comportement d'écoute de message
		addBehaviour(new ReceiveVendeurBehaviour(this));
	}
	
	//Destructeur
	protected void takeDown() {
		_gui.dispose();
        System.out.println("Vendeur-agent "+getAID().getName()+" terminating.");
        super.takeDown();
    }
	
	//Appel du destructeur
	public void stop() {
		doDelete();
	}
	
	//Publication d'une offre et attente d'enchère
	public void announce() {
		_gui.update();
		_announcing = true;
		String[] receiver = new String[] {_marcketName};
		addBehaviour( new OneMessageBehaviour(this, receiver, Protocol.TO_ANNOUNCE, _amount.toString()));
		addBehaviour(new WaitBehaviour(this));
		addBehaviour(new ProposalVendeurBehaviour(this, true));
	}
	
	//Une enchère reçu aprés timeout, on relance le timeout pour une autre enchère.
	public void waitOtherBids() {
		addBehaviour(new WaitBehaviour(this));
		addBehaviour(new ProposalVendeurBehaviour(this, false));
	}
	
	//Attribuer le gagnant de l'enchère
	public void attribute() {
		_announcing = false;
		String[] receiver = new String[] {_marcketName};
		addBehaviour(new OneMessageBehaviour(this, receiver, Protocol.TO_ATTRIBUTE, getClient(0)));
	}
	
	//Envoi du bien
	public void give() {
		String[] receiver = new String[] {_marcketName};
		addBehaviour(new OneMessageBehaviour(this, receiver, Protocol.TO_GIVE, getClient(0)));
	}
	
	//Réception du paiement - fin del'offre
	public void payment(String clName) {
		_gui.InfoMessage("Paiement reçu de " + clName + ".");
		reset();
	}

	//Prix minimum atteint - fin de l'offre
	public void noBids() {
		_announcing = false;
		_gui.InfoMessage("Prix minimum atteint et aucune enchère!");
		String[] receiver = new String[] {_marcketName};
		addBehaviour( new OneMessageBehaviour(this, receiver, Protocol.TO_ANNOUNCE, new Float(-1).toString()));
		reset();
	}
	
	//Remise à zéro aprés la fin d'une offre
	public void reset() {
		_amount = new Float(0);
		_timer = new Long(0);
		_minAmount = new Float(0);
		_stepAmount = new Float(0);
		_namesClients.clear();
		_gui.reset();
	}

	//Décliner une enchère
	public void decline(String buyer) {
		String[] receiver = new String[] {_marcketName};
		addBehaviour(new OneMessageBehaviour(this, receiver, Protocol.TO_DECLINE, buyer));
	}

	//Décliner toute les enchères
	public void declineAll() {
		String[] receiver = new String[] {_marcketName};
		for(int i=0; i<getNbClients(); i++)
			addBehaviour(new OneMessageBehaviour(this, receiver, Protocol.TO_DECLINE, getClient(i)));
	}
	
}
