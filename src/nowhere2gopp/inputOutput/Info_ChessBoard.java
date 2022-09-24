package nowhere2gopp.inputOutput;

import java.util.Collection;

import nowhere2gopp.preset.*;

/**
 * Project 3a) Info_ChessBoard implements Viewer interface
 * @author Junqi.Sun
 *
 */
public class Info_ChessBoard implements nowhere2gopp.preset.Viewer{

	private PlayerColor turn;
	private int size;
	private Status status;
	private Site blueAgent;
	private Site redAgent;
	private Collection<SiteSet> links;
	private int round;
	private int phaseChange;
	
	
	
	/**
	 * constructor for the Info_ChessBoard
	 * @param turn Red or Blue
	 * @param size size of Checkerboard
	 * @param status current status of the game
	 * @param blueAgent site of blueAgent
	 * @param redAgent site of redAgent
	 * @param links current links
	 * @param round current round
	 * @param phaseChange the 2^(k -1) rounds which represents phaseChange (from phase1 to phase2)
	 */
	public Info_ChessBoard(PlayerColor turn, int size, 
	Status status, Site blueAgent, Site redAgent, Collection<SiteSet> links, int round, int phaseChange) {
		this.turn = turn;
		this.size = size;
		this.status = status;
		this.blueAgent = blueAgent;
		this.redAgent = redAgent;
		this.links = links;
		this.round = round;
		this.phaseChange = phaseChange;
	}
	
	
	
	/**
	 * get player turn
	 * @return PlayerColor red or blue
	 */
	@Override
	public PlayerColor getTurn() {
		return turn;
	}

	
	
	/**
	 * get size of the game's field
	 * @return int integer
	 */
	@Override
	public int getSize() {
		return size;
	}

	
	
	/**
	 * get Status of the game
	 * @return Status current status
	 */
	@Override
	public Status getStatus() {
		return status;
	}

	
	
	/**
	 * get the site of the agent for the given color
	 * @param color PlayerColor
	 * @return Site agent Site
	 */
	@Override
	public Site getAgent(PlayerColor color) {
		if(color == PlayerColor.Red)
			return redAgent;
		else
			return blueAgent;
	}

	
	
	/**
	 * get the available links
	 * @return Collection Collection of SiteSets
	 */
	@Override
	public Collection<SiteSet> getLinks() {
		return links;
	}

	
	
	/**
	 * get Round of the game
	 * @return int integer
	 */
	@Override
	public int getRound() {
		return round;
	}

	
	
	/**
	 * get the change of the phase after 2^(k-1) rounds of the game
	 * @return int integer
	 */	
	@Override
	public int getPhaseChange() {
		return phaseChange;
	}

	

}