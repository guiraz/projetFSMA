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
			
			if(msg.getPerformative() == Protocol.TO_ANNOUNCE.getProtocol()) {
				Float amount = Float.parseFloat(msg.getContent());
				_papa.setAnnounce(msg.getSender().getLocalName(), amount);
			}
			
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
			
			if(msg.getPerformative() == Protocol.TO_BID.getProtocol()) {
				String[] receiver = new String[] {msg.getContent()};
				_papa.toBid(msg.getSender().getLocalName(), receiver);
			}
			
			if(msg.getPerformative() == Protocol.TO_DECLINE.getProtocol()) {
				String[] receiver = msg.getContent().split("~");
				_papa.toDecline(msg.getSender().getLocalName(), receiver);
			}
			
			if(msg.getPerformative() == Protocol.TO_ATTRIBUTE.getProtocol()) {
				String[] receiver = new String[] {msg.getContent()};
				_papa.toAttribute(msg.getSender().getLocalName(), receiver);
			}
			
			if(msg.getPerformative() == Protocol.TO_GIVE.getProtocol()) {
				String[] receiver = new String[] {msg.getContent()};
				_papa.toGive(msg.getSender().getLocalName(), receiver);
			}
			
			if(msg.getPerformative() == Protocol.TO_PAY.getProtocol()) {
				String[] receiver = new String[] {msg.getContent()};
				_papa.toPay(msg.getSender().getLocalName(), receiver);
			}
			
		}
		else
		{
			block();
		}

	}


}
