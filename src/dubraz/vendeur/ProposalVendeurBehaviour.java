package dubraz.vendeur;

import jade.core.behaviours.OneShotBehaviour;

public class ProposalVendeurBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	private Vendeur _papa;
	private boolean _firstIteration;
	
	public ProposalVendeurBehaviour(Vendeur papa, boolean first) {
		_papa = papa;
		_firstIteration = first; //Pour savoir si c'est la première ou deuxiéme fois.
	}

	@Override
	public void action() {
		switch(_papa.getNbClients()) {
			//Si aucune enchère
			case 0:
					float amount1 = _papa.getAmount();
					amount1 -= _papa.getStepAmount();
					if(amount1 < _papa.getMinAmount())
						_papa.noBids();
					else {
						_papa.setAmount(amount1);
						_papa.announce();
					}
				break;
				
			//Si une enchère
			case 1:
					if(_firstIteration)
						_papa.waitOtherBids(); //Si première fois
					else
						_papa.attribute(); //Si deuxiéme fois
				break;
				
			//Si plusieurs enchères
			default :
					_papa.declineAll();
					float amount2 = _papa.getAmount();
	                amount2 += _papa.getStepAmount();
	                _papa.setAmount(amount2);
	                _papa.announce();
				break;
		}
	}

}
