package dubraz.vendeur;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;

public class Vendeur extends Agent {

	private static final long serialVersionUID = 1L;
	private Float _amount;
	private long _timer;
	private List<String> _namesClients;
	private String _marcketName;
	private VendeurInterface _gui;
	
	public Float getAmount() {
		return _amount;
	}
	
	public long getTimer() {
		return _timer;
	}
	
	public void setAmount(Float a) {
		_amount = a;
	}
	
	public void setTimer(long t) {
		_timer = t;
	}
	
	public void addClient(String cl) {
		if(getClientIndex(cl) == -1)
			_namesClients.add(cl);
	}
	
	public String getClient(int i) {
		if(getNbClients()>0)
			return _namesClients.get(i);
		else
			return null;
	}
	
	public int getClientIndex(String cl) {
		boolean find = false;
		int i=0;
		while (i<getNbClients() && !find) {
			if(_namesClients.get(i).equals(cl))
				find = true;
			else
				i++;
		}
		
		if(find)
			return i;
		else
			return -1;
	}
	
	public int getNbClients(){
		return _namesClients.size();
	}
	
	public String getMarcket() {
		return _marcketName;
	}
	
	protected void setup() {
		System.out.println("Hello! Vendeur-agent "+getAID().getName()+" is ready.");
		
		_namesClients = new ArrayList<String>();
		_gui = new VendeurInterface(this);
		
		_marcketName = _gui.getMarcketName();
		addBehaviour(new CreateVendeurBehaviour(this));
	}
	
	protected void takeDown() {
		_gui.dispose();
        System.out.println("Vendeur-agent "+getAID().getName()+" terminating.");
        super.takeDown();
    }
	
	public void stop() {
		doDelete();
	}
	
	public void announce() {
		addBehaviour( new AnnounceBehavior(_amount));
		doWait(_timer*1000);
	}
	
	
	//Aucun acheteur, redemander le prix et le timeout
	public void reset() {
		_amount = new Float(0);
		_timer = 0;
		_namesClients.clear();
		_gui.ErrorMessage("Pas d'enchérisseur, veuillez resaisir les données.");
		_gui.reset();
	}
	
	//timeout done attribuer l'enchérisseur
	public void attribute() {
		//todo
	}
	
}
