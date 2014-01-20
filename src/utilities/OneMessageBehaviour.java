package utilities;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

//agent's behaviour to send a message to one or several receiver with a certain performative
public class OneMessageBehaviour extends OneShotBehaviour {
	
	
	private static final long serialVersionUID = 1L;
	
	//sender
	private Agent _papa;
	//receiver(s)
	private String[] _receiver;
	//performative
	private Protocol _prot;
	//content
	private String _mess;
	
	//constructor
	public OneMessageBehaviour(Agent a, String[] r, Protocol p, String m) {
		_papa = a;
		_receiver = r;
		_prot = p;
		_mess = m;
	}

	//behaviour action
	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(_prot.getProtocol());
		for(int i=0; i<_receiver.length; i++)
			msg.addReceiver(new AID(_receiver[i], AID.ISLOCALNAME));
		msg.setContent(_mess);
		_papa.send(msg);
	}

}
