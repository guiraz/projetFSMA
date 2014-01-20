package dubraz.marche;

import utilities.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

//marcket agent behaviour
public class ReceiveMarcheBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	//parent agent
	private Marche _papa;
	
	public ReceiveMarcheBehaviour(Marche papa) {
		_papa = papa;
	}

	@Override
	public void action() {
		//receive messages
		ACLMessage msg = myAgent.receive();
		if (msg != null){
			
			//create performative messages
			if(msg.getPerformative() == Protocol.TO_CREATE.getProtocol()) {
				//from buyers
				if(msg.getContent().equals("client")) {
					_papa.createClient(msg.getSender().getLocalName());
				}
			
				//from sellers
				if(msg.getContent().equals("vendeur")) {
					_papa.createVendeur(msg.getSender().getLocalName());
				}
			}
			
			//kill performative messages
			if(msg.getPerformative() == Protocol.TO_KILL.getProtocol()) {
				//from buyers
				if(msg.getContent().equals("client")) {
					_papa.killClient(msg.getSender().getLocalName());
				}
			
				//from sellers
				if(msg.getContent().equals("vendeur")) {
					_papa.killVendeur(msg.getSender().getLocalName());
				}
			}
			
			//announce performative messages
			if(msg.getPerformative() == Protocol.TO_ANNOUNCE.getProtocol()) {
				_papa.setAnnounce(msg);
			}
			
			//bid performative messages
			if(msg.getPerformative() == Protocol.TO_BID.getProtocol()) {
				_papa.toBid(msg);
			}
			
			//decline performative messages
			if(msg.getPerformative() == Protocol.TO_DECLINE.getProtocol()) {
				_papa.toDecline(msg);
			}
			
			//attribute performative messages
			if(msg.getPerformative() == Protocol.TO_ATTRIBUTE.getProtocol()) {
				_papa.toAttribute(msg);
			}
			
			//give performative messages
			if(msg.getPerformative() == Protocol.TO_GIVE.getProtocol()) {
				_papa.toGive(msg);
			}
			
			//pay performative messages
			if(msg.getPerformative() == Protocol.TO_PAY.getProtocol()) {
				_papa.toPay(msg);
			}
			
		}
		//if no message, block and wait
		else
		{
			block();
		}

	}


}
