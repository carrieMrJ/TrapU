package nowhere2gopp.chessBoard;

import nowhere2gopp.preset.*;
import nowhere2gopp.inputOutput.Info_ChessBoard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Project 2a)-2g)
 * @author Yuanhao.Min
 *
 */
public class Checkerboard implements Playable, Viewable {
	
	private Field circleField;
	
	private int round = 1;
	private int phaseChange;
	private int size;
	private Status status = Status.Ok; 
	/*red always first*/
	private PlayerColor turn = PlayerColor.Red;
	private Collection<SiteSet> links;
	private Site blueAgent = null;
	private Site redAgent = null;
	
	
	
	/**
	 * the constructor of chessBoard
	 * @param length size of the chessBoard
	 */
	public Checkerboard(int length) {
		size = length;
		phaseChange = (int) Math.pow(2 ,(size - 3) / 2) + 1;
		setCircleField(length);
		setLinks();
	}
	
	
	
	/**
	 * create a Parallelogram circleField, for reason and details please see Field-Class and Circle-Class
	 * @param length the size of circleField
	 */
	public void setCircleField(int length) {
		
		circleField = new Field(length);
		
	    for(int x = 0; x < circleField.getLength(); x++) {
	        for(int y = 0; y < circleField.getLength(); y++) {
	        	int column = x;
	        	int row = y;
	        	if(row != (length - 1) / 2)
	    			column -= (length - 1) / 2 - row;
	            circleField.place(x, y, new Circle(column, row));
	        }
	    }
	    
		for(int x = 0; x < length; x++) {
			for(int y = 0; y < length; y++) {
				if(x < ((length - 1) / 2 - y))// to hide bottom left part of the Parallelogram
					circleField.get(x, y).Hide();
				else if(x > (- y + (length - 1) * 3 / 2) && y > (length - 1) / 2)//to hide top right part
					circleField.get(x, y).Hide();
			}
		}
	}
	
	
	
	/**
	 * to initialize the link before game start
	 */
	public void setLinks() {
		links = new ArrayList<SiteSet>();
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				Circle c = circleField.get(x, y);
        		Circle[] neighbour = circleField.getNeighbour(x, y);
        		if(!c.HideStatus()) 
        			/*set links with neighbour*/
        			for(Circle toLink : neighbour) {
        				SiteSet ss = new SiteSet(toSite(c), toSite(toLink));
        				if(!links.contains(ss)) 
        					links.add(ss);
        			}
        	}
		}
	}

	

	/**
	 * getter of circleField
	 * @return the already created circleField
	 */
	public Field getCircleField() {
		return circleField;
	}


	
	/**
	 * convert Site to circle
	 * @param s Site to convert
	 * @return the circle with correct location in circleField
	 */
	public Circle toCircle(Site s) {
		int x = s.getColumn();
		int y = s.getRow();
		if(s.getRow() != (size - 1) / 2)
			x += (size - 1) / 2 - y;
		return circleField.get(x, y);
	}
	
	
	
	@Override
	public Viewer viewer() {
		return new Info_ChessBoard(turn, size, status, blueAgent, redAgent, links, round, phaseChange);
	}
	
	
	
	@Override
	public void make(final Move move) throws IllegalStateException{
		
		/*FIRST STEP to check status
		 *check status first for any kind of Move 
		 */
		if(status != Status.Ok && status != Status.Illegal) {
			throw new IllegalStateException("the game already ends! It was " + status);
		}
		
		/*SECOND STEP for null check and MoveType Check
		 *the request()-method of TextInput-Class will return any kind of wrongFormat-Move as null
		 *so we can handle here
		 */
		if(move == null) {
			status = Status.Illegal;
			throw new IllegalStateException("Please give correct Move");
		}
			
		/*if remote surrendered in net play, then local will make an end-move in our chess board
		 * the moveType end only used in remotePlay, and this is the opposite Move of surrender
		 */
		else if(move.getType() == MoveType.End) {
			if(turn == PlayerColor.Red)
				status = Status.BlueWin;
			else
				status = Status.RedWin;
			return;
		}
		
		else if(move.getType() == MoveType.Surrender) {
			if(turn == PlayerColor.Red)
				status = Status.BlueWin;
			else
				status = Status.RedWin;
			return;
		}
		
		/*linkLink can only be done in phase 1*/
		else if(move.getType() == MoveType.LinkLink && round < phaseChange){

			if(links.contains(move.getOneLink())) {
				if(links.contains(move.getOtherLink())) {
					links.remove(move.getOneLink());
					links.remove(move.getOtherLink());
				}
				else {
					status = Status.Illegal;
					throw new IllegalStateException("cant remove that link! " + move.getOtherLink());
				}
			}
			else {
				status = Status.Illegal;
				throw new IllegalStateException("cant remove that link! " + move.getOneLink());
			}
			
			/*hide isolate Site after linkLink*/
			hideIsolate(move.getOneLink());
			hideIsolate(move.getOtherLink());
			status = Status.Ok;
		}
		
		/*agentLink can only be done in phase 2 and phase 3*/
		else if(move.getType() == MoveType.AgentLink && round >= phaseChange) {
			
			if(turn == PlayerColor.Red) {
				
				/*phase 2*/
				if(redAgent == null) {
					if(links.contains(move.getLink())) {
						redAgent = move.getAgent().getSecond();
						links.remove(move.getLink());
					}
					else {
						status = Status.Illegal;
						throw new IllegalStateException("cant remove that link! " + move.getLink());
					}
				}
				
				/*phase 3*/
				else if(findPath(move.getAgent(), blueAgent, links) && move.getAgent().getFirst().equals(redAgent)) {
					if(links.contains(move.getLink())) {
						redAgent = move.getAgent().getSecond();
						links.remove(move.getLink());
					}
					else {
						status = Status.Illegal;
						throw new IllegalStateException("cant remove that link! " + move.getLink());
					}
				}
					
				/*if no path for agentMove available*/
				else {
					status = Status.Illegal;
					throw new IllegalStateException("cant move from " + move.getAgent().getFirst() + " to " + move.getAgent().getSecond());
				}
			}
				
			/*turn = blue*/
			else {
				
				/*phase 2*/
				if(blueAgent == null && !move.getAgent().getSecond().equals(redAgent)) {
					if(links.contains(move.getLink())) {
						blueAgent = move.getAgent().getSecond();
						links.remove(move.getLink());
					}
					else {
						status = Status.Illegal;
						throw new IllegalStateException("cant remove that link! " + move.getLink());
					}
				}
					
				/*phase 3*/
				else if(findPath(move.getAgent(), redAgent, links) && move.getAgent().getFirst().equals(blueAgent)) {
					if(links.contains(move.getLink())) {
						blueAgent = move.getAgent().getSecond();
						links.remove(move.getLink());
					}
					else {
						status = Status.Illegal;
						throw new IllegalStateException("cant remove that link! " + move.getLink());
					}
				}
					
				/*if no path for agentMove available*/
				else {
					status = Status.Illegal;
					throw new IllegalStateException("cant move from " + move.getAgent().getFirst() + " to " + move.getAgent().getSecond());
				}
			}
			
			/*hide isolate Site after link removed*/
			hideIsolate(move.getLink());
			status = Status.Ok;
		}
		
		/*which means doing linkLink too late or agentLink too early*/
		else {
			status = Status.Illegal;
			throw new IllegalStateException("cant do " + move.getType() + ": " + move + " at round " + round);
		}
		
		/*THIRD STEP to check weather game ends*/
		if(round >= phaseChange) {
			if(toCircle(redAgent).HideStatus())
				status = Status.BlueWin;
			else if(blueAgent != null && toCircle(blueAgent).HideStatus())
				status = Status.RedWin;
			if(isAgentBlocked(redAgent, blueAgent, links) && !isAgentBlocked(blueAgent, redAgent, links))
				status = Status.BlueWin;
			else if(isAgentBlocked(blueAgent, redAgent, links) && !isAgentBlocked(redAgent, blueAgent, links))
				status = Status.RedWin;
			else if(isAgentBlocked(blueAgent, redAgent, links) && isAgentBlocked(redAgent, blueAgent, links))
					status = Status.Draw;
		}
		
		/*FOURTH STEP to change turn after move*/
		if(turn == PlayerColor.Red)
			turn = PlayerColor.Blue;
		else {
			turn = PlayerColor.Red;
			round++;
		}
	}
	
	

	/**
	 * if the Site in the siteSet is isolate
	 * then the Site will be marked on the GUI
	 * @param toCheck the siteSet to check
	 */
	public void hideIsolate(SiteSet toCheck) {
		Site s1 = toCheck.getFirst();
		Site s2 = toCheck.getSecond();
		
		int s1LinkNum = 0;
		int s2LinkNum = 0;
		
		for(SiteSet ss : links) {
			if(ss.getFirst().equals(s1) || ss.getSecond().equals(s1))
				s1LinkNum++;
			if(ss.getFirst().equals(s2) || ss.getSecond().equals(s2))
				s2LinkNum++;
		}
		
		if(s1LinkNum == 0) {
			toCircle(s1).Hide();
			toCircle(s1).Isolate();
		}
		
		if(s2LinkNum == 0) {
			toCircle(s2).Hide();
			toCircle(s2).Isolate();
		}
	}
	
	
	

	
	
	//-----------------static methods-------------------------------------
	
	
	
	
	
	
	/**
	 * convert circle to Site
	 * @param circle circle to be converted
	 * @return the Site with correct column and row
	 */
	public static Site toSite(Circle circle) {
		Site s = new Site(circle.getColumn(), circle.getRow());
		return s;
	}
	
	
	
	/**
	 * to get the other Site in SiteSet
	 * (different from the one as parameter)
	 * @param ss the SiteSet
	 * @param one the Site we dont want
	 * @return the Site we want
	 */
	public static Site getAnother(SiteSet ss, Site one) {
		if(ss.getFirst().equals(one))
			return ss.getSecond();
		else
			return ss.getFirst();
	}
	
	
	
	/**
	 * to test if a site is contained in current links
	 * @param list current links
	 * @param s Site to test
	 * @return if contained return true else false
	 */
	public static boolean isSiteContained(CopyOnWriteArrayList<SiteSet> list, Site s) {
		for(SiteSet ss : list) 
			if(ss.getFirst().equals(s) || ss.getSecond().equals(s)) 
				return true;
		return false;
	}
	
	
	
	/**
	 * to clone the links-Collection, so that we 
	 * can change the clone one for some use(for example the simulation in simpleAI)
	 * (without changing the origin links-Collection)
	 * @param links the current links
	 * @return a clone from links
	 */
	public static CopyOnWriteArrayList<SiteSet> linksToArray(Collection<SiteSet> links){
		CopyOnWriteArrayList<SiteSet> toArray = new CopyOnWriteArrayList<SiteSet>();
		for(SiteSet ss : links)
			toArray.add(ss);
		return toArray;
	}
	
	
	
	/**
	 * to clone the links-Collection, so that we 
	 * can change the clone for some use(for example the simulation in simpleAI)
	 * (without changing the origin links-Collection)
	 * @param links the current links
	 * @return a clone from links
	 */
	public static Collection<SiteSet> cloneLinks(Collection<SiteSet> links){
		Collection<SiteSet> clone = new ArrayList<SiteSet>();;
		for(SiteSet ss : links)
			clone.add(ss);
		return clone;
	}
	
	
	
	/**
	 * to check if the agent is "blocked"(the definition of "blocked" see Project-PDF)
	 * @param test the agent to test
	 * @param opponent enemy agent Site
	 * @param links current links
	 * @return if blocked return true else false
	 */
	public static boolean isAgentBlocked(Site test, Site opponent, Collection<SiteSet> links) {
		Site testLinked = null;
		int linkNumber = 0;
		
		if(opponent == null || test == null)
			return false;
		
		for(SiteSet ss : links) {
			if(ss.getFirst().equals(test)) {
				linkNumber++;
				testLinked = ss.getSecond();
			}
			else if(ss.getSecond().equals(test)) {
				linkNumber++;
				testLinked = ss.getFirst();
			}
		}
		if(linkNumber != 1)
			return false;
		
		if(!testLinked.equals(opponent))
			return false;
					
		return true;
	}
	
	
	
	/**
	 * to get all possible agentMove (used in AI design)
	 * @param opponent enemy agent Site
	 * @param start our Site
	 * @param links current links
	 * @return an ArrayList with possible agentMove
	 */
	public static ArrayList<SiteTuple> getConnection(Site opponent, Site start, Collection<SiteSet> links){
		ArrayList<Site> possibleTargets = new ArrayList<Site>();
		ArrayList<SiteTuple> connected = new ArrayList<SiteTuple>();
		
		for(SiteSet ss : links) {
			if(!possibleTargets.contains(ss.getFirst()))
				possibleTargets.add(ss.getFirst());
			if(!possibleTargets.contains(ss.getSecond()))
				possibleTargets.add(ss.getSecond());
		}
		
		for(Site target : possibleTargets) {
			SiteTuple st = new SiteTuple(start, target);
			if(findPath(st, opponent, links))
				connected.add(st);
		}
		
		return connected;
	}
	
	
	
	/**
	 * get Node for DFS (used in AI design)
	 * @param links current links
	 * @param opponent opponent Site
	 * @return all available node except the enemy Site
	 */
	public static ArrayList<Site> getNode(Collection<SiteSet> links, Site opponent){
		ArrayList<Site> allSite = new ArrayList<Site>();
		
		for(SiteSet ss : links) {
			if(!allSite.contains(ss.getFirst()))
				allSite.add(ss.getFirst());
			if(!allSite.contains(ss.getSecond()))
				allSite.add(ss.getSecond());
		}
		
		if(opponent != null && allSite.contains(opponent))
			allSite.remove(opponent);
		return allSite;
	}
	
	
	
	/**
	 * get Side for DFS (used in AI design)
	 * @param links current links
	 * @param opponent opponent Site
	 * @return all available links except those that contains opponent Site
	 */
	public static CopyOnWriteArrayList<SiteSet> getSide(Collection<SiteSet> links, Site opponent){
		CopyOnWriteArrayList<SiteSet> temp = linksToArray(links);
		
		for(SiteSet ss : temp)
			if(ss.getFirst().equals(opponent) || ss.getSecond().equals(opponent))
					temp.remove(ss);
		return temp;
	}
	
	
	
	/**
	 * the very important Path-finding-method, to check if the agentMove in current situation possible is
	 * @param agentMove the agentMove to check
	 * @param opponent the opponent Site
	 * @param links current links
	 * @return if Path exists return true else false
	 */
	public static boolean findPath(SiteTuple agentMove, Site opponent, Collection<SiteSet> links) {
		/*find path from start to end*/
		Site start = agentMove.getFirst();
		Site target = agentMove.getSecond();
		/*use CopyOnWriteArrayList instead of ArrayList, so that 
		 * we can loop over and change the list size at the same time,
		 * otherwise Console would throw a "ConcurrentModificationException"*/
		CopyOnWriteArrayList<SiteSet> restLink = new CopyOnWriteArrayList<SiteSet>();
		CopyOnWriteArrayList<Site> nextList = new CopyOnWriteArrayList<Site>();
		
		/*dont refer to origin link, that will change the current links*/
		restLink = linksToArray(links);
		/*the first "next-Node" is start-Node*/
		nextList.add(start);
		
		/*if opponent Site is null, then we are red and at phase 2
		 * at phase 2 any kind of agentMove is allowed*/
		if(opponent == null)
			return true;
		
		/*this means we are Blue at phase 2 and Red have created agent*/
		else if(start == null && !opponent.equals(target))
			return true;
		
		/*stand still is not allowed*/
		if(start != null && start.equals(target))
			return false;
		
		/*cant move to opponent agent Site*/
		if(target.equals(opponent))
			return false;
		
		while(restLink.size() > 0) {
			
			/*in case of endless loop*/
			int beginSize = restLink.size();
			
			for(SiteSet linked : restLink) {
				
				for(Site next : nextList) {
					
					/*which means the current node in nextList has Link (not isolated)
					 * may be the node linked with current node can be added*/
					if(linked.getFirst().equals(next) || linked.getSecond().equals(next)) {
						/*make sure we didnt add same Node twice
						 * for Example we have found Node (0,0) linked with (1,1) before
						 * and now we find (1,0) linked with (0,0). we already know
						 * (0,0) is not isolated and dont want to add (0,0) again in nextList*/
						if(!nextList.contains(getAnother(linked, next))) {
							/*we dont want opponent stand in our path, otherwise
							 * this opponent Node will make the path invalid*/
							if(!next.equals(opponent))
								nextList.add(getAnother(linked, next));
						/*the "linked" contains current node and next node
						 * now the next node has been added, we dont need this
						 * link any more*/
						}
						restLink.remove(linked);
					}
					
					/*which means Terminal found, also path found*/
					if(next.equals(target))
						return true;
					
					/*which means the current node is isolate
					 *then we need to find path from other node*/
					if(!isSiteContained(restLink, next)) 
						nextList.remove(next);

				}//inner for-loop ends here
			}///outer for-loop ends here
				
			/* to avoid endless loop*/
			if(beginSize == restLink.size())
				break;
		}/*while loop ends here*/
		return false;
	}
	
	
	
}