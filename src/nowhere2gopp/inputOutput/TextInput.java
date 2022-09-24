package nowhere2gopp.inputOutput;

import nowhere2gopp.preset.Move;
import nowhere2gopp.preset.Requestable;

/**
 * Project 3b)
 * implements Requestable
 * @author Junqi.Sun
 *
 */
public class TextInput implements Requestable {

	private String readString;
	
	
	
	/**
	 * getter of the String
	 * @return String
	 */
	public String getReadString(){
		return this.readString;
	}
	
	
	
	/**
	 * setter of the String
	 * @param str string to set
	 */
	public void setReadString(String str){
		this.readString = str;
	}
	
	
	
	/**
	 * requests a move corresponding to the entered string 
	 * @return Move
	 */	
	@Override
	public Move request() {
		try {
			return Move.parse(readString);
		}
		/*we will handle this error in Checkerboard-make-method*/
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}

	
	
}