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
	
	private Marche _papa;
	private JButton _buttonQuit;
	private JTable _table;
	private JScrollPane _scrollPaneTable;
	
	public MarcheInterface(Marche papa) {
		_papa = papa;
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		_buttonQuit = new JButton("Quitter");
		_buttonQuit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionQuitter(e);
			}
		});
		
		Object[][] d = new Object[0][2];
		_table = new JTable(new MyTableModel(d, new String[] {"Vendeur","Prix"}));
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
	
	public void RessourcesUpdated() {
		List<String> names = _papa.getSellersNames();
		List<Float> amounts = _papa.getAmounts();
		
		Object[][] d = new Object[names.size()][2];
		for(int i=0; i<names.size(); i++) {
			d[i][0] = names.get(i);
			d[i][1] = amounts.get(i);
		}
		
		_table.setModel(new MyTableModel(d, new String[] {"Vendeur","Prix"}));
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

}
