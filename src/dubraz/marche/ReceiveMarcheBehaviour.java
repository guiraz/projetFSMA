package dubraz.marche;

import utilities.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiveMarcheBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private Marche _papa;
	
	public ReceiveMarcheBehaviour(Marche papa) {
		_papa = papa;
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg != null){
			
			if(msg.getPerformative() == Protocol.TO_CREATE.getProtocol()) {
				if(msg.getContent().equals("client")) {
					_papa.createClient(msg.getSender().getLocalName());
				}
			
				if(msg.getContent().equals("vendeur")) {
					_papa.createVendeur(msg.getSender().getLocalName());
				}
			}
			
			if(msg.getPerformative() == Protocol.TO_KILL.getProtocol()) {
				if(msg.getContent().equals("client")) {
					_papa.killClient(msg.getSender().getLocalName());
				}
			
				if(msg.getContent().equals("vendeur")) {
					_papa.killVendeur(msg.getSender().getLocalName());
				}
			}
			
			if(msg.getPerformative() == Protocol.TO_ANNOUNCE.getProtocol()) {
				_papa.setAnnounce(msg);
			}
			
			if(msg.getPerformative() == Protocol.TO_BID.getProtocol()) {
				_papa.toBid(msg);
			}
			
			if(msg.getPerformative() == Protocol.TO_DECLINE.getProtocol()) {
				_papa.toDecline(msg);
			}
			
			if(msg.getPerformative() == Protocol.TO_ATTRIBUTE.getProtocol()) {
				_papa.toAttribute(msg);
			}
			
			if(msg.getPerformative() == Protocol.TO_GIVE.getProtocol()) {
				_papa.toGive(msg);
			}
			
			if(msg.getPerformative() == Protocol.TO_PAY.getProtocol()) {
				_papa.toPay(msg);
			}
			
		}
		else
		{
			block();
		}

	}


}
