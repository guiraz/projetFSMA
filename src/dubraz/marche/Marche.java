package dubraz.marche;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;

public class Marche extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MarcheInterface _gui;
	private List<String> _sellersNames;
	private List<String> _buyersNames;
	private List<Float> _amounts;

	@Override
	protected void setup() {
		
		System.out.println("Hello! Le Marché d'Ordralfabétix "+getAID().getLocalName()+" is ready.");
		System.out.println("state : "+this.getState());
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
		System.out.println("Le Marché d'Ordralfabétix "+getAID().getName()+" is terminating.");
		super.takeDown();
	}
	
	public void stop() {
		doDelete();
	}
	
	public void setAnnounce(String name, Float amount) {
		boolean find = false;
		int i=0;
		while(!find && i!=_sellersNames.size()) {
			if(_sellersNames.get(i).equals(name)) {
				_amounts.set(i, amount);
				find = true;
			}
			else
				i++;
		}
		if(!find) {
			_sellersNames.add(name);
			_amounts.add(amount);
		}
		
		_gui.RessourcesUpdated();
	}
	
	public boolean createClient(String name) {
		boolean find = false;
		for(int i=0; i<_buyersNames.size(); i++)
			if(_buyersNames.get(i) == name)
				find = true;
		if(!find) {
			_buyersNames.add(name);
			return true;
		}
		else {
			return false;
		}
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

}
