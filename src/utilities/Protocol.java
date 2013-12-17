package utilities;

import jade.lang.acl.ACLMessage;

public enum Protocol {
	TO_CREATECLIENT (ACLMessage.SUBSCRIBE),
	TO_ANNOUNCE (ACLMessage.CFP),
	TO_BID (ACLMessage.PROPOSE),
	TO_ATTRIBUTE (ACLMessage.ACCEPT_PROPOSAL),
	TO_DECLINE (ACLMessage.REJECT_PROPOSAL),
	TO_GIVE (ACLMessage.REQUEST),
	TO_PAY (ACLMessage.AGREE);
	
	private final int protocol;
	
	Protocol(int prot){
		this.protocol = prot;
	}
	
	public int getProtocol() {
		return protocol;
	}
}
