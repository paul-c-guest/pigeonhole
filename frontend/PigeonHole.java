package frontend;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import backend.Analyse;
import backend.Filter;

public class PigeonHole {

	private static Path TARGET_FOLDER = null;

	private int POS_X;
	private int POS_Y;
	private int WIDTH;
	private int HEIGHT;

	private JFrame frame;
	private JTextArea textFieldChooser;
	private JButton buttonFolderChooser;
	private JButton buttonLaunchFilter;
	private JButton buttonLaunchAnalysis;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					PigeonHole window = new PigeonHole();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PigeonHole() {
		initEnvironment();
		initFrame();
		initComponents();
	}

	/**
	 * set up values for size and positioning of window in the
	 * {@link GraphicsEnvironment}
	 */
	private void initEnvironment() {
		// dimensions for UI window
		WIDTH = 350;
		HEIGHT = 140;

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();

		int SCREEN_WIDTH = device.getDisplayMode().getWidth();
		int SCREEN_HEIGHT = device.getDisplayMode().getHeight();

		// set coords to initially position window near centre/top
		POS_X = (SCREEN_WIDTH / 2) - (int) (WIDTH / 1.33);
		POS_Y = SCREEN_HEIGHT / 5;
	}

	/**
	 * set the relevant values of the {@link JFrame}
	 */
	private void initFrame() {
		frame = new JFrame();
		frame.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Pigeon Hole");
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
	}

	/**
	 * set up and place the UI components
	 */
	private void initComponents() {
		textFieldChooser = new JTextArea();
		textFieldChooser.setPreferredSize(new Dimension(180, 24));
		textFieldChooser.setEditable(false);

		buttonFolderChooser = new JButton("Select Folder");
		buttonFolderChooser.addActionListener(getFolderChooserAction());

		buttonLaunchFilter = new JButton("Filter");
		buttonLaunchFilter.addActionListener(getLaunchFilterAction());

		buttonLaunchAnalysis = new JButton("Analyse");
		buttonLaunchAnalysis.addActionListener(getLaunchAnalysisAction());

		frame.getContentPane().add(buttonFolderChooser);
		frame.getContentPane().add(textFieldChooser);
		frame.getContentPane().add(buttonLaunchFilter);
		frame.getContentPane().add(buttonLaunchAnalysis);

		setButtonStates(false);
	}

	private ActionListener getFolderChooserAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setApproveButtonText("Select");
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					try {
//						System.out.print("changing text field from " + TARGET_FOLDER);
						TARGET_FOLDER = chooser.getSelectedFile().toPath();
						textFieldChooser.setText(TARGET_FOLDER.toString());
						setButtonStates(true);
//						System.out.println(" to " + TARGET_FOLDER);
					} catch (Exception e1) {
						TARGET_FOLDER = null;
						setButtonStates(false);
					}
				}
			}
		};
	}

	private ActionListener getLaunchFilterAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						new Filter(TARGET_FOLDER);
					}
				}).start();
			}
		};
	}

	private ActionListener getLaunchAnalysisAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						new Analyse(TARGET_FOLDER);
					}
				}).start();
			}
		};
	}

	// for locking out user clicks until a folder has been chosen
	private void setButtonStates(boolean state) {
		buttonLaunchFilter.setEnabled(state);
		buttonLaunchAnalysis.setEnabled(state);
	}
}
