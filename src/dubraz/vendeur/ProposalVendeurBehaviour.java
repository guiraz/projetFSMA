package dubraz.vendeur;

import jade.core.behaviours.OneShotBehaviour;

public class ProposalVendeurBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	private Vendeur _papa;
	
	public ProposalVendeurBehaviour(Vendeur papa) {
		_papa = papa;
	}

	@Override
	public void action() {
		switch(_papa.getNbClients()) {
			case 0:
					float amount1 = _papa.getAmount();
					amount1 -= 1;
					if(amount1 < 0)
						_papa.reset();
					else {
						_papa.setAmount(amount1);
						_papa.announce();
					}
				break;
				
			case 1:
					_papa.attribute();
				break;
				
			default :
					float amount2 = _papa.getAmount();
					amount2 += 1;
					_papa.setAmount(amount2);
					_papa.announce();
				break;
		}
	}

}