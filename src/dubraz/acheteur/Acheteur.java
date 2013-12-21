package dubraz.acheteur;

import java.util.ArrayList;
import java.util.List;

import utilities.OneMessageBehaviour;
import utilities.Protocol;

import jade.core.Agent;

public class Acheteur extends Agent {

	private static final long serialVersionUID = 1L;
	
	private boolean _automatique;
	private Float _defaultAmount;
	private List<String> _sellersNames;
	private List<Float> _amounts;
	private final String _marcketName = "marché";
	
	private AcheteurInterface _gui;
	
	public void setup() {
		System.out.println("Hello! Acheteur-agent "+getAID().getName()+" is ready.");
		
		_sellersNames = new ArrayList<String>();
		_amounts = new ArrayList<Float>();
		
		_gui = new AcheteurInterface(this);
		
		_automatique = _gui.getBidProcess();
		
		if(_automatique)
			_defaultAmount = _gui.getDefaultAmount();
		
		String[] receivers = new String[] {_marcketName};
		addBehaviour(new OneMessageBehaviour(this, receivers, Protocol.TO_CREATE, "client"));
		addBehaviour(new ReceiveAcheteurBehaviour(this));
	}

	protected void takeDown() {
		_gui.dispose();
        System.out.println("Acheteur-agent "+getAID().getName()+" terminating.");
        super.takeDown();
    }
	
	public void stop() {
		doDelete();
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
}
