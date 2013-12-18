package dubraz.acheteur;

import utilities.Protocol;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class CreateClientBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(Protocol.TO_CREATE.getProtocol());
		msg.addReceiver(new AID("marché", AID.ISLOCALNAME));
		msg.setContent("client");
		myAgent.send(msg);
	}

}
