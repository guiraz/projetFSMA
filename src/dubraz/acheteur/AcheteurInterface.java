package dubraz.acheteur;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class AcheteurInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Acheteur _papa;
	private JButton _buttonQuit;
	private JTable _table;
	private JScrollPane _scrollPane;
	
	public AcheteurInterface(Acheteur papa) {
		_papa = papa;
		
		_buttonQuit = new JButton("Quitter");
		_buttonQuit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionQuitter(e);
			}
		});
		
		_table = new JTable(new Object[0][3], new String[] {"Vendeur","Prix","Enchérir"});
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
                    .addComponent(_buttonQuit)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
	                .addComponent(_scrollPane)
                    .addComponent(_buttonQuit)
        );
        
        pack();
		
		launch();
	}
	
	public void ErrorMessage(String mess) {
		JOptionPane.showMessageDialog(this, mess, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public String getMarcketName() {
		String result = null;
		while(result==null || result.equals("")) {
			result = JOptionPane.showInputDialog("Nom du marché :");
		}
		return result;
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
