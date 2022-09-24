package nowhere2gopp.mainProgramm;

import nowhere2gopp.chessBoard.*;
import nowhere2gopp.inputOutput.*;
import nowhere2gopp.player.*;
import nowhere2gopp.preset.*;

/**
 * Project 4e)-4f), 6)smarter AI
 * Thread for red-Human-blue-AI-play
 * @author Mengru.Ji
 *
 */
public class HumanAIThread extends Thread{
	
	private String str;

	private ArgumentParser input;
	
	private Frame f;
	private Checkerboard mainBoard;
	private TextInput txtInput;

	private Player_Interactive redHuman = null;
	private Player_AI_Random blueRandom = null;
	private Player_AI_Simple blueSimple = null;
	private Player_AI_Extended blueExtended = null;
	
	private Move AIMove = null;
	
	
	
	/**
	 * constructor
	 * @param str the "Object" for synchronization
	 * @param input input of main program
	 * @param f a instance of Frame
	 * @throws Exception RemoteException, IllegalStateException, MoveFormatException etc...
	 */
	public HumanAIThread(String str, ArgumentParser input, Frame f) throws Exception {
		this.str = str;
		this.input = input;
		this.f = f;
		this.mainBoard = f.getCheckerboard();
		txtInput = f.getTextInput();
		
		redHuman = new Player_Interactive(txtInput);
		redHuman.init(input.getSize(), PlayerColor.Red);
		
		/*to create different types of AI*/
		if(str == "HumanRandom") {
			blueRandom = new Player_AI_Random();
			blueRandom.init(input.getSize(), PlayerColor.Blue);
		}
		else if(str == "HumanSimple") {
			blueSimple = new Player_AI_Simple();
			blueSimple.init(input.getSize(), PlayerColor.Blue);
		}
		else {
			blueExtended = new Player_AI_Extended();
			blueExtended.init(input.getSize(), PlayerColor.Blue);
		}
	}	
	
	
	
	@Override
	public void run(){
		while(true){
			synchronized(str){
				try {
					str.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(str == "HumanRandom")
				blueRandom();
			else if(str == "HumanSimple")
				blueSimple();
			else
				blueExtended();
		}
	}
	
	
	
	/**
	 * when blue is RandomAI
	 */
	public void blueRandom() {
		if(mainBoard.viewer().getStatus() == Status.Ok) {
			try {
				if(AIMove != null)
					redHuman.update(AIMove, mainBoard.viewer().getStatus());
				redHuman.request();
				redHuman.confirm(mainBoard.viewer().getStatus());
				
				blueRandom.update(redHuman.request(), mainBoard.viewer().getStatus());
				AIMove = blueRandom.request();
				Thread.sleep(1000 * input.getDelay());
				/*we need to check the mainBoard status again, because its possible
				 * that during Thread.sleep human surrendered*/
				if(mainBoard.viewer().getStatus() != Status.Ok)
					return;
				/*mainBoard must update before blue confirm, cause we need latest mainBoard 
				* status for the confirm-method*/
				mainBoard.make(AIMove);
				blueRandom.confirm(mainBoard.viewer().getStatus());
			} catch(Exception e) {
				System.out.println(e);
			}
			f.updateInfo();
		}
	}
	
	
	
	/**
	 * when blue is SimpleAI
	 */
	public void blueSimple() {
		if(mainBoard.viewer().getStatus() == Status.Ok) {
			try {
				if(AIMove != null)
					redHuman.update(AIMove, mainBoard.viewer().getStatus());
				redHuman.request();
				redHuman.confirm(mainBoard.viewer().getStatus());
				
				blueSimple.update(redHuman.request(), mainBoard.viewer().getStatus());
				AIMove = blueSimple.request();
				Thread.sleep(1000 * input.getDelay());
				/*we need to check the mainBoard status again, because its possible
				 * that during Thread.sleep human surrendered*/
				if(mainBoard.viewer().getStatus() != Status.Ok)
					return;
				/*mainBoard must update before blue confirm, cause we need latest mainBoard 
				* status for the confirm-method*/
				mainBoard.make(AIMove);
				blueSimple.confirm(mainBoard.viewer().getStatus());
			} catch(Exception e) {
				System.out.println(e);
			}
			f.updateInfo();
		}
	}
	
	
	
	/**
	 * when blue is ExtendedAI
	 */
	public void blueExtended() {
		if(mainBoard.viewer().getStatus() == Status.Ok) {
			try {
				if(AIMove != null)
					redHuman.update(AIMove, mainBoard.viewer().getStatus());
				redHuman.request();
				redHuman.confirm(mainBoard.viewer().getStatus());
				
				blueExtended.update(redHuman.request(), mainBoard.viewer().getStatus());
				AIMove = blueExtended.request();
				Thread.sleep(1000 * input.getDelay());
				/*we need to check the mainBoard status again, because its possible
				 * that during Thread.sleep human surrendered*/
				if(mainBoard.viewer().getStatus() != Status.Ok)
					return;
				/*mainBoard must update before blue confirm, cause we need latest mainBoard 
				* status for the confirm-method*/
				mainBoard.make(AIMove);
				blueExtended.confirm(mainBoard.viewer().getStatus());
			} catch(Exception e) {
				System.out.println(e);
			}
			f.updateInfo();
		}
	}
	
	
	
}