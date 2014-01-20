package utilities;

import jade.lang.acl.ACLMessage;

//enumerator of all the performatives of the project
public enum Protocol {
	TO_CREATE (ACLMessage.SUBSCRIBE),			//Subscribe to the market (buyers & sellers)
	TO_KILL (ACLMessage.CANCEL),				//Unsubscribe to the market (buyers & sellers)
	TO_ANNOUNCE (ACLMessage.CFP),				//Announce offer
	TO_BID (ACLMessage.PROPOSE),				//Bid on an offer
	TO_ATTRIBUTE (ACLMessage.ACCEPT_PROPOSAL),	//Attribute a winner to an offer
	TO_DECLINE (ACLMessage.REJECT_PROPOSAL),	//Decline a bid on an offer
	TO_GIVE (ACLMessage.REQUEST),				//Send the goods to the winner of the offer
	TO_PAY (ACLMessage.AGREE);					//Send payment to the seller of the good
	
	private final int protocol;
	
	Protocol(int prot){
		this.protocol = prot;
	}
	
	public int getProtocol() {
		return protocol;
	}
}
