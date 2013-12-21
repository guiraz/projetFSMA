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
	private final String _marcketName = "marché";
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
		if(getClientIndex(cl) == -1)
			_namesClients.add(cl);
		_gui.update();
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
	
	public void setAnnouncing(boolean a) {
		_announcing = a;
	}
	
	public boolean getAnnouncing() {
		return _announcing;
	}
	
	protected void setup() {
		System.out.println("Hello! Vendeur-agent "+getAID().getName()+" is ready.");
		
		_amount = new Float(0);
		_timer = new Long(0);
		_announcing = false;
		_namesClients = new ArrayList<String>();
		_gui = new VendeurInterface(this);

		String[] receivers = new String[] {_marcketName};
		addBehaviour(new OneMessageBehaviour(this, receivers, Protocol.TO_CREATE, "vendeur"));
		addBehaviour(new ReceiveVendeurBehaviour(this));
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
		_gui.update();
		_announcing = true;
		String[] receivers = new String[] {_marcketName};
		addBehaviour( new OneMessageBehaviour(this, receivers, Protocol.TO_ANNOUNCE, _amount.toString()));
		doWait(_timer*1000);
		addBehaviour(new ProposalVendeurBehaviour(this));
	}
	
	
	//Aucun acheteur, redemander le prix et le timeout
	public void reset() {
		_announcing = false;
		_amount = new Float(0);
		_timer = new Long(0);
		_namesClients.clear();
		_gui.ErrorMessage("Pas d'enchérisseur, veuillez resaisir les données.");
		_gui.reset();
	}
	
	//timeout done attribuer l'enchérisseur
	public void attribute() {
		_announcing = false;
		String[] receivers = new String[] {_marcketName};
		addBehaviour(new OneMessageBehaviour(this, receivers, Protocol.TO_ATTRIBUTE, getClient(0)));
	}
	
	public void payment(String clName) {
		_amount = new Float(0);
		_timer = new Long(0);
		_namesClients.clear();
		_gui.ErrorMessage("Paiement reçude " + clName + ".");
		_gui.reset();
	}
	
}
