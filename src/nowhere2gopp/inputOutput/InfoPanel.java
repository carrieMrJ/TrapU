package nowhere2gopp.inputOutput;

import java.awt.*;
import javax.swing.*;

import nowhere2gopp.chessBoard.Checkerboard;
import nowhere2gopp.preset.PlayerColor;

/**
 * Project 3d)
 * the InfoPanel implements the panel containing the input and output including
 * status information for the game
 * @author Junqi.Sun
 *
 */
public class InfoPanel extends JPanel{

	protected JButton moveBtn, surrBtn, saveBtn, loadBtn;
	protected JLabel redAgent, blueAgent, turnLabel2, statusLabel2, roundLabel2, phaseLabel2;
	protected JTextField inputText;
	private Dimension btnSize = new Dimension(100, 40);
	
	Font font = new Font("American Typewriter", Font.BOLD, 16);
	
	
	
	/**
	 * constructor for the panel with the input and output including
	 * status information for the game
	 * @param c Checkerboard
	 */
	public InfoPanel(Checkerboard c) {

		setLayout(new BorderLayout());
		
		
		
		/* first Panel with the input field and the status information*/
		JPanel jpUp = new JPanel();
		jpUp.setLayout(new GridLayout(0, 2, 5, 20));
		
		
		
		/*text input row*/
		JLabel inputLabel = new JLabel("Move Input:");
		inputLabel.setFont(font);
		JPanel inputPanel = new JPanel();
		inputText = new JTextField(15);
		inputText.setFont(font);
		
		
		
		/*use FlowLayout to avoid automatic width change, 0 refers to left align*/
		inputPanel.setLayout(new FlowLayout(0));
		inputPanel.add(inputText);
		
		
		
		jpUp.add(inputLabel);
		jpUp.add(inputPanel);
		
		
		
		/*agent location*/
		redAgent = new JLabel("Red Current Site:  " + c.viewer().getAgent(PlayerColor.Red));
		redAgent.setFont(font);
		blueAgent = new JLabel("Blue Current Site:  " + c.viewer().getAgent(PlayerColor.Blue));
		blueAgent.setFont(font);
		
		
		
		/*game turn*/
		JLabel turnLabel1 = new JLabel("Turn:");
		turnLabel1.setFont(font);
		turnLabel2 = new JLabel("" + c.viewer().getTurn());
		turnLabel2.setFont(font);
		turnLabel2.setForeground(Color.RED);

		
		
		/*game status*/
		JLabel statusLabel1 = new JLabel("Game Status:");
		statusLabel1.setFont(font);
		statusLabel2 = new JLabel("" + c.viewer().getStatus());
		statusLabel2.setFont(font);
		
		
		
		/*game round*/
		JLabel roundLabel1 = new JLabel("Game Round:");
		roundLabel1.setFont(font);
		roundLabel2 = new JLabel("" + c.viewer().getRound());
		roundLabel2.setFont(font);
		
		
		
		/*phase change after 2^(k-1) round*/
		JLabel phaseLabel1 = new JLabel("Phase Change at:");
		phaseLabel1.setFont(font);
		phaseLabel2 = new JLabel("Round " + c.viewer().getPhaseChange());
		phaseLabel2.setFont(font);
		
		
		
		jpUp.add(redAgent);
		jpUp.add(blueAgent);
		jpUp.add(turnLabel1);
		jpUp.add(turnLabel2);
		jpUp.add(statusLabel1);
		jpUp.add(statusLabel2);
		jpUp.add(roundLabel1);
		jpUp.add(roundLabel2);
		jpUp.add(phaseLabel1);
		jpUp.add(phaseLabel2);
		
		
		
		/*second Panel with the buttons*/
		JPanel buttonPanel = new JPanel();
		JPanel buttonPanel1 = new JPanel();
		JPanel buttonPanel2 = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 1, 5, 20));
		buttonPanel1.setLayout(new FlowLayout(FlowLayout.CENTER, 160, 0));
		buttonPanel2.setLayout(new FlowLayout(FlowLayout.CENTER, 160, 0));
		
		
		
		moveBtn = new JButton("Move");
		surrBtn = new JButton("Surrender");
		saveBtn = new JButton("Save");
		loadBtn = new JButton("Load");
		moveBtn.setPreferredSize(btnSize);
		surrBtn.setPreferredSize(btnSize);
		saveBtn.setPreferredSize(btnSize);
		loadBtn.setPreferredSize(btnSize);
		
		
		
		buttonPanel1.add(moveBtn);
		buttonPanel1.add(surrBtn);
		buttonPanel2.add(saveBtn);
		buttonPanel2.add(loadBtn);
		buttonPanel.add(buttonPanel1);
		buttonPanel.add(buttonPanel2);

		
		
		/*the combined main Panel*/
		add(jpUp, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	
	
	/**
	 * getter for text in inputText field
	 * @return String
	 */
	public String getInput() {
		return inputText.getText();
	}
	
	
	
	/**
	 * paints a vertical line left to the info panel
	 * @param g Graphics 
	 */
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, 0, 0, getHeight());
	}
	
	
	
	/**
	 * method to get the preferred dimension of this panel
	 * @return Dimension
	 */	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500 , 600);
	}
	
	
	
	private static final long serialVersionUID = 1L;
}
