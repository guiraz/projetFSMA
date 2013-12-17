package dubraz.vendeur;

import java.net.ProtocolException;

import utilities.*;

import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;


public class AnnounceBehavior extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	private Float _amount;
	
	public AnnounceBehavior(Float a) {
		_amount = a;
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(Protocol.TO_ANNOUNCE.getProtocol());
		msg.addReceiver(new AID("marché", AID.ISLOCALNAME));
		msg.setContent(_amount.toString());
		myAgent.send(msg);
	}

}
