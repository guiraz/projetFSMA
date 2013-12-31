package dubraz.marche;

import java.util.ArrayList;
import java.util.List;

import utilities.*;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Marche extends Agent {
	
	private static final long serialVersionUID = 1L;
	private MarcheInterface _gui;
	private volatile List<String> _sellersNames;
	private volatile List<String> _buyersNames;
	private volatile List<Offer> _offers;
	private static boolean _singleton = false;

	@Override
	protected void setup() {
		
		System.out.println("Hello! Le Marché "+getAID().getLocalName()+" is ready.");
		_sellersNames = new ArrayList<String>();
		_buyersNames = new ArrayList<String>();
		_offers = new ArrayList<Offer>();
		_gui = new MarcheInterface(this);
		
		//Une seule instance de l'agent Marche peut être créé
		if(_singleton){
			_gui.ErrorMessage("Un agent marché est déjà lancé. Arrêt de l'agent.");
			stop();
			return;
		}
		else
			_singleton = true;
		
		//L'agent doit être nommé 'marche' pour pouvoir communiquer
		if(!getLocalName().equals("marche")) {
			_gui.ErrorMessage("L'agent doit être nommé : 'marche'. Arrêt de l'agent.");
			stop();
			return;
		}
		
		addBehaviour(new ReceiveMarcheBehaviour(this));
	}

	@Override
	protected void takeDown() {
		if(_gui != null)
			_gui.dispose();
		System.out.println("Le Marché "+getAID().getName()+" is terminating.");
		_singleton = false;
		super.takeDown();
	}
	
	public void stop() {
		doDelete();
	}
	
	public void setAnnounce(ACLMessage msg) {
		Offer offer = Offer.fromACLMessage(msg.getContent());
		
		if(_offers.contains(offer)){
			int index = _offers.indexOf(offer);
			if(offer.getAmount() < 0)
				_offers.remove(index);
			else
				_offers.get(index).setAmount(offer.getAmount());
		}
		else
			_offers.add(offer);
		_gui.RessourcesUpdated();
		
		String mess = offer.toACLMessage();
		String[] bn = getStringArray(_buyersNames);
		if(bn.length > 0)
			addBehaviour(new OneMessageBehaviour(this, bn, Protocol.TO_ANNOUNCE, mess));
	}
	
	public void createClient(String name) {
		_buyersNames.add(name);
	}
	
	public void createVendeur(String name) {
		_sellersNames.add(name);
	}
	
	public List<String> getSellersNames() {
		return _sellersNames;
	}
	
	public List<String> getBuyersNames() {
		return _buyersNames;
	}
	
	public List<Offer> getOffers() {
		return _offers;
	}

	public void toBid(ACLMessage msg) {
		Offer offer = Offer.fromACLMessage(msg.getContent());
		String mess = offer.toACLMessage() + "~" + msg.getSender().getLocalName();
		String[] seller = new String[] {offer.getSellerName()};
		addBehaviour(new OneMessageBehaviour(this, seller, Protocol.TO_BID, mess));
	}

	public void toDecline(ACLMessage msg) {
		String[] buyer = new String[] {msg.getContent()};
		addBehaviour(new OneMessageBehaviour(this, buyer, Protocol.TO_DECLINE, ""));
	}

	public void toAttribute(ACLMessage msg) {
		String[] buyer = new String[] {msg.getContent()};
		addBehaviour(new OneMessageBehaviour(this, buyer, Protocol.TO_ATTRIBUTE, msg.getSender().getLocalName()));
	}
	
	private String[] getStringArray(List<String> ls) {
		String[] result = new String[ls.size()];
		for(int i=0; i<ls.size(); i++)
			result[i] = ls.get(i);
		return result;
	}

	public void toGive(ACLMessage msg) {
		String[] buyer = new String[] {msg.getContent()};
		addBehaviour(new OneMessageBehaviour(this, buyer, Protocol.TO_GIVE, msg.getSender().getLocalName()));
	}

	public void toPay(ACLMessage msg) {
		Offer offer = Offer.fromACLMessage(msg.getContent());
		String mess = offer.toACLMessage() + "~" + msg.getSender().getLocalName();
		String[] seller = new String[] {offer.getSellerName()};
		addBehaviour(new OneMessageBehaviour(this, seller, Protocol.TO_PAY, mess));
	}

	public void killClient(String buyer) {
		_buyersNames.remove(buyer);
	}

	public void killVendeur(String seller) {
		int id = _sellersNames.indexOf(seller);
		_sellersNames.remove(id);
		
		for(int i=0; i<_offers.size(); i++) {
			if(_offers.get(i).equals(seller)) {
				_offers.remove(i);
				i--;
			}
		}
		
		_gui.RessourcesUpdated();
	}

}
