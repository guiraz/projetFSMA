package dubraz.acheteur;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import utilities.*;

//buying agent's gui
public class AcheteurInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Acheteur _papa;
	private JButton _buttonQuit;
	private JButton _buttonBid;
	private JTable _table;
	private JScrollPane _scrollPane;
	private JLabel _labelDefaultAmount;
	
	public AcheteurInterface(Acheteur papa) {
		_papa = papa;
		setTitle(_papa.getName());
		
		_buttonQuit = new JButton("Quitter");
		_buttonQuit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionQuitter(e);
			}
		});
		
		_buttonBid = new JButton("Enchérir");
		_buttonBid.setEnabled(false);
		_buttonBid.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionBid(e);
			}
		});
		
		_table = new JTable(new Object[0][3], new String[] {"Vendeur", "Offre","Prix"});
		_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_scrollPane = new JScrollPane();
		_scrollPane.setViewportView(_table);
		_scrollPane.setMinimumSize(new Dimension(200, 200));
		
		_labelDefaultAmount = new JLabel("");
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                	.addComponent(_scrollPane)
                	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                			.addComponent(_labelDefaultAmount)
                			.addComponent(_buttonBid)
                			.addComponent(_buttonQuit))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            			.addComponent(_scrollPane)
            			.addGroup(layout.createSequentialGroup()
        					.addComponent(_labelDefaultAmount)
        					.addComponent(_buttonBid)))
        			.addComponent(_buttonQuit)
        );
        
        pack();
		
		launch();
	}
	
	//display error message 'mess'
	public void ErrorMessage(String mess) {
		JOptionPane.showMessageDialog(this, mess, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	//display informative message 'mess'
	public void InfoMessage(String mess) {
		JOptionPane.showMessageDialog(this, mess, "Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	//display all the offers and enable/disable the buttons when necessary
	public void ressourcesUpdated() {
		List<Offer> offers = _papa.getOffers();
		
		//display offers
		Object[][] data = new Object[offers.size()][3];
		for(int i=0; i<offers.size(); i++) {
			data[i][0] = offers.get(i).getSellerName();
			data[i][1] = offers.get(i).getOfferName();
			if(offers.get(i).getAmount() >= 0)
				data[i][2] = offers.get(i).getAmount();
			else
				data[i][2] = new String("n/c");
		}
		
		String[] columnsNames = new String[] {"Vendeur", "Offre","Prix"};
		_table.setModel(new MyTableModel(data, columnsNames));
		
		//enable/disable buttons
		if(offers.size() > 0)
			if(!_papa.isAutomatique() && _papa.getOfferBid()==null)
				_buttonBid.setEnabled(true);
			else
				_buttonBid.setEnabled(false);
		else
			_buttonBid.setEnabled(false);
	}
	
	//runnable method
	private void launch() {
		java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	try {
                    UIManager.setLookAndFeel(
                                  "javax.swing.plaf.metal.MetalLookAndFeel");
                                //  "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                                //UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                setVisible(true);
            }
        });
	}
	
	//quit request
	private void ActionQuitter(ActionEvent ae) {
		Object[] options = { "OK", "CANCEL" };
        if(JOptionPane.showOptionDialog(null, "Voulez vous quitter?", "Quitter", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]) == JOptionPane.OK_OPTION) {
            _papa.stop();
        }
	}
	
	//bid button action
	//get the infos in the selected row in the table and launch the bid method
	private void ActionBid(ActionEvent ae) {
		try{
			Offer offer = new Offer();
			offer.setSellerName((String) _table.getModel().getValueAt(_table.getSelectedRow(), 0));
			offer.setOfferName((String) _table.getModel().getValueAt(_table.getSelectedRow(), 1));
			offer.setAmount((Float) _table.getModel().getValueAt(_table.getSelectedRow(), 2));
			_buttonBid.setEnabled(false);
			_papa.setOfferBid(offer);
			_papa.bid();
		}catch(Exception e){
			ErrorMessage("Pas d'offre pour ce vendeur.");
		}
	}

	//ask the user the type of bider (manuel / auto)
	public boolean getBidProcess() {
		String[] possibilities = new String[] {"Manuel", "Automatique"};
		String response = (String) JOptionPane.showInputDialog(null, "Quel type de fonctionnement voulez-vous utiliser pour les enchères?", "Types des enchères", JOptionPane.QUESTION_MESSAGE, null, possibilities, possibilities[0]);
		if(response.equals("Manuel"))
			return false;
		else
			return true;
	}

	//ask the user the maximum amount for the auto bider
	public Float getDefaultAmount() {
		Float result = null;
		while(result == null) {
			String response = JOptionPane.showInputDialog("Quel est le montant maximum des enchères?");
			try{
				result = Float.parseFloat(response);
				if(result<0)
					throw new Exception();
			}catch(Exception e) {
				ErrorMessage("Le montant maximum doit être un nombre réel positif.");
				result = null;
			}
		}
		return result;
	}

	//display the default amount for the auto bider
	public void setLabelAmount() {
		_labelDefaultAmount.setText("Enchères max : " + _papa.getDefaultAmount());
	}

}
