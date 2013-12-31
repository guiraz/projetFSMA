package dubraz.vendeur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class VendeurInterface extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private Vendeur _papa;
	private JButton _buttonQuit;
	private JButton _buttonAnnounce;
	private JTextField _amountTextField;
	private JTextField _minAmountTextField;
	private JTextField _stepAmountTextField;
	private JTextField _timerTextField;
	private JLabel _amountLabel;
	private JLabel _minAmountLabel;
	private JLabel _stepAmountLabel;
	private JLabel _timerLabel;

	public VendeurInterface(Vendeur papa) {
		_papa = papa;
		this.setTitle(_papa.getName().toString());
		
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
		_minAmountTextField = new JTextField();
		_stepAmountTextField = new JTextField();
		_timerTextField = new JTextField();
		_amountLabel = new JLabel("Prix : ");
		_minAmountLabel = new JLabel("Prix minimum : ");
		_stepAmountLabel = new JLabel("<html>Pas de l'évolution<br />du prix : </html>");
		_timerLabel = new JLabel("Timer (en secondes) : ");
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
		
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
		layout.setHorizontalGroup(
                layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(Alignment.LEADING)
		                .addComponent(_amountLabel)
		                .addComponent(_minAmountLabel)
		                .addComponent(_stepAmountLabel)
	                    .addComponent(_timerLabel)
	                    .addComponent(_buttonAnnounce))
                	.addGroup(layout.createParallelGroup(Alignment.CENTER)
		                .addComponent(_amountTextField)
		                .addComponent(_minAmountTextField)
		                .addComponent(_stepAmountTextField)
		                .addComponent(_timerTextField)
	                    .addComponent(_buttonQuit))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(Alignment.CENTER)
		                .addComponent(_amountLabel)
	                    .addComponent(_amountTextField))
                    .addGroup(layout.createParallelGroup(Alignment.CENTER)
		                .addComponent(_minAmountLabel)
	                    .addComponent(_minAmountTextField))
                    .addGroup(layout.createParallelGroup(Alignment.CENTER)
		                .addComponent(_stepAmountLabel)
	                    .addComponent(_stepAmountTextField))
                    .addGroup(layout.createParallelGroup(Alignment.CENTER)
		                .addComponent(_timerLabel)
	                    .addComponent(_timerTextField))
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
	
	public void InfoMessage(String mess) {
		JOptionPane.showMessageDialog(this, mess, "Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
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
	
	private void ActionQuitter(ActionEvent ae) {
		Object[] options = { "OK", "CANCEL" };
        if(JOptionPane.showOptionDialog(null, "Voulez vous quitter?", "Quitter", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]) == JOptionPane.OK_OPTION) {
            _papa.stop();
        }
	}
	
	private void ActionAnnounce(ActionEvent ae) {
		if(_amountTextField.getText().isEmpty() || _timerTextField.getText().isEmpty() || _minAmountTextField.getText().isEmpty() || _stepAmountTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Tout les champs doivent être renseignés!", " Erreur de saisie ", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			Float amount, minAmount, stepAmount;
			Long timer;
			try{
				amount = Float.parseFloat(_amountTextField.getText());
				if(amount<=0)
					throw new Exception("Le prix doit être un réel positif.");
				
				minAmount = Float.parseFloat(_minAmountTextField.getText());
				if(minAmount<=0)
					throw new Exception("Le prix minimum doit être un réel positif.");
				if(minAmount>=amount)
					throw new Exception("Le prix minimum doit être strictement inférieur au prix d'origine.");
				
				stepAmount = Float.parseFloat(_stepAmountTextField.getText());
				if(stepAmount<=0)
					throw new Exception("Le pas doit être un réel positif.");
				if(stepAmount>amount)
					throw new Exception("Le pas doit être inférieur ou égal au prix d'origine.");
				
				timer = Long.parseLong(_timerTextField.getText());
				if(timer<=0 || timer>60)
					throw new Exception("Le timer doit être un entier entre ]0..60].");
				
				_papa.setAmount(amount);
				_papa.setMinAmount(minAmount);
				_papa.setStepAmount(stepAmount);
				_papa.setTimer(timer);
				_papa.announce();
				_amountTextField.setText("");
				_timerTextField.setText("");
				_minAmountTextField.setText("");
				_stepAmountTextField.setText("");
			}
			catch(Exception e) {
				if(e.getClass() == new NumberFormatException().getClass())
					JOptionPane.showMessageDialog(this, "Les entrées doivent être des réels positifs ou un entier pour le timer.", " Erreur de saisie ", JOptionPane.ERROR_MESSAGE);
				else
					JOptionPane.showMessageDialog(this, e.getMessage(), " Erreur de saisie ", JOptionPane.ERROR_MESSAGE);
				_amountTextField.setText("");
				_timerTextField.setText("");
				_minAmountTextField.setText("");
				_stepAmountTextField.setText("");
			}
		}
	}
	
}
