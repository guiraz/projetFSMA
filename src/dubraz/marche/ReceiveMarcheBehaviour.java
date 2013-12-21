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
			
			if(msg.getPerformative() == Protocol.TO_BID.getProtocol()) {
			}
			
		}
		else
		{
			block();
		}

	}


}
