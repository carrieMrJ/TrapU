package nowhere2gopp.inputOutput;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nowhere2gopp.chessBoard.*;
import nowhere2gopp.preset.PlayerColor;

/**
 * the Thread used for human-AI-play
 * @author Junqi.Sun
 *
 */
public class ProdThread extends Thread{

	private String str;
	private Frame f;
	private InfoPanel infoP;
	private TextInput txtInput;
	private Checkerboard c;
	
	private PlayerColor me;
	
	

	/**
	 * constructor for the thread
	 * @param str the "Object" for synchronization
	 * @param f Object of the Frame-Class
	 * @param infoP InfoPanel
	 * @param txtInput TextInput
	 * @param me human color
	 */
	public ProdThread(String str, Frame f, InfoPanel infoP, TextInput txtInput, PlayerColor me){
		this.str = str;
		this.f = f;
		this.infoP = infoP;
		this.txtInput = txtInput;
		this.me = me;
		c = f.getCheckerboard();
	}


	
	/**
	 * customized run method for the thread
	 */	
	@Override
	public void run() {
		
		infoP.moveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(c.viewer().getTurn() == me) {
					String inputStr = infoP.getInput();
					txtInput.setReadString(inputStr);
					try {
						c.make(f.request());
					} catch(Exception error) {
						System.out.println(error);
					}
					f.updateInfo();
					
					synchronized(str){
						str.notify();
					}
				}
				else
					System.out.println("Its not your turn yet !!!");
			}
		});
	}
	
	
	
}