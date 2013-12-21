package dubraz.marche;

import java.util.ArrayList;
import java.util.List;

import utilities.OneMessageBehaviour;
import utilities.Protocol;

import jade.core.Agent;

public class Marche extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MarcheInterface _gui;
	private volatile List<String> _sellersNames;
	private volatile List<String> _buyersNames;
	private volatile List<Float> _amounts;

	@Override
	protected void setup() {
		
		System.out.println("Hello! Le Marché "+getAID().getLocalName()+" is ready.");
		_sellersNames = new ArrayList<String>();
		_buyersNames = new ArrayList<String>();
		_amounts = new ArrayList<Float>();
		_gui = new MarcheInterface(this);
		
		addBehaviour(new ReceiveMarcheBehaviour(this));
	}

	@Override
	protected void takeDown() {
		if(_gui != null)
			_gui.dispose();
		System.out.println("Le Marché "+getAID().getName()+" is terminating.");
		super.takeDown();
	}
	
	public void stop() {
		doDelete();
	}
	
	public void setAnnounce(String name, Float amount) {
		if(_sellersNames.contains(name)) {
			System.out.println(_sellersNames.indexOf(name) + " " + amount);
			_amounts.set(_sellersNames.indexOf(name), amount);
		}
		else {
			_sellersNames.add(name);
			_amounts.add(amount);
		}
		_gui.RessourcesUpdated();
		
		String mess = name + "~" + amount.toString();
		String[] bn = getStringArray(_buyersNames);
		if(bn.length > 0)
			addBehaviour(new OneMessageBehaviour(this, bn, Protocol.TO_ANNOUNCE, mess));
	}
	
	public void createClient(String name) {
		_buyersNames.add(name);
		System.out.println(_buyersNames.size());
	}
	
	public void createVendeur(String name) {
		_sellersNames.add(name);
		_amounts.add(new Float(-1));
	}
	
	public List<String> getSellersNames() {
		return _sellersNames;
	}
	
	public List<String> getBuyersNames() {
		return _buyersNames;
	}
	
	public List<Float> getAmounts() {
		return _amounts;
	}

	public void toBid(String buyer, String[] seller) {
		addBehaviour(new OneMessageBehaviour(this, seller, Protocol.TO_BID, buyer));
	}

	public void toDecline(String seller, String[] buyers) {
		addBehaviour(new OneMessageBehaviour(this, buyers, Protocol.TO_DECLINE, seller));
	}

	public void toAttribute(String seller, String[] buyer) {
		addBehaviour(new OneMessageBehaviour(this, buyer, Protocol.TO_ATTRIBUTE, seller));
		
		int idSeller = _sellersNames.indexOf(seller);
		_amounts.set(idSeller, new Float(-1));
		String[] receiver = getStringArray(_buyersNames);
		addBehaviour(new OneMessageBehaviour(this, receiver, Protocol.TO_ANNOUNCE, _amounts.get(idSeller).toString()));
	}
	
	private String[] getStringArray(List<String> ls) {
		String[] result = new String[ls.size()];
		for(int i=0; i<ls.size(); i++)
			result[i] = ls.get(i);
		return result;
	}

	public void toGive(String seller, String[] buyer) {
		addBehaviour(new OneMessageBehaviour(this, buyer, Protocol.TO_GIVE, seller));
	}

	public void toPay(String buyer, String[] seller) {
		addBehaviour(new OneMessageBehaviour(this, seller, Protocol.TO_PAY, buyer));
	}

}
