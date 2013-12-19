package utilities;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class OneReceiverMessageBehaviour extends OneShotBehaviour {
	
	
	private static final long serialVersionUID = 1L;
	
	private Agent _papa;
	private String _receiver;
	private Protocol _prot;
	private String _mess;
	
	public OneReceiverMessageBehaviour(Agent a, String r, Protocol p, String m) {
		_papa = a;
		_receiver = r;
		_prot = p;
		_mess = m;
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(_prot.getProtocol());
		msg.addReceiver(new AID(_receiver, AID.ISLOCALNAME));
		msg.setContent(_mess);
		_papa.send(msg);
	}

}
