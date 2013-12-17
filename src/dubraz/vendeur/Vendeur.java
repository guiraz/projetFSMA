package dubraz.vendeur;

import jade.core.Agent;

public class Vendeur extends Agent {

	private static final long serialVersionUID = 1L;
	private Float _amount;
	private Float _timer;
	private VendeurInterface _gui;
	
	public Float getAmount() {
		return _amount;
	}
	
	protected void setup() {
		System.out.println("Hello! Vendeur-agent "+getAID().getName()+" is ready.");
		
		_gui = new VendeurInterface(this);
	}
	
	protected void takeDown() {
		_gui.dispose();
        System.out.println("Vendeur-agent "+getAID().getName()+" terminating.");
        super.takeDown();
    }
	
	public void stop() {
		doDelete();
	}
	
	public void setAmount(Float f) {
		_amount = f;
	}
	
	public void setTimer(Float f) {
		_timer = f;
	}
	
	public void announce() {
		addBehaviour( new AnnounceBehavior(_amount));
	}
	
}
