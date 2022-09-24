package nowhere2gopp.mainProgramm;

import nowhere2gopp.chessBoard.Checkerboard;
import nowhere2gopp.inputOutput.*;
import nowhere2gopp.player.*;
import nowhere2gopp.preset.*;

/**
 * Project 4e)-4f), 6)smarter AI
 * Thread for red-AI-blue-Human-play
 * @author Mengru.Ji
 *
 */
public class AIHumanThread extends Thread {
	
	private String str;

	private ArgumentParser input;
	
	private Frame f;
	private Checkerboard mainBoard;
	private TextInput txtInput;

	private Player_Interactive blueHuman = null;
	private Player_AI_Random redRandom = null;
	private Player_AI_Simple redSimple = null;
	private Player_AI_Extended redExtended = null;
	
	private Move AIMove = null;
	
	
	
	/**
	 * constructor
	 * @param str the "Object" for synchronization
	 * @param input input of main program
	 * @param f a instance of Frame
	 * @throws Exception RemoteException, IllegalStateException, MoveFormatException etc...
	 */
	public AIHumanThread(String str, ArgumentParser input, Frame f) throws Exception {
		
		this.str = str;
		this.input = input;
		this.f = f;
		this.mainBoard = f.getCheckerboard();
		txtInput = f.getTextInput();
		
		blueHuman = new Player_Interactive(txtInput);
		blueHuman.init(input.getSize(), PlayerColor.Blue);
		
		if(str == "RandomHuman") {
			redRandom = new Player_AI_Random();
			redRandom.init(input.getSize(), PlayerColor.Red);
		}
		else if(str == "SimpleHuman") {
			redSimple = new Player_AI_Simple();
			redSimple.init(input.getSize(), PlayerColor.Red);
		}
		else {
			redExtended = new Player_AI_Extended();
			redExtended.init(input.getSize(), PlayerColor.Red);
		}
	}	
	
	
	
	@Override
	public void run(){
		while(true){
			
			if(str == "RandomHuman")
				redRandom1();
			else if(str == "SimpleHuman")
				redSimple1();
			else
				redExtended1();
			
			synchronized(str){
				try {
					str.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			red2();
		}
	}
	
	
	
	/**
	 * when red is RandomAI
	 */
	public void redRandom1() {
		if(mainBoard.viewer().getStatus() == Status.Ok) {
			try {
				Thread.sleep(1000 * input.getDelay());
				/*we need to check the mainBoard status again, because its possible
				 * that during Thread.sleep human surrendered*/
				if(mainBoard.viewer().getStatus() != Status.Ok)
					return;
				if(f.getRound() != 1) 
					redRandom.update(blueHuman.request(), mainBoard.viewer().getStatus());
				
				AIMove = redRandom.request();
				
				mainBoard.make(AIMove);
				redRandom.confirm(mainBoard.viewer().getStatus());
			} catch(Exception e) {
				System.out.println(e);
			}
			f.updateInfo();
		}
	}
	
	
	
	/**
	 * when red is SimpleAI
	 */
	public void redSimple1() {
		if(mainBoard.viewer().getStatus() == Status.Ok) {
			try {
				Thread.sleep(1000 * input.getDelay());
				/*we need to check the mainBoard status again, because its possible
				 * that during Thread.sleep human surrendered*/
				if(mainBoard.viewer().getStatus() != Status.Ok)
					return;
				if(f.getRound() != 1) 
					redSimple.update(blueHuman.request(), mainBoard.viewer().getStatus());
				
				AIMove = redSimple.request();
				
				mainBoard.make(AIMove);
				redSimple.confirm(mainBoard.viewer().getStatus());
			} catch(Exception e) {
				System.out.println(e);
			}
			f.updateInfo();
		}
	}
	
	
	
	/**
	 * when red is SimpleAI
	 */
	public void redExtended1() {
		if(mainBoard.viewer().getStatus() == Status.Ok) {
			try {
				Thread.sleep(1000 * input.getDelay());
				/*we need to check the mainBoard status again, because its possible
				 * that during Thread.sleep human surrendered*/
				if(mainBoard.viewer().getStatus() != Status.Ok)
					return;
				if(f.getRound() != 1) 
					redExtended.update(blueHuman.request(), mainBoard.viewer().getStatus());
				
				AIMove = redExtended.request();
				
				mainBoard.make(AIMove);
				redExtended.confirm(mainBoard.viewer().getStatus());
			} catch(Exception e) {
				System.out.println(e);
			}
			f.updateInfo();
		}
	}
	
	
	
	/**
	 * no matter what kind of AI red is, blue Human has same reaction
	 */
	public void red2() {
		if(mainBoard.viewer().getStatus() == Status.Ok) {
			try {
				blueHuman.update(AIMove, mainBoard.viewer().getStatus());
				blueHuman.request();
				blueHuman.confirm(mainBoard.viewer().getStatus());
				
			} catch(Exception e) {
				System.out.println(e);
			}
		}
	}
	
	

}