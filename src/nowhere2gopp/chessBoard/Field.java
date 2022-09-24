package nowhere2gopp.chessBoard;

import java.util.ArrayList;

/**
 * this is a field of all circles(even include those hidden)
 * Its 2D-field[width][height]
 * @author Yuanhao.Min
 *
 */
public class Field {
	
	private int length;
    private Circle[][] field;
    
    
    
    /**
     * constructor of field
     * @param length field size(and it is chessBoard size)
     */
    public Field(int length){
        this.length = length;
        field = new Circle[length][length];
    }
    
    
    
    /**
     * getter of length
     * @return value between 3-11
     */
    public int getLength() {
        return length;
    }

    
    
    /**
     * to put a circle at field[x][y]
     * @param x in width
     * @param y in height
     * @param c the circle to place
     */
    public void place(int x, int y, Circle c){
        field[x][y]=c;
    }
    
    
    
    /**
     * getter of circle
     * @param x in width
     * @param y in height
     * @return circle needed
     */
    public Circle get(int x, int y){
        return field[x][y];
    }
    
    
    
    /**
     * get the neighbor of a circle that can be linked with
     * @param x in width
     * @param y in height
     * @return a 1D-field
     */
    public Circle[] getNeighbour(int x,int y){
    	ArrayList<Circle> list = new ArrayList<Circle>();
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                int r = x + i;
                int c = y + j;
                int res = i + j;
                if(!(i == 0 && j == 0) && res != 2 && res != -2 )
                	if(r >= 0 && c >= 0 && r < length && c < length)
                		if(!field[r][c].HideStatus())
                			list.add(field[r][c]);
            }
        }
        return list.toArray(new Circle[list.size()]);
    }
    
    
    
}