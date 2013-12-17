package dubraz.acheteur;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;

public class Acheteur extends Agent {

	private static final long serialVersionUID = 1L;
	
	private List<String> _sellersNames;
	private List<Float> _amounts;
	
	private AcheteurInterface _gui;
	
	public void setup() {
		System.out.println("Hello! Acheteur-agent "+getAID().getName()+" is ready.");
		
		_sellersNames = new ArrayList<String>();
		_amounts = new ArrayList<Float>();
		
		_gui = new AcheteurInterface(this);
		
		addBehaviour(new ReceiveAcheteurBehaviour(this));
		addBehaviour(new CreateClientBehaviour(getAID().getLocalName().toString()));
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
	
	public void nameAlreadyExist() {
		_gui.ErrorMessage("Ce nom existe déjà! Arrêt de l'agent.");
		stop();
	}
}
