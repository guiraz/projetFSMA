package dubraz.vendeur;

import java.util.List;

import utilities.Offer;
import utilities.OneMessageBehaviour;
import utilities.Protocol;
import jade.core.behaviours.Behaviour;

//offer's behaviour
public class OfferBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;
	
	//defines when behaviour is done
	//defines if payment and goods are sent/received
	private boolean _done, _give, _pay;
	
	//parent agent
	private Vendeur _papa;
	//parent offer
	private Offer _offer;
	//current amount
	private Float _amount;
	//minimum amount
	private Float _minAmount;
	//step of the amount
	private Float _stepAmount;
	//step timer
	private Long _timer;
	//offer's gui
	private OfferInterface _gui;
	
	public OfferBehaviour(Vendeur papa, Offer offer) {
		_papa = papa;
		_offer = offer;
		
		_done = false;
		_give = false;
		_pay = false;
		_amount = _papa.getAmount();
		_minAmount = _papa.getMinAmount();
		_stepAmount = _papa.getStepAmount();
		_timer = _papa.getTimer();
		
		_gui = new OfferInterface(this);
		_gui.update();
		
		//announce the offer to the market
		String[] receiver = new String[] {_papa.getMarcket()};
		_papa.addBehaviour(new OneMessageBehaviour(_papa, receiver, Protocol.TO_ANNOUNCE, offer.toACLMessage()));
	}
	
	//GETTERS//
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
	
	public Offer getOffer() {
		return _offer;
	}

	@Override
	public void action() {
		//while payment and goods are not sent/received
		if(!_give && !_pay) {
			//wait timer time
			waitForBid();
			List<String> clients = getClients();
			switch (clients.size()) {
				//if 0 bidder
				case 0 :
					_amount -= _stepAmount;
					if(_amount >= _minAmount) {
						announce();
					}
					else {
						_gui.InfoMessage("Le montant minimum a été atteint pour l'enchère : " + _offer.getOfferName() + ".");
						_done = true;
					}
				break;
				
				//if 1 bidder
				case 1 :
					waitForBid();
					clients = getClients();
					if(clients.size() > 1) {
						_amount += _stepAmount;
						declineAll(clients);
						announce();
					}
					else {
						bidSuccess();
					}
				break;
				
				//if several bidders
				default :
					_amount += _stepAmount;
					declineAll(clients);
					announce();
				break;
			}
		}
	}

	//send a decline message to all bidders
	private void declineAll(List<String> cls) {
		String[] receiver = new String[] {_papa.getMarcket()};
		for(int i=0; i<cls.size(); i++)
			_papa.addBehaviour(new OneMessageBehaviour(_papa, receiver, Protocol.TO_DECLINE, cls.get(i)));
		
		getClients().clear();
	}
	
	//announce the offer
	private void announce() {
		_offer.setAmount(_amount);
		String[] receiver = new String[] {_papa.getMarcket()};
		_papa.addBehaviour(new OneMessageBehaviour(_papa, receiver, Protocol.TO_ANNOUNCE, _offer.toACLMessage()));
		_gui.update();
	}
	
	//attribute a winner and give goods
	private void bidSuccess() {
		List<String> cl = getClients();
		_gui.InfoMessage("Le gagnant de l'enchère : " + _offer.getOfferName() + " est le client : " + cl.get(0) + ".");
		String[] receiver = new String[] {_papa.getMarcket()};
		_papa.addBehaviour(new OneMessageBehaviour(_papa, receiver, Protocol.TO_ATTRIBUTE, cl.get(0)));
		_papa.addBehaviour(new OneMessageBehaviour(_papa, receiver, Protocol.TO_GIVE, cl.get(0)));
		_give = true;
	}
	
	//receive payment
	public void payment(String cl) {
		_gui.InfoMessage("Vous avez reçu le paiement de l'enchère : " + _offer.getOfferName() + " par le client : " + cl + ".");
		_pay = true;
	}

	//check if payment and goods are sent/received
	//if yes, end the offer
	@Override
	public boolean done() {
		if(_done || (_give && _pay)) {
			_gui.dispose();
			_papa.endOffer(_offer);
			return true;
		}
		else
			return false;
	}

	public List<String> getClients() {
		return _papa.getClients(_offer);
	}

	//call the update of the gui
	public void update() {
		_gui.update();
	}
	
	//wait timer time method
	public void waitForBid() {
		try {
			Thread.sleep(_timer*1000);
		}catch(Exception e) {
			System.err.println("Sleep failed");
		}
	}

}
