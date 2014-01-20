package dubraz.marche;

import utilities.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

public class MarcheInterface extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	//parent agent
	private Marche _papa;
	private JButton _buttonQuit;
	private JTable _table;
	private JScrollPane _scrollPaneTable;
	
	public MarcheInterface(Marche papa) {
		_papa = papa;
		setTitle(_papa.getName());
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		_buttonQuit = new JButton("Quitter");
		_buttonQuit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionQuitter(e);
			}
		});
		
		Object[][] d = new Object[0][3];
		_table = new JTable(new MyTableModel(d, new String[] {"Vendeur", "Offre","Prix"}));
		_table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		
		_scrollPaneTable = new JScrollPane();
		_scrollPaneTable.setViewportView(_table);
        _scrollPaneTable.setMinimumSize(new Dimension(200, 200));
		
		_table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
	                .addComponent(_scrollPaneTable)
                    .addComponent(_buttonQuit)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
	                .addComponent(_scrollPaneTable)
                    .addComponent(_buttonQuit)
        );
        
        pack();
		
		launch();
	}
	
	//display all offers
	public void RessourcesUpdated() {
		List<Offer> offers = _papa.getOffers();
		
		Object[][] d = new Object[offers.size()][3];
		for(int i=0; i<offers.size(); i++) {
			d[i][0] = offers.get(i).getSellerName();
			d[i][1] = offers.get(i).getOfferName();
			if(offers.get(i).getAmount() >= 0)
				d[i][2] = offers.get(i).getAmount();
			else
				d[i][2] = "n/c";
		}
		
		_table.setModel(new MyTableModel(d, new String[] {"Vendeur", "Offre","Prix"}));
	}
	
	//display error message 'mess'
	public void ErrorMessage(String mess) {
		JOptionPane.showMessageDialog(this, mess, "Error", JOptionPane.ERROR_MESSAGE);
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

}
