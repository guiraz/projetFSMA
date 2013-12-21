package dubraz.acheteur;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import utilities.MyTableModel;

public class AcheteurInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Acheteur _papa;
	private JButton _buttonQuit;
	private JButton _buttonBid;
	private JTable _table;
	private JScrollPane _scrollPane;
	
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
		
		_table = new JTable(new Object[0][2], new String[] {"Vendeur","Prix"});
		_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_scrollPane = new JScrollPane();
		_scrollPane.setViewportView(_table);
		_scrollPane.setMinimumSize(new Dimension(200, 200));
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                	.addComponent(_scrollPane)
                	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                			.addComponent(_buttonBid)
                			.addComponent(_buttonQuit))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            			.addComponent(_scrollPane)
            			.addComponent(_buttonBid))
        			.addComponent(_buttonQuit)
        );
        
        pack();
		
		launch();
	}
	
	public void ErrorMessage(String mess) {
		JOptionPane.showMessageDialog(this, mess, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void ressourcesUpdated() {
		Object[][] data = new Object[_papa.getSellersNames().size()][2];
		for(int i=0; i<_papa.getSellersNames().size(); i++) {
			data[i][0] = _papa.getSellersNames().get(i);
			data[i][1] = _papa.getAmounts().get(i);
		}
		
		String[] columnsNames = new String[] {"Vendeur", "Prix"};
		_table.setModel(new MyTableModel(data, columnsNames));
		
		if(_papa.getSellersNames().size() > 0)
			_buttonBid.setEnabled(true);
		else
			_buttonBid.setEnabled(false);
	}
	
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
	
	private void ActionQuitter(ActionEvent ae) {
		Object[] options = { "OK", "CANCEL" };
        if(JOptionPane.showOptionDialog(null, "Voulez vous quitter?", "Quitter", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]) == JOptionPane.OK_OPTION) {
            _papa.stop();
        }
	}
	
	private void ActionBid(ActionEvent e) {
		//todo
	}

}
