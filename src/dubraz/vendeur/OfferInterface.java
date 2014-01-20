package dubraz.vendeur;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

import utilities.MyTableModel;

//gui of an offer of a Seller agent
public class OfferInterface extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	//parent agent
	private OfferBehaviour _papa;
	
	private JLabel _amount, _minAmount, _step, _timer;
	
	private JTable _table;
	private JScrollPane _scrollPane;
	
	public OfferInterface(OfferBehaviour papa) {
		_papa = papa;
		setTitle(_papa.getOffer().getOfferName() + "@" + _papa.getOffer().getSellerName());
		
		_table = new JTable(new MyTableModel(new Object[0][1], new String[] {"Clients"}));
		_scrollPane = new JScrollPane();
		_table.setMinimumSize(new Dimension(200, 200));
		_scrollPane.setViewportView(_table);
		
		_amount = new JLabel();
		_minAmount = new JLabel();
		_step = new JLabel();
		_timer = new JLabel();
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
		
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
		layout.setHorizontalGroup(
                layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup()
            			.addComponent(_amount)
            			.addComponent(_minAmount)
            			.addComponent(_step)
            			.addComponent(_timer)
            			.addComponent(_scrollPane))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                	.addComponent(_amount)
        			.addComponent(_minAmount)
        			.addComponent(_step)
        			.addComponent(_timer)
        			.addComponent(_scrollPane)
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
	
	//runnable method
	private void launch() {
		java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	try {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                setVisible(true);
            }
        });
	}

	//displaying offer's specifications and bidders
	public void update() {
		List<String> clients = _papa.getClients();
		
		if(clients != null) {
			Object[][] data = new Object[clients.size()][1];
			for(int i=0; i<clients.size(); i++) {
				data[i][0] = clients.get(i);
			}
			_table.setModel(new MyTableModel(data, new String[] {"Clients"}));
		}
		
		_amount.setText("Prix : " + _papa.getAmount());
		_minAmount.setText("Prix minimum : " + _papa.getMinAmount());
		_step.setText("Pas d'évolution du prix : " + _papa.getStepAmount());
		_timer.setText("Timer : " + _papa.getTimer());
	}
}
