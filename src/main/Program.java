package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Program {

	private JFrame frame;
	private JTextField textImageTest;
	private JTextField textAnswer;
	private ResourceHandler rHandler;
	private NeuralNetwork network;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Program window = new Program();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Program() {
		rHandler = new ResourceHandler();
		try {
			rHandler.init();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		network = new NeuralNetwork(0.1);
		network.init();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JButton btnCompute = new JButton("Compute");
		btnCompute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int resourceNum = Integer.parseInt(textImageTest.getText()) - 1;
				int[] iData = rHandler.getResource(resourceNum);
				float[] fData = new float[iData.length];
				
				for (int i = 0; i < iData.length; i++) {
					fData[i] = ((float)iData[i]/255.0f);
				}
				
				int answer = network.analyze(fData);
				textAnswer.setText("Real Value: " + rHandler.getLabel(resourceNum) + " Returned Value: " + answer);
			}
		});
		frame.getContentPane().add(btnCompute, BorderLayout.CENTER);
		
		textImageTest = new JTextField();
		textImageTest.setText("1");
		textImageTest.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(textImageTest, BorderLayout.SOUTH);
		textImageTest.setColumns(10);
		
		textAnswer = new JTextField();
		textAnswer.setEditable(false);
		frame.getContentPane().add(textAnswer, BorderLayout.NORTH);
		textAnswer.setColumns(10);
	}

}
