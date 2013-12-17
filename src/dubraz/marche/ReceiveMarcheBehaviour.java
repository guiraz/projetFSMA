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
				_papa.setAnnounce(msg.getSender().getLocalName().toString(), amount);
			}
			
			if(msg.getPerformative() == Protocol.TO_CREATECLIENT.getProtocol()) {
				if(!_papa.createClient(msg.getContent())){
					ACLMessage response = new ACLMessage(Protocol.TO_DECLINE.getProtocol());
					response.addReceiver(msg.getSender());
					_papa.send(response);
				}
			}
			
		}
		else
		{
			block();
		}

	}


}
