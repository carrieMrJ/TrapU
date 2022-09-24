package nowhere2gopp.chessBoard;

/**
 * I write this class instead of using "Site" directly because it helps me to draw chess board
 * since the chess board has a shape of Hexagon, it would be simple if we draw Parallelogram
 * at first and then hide bottom-left part and top-right part of the Parallelogram
 * @author Yuanhao.Min
 *
 */
public class Circle {
	
	private boolean isHidden;
	private boolean isolated;
	private int xPosition;
	private int yPosition;
	private int column;
	private int row;
	
	
	
	/**
	 * constructor of Circle 
	 * @param column should be the same column of the Site at same position
	 * @param row should be the same row of the Site at same position
	 */
	public Circle(final int column, final int row) {
		isHidden = false;
		isolated = false;
		this.column = column;
		this.row = row;
	}
	
	
	
	/**
	 * Hide means the circle is invisible in Parallelogram
	 */
	public void Hide() {
		isHidden = true;
	}
	
	
	
	/**
	 * Show means the circle is visible, the units of circles look like a Bienenwabe
	 */
	public void Show() {
		isHidden = false;
	}
	
	
	
	/**
	 * Isolate means the circle has no link with other circles
	 */
	public void Isolate() {
		isolated = true;
	}
	
	
	
	/**
	 * getter of isHidden
	 * @return if hidden return true else return false
	 */
	public boolean HideStatus() {
		return isHidden;
	}
	
	
	
	/**
	 * getter of isolated
	 * @return if isolated return true else return false
	 */
	public boolean IsolateStatus() {
		return isolated;
	}
	
	
	
	/**
	 * not row, not the position in circleField, this xPosition
	 * refers to the absolute xPosition in ImagePanel(JPanel) 
	 * @param xPosition between 0 and max width of Panel
	 */
	public void setX(int xPosition) {
		this.xPosition = xPosition;
	}
	
	
	
	/**
	 * not column, not the position in circleField, this yPosition
	 * refers to the absolute yPosition in ImagePanel(JPanel) 
	 * @param yPosition between 0 and max height of Panel
	 */
	public void setY(int yPosition) {
		this.yPosition = yPosition;
	}
	
	
	
	/**
	 * get xPosition in Panel
	 * @return value
	 */
	public int getX() {
		return xPosition;
	}
	
	
	
	/**
	 * get yPosition in Panel
	 * @return value
	 */
	public int getY() {
		return yPosition;
	}
	
	
	
	/**
	 * get column, same as column of the Site in same location
	 * @return column value(in y-Axis)
	 */
	public int getColumn() {
		return column;
	}
	
	
	
	/**
	 * get row, same as row of the Site in same location
	 * @return row value(in x-Axis)
	 */
	public int getRow() {
		return row;
	}

	
	
}