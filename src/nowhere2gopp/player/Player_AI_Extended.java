package nowhere2gopp.player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import nowhere2gopp.chessBoard.Checkerboard;
import nowhere2gopp.preset.*;

/**
 * Project 6)smarter AI
 * @author Xinying.He
 *
 */
public class Player_AI_Extended implements Player {
	
	private Checkerboard c;
	
	private Move idealMove;

	private PlayerColor color;
	private PlayerColor opponent;
	
	private ArrayList<SiteTuple> agentAllowed;
	private CopyOnWriteArrayList<SiteSet> linkAllowed;
	private ArrayList<Move> fakeMove1 = new ArrayList<Move>();
	private ArrayList<Move> fakeMove2 = new ArrayList<Move>();
	
	private int ourScore;
	private int enemyScore;
	
	
	
	public int linkNum(Site test, Collection<SiteSet> links) {
		int linkNum = 0;
		for(SiteSet ss : links) {
			if(ss.getFirst().equals(test) || ss.getSecond().equals(test))
				linkNum++;
		}
		return linkNum;
	}
		
	
	
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
		int difference = 0;
		int bestDiff = -9999;
		
		Site ourPos = c.viewer().getAgent(color);
		Site enemyPos = c.viewer().getAgent(opponent);
		Site enemyPosFake = null;
		if(enemyPos != null)
			enemyPosFake = new Site(enemyPos.getColumn(), enemyPos.getColumn());
		
		Collection<SiteSet> links = Checkerboard.cloneLinks(c.viewer().getLinks());;
		Collection<SiteSet> tempLinks = null;

		Move lastChoice = null;
		/*reset idealMove used in last request()*/
		idealMove = null;
		
		/*in phase 1 we(the extendedAI) acts like randomAI*/
		if(c.viewer().getRound() < c.viewer().getPhaseChange()) {
			CopyOnWriteArrayList<SiteSet> linksArray = Checkerboard.linksToArray(c.viewer().getLinks());
			int index1 = (int) (Math.random()* linksArray.size());
			SiteSet link1 = linksArray.get(index1);
			linksArray.remove(index1);
			
			int index2 = (int) (Math.random()* linksArray.size());
			SiteSet link2 = linksArray.get(index2);
			/*just make random Move*/
			idealMove = new Move(link1, link2);
			return idealMove;
		}
		
		/*phase 2 now we will create agent and remove link*/
		if(ourPos == null) {
			/*List of the Site that we can put our agent on it*/
			ArrayList<Site> target = Checkerboard.getNode(links, enemyPos);
			
			linkAllowed = Checkerboard.linksToArray(links);
			for(int i = 0; i < target.size(); i++)
				for(int j = 0; j < linkAllowed.size(); j++)
					fakeMove1.add(new Move(new SiteTuple(ourPos, target.get(i)), linkAllowed.get(j)));
		}
		
		/*phase 3 now we want to move agent and remove link*/
		else {
			/*allowed agentMove*/
			agentAllowed = Checkerboard.getConnection(enemyPos, ourPos, links);
			/*allowed linkRemove*/
			linkAllowed = Checkerboard.linksToArray(links);
			for(int i = 0; i < agentAllowed.size(); i++)
				for(int j = 0; j < linkAllowed.size(); j++)
					fakeMove1.add(new Move(agentAllowed.get(i), linkAllowed.get(j)));
		}

		/*phase 2 and 3, we make a simulated Move (fakeMove1) first*/
		for(int i = 0; i < fakeMove1.size(); i++) {
			/*if fakeMove1 can cause lose of the game*/
			boolean causeLose = false;
			
			/*which means AI play Red and human play blue, and 
			 * now human has no Agent on chess board and AI already has (after AI fakeMove1)*/
			if(enemyPos == null)
				enemyPosFake = null;
			/*to reset simulated enemyPos after last use*/
			else
				enemyPosFake = new Site(enemyPos.getColumn(), enemyPos.getRow());

			/*after fakeMove1 our agent moved*/
			ourPos = fakeMove1.get(i).getAgent().getSecond();
			links = Checkerboard.cloneLinks(c.viewer().getLinks());
			/*after fakeMove1 one link removed*/
			links.remove(fakeMove1.get(i).getLink());
			
			/*if this fakeMove1 can lead us to win, then this is idealMove*/
			if(Checkerboard.isAgentBlocked(enemyPosFake, fakeMove1.get(i).getAgent().getSecond(), links)) {
				idealMove = fakeMove1.get(i);
				return idealMove;
			}
				
			
			
			/*now its opponent turn, from his perspective "ourPos" is his "enemyPos"
			 *he gets the current available agentMove List*/
			agentAllowed = Checkerboard.getConnection(ourPos, enemyPosFake, links);
			/*he gets the current available linkRemove List*/
			linkAllowed = Checkerboard.linksToArray(links);
			for(int j = 0; j < agentAllowed.size(); j++)
				for(int k = 0; k < linkAllowed.size(); k++)
					fakeMove2.add(new Move(agentAllowed.get(j), linkAllowed.get(k)));

			/*fakeMove2 is the simulated enemy Move after our fakeMove1*/
			for(int j = 0; j < fakeMove2.size(); j++) {
				/*if fakeMove1 make us lose even before fakeMove2, then causeLose = true
				 *attention! the "tempLink" has not been created yet,
				 *so we will use "links" as the third parameter
				 */
				if(Checkerboard.isAgentBlocked(ourPos, enemyPosFake, links) || linkNum(ourPos, links) == 0)
					causeLose = true;
				
				/*after fakeMove2 enemy agent moved*/
				enemyPosFake = fakeMove2.get(j).getAgent().getSecond();
				tempLinks = Checkerboard.cloneLinks(links);
				/*after fakeMove2 one link removed*/
				tempLinks.remove(fakeMove2.get(j).getLink());
				
				/*prepare a random Move 
				 *if every fakeMove1 can make us lose game, then choose this random later*/
				if(!causeLose)
					lastChoice = fakeMove1.get(i);
					
				/*if fakeMove1 cause a possible fakeMove2, which will make us lose the game*/
				if(Checkerboard.isAgentBlocked(ourPos, enemyPosFake, tempLinks) || linkNum(ourPos, tempLinks) == 0) 
					causeLose = true;

				/*when our agent Site has more link than enemy Site after fakeMove
				 *then we have disadvantage*/
				ourScore = linkNum(ourPos, tempLinks);
				enemyScore = linkNum(enemyPos, tempLinks);

				/*try to get max difference
				 *our agent has as much neighbor as possible
				 *and enemy agent has as few neighbor as possible*/
				difference = ourScore - enemyScore;
				
			}//fakeMove2-loop ends
			
			/*reset fakeMove2*/
			fakeMove2 = new ArrayList<Move>();
			/*to choose the idealMove, except those Moves that can make us lose*/
			if(difference > bestDiff && !causeLose) {
				bestDiff = difference;
				idealMove = fakeMove1.get(i);
			}
		}//fakeMove1-loop ends
		
		/*reset fakeMove1 for next request()*/
		fakeMove1 = new ArrayList<Move>();
		
		/*which means every fakeMove1 could make us lose*/
		if(idealMove == null) {
			System.out.println("ExtendedAI find that u will surely win the game in next turn");
			System.out.println("so AI made a random Move: " + lastChoice + " and gave up");
			/*use the prepared fakeMove1*/
			idealMove = lastChoice;
			return idealMove;
		}
		
		return idealMove;
	}

	
	
	@Override
	public void confirm(Status status) throws Exception, RemoteException {
		c.make(idealMove);
		
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