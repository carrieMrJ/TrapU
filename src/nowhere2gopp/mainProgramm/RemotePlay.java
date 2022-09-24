package nowhere2gopp.mainProgramm;

import nowhere2gopp.chessBoard.*;
import nowhere2gopp.inputOutput.*;
import nowhere2gopp.player.*;
import nowhere2gopp.preset.*;

/**
 * Project 5i)
 * @author Mengru.Ji
 * 
 */
public class RemotePlay {
	
	private Frame f;
	private Checkerboard mainBoard;
	private TextInput txtInput;

	private Player_Interactive redHuman = null;
	private Player_Remote redRemote = null;
	private Player_Interactive blueHuman = null;
	private Player_Remote blueRemote = null;
	Player_Remote local = null;
	Player_Son p = null;
	
	NetPlay net = new NetPlay();
	String temp = null;
	
	Move opponentMove = null;
	PlayerColor me;
	
	
	
	/**
	 * constructor
	 * @param input input from main program
	 * @param f Frame Object
	 * @throws Exception RemoteException, IllegalStateException etc...
	 */
	public RemotePlay(ArgumentParser input, Frame f) throws Exception {
		
		this.f = f;
		this.mainBoard = f.getCheckerboard();
		txtInput = f.getTextInput();
		
		redHuman = new Player_Interactive(txtInput);
		redRemote = new Player_Remote(redHuman);
		redRemote.init(input.getSize(), PlayerColor.Red);
		
		blueHuman = new Player_Interactive(txtInput);
		blueRemote = new Player_Remote(blueHuman);
		blueRemote.init(input.getSize(), PlayerColor.Blue);
		
		/*which means local is Red*/
		if(input.localColor() == 1) {
			local = redRemote;
			net.offer(redRemote, input.localHost(), "1099", "redRemote");
			/*in case of short connection time out*/
			Thread.sleep(5000);
			p = net.find(input.remoteHost(), "1099", "blueRemote");
		}
		else {
			local = blueRemote;
			net.offer(blueRemote, input.localHost(), "1099", "blueRemote");
			Thread.sleep(5000);
			p = net.find(input.remoteHost(), "1099", "redRemote");
		}
	}		
	
	
	
	/**
	 * the remote Method for 2 Player
	 * @throws Exception RemoteException, IllegalStateException etc...
	 */
	public void remote() throws Exception {
		while(mainBoard.viewer().getStatus() == Status.Ok || mainBoard.viewer().getStatus() == Status.Illegal) {
			
			/*refresh per second, the game has 1000 Ping Lag*/
			Thread.sleep(1000);
			
			try {
				if(f.getTrigger() && mainBoard.viewer().getStatus() == Status.Ok) {
					if(f.getRound() != 1)
						local.update(opponentMove, mainBoard.viewer().getStatus());
					local.request();
					local.confirm(mainBoard.viewer().getStatus());
					f.setTrigger(false);
				}
				
				/*which means the remote Player has given some Input and then clicked move-Button*/
				if(p.getInput() != null) {
					
					/*which means the remote Player surrendered*/
					if(p.getInput().equals("surr")) {
						mainBoard.make(new Move(MoveType.End));
						f.updateInfo();
					}
					
					/*make sure receive the Move from remote Player only once*/
					else if(temp == null || !temp.equals(p.getInput())) {
						opponentMove = p.request();
						mainBoard.make(opponentMove);
						f.updateInfo();
						temp = p.getInput();
					}
				}
			}
			catch (Exception e) {
				System.out.println(e);
				p.setInput(null);
			}
		}//while(true) loop ends
		
		/*which means we have pushed surrender-button, not lose by normal play*/
		if(mainBoard.viewer().getStatus() == Status.RedWin || mainBoard.viewer().getStatus() == Status.BlueWin) {
				local.setInput("surr");
		}
	}

	
	
}