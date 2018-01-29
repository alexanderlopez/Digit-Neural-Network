package display;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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

public class Display {

	private JFrame frame;
	private JTextField txtLabel;
	private ImageHandler handler;
	private JTextField position;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Display window = new Display();
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
	public Display() {
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
		
		JLabel lblDigit = new JLabel("Image");
		frame.getContentPane().add(lblDigit, BorderLayout.CENTER);
		
		txtLabel = new JTextField();
		txtLabel.setHorizontalAlignment(SwingConstants.CENTER);
		txtLabel.setEditable(false);
		txtLabel.setText("Label: ");
		frame.getContentPane().add(txtLabel, BorderLayout.SOUTH);
		txtLabel.setColumns(10);
		
		JButton btnGo = new JButton("Go");
		btnGo.setEnabled(false);
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblDigit.setIcon(new ImageIcon(handler.getImage(Integer.parseInt(position.getText())-1)));
				txtLabel.setText("Label: " + handler.getLabel(Integer.parseInt(position.getText())-1));
			}
		});
		frame.getContentPane().add(btnGo, BorderLayout.WEST);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handler = new ImageHandler();
				try {
					handler.loadData();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				lblDigit.setIcon(new ImageIcon(handler.getImage(Integer.parseInt(position.getText())-1)));
				txtLabel.setText("Label: " + handler.getLabel(Integer.parseInt(position.getText())-1));
				btnLoad.setEnabled(false);
				btnGo.setEnabled(true);
			}
		});
		frame.getContentPane().add(btnLoad, BorderLayout.NORTH);
		
		position = new JTextField();
		position.setText("1");
		frame.getContentPane().add(position, BorderLayout.EAST);
		position.setColumns(10);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExportImage = new JMenuItem("Export Image");
		mntmExportImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.showSaveDialog(frame);
				
				File path = chooser.getSelectedFile();
				
				try {
					ImageIO.write(handler.getRawImage(Integer.parseInt(position.getText())-1), "png", path);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		mnFile.add(mntmExportImage);
	}

}
