package dubraz.acheteur;

import utilities.Protocol;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

//buyer agent behaviour
public class ReceiveAcheteurBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	
	//parent agent
	private Acheteur _papa;
	
	public ReceiveAcheteurBehaviour(Acheteur papa) {
		_papa = papa;
	}
	
	@Override
	public void action() {
		//receive messages
		ACLMessage msg = myAgent.receive();
		if (msg != null){
			
			//announce performative message
			if(msg.getPerformative() == Protocol.TO_ANNOUNCE.getProtocol()) {
				_papa.addOffer(msg);
			}
			
			//decline performative message
			if(msg.getPerformative() == Protocol.TO_DECLINE.getProtocol()) {
				_papa.decline();
			}
			
			//attribute performative message
			if(msg.getPerformative() == Protocol.TO_ATTRIBUTE.getProtocol()) {
				_papa.attribute(msg.getContent());
			}
			
			//give performative message
			if(msg.getPerformative() == Protocol.TO_GIVE.getProtocol()) {
				_papa.give(msg.getContent());
			}
			
		}
		//if no message block and wait
		else
		{
			block();
		}
	}

}
