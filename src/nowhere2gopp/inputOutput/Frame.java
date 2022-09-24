package nowhere2gopp.inputOutput;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import nowhere2gopp.chessBoard.Checkerboard;
import nowhere2gopp.preset.*;

/**
 * Project 3d)
 * implements the Requestable and the extended Viewer interfaces
 * @author Junqi.Sun
 *
 */
public class Frame implements Requestable, Viewer {
	private JFrame frame;
	private InfoPanel infoP;
	private BoardPanel boardP;

	private Checkerboard c;
	private boolean trigger = false;
	private boolean moved = false;

	private TextInput txtInput = new TextInput();
	private String inputStr;
	private ArrayList<String> moveHistory = new ArrayList<String>();

	private PlayerColor me;

	/**
	 * constructor of the Frame
	 * @param length the length of the Checkerboard
	 * @param str the string for synchronization in Thread
	 * @param me local turn
	 */
	public Frame(int length, String str, PlayerColor me) {
		this.me = me;

		frame = new JFrame();
		c = new Checkerboard(length);

		/*in case of lost vision of Panel*/
		if(length < 11)
			frame.setMinimumSize(new Dimension(1116 , 600));
		else
			frame.setMinimumSize(new Dimension(1316 , 800));

		frame.setLayout(new GridLayout(1, 2));
	    boardP = new BoardPanel(c.getCircleField(), c);
	    infoP = new InfoPanel(c);


		Font titleFont = new Font("AppleGothic", Font.ROMAN_BASELINE, 18);
	    boardP.setBorder(BorderFactory.createTitledBorder(null, "Board panel", TitledBorder.CENTER, TitledBorder.TOP, titleFont));
	    infoP.setBorder(BorderFactory.createTitledBorder(null, "Info panel", TitledBorder.CENTER, TitledBorder.TOP, titleFont));


	    frame.add(boardP);
	    frame.add(infoP);

	    if(str == "Local") {
			infoP.moveBtn.addActionListener(moveLocal);
			infoP.surrBtn.addActionListener(surrender);
			infoP.saveBtn.addActionListener(saveLocal);
			infoP.loadBtn.addActionListener(loadLocal);
	    }

	    else if(str == "Remote") {
			infoP.moveBtn.addActionListener(moveRemote);
			infoP.surrBtn.addActionListener(surrenderRemote);
			infoP.saveBtn.setVisible(false);
			infoP.loadBtn.setVisible(false);
	    }

	    else {
			Thread pThread = new ProdThread(str, this, infoP, txtInput, me);
			pThread.start();
			infoP.surrBtn.addActionListener(surrenderRemote);
			infoP.saveBtn.setVisible(false);
			infoP.loadBtn.setVisible(false);
	    }



		frame.pack();
		frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}



	ActionListener moveLocal = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			inputStr = infoP.getInput();
			txtInput.setReadString(inputStr);

			try {
				c.make(request());
				/*only save correct move*/
				moveHistory.add(inputStr);
				moved = true;
			} catch(Exception error) {
				System.out.println(error);
			}
			updateInfo();
		}
	};



	ActionListener moveRemote = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(c.viewer().getTurn() == me) {
				trigger = true;

				inputStr = infoP.getInput();
				txtInput.setReadString(inputStr);
				try {
					c.make(request());
				} catch(Exception error) {
					System.out.println(error);
				}
				updateInfo();
			}
			else
				System.out.println("Its not your turn yet !!!");
		}
	};



	ActionListener surrender = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				c.make(new Move(MoveType.Surrender));
				moved = true;
			} catch (Exception error) {
				System.out.println(error);
			}

			infoP.statusLabel2.setText("" + getStatus());
			if(c.viewer().getStatus() == Status.RedWin)
				infoP.statusLabel2.setForeground(Color.RED);
			else if(c.viewer().getStatus() == Status.BlueWin)
				infoP.statusLabel2.setForeground(Color.BLUE);
			frame.repaint();
		}
	};

	ActionListener surrenderRemote = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(c.viewer().getTurn() == me) {
				try {
					c.make(new Move(MoveType.Surrender));
					moved = true;
				} catch (Exception error) {
					System.out.println(error);
				}

				infoP.statusLabel2.setText("" + getStatus());
				if(c.viewer().getStatus() == Status.RedWin)
					infoP.statusLabel2.setForeground(Color.RED);
				else if(c.viewer().getStatus() == Status.BlueWin)
					infoP.statusLabel2.setForeground(Color.BLUE);
				frame.repaint();
			}
			else
				System.out.println("Its not your turn yet !!!");
		}
	};



	ActionListener saveLocal = new ActionListener() {
		private Scanner sc;

		@Override
		public void actionPerformed(ActionEvent e) {
			String answer = null;

			try {
				File file = new File("saveFile.txt");
				if(file.exists()) {
					System.out.println("saveFile already exists, do you want to overwrite it?");
					System.out.println("Y/N?  ");
					sc = new Scanner(System.in);
					answer = sc.next();
					if(!answer.equals("Y") && !answer.equals("y")) {
						System.out.println("cancel overwrite");
						return;
					}
					else
						System.out.println("saveFile overwritten");
				}
				else
					System.out.println("new saveFile created");

				file.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(file));
				for(int i = 0; i < moveHistory.size(); i++)
					out.write(moveHistory.get(i) + "\r\n");
				out.flush();
				out.close();
				moved = true;
			}
			catch(IOException error) {
				System.out.println(error);
			}
		}
	};



	ActionListener loadLocal = new ActionListener() {
		private BufferedReader br;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(moved) {
				System.out.println("cannot load game with changed Checkerboard!");
				System.out.println("Please try with origin Checkerboard");
				return;
			}

			File file = new File("saveFile.txt");
			if(!file.exists()) {
				System.out.println("cant find saveFile!");
				return;
			}

			InputStreamReader reader = null;
			try {
				reader = new InputStreamReader(new FileInputStream(file));
				br = new BufferedReader(reader);
				String line = "";
				while (line != null) {
					line = br.readLine();
					if(line == null)
						break;
					c.make(Move.parse(line));
					updateInfo();
				}
				System.out.println("successfully load game!");
				System.out.println("now its " + c.viewer().getTurn() + " turn");
				moved = true;
			} catch (Exception error) {
				System.out.println(error);
			}

		}
	};



	/**
	 * update InfoPanel once some information changed
	 */
	public void updateInfo() {
		infoP.redAgent.setText("Red Current Site:  " + getAgent(PlayerColor.Red));
		infoP.blueAgent.setText("Blue Current Site:  " + getAgent(PlayerColor.Blue));
		infoP.turnLabel2.setText("" + getTurn());
		infoP.roundLabel2.setText("" + getRound());
		infoP.phaseLabel2.setText("Round " + getPhaseChange());

		if(c.viewer().getTurn() == PlayerColor.Red)
			infoP.turnLabel2.setForeground(Color.RED);
		else
			infoP.turnLabel2.setForeground(Color.BLUE);
		infoP.statusLabel2.setText("" + getStatus());
		if(c.viewer().getStatus() == Status.RedWin)
			infoP.statusLabel2.setForeground(Color.RED);
		else if(c.viewer().getStatus() == Status.BlueWin)
			infoP.statusLabel2.setForeground(Color.BLUE);
		infoP.inputText.setText("");

		frame.repaint();
	}



	/**
	 * get TextInput-Object
	 * @return TextInput-Object
	 */
	public TextInput getTextInput() {
		return txtInput;
	}



	/**
	 * get Checkerboard
	 * @return the Checkerboard of this Frame
	 */
	public Checkerboard getCheckerboard() {
		return c;
	}



	/**
	 * setter of Trigger for remote Play (see nowhere2gopp.mainProgramm.RemotePlay)
	 * @param t Trigger
	 */
	public void setTrigger(boolean t) {
		trigger = t;
	}



	/**
	 * getter of Trigger
	 * @return Trigger value
	 */
	public boolean getTrigger() {
		return trigger;
	}



	@Override
	public PlayerColor getTurn() {
		return c.viewer().getTurn();
	}



	@Override
	public int getSize() {
		return c.viewer().getSize();
	}



	@Override
	public Status getStatus() {
		return c.viewer().getStatus();
	}



	@Override
	public Site getAgent(PlayerColor color) {
		return c.viewer().getAgent(color);
	}



	@Override
	public Collection<SiteSet> getLinks() {
		return c.viewer().getLinks();
	}



	@Override
	public int getRound() {
		return c.viewer().getRound();
	}



	@Override
	public int getPhaseChange() {
		return c.viewer().getPhaseChange();
	}



	@Override
	public Move request() throws Exception {
		return txtInput.request();
	}


}
