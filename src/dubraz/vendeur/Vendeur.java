package dubraz.vendeur;

import java.util.ArrayList;
import java.util.List;

import utilities.*;

import jade.core.Agent;
import jade.core.behaviours.ThreadedBehaviourFactory;

public class Vendeur extends Agent {

	private static final long serialVersionUID = 1L;
	private Float _amount;
	private Float _minAmount;
	private Float _stepAmount;
	private Long _timer;
	
	private List<Offer> _offers;
	private List<List<String> > _namesClients;
	private List<OfferBehaviour> _offerBehaviours;
	
	private final String _marcketName = "marche";
	private VendeurInterface _gui;
	private ThreadedBehaviourFactory _tbf;
	
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
	
	public int getOfferIndex(Offer offer) {
		return _offers.indexOf(offer);
	}

	public List<String> getClients(Offer offer) {
		int index = getOfferIndex(offer);
		if(index >= 0)
			return _namesClients.get(index);
		else
			return null;
	}
	
	public void addClient(String client, Offer offer) {
		int index = _offers.indexOf(offer);
		if(index >= 0) {
			_namesClients.get(index).add(client);
		}
	}
	
	public OfferBehaviour getOfferBehaviour(Offer offer) {
		int index = getOfferIndex(offer);
		if(index >= 0)
			return _offerBehaviours.get(index);
		else
			return null;
	}
	
	public String getMarcket() {
		return _marcketName;
	}
	
	//Constructeur de l'agent
	protected void setup() {
		System.out.println("Hello! Vendeur-agent "+getAID().getName()+" is ready.");
		
		_amount = new Float(0);
		_timer = new Long(0);
		_offers = new ArrayList<Offer>();
		_namesClients = new ArrayList<List <String> >();
		_offerBehaviours = new ArrayList<OfferBehaviour>();
		_tbf = new ThreadedBehaviourFactory();
		_gui = new VendeurInterface(this);

		//Requête de souscription au marché
		String[] receiver = new String[] {_marcketName};
		addBehaviour(new OneMessageBehaviour(this, receiver, Protocol.TO_CREATE, "vendeur"));
		
		addBehaviour(_tbf.wrap(new ReceiveVendeurBehaviour(this)));
	}
	
	//Destructeur
	protected void takeDown() {
        System.out.println("Vendeur-agent "+getAID().getName()+" terminating.");
        super.takeDown();
    }
	
	//Appel du destructeur
	public void stop() {
		String[] receivers = new String[] {_marcketName};
		addBehaviour(_tbf.wrap(new OneMessageBehaviour(this, receivers, Protocol.TO_KILL, "vendeur")));
		_tbf.interrupt();
		_gui.dispose();
		doDelete();
	}
	
	//Annoncer une offre
	public void announce() {
		Offer offer = new Offer();
		offer.setSellerName(getLocalName());
		offer.setAmount(_amount);
		offer.setOfferName(offerNameGenerator());
		_offers.add(offer);
		_namesClients.add(new ArrayList<String>());
		_offerBehaviours.add(new OfferBehaviour(this, offer));
		
		addBehaviour(_tbf.wrap(_offerBehaviours.get(getOfferIndex(offer))));
	}

	private String offerNameGenerator() {
		Integer i = 0;
		String name = null;
		while(name == null) {
			name = getLocalName() + "-" + i.toString();
			Offer offer = new Offer();
			offer.setOfferName(name);
			offer.setSellerName(getLocalName());
			if(_offers.contains(offer)) {
				name = null;
				i++;
			}
		}
		return name;
	}

	public void endOffer(Offer offer) {
		int index = _offers.indexOf(offer);
		
		String[] receiver = new String[] {_marcketName};
		for(int i=1; i<_namesClients.get(index).size(); i++) {
			String mess = _namesClients.get(index).get(i);
			addBehaviour(new OneMessageBehaviour(this, receiver, Protocol.TO_DECLINE, mess));
		}
		
		offer.setAmount(new Float(-1.));
		addBehaviour(new OneMessageBehaviour(this, receiver, Protocol.TO_ANNOUNCE, offer.toACLMessage()));
		
		_offers.remove(index);
		_namesClients.remove(index);
		_offerBehaviours.remove(index);
	}
	
}
