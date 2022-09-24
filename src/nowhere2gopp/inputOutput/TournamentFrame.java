package nowhere2gopp.inputOutput;

import java.awt.*;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.TitledBorder;

import nowhere2gopp.chessBoard.Checkerboard;
import nowhere2gopp.preset.*;

/**
 * Project 3d) 6) tournament
 * implements the extended Viewer interfaces for the game in 
 * tournament mode 
 * @author Junqi.Sun
 *
 */
public class TournamentFrame implements Viewer {
	
	private JFrame frame;
	private TournamentPanel tourP;
	private BoardPanel boardP;
	
	private Checkerboard c;
	
	private int length;
	private int match = 1;
	private int redWin = 0;
	private int blueWin = 0;
	
	
	
	/**
	 * constructor of Frame
	 * @param length the length of Checkerboard
	 */
	public TournamentFrame(int length) {
		frame = new JFrame();
		this.length = length;
		c = new Checkerboard(length);
		
		/*in case of lost vision of Panel*/
		if(length < 11)
			frame.setMinimumSize(new Dimension(1116 , 600));
		else
			frame.setMinimumSize(new Dimension(1316 , 800));
		
		frame.setLayout(new GridLayout(1, 2, 2, 0));
	    boardP = new BoardPanel(c.getCircleField(), c);
	    tourP = new TournamentPanel(c, match, redWin, blueWin);
	    
	    
		Font titleFont = new Font("AppleGothic", Font.ROMAN_BASELINE, 18);
	    boardP.setBorder(BorderFactory.createTitledBorder(null, "Board panel", TitledBorder.CENTER, TitledBorder.TOP, titleFont));
	    tourP.jpUp.setBorder(BorderFactory.createTitledBorder(null, "Info panel", TitledBorder.CENTER, TitledBorder.TOP, titleFont));
	    tourP.jpBot.setBorder(BorderFactory.createTitledBorder(null, "Statistics panel", TitledBorder.CENTER, TitledBorder.TOP, titleFont));

	    frame.add(boardP);
	    frame.add(tourP);

		frame.pack();
		frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	

	
	/**
	 * update InfoPanel once some information changed
	 */
	public void updateInfo() {
		tourP.redAgent.setText("Red Current Site:  " + getAgent(PlayerColor.Red));
		tourP.blueAgent.setText("Blue Current Site:  " + getAgent(PlayerColor.Blue));
		tourP.turnLabel2.setText("" + getTurn());
		tourP.roundLabel2.setText("" + getRound());
		tourP.phaseLabel2.setText("Round " + getPhaseChange());
		
		if(c.viewer().getTurn() == PlayerColor.Red)
			tourP.turnLabel2.setForeground(Color.RED);
		else
			tourP.turnLabel2.setForeground(Color.BLUE);
		tourP.statusLabel2.setText("" + getStatus());
		if(c.viewer().getStatus() == Status.RedWin)
			tourP.statusLabel2.setForeground(Color.RED);
		else if(c.viewer().getStatus() == Status.BlueWin)
			tourP.statusLabel2.setForeground(Color.BLUE);
		
		frame.repaint();
	}
	
	
	
	/**
	 * resets the Checkerboard after one match
	 * @param whoWin integer
	 */
	public void resetCheckerboard(int whoWin) {
		c = new Checkerboard(length);
		match++;
		
		if(match % 2 == 0) {
			if(whoWin == 1)
				redWin++;
			else if(whoWin == -1)
				blueWin++;
		}
		else {
			if(whoWin == 1)
				blueWin++;
			else if(whoWin == -1)
				redWin++;
		}
		
		frame.remove(boardP);
		frame.remove(tourP);
	    
		frame.setLayout(new GridLayout(1, 2));
		boardP = new BoardPanel(c.getCircleField(), c);
	    tourP = new TournamentPanel(c, match, redWin, blueWin);
	    
	    Font titleFont = new Font("AppleGothic", Font.ROMAN_BASELINE, 18);
	    boardP.setBorder(BorderFactory.createTitledBorder(null, "Board panel", TitledBorder.CENTER, TitledBorder.TOP, titleFont));
	    tourP.jpUp.setBorder(BorderFactory.createTitledBorder(null, "Info panel", TitledBorder.CENTER, TitledBorder.TOP, titleFont));
	    tourP.jpBot.setBorder(BorderFactory.createTitledBorder(null, "Statistics panel", TitledBorder.CENTER, TitledBorder.TOP, titleFont));
	    
	    frame.add(boardP);
	    frame.add(tourP);
	    frame.pack();
        frame.repaint();
	}
	
	
	
	/**
	 * get current match number
	 * @return current match
	 */
	public int getMatch() {
		return match;
	}
	
	
	
	/**
	 * getter for count of redWin
	 * @return redWin 
	 */
	public int getPlayer1Win() {
		return redWin;
	}
	
	
	
	/**
	 * getter for count of blueWin
	 * @return blueWin 
	 */
	public int getPlayer2Win() {
		return blueWin;
	}
	
	
	
	/**
	 * get Checkerboard
	 * @return the Checkerboard of this Frame
	 */
	public Checkerboard getCheckerboard() {
		return c;
	}
	
	
	
	/**
	 * get player turn
	 * @return PlayerColor red or blue
	 */
	@Override
	public PlayerColor getTurn() {
		return c.viewer().getTurn();
	}


	
	/**
	 * get size of the game's field
	 * @return int integer
	 */
	@Override
	public int getSize() {
		return c.viewer().getSize();
	}



	/**
	 * get Status of the game
	 * @return Status current status
	 */
	@Override
	public Status getStatus() {
		return c.viewer().getStatus();
	}



	/**
	 * get the site of the agent for the given color
	 * @param color PlayerColor red or blue
	 * @return Site agent Site
	 */
	@Override
	public Site getAgent(PlayerColor color) {
		return c.viewer().getAgent(color);
	}



	/**
	 * get the available links
	 * @return Collection Collection of SiteSets
	 */
	@Override
	public Collection<SiteSet> getLinks() {
		return c.viewer().getLinks();
	}

	
	
	/**
	 * get current Round of the game
	 * @return int integer
	 */
	@Override
	public int getRound() {
		return c.viewer().getRound();
	}

	
	
	/**
	 * get the 2^(k-1) round of the game
	 * @return int
	 */	
	@Override
	public int getPhaseChange() {
		return c.viewer().getPhaseChange();
	}
	
	
	
}