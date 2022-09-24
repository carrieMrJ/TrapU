package nowhere2gopp.inputOutput;

import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nowhere2gopp.chessBoard.Checkerboard;
import nowhere2gopp.preset.PlayerColor;

/**
 * Project 6) tournament
 * Panel for Tournament Frame
 * @author Junqi.Sun
 *
 */
public class TournamentPanel extends JPanel{

	protected JLabel redAgent, blueAgent, pl1Color, pl2Color, 
	turnLabel2, statusLabel2, roundLabel2, phaseLabel2;
	protected JPanel jpUp, jpBot;
	
	Font font = new Font("American Typewriter", Font.BOLD, 16);
	
	protected int match;
	protected int redWin;
	protected int blueWin;
	
	

	/**
	 * constructor
	 * @param c Checkerboard
	 * @param match current match
	 * @param redWin current redWin count
	 * @param blueWin current blueWin count
	 */
	public TournamentPanel(Checkerboard c, int match, int redWin, int blueWin) {

		this.match = match;
		this.redWin = redWin;
		this.blueWin = blueWin;
		
		setLayout(new BorderLayout());
		
		
		
		/*first Panel*/
		jpUp = new JPanel();
		jpUp.setLayout(new GridLayout(0, 2, 0, 30));
		
		
		
		/*agent location*/
		redAgent = new JLabel("Red Current Site:  " + c.viewer().getAgent(PlayerColor.Red));
		redAgent.setFont(font);
		blueAgent = new JLabel("Blue Current Site:  " + c.viewer().getAgent(PlayerColor.Blue));
		blueAgent.setFont(font);
		jpUp.add(redAgent);
		jpUp.add(blueAgent);
		
		
		
		/*game turn*/
		JLabel turnLabel1 = new JLabel("Turn:");
		turnLabel1.setFont(font);
		turnLabel2 = new JLabel("" + c.viewer().getTurn());
		turnLabel2.setFont(font);
		turnLabel2.setForeground(Color.RED);
		jpUp.add(turnLabel1);
		jpUp.add(turnLabel2);
		
		
		
		/*game status*/
		JLabel statusLabel1 = new JLabel("Game Status:");
		statusLabel1.setFont(font);
		statusLabel2 = new JLabel("" + c.viewer().getStatus());
		statusLabel2.setFont(font);
		jpUp.add(statusLabel1);
		jpUp.add(statusLabel2);
		
		
		
		/*game round*/
		JLabel roundLabel1 = new JLabel("Game Round:");
		roundLabel1.setFont(font);
		roundLabel2 = new JLabel("" + c.viewer().getRound());
		roundLabel2.setFont(font);
		jpUp.add(roundLabel1);
		jpUp.add(roundLabel2);
		
		
		
		/*phase change*/
		JLabel phaseLabel1 = new JLabel("Phase Change at:");
		phaseLabel1.setFont(font);
		phaseLabel2 = new JLabel("Round " + c.viewer().getPhaseChange());
		phaseLabel2.setFont(font);
		jpUp.add(phaseLabel1);
		jpUp.add(phaseLabel2);
		
		
		
		/*second Panel*/
		jpBot = new JPanel();
		jpBot.setLayout(new GridLayout(0, 2, 0, 30));
		
		
		
		/*PlayerColor*/
		if(match % 2 == 1) {
			pl1Color = new JLabel("Player1:   Red");
			pl1Color.setFont(font);
			pl1Color.setForeground(Color.RED);
			pl2Color = new JLabel("Player2:   Blue");
			pl2Color.setFont(font);
			pl2Color.setForeground(Color.BLUE);
		}
		else {
			pl1Color = new JLabel("Player1:   Blue");
			pl1Color.setFont(font);
			pl1Color.setForeground(Color.BLUE);
			pl2Color = new JLabel("Player2:   Red");
			pl2Color.setFont(font);
			pl2Color.setForeground(Color.RED);
		}
		jpBot.add(pl1Color);
		jpBot.add(pl2Color);
		
		
		
		/*current Match*/
		JLabel matchLabel1 = new JLabel("current Match:");
		matchLabel1.setFont(font);
		JLabel matchLabel2 = new JLabel("" + match);
		matchLabel2.setFont(font);
		jpBot.add(matchLabel1);
		jpBot.add(matchLabel2);
		
		
		
		/*player1 statistics*/
		JLabel pl1Label1 = new JLabel("Player1 credits:");
		pl1Label1.setFont(font);
		JLabel pl1Label2;
		pl1Label2 = new JLabel("" + redWin);
		pl1Label2.setFont(font);
		jpBot.add(pl1Label1);
		jpBot.add(pl1Label2);
		
		
		
		/*player2 statistics*/
		JLabel pl2Label1 = new JLabel("Player2 credits:");
		pl2Label1.setFont(font);
		JLabel pl2Label2;
		pl2Label2 = new JLabel("" + blueWin);
		pl2Label2.setFont(font);
		jpBot.add(pl2Label1);
		jpBot.add(pl2Label2);
		
		
		
		/*the main Panel*/
		add(jpUp, BorderLayout.NORTH);
		add(jpBot, BorderLayout.SOUTH);
	}
		
	
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500 , 600);
	}
	
	
	
	private static final long serialVersionUID = 1L;
}