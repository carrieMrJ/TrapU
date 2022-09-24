package nowhere2gopp.player;

import java.rmi.Remote;
import java.rmi.RemoteException;

import nowhere2gopp.preset.*;

/**
 * the sub interface of Player to extend some methods (allowed in Project-PDF)
 * @author Xinying.He
 *
 */
public interface Player_Son extends Player, Remote {
    Move request() throws Exception, RemoteException;
    
    void confirm(Status status) throws Exception, RemoteException;
    
    void update(Move opponentMove, Status status) throws Exception, RemoteException;
    
    void init(int boardSize, PlayerColor color) throws Exception, RemoteException;
    
    String getInput() throws Exception, RemoteException;
    
    void setInput(String str) throws Exception, RemoteException;
}