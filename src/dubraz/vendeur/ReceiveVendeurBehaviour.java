package dubraz.vendeur;

import utilities.Offer;
import utilities.Protocol;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

//seller agent's behaviour
public class ReceiveVendeurBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
    
	//parent agent
    private Vendeur _papa;
    
    public ReceiveVendeurBehaviour(Vendeur papa) {
        _papa = papa;
    }

    @Override
    public void action() {
    	//receive messages
        ACLMessage msg = _papa.receive();
        if(msg != null) {
            //bid performative message
            if(msg.getPerformative() == Protocol.TO_BID.getProtocol()) {
        		Offer offer = Offer.fromACLMessage(msg.getContent());
        		String client = msg.getContent().split("~")[3];
                _papa.addClient(client, offer);
                _papa.getOfferBehaviour(offer).update();
            }
            
            //pay performative message
            if(msg.getPerformative() == Protocol.TO_PAY.getProtocol()) {
            	Offer offer = Offer.fromACLMessage(msg.getContent());
        		String client = msg.getContent().split("~")[3];
        		_papa.getOfferBehaviour(offer).payment(client);
            }
        }
        //if no message, block and wait
        else
        {
            block();
        }
    }

}
