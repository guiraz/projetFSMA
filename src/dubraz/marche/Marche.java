package dubraz.marche;

import java.util.ArrayList;
import java.util.List;

import utilities.*;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

//market agent
public class Marche extends Agent {
	
	private static final long serialVersionUID = 1L;
	
	//market agent's gui
	private MarcheInterface _gui;
	//subscribed sellers
	private volatile List<String> _sellersNames;
	//subscribed buyers
	private volatile List<String> _buyersNames;
	//all offers
	private volatile List<Offer> _offers;
	//singleton pattern boolean
	private static boolean _singleton = false;

	@Override
	protected void setup() {
		
		System.out.println("Hello! Le Marché "+getAID().getLocalName()+" is ready.");
		
		//init variables
		_sellersNames = new ArrayList<String>();
		_buyersNames = new ArrayList<String>();
		_offers = new ArrayList<Offer>();
		_gui = new MarcheInterface(this);
		
		//one instance of market can be created
		if(_singleton){
			_gui.ErrorMessage("Un agent marché est déjà lancé. Arrêt de l'agent.");
			stop();
			return;
		}
		else
			_singleton = true;
		
		//marcket agent must be named 'marche'
		if(!getLocalName().equals("marche")) {
			_gui.ErrorMessage("L'agent doit être nommé : 'marche'. Arrêt de l'agent.");
			stop();
			return;
		}
		
		//launch message receival behaviour
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
	
	//kill the agent
	public void stop() {
		doDelete();
	}
	
	//add or modify an offer in the list
	public void setAnnounce(ACLMessage msg) {
		Offer offer = Offer.fromACLMessage(msg.getContent());
		
		//checking if offer already exits
		//modify
		if(_offers.contains(offer)){
			int index = _offers.indexOf(offer);
			if(offer.getAmount() < 0)
				_offers.remove(index);
			else
				_offers.get(index).setAmount(offer.getAmount());
		}
		//add
		else
			_offers.add(offer);
		_gui.RessourcesUpdated();
		
		//inform buyer of the new/modify offer
		String mess = offer.toACLMessage();
		String[] bn = getStringArray(_buyersNames);
		if(bn.length > 0)
			addBehaviour(new OneMessageBehaviour(this, bn, Protocol.TO_ANNOUNCE, mess));
	}
	
	//subscribe a buyer
	public void createClient(String name) {
		_buyersNames.add(name);
	}
	
	//subscribe a seller
	public void createVendeur(String name) {
		_sellersNames.add(name);
	}
	
	//get sellers list
	public List<String> getSellersNames() {
		return _sellersNames;
	}
	
	//get buyers list
	public List<String> getBuyersNames() {
		return _buyersNames;
	}
	
	//get offers list
	public List<Offer> getOffers() {
		return _offers;
	}

	//bid performative action
	public void toBid(ACLMessage msg) {
		Offer offer = Offer.fromACLMessage(msg.getContent());
		String mess = offer.toACLMessage() + "~" + msg.getSender().getLocalName();
		String[] seller = new String[] {offer.getSellerName()};
		addBehaviour(new OneMessageBehaviour(this, seller, Protocol.TO_BID, mess));
	}

	//decline performative action
	public void toDecline(ACLMessage msg) {
		String[] buyer = new String[] {msg.getContent()};
		addBehaviour(new OneMessageBehaviour(this, buyer, Protocol.TO_DECLINE, ""));
	}

	//attribute performative action
	public void toAttribute(ACLMessage msg) {
		String[] buyer = new String[] {msg.getContent()};
		addBehaviour(new OneMessageBehaviour(this, buyer, Protocol.TO_ATTRIBUTE, msg.getSender().getLocalName()));
	}
	
	//convert List<String> -> String[]
	private String[] getStringArray(List<String> ls) {
		String[] result = new String[ls.size()];
		for(int i=0; i<ls.size(); i++)
			result[i] = ls.get(i);
		return result;
	}

	//give performative action
	public void toGive(ACLMessage msg) {
		String[] buyer = new String[] {msg.getContent()};
		addBehaviour(new OneMessageBehaviour(this, buyer, Protocol.TO_GIVE, msg.getSender().getLocalName()));
	}

	//pay performative action
	public void toPay(ACLMessage msg) {
		Offer offer = Offer.fromACLMessage(msg.getContent());
		String mess = offer.toACLMessage() + "~" + msg.getSender().getLocalName();
		String[] seller = new String[] {offer.getSellerName()};
		addBehaviour(new OneMessageBehaviour(this, seller, Protocol.TO_PAY, mess));
	}

	//unsubscribe buyer
	public void killClient(String buyer) {
		_buyersNames.remove(buyer);
	}

	//unsubscribe seller and remove all his offers
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
