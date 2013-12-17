package dubraz.acheteur;

import utilities.Protocol;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiveAcheteurBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	
	private Acheteur _papa;
	
	public ReceiveAcheteurBehaviour(Acheteur papa) {
		_papa = papa;
	}
	
	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg != null){
			
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
