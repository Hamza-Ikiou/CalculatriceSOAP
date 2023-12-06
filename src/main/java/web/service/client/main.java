package web.service.client;

import java.awt.EventQueue;

import javax.swing.*;

import web.service.client.service.Calculator;
import web.service.client.service.CalculatorSoap;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class main {

	private JFrame frmCalculatrice;
	private JTextField textNbr1;
	private JTextField textNbr2;
	private JTextField tResult;
	private JLabel lblOperateur;
	private CalculatorSoap proxy;
	private String currentOperator = "+";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main window = new main();
					window.frmCalculatrice.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public main() {
		initialize();
		initSoap();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCalculatrice = new JFrame();
		frmCalculatrice.setTitle("Calculatrice");
		frmCalculatrice.setResizable(false);
		frmCalculatrice.setBounds(100, 100, 482, 320);
		frmCalculatrice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCalculatrice.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 466, 279);
		frmCalculatrice.getContentPane().add(panel);
		panel.setLayout(null);

		textNbr1 = new JTextField();
		textNbr1.setBounds(30, 50, 160, 32);
		panel.add(textNbr1);
		textNbr1.setColumns(10);

		textNbr2 = new JTextField();
		textNbr2.setColumns(10);
		textNbr2.setBounds(275, 50, 160, 32);
		panel.add(textNbr2);

		JRadioButton rdbtbPlus = new JRadioButton("+");
		rdbtbPlus.setBackground(Color.WHITE);
		rdbtbPlus.setBounds(108, 167, 43, 32);
		panel.add(rdbtbPlus);

		JRadioButton rdbtnMoins = new JRadioButton("-");
		rdbtnMoins.setBackground(Color.WHITE);
		rdbtnMoins.setBounds(178, 167, 43, 32);
		panel.add(rdbtnMoins);

		JRadioButton rdbtnMultiply = new JRadioButton("x");
		rdbtnMultiply.setBackground(Color.WHITE);
		rdbtnMultiply.setBounds(248, 167, 38, 32);
		panel.add(rdbtnMultiply);

		JRadioButton rdbtnDivide = new JRadioButton(" /");
		rdbtnDivide.setBackground(Color.WHITE);
		rdbtnDivide.setBounds(318, 167, 37, 32);
		panel.add(rdbtnDivide);

		ButtonGroup operator = new ButtonGroup();
		operator.add(rdbtbPlus);
		operator.add(rdbtnMoins);
		operator.add(rdbtnMultiply);
		operator.add(rdbtnDivide);
		rdbtbPlus.setSelected(true);

		JButton btnCalcul = this.initCalculButton();
		panel.add(btnCalcul);

		tResult = new JTextField();
		tResult.setEnabled(false);
		tResult.setBounds(30, 125, 405, 35);
		panel.add(tResult);
		tResult.setColumns(10);

		lblOperateur = new JLabel("+");
		lblOperateur.setFont(new Font("Arial", Font.BOLD, 18));
		lblOperateur.setBounds(226, 59, 18, 14);
		panel.add(lblOperateur);

		JLabel lblNbr1 = new JLabel("Nombre 1");
		lblNbr1.setBounds(30, 25, 80, 14);
		panel.add(lblNbr1);

		JLabel lblNbr2 = new JLabel("Nombre 2");
		lblNbr2.setBounds(275, 25, 80, 14);
		panel.add(lblNbr2);

		JLabel lblNewLabel = new JLabel("Resultat");
		lblNewLabel.setBounds(30, 100, 80, 14);
		panel.add(lblNewLabel);

		rdbtnMoins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setOperator("-");
			}
		});

		rdbtbPlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { setOperator("+"); }
		});

		rdbtnMultiply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setOperator("x");
			}
		});

		rdbtnDivide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { setOperator("/"); }
		});
	}

	private JButton initCalculButton() {
		JButton btnCalcul = new JButton("Calculer");
		btnCalcul.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textNbr1.getText().trim().isEmpty() || textNbr2.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Veuillez insérer les nombres à calculer !", "Attention!", JOptionPane.WARNING_MESSAGE);
				} else {
					int a, b;
					try {
						a = Integer.parseInt(textNbr1.getText());
						b = Integer.parseInt(textNbr2.getText());
						if(b == 0 && currentOperator.equals("/")) {
							JOptionPane.showMessageDialog(null, "Division par 0 non permise!", "Erreur!", JOptionPane.ERROR_MESSAGE);
						}else {
							Calcul(a, b, currentOperator);
						}
					} catch (NumberFormatException e2) {
						JOptionPane.showMessageDialog(null, "Veuillez insérer des nombres entiers ! ", "Erreur!", JOptionPane.ERROR_MESSAGE);
					}
					catch (Exception e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage(), "Erreur!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnCalcul.setBounds(169, 226, 117, 41);
		return btnCalcul;
	}

	public void initSoap() {
		try {
			URL url = new URL("http://www.dneonline.com/calculator.asmx?WSDL");
			Calculator calc = new Calculator(url);
			proxy = calc.getCalculatorSoap();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void Calcul(int a, int b, String operator) {
        switch (operator) {
            case "+":
                tResult.setText("" + proxy.add(a, b));
                break;
            case "-":
                tResult.setText("" + proxy.subtract(a, b));
                break;
            case "x":
                tResult.setText("" + proxy.multiply(a, b));
                break;
            case "/":
                tResult.setText("" + proxy.divide(a, b));
                break;
        }
	}

	public void setOperator(String Operator) {
		lblOperateur.setText(Operator);
		currentOperator = Operator;
	}
}
