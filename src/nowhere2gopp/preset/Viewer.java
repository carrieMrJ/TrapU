package nowhere2gopp.preset;

import java.util.*;

public interface Viewer {
    PlayerColor getTurn();
    int getSize();
    Status getStatus();

    /** site blocked by the agent of given color */
    Site getAgent(final PlayerColor color);

    /** current links */
    Collection<SiteSet> getLinks();
    
    
    
    // ********************************************************************
    //  weiter Funktion
    // ********************************************************************
    
    
    
    /**
     * get current round
     * @return integer value
     */
    int getRound();
    
    /**
     * get phase changing round
     * @return integer value
     */
    int getPhaseChange();
}
