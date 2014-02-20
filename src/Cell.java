import java.awt.Color;
import java.awt.Graphics;


public class Cell {
	//Representations for each cell state
	public static final char slimeChar = '1';
	public static final char tenticleChar = '2';
	public static final char deadChar = 'x';
	public static final char wallChar = '*';
	public static final char traceChar = '3';
	public static final char foodChar = '@';
	public static final char emptyChar = '0';
	
	
	public String cellState;
	public String prevState;
	// celState = { trace=3, wall=*, slime=1, food=@, dead=x, tenticle=2, empty=0}
	public int x, y;
	public int previousCell;
	int count;
	
	public Cell(String cellState,String prevState, int x, int y, int previousCell, int count) {
		this.cellState = cellState;
		this.prevState = prevState;
		this.x = x;
		this.y = y;
		this.previousCell = previousCell;
		this.count = count;
	}
	
    public void paintCell(Graphics gr)
    {
    	if(cellState == "wall")
    		gr.setColor(Color.BLACK);
    	else if(cellState == "trace")
    		gr.setColor(Color.MAGENTA);
    	else if(cellState == "slime")
    		gr.setColor(Color.YELLOW);
    	else if(cellState == "food")
    		gr.setColor(Color.RED);
    	else if(cellState == "empty")
    		gr.setColor(Color.LIGHT_GRAY);
    	else if(cellState == "dead")
    		gr.setColor(Color.BLUE);
    	else if(cellState == "tenticle")
    		gr.setColor(Color.YELLOW);
    	
    	gr.fillRect(x*40, y*40, 40, 40);
    }
}
