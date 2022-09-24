package nowhere2gopp.mainProgramm;

import nowhere2gopp.preset.*;
import nowhere2gopp.inputOutput.*;

/**
 * Project 5a)-5i), 6)Tournament
 * MainProgram for nowhere2gopp-Game
 * @author Mengru.Ji
 *
 */
public class MainGame {

	/**
	 * check Input from console
	 * @param input argument parser
	 * @return if correct return true, else false
	 */
	public static boolean checkInput(final ArgumentParser input) {

		try {
			input.getSize();
			if(input.getSize() > 11 || input.getSize() < 3){
				System.out.println("size must between 3 and 11!");
				return false;
			}
			if(input.getSize() % 2 == 0){
				System.out.println("size must be odd number!");
				return false;
			}
		}
		catch (Exception e) {
			System.out.println("the size of chess board is required!, please give one with correct format");
			System.out.println("for Example: -size 5");
			return false;
		}

		try {
			input.getRed();
		}
		catch (Exception e) {
			System.out.println("the type of red player is required!, please give one with correct format");
			System.out.println("for Example: -red human");
			return false;
		}

		try {
			input.getBlue();
		}
		catch (Exception e) {
			System.out.println("the type of blue player is required!, please give one with correct format");
			System.out.println("for Example: -blue human");
			return false;
		}

		return true;
	}



	/**
	 * Main Class
	 * @param args input from console
	 * @throws Exception ArgumentParserException
	 */
	public static void main(String[] args) throws Exception {

		final ArgumentParser input = new ArgumentParser(args);
		boolean isCorrect = checkInput(input);
		if(!isCorrect)
			return;



		/*red Human versus blue RandomAI
		 *-size 3 -delay 3 -red human -blue random
		 **/
		if(input.getRed() == PlayerType.Human && input.getBlue() == PlayerType.RandomAI) {
			String str = "HumanRandom";
			Frame f = new Frame(input.getSize(), str, PlayerColor.Red);
			HumanAIThread t = new HumanAIThread(str, input, f);
			t.start();
		}



		/*red RandomAI versus blue Human
		 *-size 3 -delay 3 -red random -blue human
		 **/
		else if(input.getRed() == PlayerType.RandomAI && input.getBlue() == PlayerType.Human) {
			String str = "RandomHuman";
			Frame f = new Frame(input.getSize(), str, PlayerColor.Blue);
			AIHumanThread t = new AIHumanThread(str, input, f);
			t.start();
		}



		/*red Human versus blue SimpleAI
		 *-size 3 -delay 3 -red human -blue simple
		 */
		else if(input.getRed() == PlayerType.Human && input.getBlue() == PlayerType.SimpleAI) {
			String str = "HumanSimple";
			Frame f = new Frame(input.getSize(), str, PlayerColor.Red);
			HumanAIThread t = new HumanAIThread(str, input, f);
			t.start();
		}



		/*red SimpleAI versus blue Human
		 *-size 3 -delay 3 -red simple -blue human
		 **/
		else if(input.getRed() == PlayerType.SimpleAI && input.getBlue() == PlayerType.Human) {
			String str = "SimpleHuman";
			Frame f = new Frame(input.getSize(), str, PlayerColor.Blue);
			AIHumanThread t = new AIHumanThread(str, input, f);
			t.start();
		}



		/*red Human versus blue ExtendedAI
		 *-size 3 -delay 3 -red human -blue extended
		 **/
		else if(input.getRed() == PlayerType.Human && input.getBlue() == PlayerType.ExtendedAI) {
			String str = "HumanExtended";
			Frame f = new Frame(input.getSize(), str, PlayerColor.Red);
			HumanAIThread t = new HumanAIThread(str, input, f);
			t.start();
		}



		/*red ExtendedAI versus blue Human
		 *-size 3 -delay 3 -red extended -blue human
		 **/
		else if(input.getRed() == PlayerType.ExtendedAI && input.getBlue() == PlayerType.Human) {
			String str = "ExtendedHuman";
			Frame f = new Frame(input.getSize(), str, PlayerColor.Blue);
			AIHumanThread t = new AIHumanThread(str, input, f);
			t.start();
		}



		/*netPlay
		 *-size 3 -red human -blue remote -localHost 192.168.1.249 -remoteHost 192.168.1.193 -meRed 1
		 **/
		else if(input.getRed() == PlayerType.Remote || input.getBlue() == PlayerType.Remote) {
			Frame f = null;
			String str = "Remote";

			/*which means local playerColor is Red*/
			if(input.localColor() == 1)
				f = new Frame(input.getSize(), str, PlayerColor.Red);
			else
				f = new Frame(input.getSize(), str, PlayerColor.Blue);

			RemotePlay r = new RemotePlay(input, f);
			r.remote();
		}



		/*localPlay
		 *-size 3 -red human -blue human
		 **/
		else if(input.getRed() == PlayerType.Human && input.getBlue() == PlayerType.Human) {
			String str = "Local";
			new Frame(input.getSize(), str, null);
		}



		/*tournament
		 *-size 7 -red random -blue random -match 3
		 **/
		else if(input.getRed() == PlayerType.RandomAI && input.getBlue() == PlayerType.RandomAI) {
			if(input.getDelay() == 0)
				System.out.println("Please set positive delay time for Computer Players");

			/*the number of matches in this tournament*/
			int match = input.getMatch();
			Tournament t = new Tournament(input);

			for(int i = 0; i < match; i++) {
				t.nextMatch();
				Thread.sleep(1000);
				System.out.println("Match " + t.getMatch() + " has ended!");
				if(i != match - 1)
					System.out.println("next Match starts in 10 second!!!");
				else
					System.out.println("Wait 10 second please, we will show the result!");
				Thread.sleep(10000);
				t.reset();
			}
			System.out.println("After Tournament with " + (t.getMatch()-1) + " matches");
			System.out.print("Player1 won " + t.getPlayer1Win() + " of them");
			System.out.println("   Player2 won " + t.getPlayer2Win() + " of them");
			if(t.getPlayer1Win() < t.getPlayer2Win())
				System.out.println("Player2 won the Tournament!");
			else if(t.getPlayer1Win() > t.getPlayer2Win())
				System.out.println("Player1 won the Tournament!");
			else
				System.out.println("nobody Win! the Tournament ended in a draw!");
		}



		else {
			System.out.println("We have no game method for red-" + input.getRed() + " and blue-" + input.getBlue());
			System.out.println("Sorry! maybe this comes later");
		}
	}



}
