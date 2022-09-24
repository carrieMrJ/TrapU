package nowhere2gopp.player;

import java.rmi.RemoteException;

import nowhere2gopp.chessBoard.Checkerboard;
import nowhere2gopp.inputOutput.TextInput;
import nowhere2gopp.preset.*;

/**
 * Project 4d)
 * @author Xinying.He
 *
 */
public class Player_Interactive implements Player{

	private Checkerboard c;
	private TextInput txtInput;
	
	
	
	/**
	 * constructor
	 * @param txtInput instance of TextInput class
	 */
	public Player_Interactive(TextInput txtInput) {
		this.txtInput = txtInput;
	}
	
	
	
	/**
	 * getter of input string
	 * @return string
	 */
	public String getInput() {
		return txtInput.getReadString();
	}
	
	
	
	/**
	 * setter of input string
	 * @param str the string to set
	 */
	public void setInput(String str) {
		txtInput.setReadString(str);
	}
	
	
	
	@Override
	public void init(int boardSize, PlayerColor color) throws Exception, RemoteException {
		c = new Checkerboard(boardSize);
	}
	
	
	
	@Override
	public Move request() throws Exception, RemoteException {
		return Move.parse(txtInput.getReadString());
	}

	
	
	@Override
	public void confirm(Status status) throws Exception, RemoteException {
		c.make(request());
		if(status != c.viewer().getStatus())
			throw new RemoteException("confirm failed! try right move again");
		
	}

	
	
	@Override
	public void update(Move opponentMove, Status status) throws Exception, RemoteException {
		c.make(opponentMove);
		if(status != c.viewer().getStatus())
			throw new RemoteException("update failed! try right move again");
	}



}