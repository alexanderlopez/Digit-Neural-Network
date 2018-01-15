package main;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Program {

	private JFrame frame;
	private JTextField numberField;
	private JTextField answerField;
	private JLabel lblImage;

	private NeuralNetwork network;
	private ResourceHandler rHandler;
	
	private boolean isTestSet = false;
	
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmChangeSet;
	private JMenuItem mntmExportNetworkConf;
	private JMenuItem mntmImportNetworkConf;
	private JMenuItem mntmVerifyAccuracy;
	
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
			rHandler.init(ResourceHandler.TRAIN_SET);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		network = new NeuralNetwork(0.1);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnCompute = new JButton("Compute");
		btnCompute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = Integer.parseInt(numberField.getText());
				int[] idata = rHandler.getResource(index - 1);
				float[] fData = new float[idata.length];
				
				for (int i = 0; i < fData.length; i++)
					fData[i] = (float)idata[i]/255.0f;
				
				int answer = network.analyze(fData);
				
				answerField.setText("Result: " + answer);
				lblImage.setIcon(new ImageIcon(rHandler.getResourceAsImage(index - 1)));
			}
		});
		frame.getContentPane().add(btnCompute);
		
		JButton btnTrain = new JButton("Train");
		btnTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				network.train(rHandler, 600);
				answerField.setText("Training Complete");
			}
		});
		frame.getContentPane().add(btnTrain);
		
		numberField = new JTextField();
		numberField.setHorizontalAlignment(SwingConstants.CENTER);
		numberField.setText("1");
		frame.getContentPane().add(numberField);
		numberField.setColumns(10);
		
		lblImage = new JLabel("");
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblImage);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numberField.setText(Integer.toString(Integer.parseInt(numberField.getText()) + 1));

				lblImage.setIcon(new ImageIcon(rHandler.getResourceAsImage(Integer.parseInt(numberField.getText()) - 1)));
			}
		});
		frame.getContentPane().add(btnNext);
		
		answerField = new JTextField();
		answerField.setHorizontalAlignment(SwingConstants.CENTER);
		answerField.setEditable(false);
		frame.getContentPane().add(answerField);
		answerField.setColumns(10);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmChangeSet = new JMenuItem("Change to Test Set");
		mntmChangeSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (isTestSet) {
						rHandler.init(ResourceHandler.TRAIN_SET);
						mntmChangeSet.setText("Change to Test Set");
					} else {
						rHandler.init(ResourceHandler.TEST_SET);
						mntmChangeSet.setText("Change to Train Set");
					}
				} catch (IOException n) {
					n.printStackTrace();
				}
				
				isTestSet = !isTestSet;
			}
		});
		mnFile.add(mntmChangeSet);
		
		mntmExportNetworkConf = new JMenuItem("Export Network Conf.");
		mntmExportNetworkConf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser choose = new JFileChooser();
				choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				choose.showSaveDialog(frame);
				File path = choose.getSelectedFile();
				
				if (!path.exists())
					return;
				
				Matrix l1W = network.getLayerWeights(0);
				Matrix l2W = network.getLayerWeights(1);
				Matrix olW = network.getLayerWeights(2);
				Matrix l1B = network.getLayerBiases(0);
				Matrix l2B = network.getLayerBiases(1);
				Matrix olB = network.getLayerBiases(2);
				
				File l1WP = new File(path, "l1W.csv");
				File l2WP = new File(path, "l2W.csv");
				File olWP = new File(path, "olW.csv");
				File l1BP = new File(path, "l1B.csv");
				File l2BP = new File(path, "l2B.csv");
				File olBP = new File(path, "olB.csv");
				
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(l1WP));
					bw.write(Matrix.toCSV(l1W));
					bw.close();
					
					bw = new BufferedWriter (new FileWriter(l2WP));
					bw.write(Matrix.toCSV(l2W));
					bw.close();
					
					bw = new BufferedWriter (new FileWriter(olWP));
					bw.write(Matrix.toCSV(olW));
					bw.close();
					
					bw = new BufferedWriter (new FileWriter(l1BP));
					bw.write(Matrix.toCSV(l1B));
					bw.close();
					
					bw = new BufferedWriter (new FileWriter(l2BP));
					bw.write(Matrix.toCSV(l2B));
					bw.close();
					
					bw = new BufferedWriter (new FileWriter(olBP));
					bw.write(Matrix.toCSV(olB));
					bw.close();
				} catch (IOException n) {
					n.printStackTrace();
				}
				
				answerField.setText("Exported");
			}
		});
		mnFile.add(mntmExportNetworkConf);
		
		mntmImportNetworkConf = new JMenuItem("Import Network Conf.");
		mntmImportNetworkConf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mnFile.add(mntmImportNetworkConf);
		
		mntmVerifyAccuracy = new JMenuItem("Verify Accuracy");
		mntmVerifyAccuracy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int count = (isTestSet) ? 10000 : 60000;
				int correct = 0;
				
				for (int i = 0; i < count; i++) {
					int[] idata = rHandler.getResource(i);
					float[] fData = new float[idata.length];
					
					for (int j = 0; j < fData.length; j++)
						fData[j] = (float)idata[j]/255.0f;
					
					int answer = network.analyze(fData);
					
					if (answer == rHandler.getLabel(i))
						correct++;
				}
				double answer = (double)Math.round(((double)correct/(double)count)*10000.0d)/100.0d;
				answerField.setText("Accuracy: " + answer + "%");
			}
		});
		mnFile.add(mntmVerifyAccuracy);
	}

}
