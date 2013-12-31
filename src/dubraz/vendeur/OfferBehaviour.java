package dubraz.vendeur;

import java.util.List;

import utilities.Offer;
import utilities.OneMessageBehaviour;
import utilities.Protocol;
import jade.core.behaviours.Behaviour;

public class OfferBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;
	
	private boolean _done, _give, _pay;
	
	private Vendeur _papa;
	private Offer _offer;
	private Float _amount;
	private Float _minAmount;
	private Float _stepAmount;
	private Long _timer;
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
		
		String[] receiver = new String[] {_papa.getMarcket()};
		_papa.addBehaviour(new OneMessageBehaviour(_papa, receiver, Protocol.TO_ANNOUNCE, offer.toACLMessage()));
	}
	
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
		if(!_give && !_pay) {
			waitForBid();
			List<String> clients = getClients();
			switch (clients.size()) {
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
					
				default :
					_amount += _stepAmount;
					declineAll(clients);
					announce();
				break;
			}
		}
	}

	private void declineAll(List<String> cls) {
		String[] receiver = new String[] {_papa.getMarcket()};
		for(int i=0; i<cls.size(); i++)
			_papa.addBehaviour(new OneMessageBehaviour(_papa, receiver, Protocol.TO_DECLINE, cls.get(i)));
		
		getClients().clear();
	}
	
	private void announce() {
		_offer.setAmount(_amount);
		String[] receiver = new String[] {_papa.getMarcket()};
		_papa.addBehaviour(new OneMessageBehaviour(_papa, receiver, Protocol.TO_ANNOUNCE, _offer.toACLMessage()));
		_gui.update();
	}
	
	private void bidSuccess() {
		List<String> cl = getClients();
		_gui.InfoMessage("Le gagnant de l'enchère : " + _offer.getOfferName() + " est le client : " + cl.get(0) + ".");
		String[] receiver = new String[] {_papa.getMarcket()};
		_papa.addBehaviour(new OneMessageBehaviour(_papa, receiver, Protocol.TO_ATTRIBUTE, cl.get(0)));
		_papa.addBehaviour(new OneMessageBehaviour(_papa, receiver, Protocol.TO_GIVE, cl.get(0)));
		_give = true;
	}
	
	public void payment(String cl) {
		_gui.InfoMessage("Vous avez reçu le paiement de l'enchère : " + _offer.getOfferName() + " par le client : " + cl + ".");
		_pay = true;
	}

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

	public void update() {
		_gui.update();
	}
	
	public void waitForBid() {
		try {
			Thread.sleep(_timer*1000);
		}catch(Exception e) {
			System.err.println("Sleep failed");
		}
	}

}
