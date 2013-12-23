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
			
			if(msg.getPerformative() == Protocol.TO_ANNOUNCE.getProtocol()) {
				String[] content = msg.getContent().split("~");
				_papa.addOffer(content[0], Float.parseFloat(content[1]));
			}
			
			if(msg.getPerformative() == Protocol.TO_DECLINE.getProtocol()) {
				_papa.decline();
			}
			
			if(msg.getPerformative() == Protocol.TO_ATTRIBUTE.getProtocol()) {
				_papa.attribute(msg.getContent());
			}
			
			if(msg.getPerformative() == Protocol.TO_GIVE.getProtocol()) {
				_papa.give(msg.getContent());
			}
			
		}
		else
		{
			block();
		}
	}

}
