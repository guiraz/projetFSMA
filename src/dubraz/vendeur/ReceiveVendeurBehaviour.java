package dubraz.vendeur;

import utilities.Protocol;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiveVendeurBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	
	private Vendeur _papa;
	
	public ReceiveVendeurBehaviour(Vendeur papa) {
		_papa = papa;
	}

	@Override
	public void action() {
		ACLMessage msg = _papa.receive();
		if(msg != null) {
			
			//Réception d'une enchère
			if(msg.getPerformative() == Protocol.TO_BID.getProtocol()) {
				if(_papa.getAnnouncing()) {
					_papa.addClient(msg.getContent());
				}
				else
					_papa.decline(msg.getContent());
			}
			
			//Réception de paiement
			if(msg.getPerformative() == Protocol.TO_PAY.getProtocol()) {
				_papa.payment(msg.getContent());
			}
		}
		else
		{
			block();
		}
	}

}
