package dubraz.vendeur;

import jade.core.behaviours.OneShotBehaviour;

public class WaitBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;
	private Vendeur _papa;
	
	public WaitBehaviour(Vendeur papa) {
		_papa = papa;
	}

	@Override
	public void action() {
		try{Thread.sleep(_papa.getTimer()*1000);}catch(Exception e){System.err.println("Sleep failed");}
	}

}
