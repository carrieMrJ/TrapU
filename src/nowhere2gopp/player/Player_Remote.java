package nowhere2gopp.player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nowhere2gopp.preset.*;

/**
 * Project 4g)
 * Remote Player
 * @author Xinying.He
 *
 */
public class Player_Remote extends UnicastRemoteObject implements Player_Son {
	
	private Player_Interactive player;
	
	
	
	/**
	 * constructor
	 * @param player player for following methods
	 * @throws Exception Exception
	 * @throws RemoteException Exception
	 */
	public Player_Remote(Player_Interactive player) throws Exception, RemoteException {
		this.player = player;
	}
	
	
	
	@Override
	public void init(int boardSize, PlayerColor color) throws Exception, RemoteException {
		player.init(boardSize, color);
	}
	
	
	
	@Override
	public Move request() throws Exception, RemoteException {
		return player.request();
	}

	
	
	@Override
	public void confirm(Status status) throws Exception, RemoteException {
		player.confirm(status);
	}

	
	
	@Override
	public void update(Move opponentMove, Status status) throws Exception, RemoteException {
		player.update(opponentMove, status);
	}

	
	
	@Override
	public String getInput() throws Exception, RemoteException {
		return player.getInput();
	}

	
	
	@Override
	public void setInput(String str) throws Exception, RemoteException {
		player.setInput(str);
	}

	

}