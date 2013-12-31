package dubraz.vendeur;

import utilities.Offer;
import utilities.Protocol;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiveVendeurBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
    
    private Vendeur _papa;
    
    public ReceiveVendeurBehaviour(Vendeur papa) {
        _papa = papa;
    }

    @Override
    public void action() {
        ACLMessage msg = _papa.receive();
        if(msg != null) {
            //Réception d'une enchère
            if(msg.getPerformative() == Protocol.TO_BID.getProtocol()) {
        		Offer offer = Offer.fromACLMessage(msg.getContent());
        		String client = msg.getContent().split("~")[3];
                _papa.addClient(client, offer);
                _papa.getOfferBehaviour(offer).update();
            }
            
            //Réception de paiement
            if(msg.getPerformative() == Protocol.TO_PAY.getProtocol()) {
            	Offer offer = Offer.fromACLMessage(msg.getContent());
        		String client = msg.getContent().split("~")[3];
        		_papa.getOfferBehaviour(offer).payment(client);
            }
        }
        else
        {
            block();
        }
    }

}
