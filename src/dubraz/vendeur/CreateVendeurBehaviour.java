package dubraz.vendeur;

import utilities.Protocol;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class CreateVendeurBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	
	private Vendeur _papa;
	
	public CreateVendeurBehaviour(Vendeur papa) {
		_papa = papa;
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(Protocol.TO_CREATE.getProtocol());
		msg.addReceiver(new AID(_papa.getMarcket(), AID.ISLOCALNAME));
		msg.setContent("vendeur");
		myAgent.send(msg);
	}

}
