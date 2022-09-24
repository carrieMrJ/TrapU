package nowhere2gopp.inputOutput;

import java.awt.*;

import javax.swing.*;

import nowhere2gopp.chessBoard.*;
import nowhere2gopp.preset.PlayerColor;
import nowhere2gopp.preset.SiteSet;

/**
 * Project 3d)
 * the BoardPanel is used to draw the graphic of the game with the links and sites 
 * @author Junqi.Sun
 *
 */
public class BoardPanel extends JPanel {
	
	private int length;
	private int startX;
	private int startY;
	/* distance is the distance between two sites */
	private int distance;
	/* r is the radius for the circles of the sites */
	private int r;
	
	private int startXcorr;
	private int startYcorr;
	private int xCorr;
	private int yCorr1;
	private int yCorr2;
	
	private Field circleField;
	private Checkerboard c;
	
	
	
	/**
	 * constructor of the panel for the board with the graphic of the game
	 * @param field the field set in Checkerboard
	 * @param c the Checkerboard to show
	 */
	public BoardPanel(Field field, Checkerboard c) {
		this.c = c;
		circleField = field;
		length = field.getLength();
		
		distance = 67;
		r = 20;
		startXcorr = (int)(distance * 3 / 4);
		startYcorr = (int)(distance * Math.sqrt(3) / 4);
		xCorr = distance;
		yCorr1 = (int)(distance / 2);
		yCorr2 = (int)(distance / 2 * Math.sqrt(3));
	}

	
	
	/**
	 * method to paint the graphic with the sites and links of the game
	 * and set the color of the site where an agent is placed 
	 * @param g is the graphic 
	 */
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.GRAY);
        
		/* paint method is the only method that we can get size of Panel before
		 * JFrame set visible.*/
        startX = (getWidth() / 2) - ((length - 1) * startXcorr);
		startY = (getHeight() / 2) + ((length - 1) * startYcorr);
        
        for(int x = 0; x < length; x++) {
        	for(int y = 0; y < length; y++) {
        		
        		Circle c = circleField.get(x, y);
        		
        		c.setX(startX + y * yCorr1 + x * xCorr);
        		c.setY(startY - y * yCorr2);
        		
        		if(!c.HideStatus() && !c.IsolateStatus()) {
        			g2.drawOval(c.getX()-r, c.getY()-r, r*2, r*2);
        			g2.fillOval(c.getX()-r, c.getY()-r, r*2, r*2);
        		}
        		
        		else if(c.HideStatus() && c.IsolateStatus()) {
        			g2.drawOval(c.getX()-r, c.getY()-r, r*2, r*2);
        		}
        	}
        }
        
        //to draw wider line
        g2.setStroke(new BasicStroke(4));
        
        //to repaint all the line once cutting links
        for(SiteSet ss : c.viewer().getLinks()) {
        	Circle c1 = c.toCircle(ss.getFirst());
        	Circle c2 = c.toCircle(ss.getSecond());
        	g2.drawLine(c1.getX(), c1.getY(), c2.getX(), c2.getY());
        }
        
        //back to normal width
        g2.setStroke(new BasicStroke(1));
        
     	//to repaint agent after moving
        if(c.viewer().getAgent(PlayerColor.Red) != null) {
        	Circle redAgent = c.toCircle(c.viewer().getAgent(PlayerColor.Red));
            g2.setColor(Color.RED);
            g2.fillOval(redAgent.getX() - 20, redAgent.getY() - 20, 40, 40);
        }
        
        if(c.viewer().getAgent(PlayerColor.Blue) != null) {
            Circle blueAgent = c.toCircle(c.viewer().getAgent(PlayerColor.Blue));
            g2.setColor(Color.BLUE);
            g2.fillOval(blueAgent.getX() - 20, blueAgent.getY() - 20, 40, 40);
        }
	}
	
	

	/**
	 * method to get the preferred dimension of this panel
	 * @return Dimension
	 */	
	@Override
	public Dimension getPreferredSize() {
		if(length < 11)
			return new Dimension(600 , 600);
		else
			return new Dimension(800 , 800);
	}
	
	
	
	private static final long serialVersionUID = 1L;
}