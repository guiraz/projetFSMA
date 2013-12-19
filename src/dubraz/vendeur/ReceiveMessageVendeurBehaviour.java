package dubraz.vendeur;

import utilities.Protocol;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiveMessageVendeurBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	
	private Vendeur _papa;
	
	public ReceiveMessageVendeurBehaviour(Vendeur papa) {
		_papa = papa;
	}

	@Override
	public void action() {
		ACLMessage msg = _papa.receive();
		if(msg != null) {
			
			if(msg.getPerformative() == Protocol.TO_BID.getProtocol()) {
				if(_papa.getAnnouncing()) {
					_papa.addClient(msg.getContent());
				}
			}
			
			if(msg.getPerformative() == Protocol.TO_PAY.getProtocol()) {
				_papa.payment(msg.getContent());
			}
			
			if(msg.getPerformative() == Protocol.TO_DECLINE.getProtocol()) {
				_papa.nameAlreadyExist();
			}
		}
		else
		{
			block();
		}
	}

}
