package utilities;

import jade.lang.acl.ACLMessage;

public enum Protocol {
	TO_CREATE (ACLMessage.SUBSCRIBE),			//Créer un acheteur ou un vendeur
	TO_ANNOUNCE (ACLMessage.CFP),				//Annonce des offres
	TO_BID (ACLMessage.PROPOSE),				//L'acheteur enchéris sur une offre
	TO_ATTRIBUTE (ACLMessage.ACCEPT_PROPOSAL),	//Le vendeur définis l'enchère gagnante 
	TO_DECLINE (ACLMessage.REJECT_PROPOSAL),	//Refus des enchères
	TO_GIVE (ACLMessage.REQUEST),				//Envoi du bien du vendeur au client
	TO_PAY (ACLMessage.AGREE);					//Envoi de l'argent du client jusqu'au vendeur
	
	private final int protocol;
	
	Protocol(int prot){
		this.protocol = prot;
	}
	
	public int getProtocol() {
		return protocol;
	}
}
