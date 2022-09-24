package nowhere2gopp.player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import nowhere2gopp.chessBoard.Checkerboard;
import nowhere2gopp.preset.*;

/**
 * Project 4e)
 * Random AI
 * @author Xinying.He
 *
 */
public class Player_AI_Random implements Player{
	
	private Checkerboard c;
	private PlayerColor color;
	private PlayerColor opponent;
	private Move move;

	
	
	@Override
	public void init(int boardSize, PlayerColor color) throws Exception, RemoteException {
		c = new Checkerboard(boardSize);
		this.color = color;
		if(color == PlayerColor.Red)
			opponent = PlayerColor.Blue;
		else
			opponent = PlayerColor.Red;
	}
	
	
	
	@Override
	public Move request() throws Exception, RemoteException {
		
		CopyOnWriteArrayList<SiteSet> linksArray = Checkerboard.linksToArray(c.viewer().getLinks());
		/*choose a random move*/
		int index2 = (int) (Math.random()* linksArray.size());
		
		if(c.viewer().getRound() < c.viewer().getPhaseChange()) {
			SiteSet link2 = linksArray.get(index2);
			linksArray.remove(index2);
			int index1 = (int) (Math.random()* linksArray.size());
			SiteSet link1 = linksArray.get(index1);
			move = new Move(link1, link2);
		}
		
		else {
			ArrayList<SiteTuple> connected = Checkerboard.getConnection(c.viewer().getAgent(opponent), c.viewer().getAgent(color), c.viewer().getLinks());
			int index1 = (int) (Math.random()* connected.size());
			move = new Move(connected.get(index1), linksArray.get(index2));
		}
		return move;
	}

	
	
	@Override
	public void confirm(Status status) throws Exception, RemoteException {
		c.make(move);
		
		/*actually playing local AI wont cause remoteException*/
		if(status != c.viewer().getStatus())
			throw new RemoteException();
		
	}

	
	
	@Override
	public void update(Move opponentMove, Status status) throws Exception, RemoteException {
		c.make(opponentMove);
		if(status != c.viewer().getStatus())
			throw new RemoteException();
	}



}