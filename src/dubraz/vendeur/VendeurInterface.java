package dubraz.vendeur;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

import utilities.MyTableModel;

public class VendeurInterface extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private Vendeur _papa;
	private JButton _buttonQuit;
	private JButton _buttonAnnounce;
	private JTextField _amountTextField;
	private JTextField _timerTextField;
	private JTable _clientTable;
	private JScrollPane _scrollPaneTable;
	private JLabel _amountLabel;
	private JLabel _timerLabel;
	private JLabel _clientLabel;

	public VendeurInterface(Vendeur papa) {
		_papa = papa;
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		_buttonQuit = new JButton("Quitter");
		_buttonQuit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionQuitter(e);
			}
		});
		
		_buttonAnnounce = new JButton("Annoncer le prix");
		_buttonAnnounce.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionAnnounce(e);
			}
		});
		
		_amountTextField = new JTextField();
		_timerTextField = new JTextField();
		_clientTable = new JTable(1,1);
		_scrollPaneTable = new JScrollPane();
		_amountLabel = new JLabel("Prix : ");
		_timerLabel = new JLabel("Timer : ");
		_clientLabel = new JLabel("Clients : ");
		
		_scrollPaneTable.setViewportView(_clientTable);
		_scrollPaneTable.setMinimumSize(new Dimension(200, 200));
		
		_clientTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
		
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
		layout.setHorizontalGroup(
                layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(Alignment.LEADING)
		                .addComponent(_amountLabel)
	                    .addComponent(_timerLabel)
	                    .addComponent(_clientLabel)
	                    .addComponent(_buttonAnnounce))
                	.addGroup(layout.createParallelGroup(Alignment.CENTER)
		                .addComponent(_amountTextField)
		                .addComponent(_timerTextField)
		                .addComponent(_scrollPaneTable)
	                    .addComponent(_buttonQuit))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(Alignment.CENTER)
		                .addComponent(_amountLabel)
	                    .addComponent(_amountTextField))
                    .addGroup(layout.createParallelGroup(Alignment.CENTER)
		                .addComponent(_timerLabel)
	                    .addComponent(_timerTextField))
                    .addGroup(layout.createParallelGroup(Alignment.CENTER)
		                .addComponent(_clientLabel)
	                    .addComponent(_scrollPaneTable))
	                .addGroup(layout.createParallelGroup(Alignment.CENTER)
		                .addComponent(_buttonAnnounce)
	                    .addComponent(_buttonQuit))
        );
		
		pack();
		
		launch();
	}
	
	public void ErrorMessage(String mess) {
		JOptionPane.showMessageDialog(this, mess, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void reset() {
		_timerTextField.setEditable(true);
		_amountTextField.setEditable(true);
		_buttonAnnounce.setEnabled(true);
		_clientTable.setModel(new MyTableModel(null, new String[] {"Clients"}));
	}
	
	public String getAmount() {
		return _amountTextField.getText();
	}
	
	public String getTimer() {
		return _timerTextField.getText();
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
	
	private void ActionAnnounce(ActionEvent ae) {
		if(_amountTextField.getText().isEmpty() || _timerTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Timer et prix doivent être renseigné", " Erreur de saisie ", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			Float amount;
			Long timer;
			try{
				amount = Float.parseFloat(_amountTextField.getText());
				_papa.setAmount(amount);
				timer = Long.parseLong(_timerTextField.getText());
				_papa.setTimer(timer);
				_papa.announce();
				_buttonAnnounce.setEnabled(false);
				_timerTextField.setEditable(false);
				_amountTextField.setEditable(false);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(this, "Timer doit être un entier et prix doit être un réel", " Erreur de saisie ", JOptionPane.ERROR_MESSAGE);
				_amountTextField.setText("");
				_timerTextField.setText("");
			}
		}
	}
	
}
