package nowhere2gopp.mainProgramm;

import nowhere2gopp.chessBoard.Checkerboard;
import nowhere2gopp.inputOutput.TournamentFrame;
import nowhere2gopp.player.Player_AI_Random;
import nowhere2gopp.preset.*;

/**
 * Project 6)Tournament
 * Tournament only works for two RandomAI, because the tournament with 
 * large chess board(size 7, 9, 11) for SimpleAI takes too much time
 * @author Mengru.Ji
 *
 */
public class Tournament {
	
	private ArgumentParser input;
	
	/*not instance of Frame, we need different GUI*/
	private TournamentFrame f;
	private Checkerboard mainBoard;
	
	private Player_AI_Random redRandom = null;
	private Player_AI_Random blueRandom = null;
	
	private Move AIMoveRed = null;
	private Move AIMoveBlue = null;
	
	private int whoWin;
	
	
	
	/***
	 * constructor
	 * @param input input from main program
	 * @throws Exception ArgumentParserException
	 */
	public Tournament(ArgumentParser input) throws Exception {
		this.input = input;
		f = new TournamentFrame(input.getSize());
		this.mainBoard = f.getCheckerboard();
		
		redRandom = new Player_AI_Random();
		redRandom.init(input.getSize(), PlayerColor.Red);
		
		blueRandom = new Player_AI_Random();
		blueRandom.init(input.getSize(), PlayerColor.Blue);
	}
	
	
	
	/**
	 * start next match
	 * @throws Exception IllegalStateException
	 */
	public void nextMatch() throws Exception{
		
		while(mainBoard.viewer().getStatus() == Status.Ok) {
			try {
				if(AIMoveBlue != null)
					redRandom.update(AIMoveBlue, mainBoard.viewer().getStatus());
				AIMoveRed = redRandom.request();
				Thread.sleep(1000 * input.getDelay());
				mainBoard.make(AIMoveRed);
				f.updateInfo();
				redRandom.confirm(mainBoard.viewer().getStatus());
				
				if(mainBoard.viewer().getStatus() == Status.Ok) {
					blueRandom.update(AIMoveRed, mainBoard.viewer().getStatus());
					AIMoveBlue = blueRandom.request();
					Thread.sleep(1000 * input.getDelay());
					/*mainBoard must update before blue confirm, cause we need latest mainBoard 
					* status for the confirm-method*/
					mainBoard.make(AIMoveBlue);
					f.updateInfo();
					blueRandom.confirm(mainBoard.viewer().getStatus());
				}
			} catch(Exception e) {
				System.out.println(e);
			}
		}
	}
	
	
	
	/**
	 * reset the chess board
	 * @throws Exception IllegalStateException
	 */
	public void reset() throws Exception {
		
		if(mainBoard.viewer().getStatus() == Status.RedWin)
			whoWin = 1;
		else if(mainBoard.viewer().getStatus() == Status.BlueWin)
			whoWin = -1;
		/*which means the match ends in draw*/
		else
			whoWin = 0;

		f.resetCheckerboard(whoWin);
		f.updateInfo();
		
		/*reset AI move for next match*/
		AIMoveRed = null;
		AIMoveBlue = null;
		this.mainBoard = f.getCheckerboard();
		
		/*reset two RandomAI*/
		redRandom = new Player_AI_Random();
		redRandom.init(input.getSize(), PlayerColor.Red);
		
		blueRandom = new Player_AI_Random();
		blueRandom.init(input.getSize(), PlayerColor.Blue);
	}
	
	
	
	/*get current match*/
	public int getMatch() {
		return f.getMatch();
	}
	
	
	
	/*get the number of matches won by Player1*/
	public int getPlayer1Win() {
		return f.getPlayer1Win();
	}
	
	
	
	/*get the number of matches won by Player2*/
	public int getPlayer2Win() {
		return f.getPlayer2Win();
	}
	
	
	
}