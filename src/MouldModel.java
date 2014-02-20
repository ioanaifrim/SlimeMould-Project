
import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MouldModel extends JFrame implements Runnable{

	/**
	 * @param args
	 */
	static int numRows = 0; // Number of the rows in the game
	static int numColumns = 0; // Number of columns in the game
	static boolean unfinished = true;
	static int foodIndex = -1;
	static int minMoves= 0;

	public static ArrayList<Cell> cells = new ArrayList<Cell>(); // The list that holds
															
	private static BufferedReader br;
	
	public MouldModel(String title)
	{
		super(title);
	}

	public void paint(Graphics gr)
	{
		super.paintComponents(gr);
		gr.setColor(Color.BLACK);
		for(int x=0; x<40; x++)
		{
			gr.drawLine(40*x, 0, 40*x, 800);
		}
		for(int y=0; y<40; y++)
		{
			gr.drawLine(0, 40*y, 800, 40*y);
		}
		for (Cell cell : cells)
		{
			cell.paintCell(gr);
		}
		PrintCells(numRows, numColumns, cells);
		System.out.println("model");
	}
	
	public static void main(String[] args) throws IOException {

		MouldModel model = new MouldModel("Slime Mould Simulation");
		model.setSize(500,500);
		model.setLocation(300, 300);
		model.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		model.setUndecorated(true);
		model.setVisible(true);
		model.run();
		
		
	}

	public static int noOfDeadAround(int index, int numColumns, ArrayList<Cell> cells)
	{
		int deadAround = 0;
		
		if (index + 1 < cells.size())
			if (cells.get(index + 1).cellState == "wall"
					|| cells.get(index + 1).cellState == "dead")
				deadAround++;
		if (index - 1 > -1)
			if (cells.get(index - 1).cellState == "wall"
					|| cells.get(index - 1).cellState == "dead")
				deadAround++;
		if (index + numColumns < cells.size())
			if (cells.get(index + numColumns).cellState == "wall"
					|| cells.get(index + numColumns).cellState == "dead")
				deadAround++;
		if (index - numColumns > -1)
			if (cells.get(index - numColumns).cellState == "wall"
					|| cells.get(index - numColumns).cellState == "dead")
				deadAround++;	
		
		return deadAround;
	}
	
	public static boolean foundMould(int index, int numColumns, ArrayList<Cell> cells)
	{
		boolean foundMould = false;
		
		if (index + 1 < cells.size())
			if (cells.get(index + 1).cellState == "slime" )
				foundMould = true;
		if (index - 1 > -1)
			if (cells.get(index - 1).cellState == "slime" )
				
		if (index + numColumns < cells.size())
			if (cells.get(index + numColumns).cellState == "slime" )
				foundMould = true;
		if (index - numColumns > -1)
			if (cells.get(index - numColumns).cellState == "slime")
				foundMould = true;	
		
		return foundMould;
	}
	
	public static ArrayList<Cell> simulateStep(int numRows, int numColumns, ArrayList<Cell> cells) {
		// This is a new ArrayList of cells that holds the next generation of
		// cells
		//ArrayList<Cell> cells = new ArrayList<Cell>();
		unfinished = false;
		boolean foundEmpty = false;

		for (Cell cell : cells) 
			if(cell.cellState == "empty")
				foundEmpty = true;
		
		for (int y = 0; y < numRows; y++) {
			for (int x = 0; x < numColumns; x++) // Loop through each cell
			{
		//for (Cell cell : cells) { // Check how many cells each cell is touching
			//int index = cells.indexOf(cell); // Get the position of the cell
												// within the ArrayList
			int index = numColumns * y + x;
			short deadAround = 0;
			boolean foundFood = false;
			short traceAround = 0;

			int nextToSlime = 0;
			boolean nextToMould = false;
			boolean right=false, left=false, up=false, down=false;
			int prevIndex = 0;
			//int foodIndex = -1;
			//int minMoves= 0;

			// To get the cell to the right of the cell, we add 1 to the index.
			// But first, we have to make sure that that index is less than the
			// size of the
			// ArrayList (so we don't get an error saying that the index is out
			// of range.)
			if (index + 1 < cells.size())
				if (cells.get(index + 1).cellState == "wall"
						||( cells.get(index + 1).cellState == "dead" && cells.get(index + 1).prevState == "dead") )
					deadAround++;
				else if (cells.get(index + 1).cellState == "tenticle")
					nextToSlime ++;
				else if (cells.get(index + 1).cellState == "food")
					foundFood = true;
				else if (cells.get(index + 1).cellState == "trace" && cells.get(index + 1).prevState == "trace")
				{
					traceAround++;
					right = true;
				}
				else if (cells.get(index + 1).cellState == "slime")
					nextToMould = true;

			// To get the cell to the left of the cell, we subtract 1 to the
			// index.
			if (index - 1 > -1)
				if (cells.get(index - 1).cellState == "wall"
						|| (cells.get(index - 1).cellState == "dead" && cells.get(index - 1).prevState == "dead"))
					deadAround++;
				else if (cells.get(index - 1).cellState == "tenticle")
					nextToSlime ++;
				else if (cells.get(index - 1).cellState == "slime")
					nextToMould = true;
				else if (cells.get(index - 1).cellState == "food")
					foundFood = true;
				else if (cells.get(index - 1).cellState == "trace" && cells.get(index - 1).prevState == "trace")
				{
					traceAround++;
					left = true;
				}

			// To get the cell below the cell, we add the number of columns to
			// the index.
			if (index + numColumns < cells.size())
				if (cells.get(index + numColumns).cellState == "wall"
						||( cells.get(index + numColumns).cellState == "dead" && cells.get(index + numColumns).prevState == "dead"))
					deadAround++;
				else if (cells.get(index + numColumns).cellState == "tenticle")
					nextToSlime ++;
				else if (cells.get(index + numColumns).cellState == "slime")
					nextToMould = true;
				else if (cells.get(index + numColumns).cellState == "food")
					foundFood = true;
				else if (cells.get(index + numColumns).cellState == "trace" && cells.get(index + numColumns).prevState == "trace")
				{
					traceAround++;
					down = true;
				}

			
			// To get the cell above the cell, we subtract the number of columns
			// to the index.
			if (index - numColumns > -1)
				if (cells.get(index - numColumns).cellState == "wall"
						|| (cells.get(index - numColumns).cellState == "dead" && cells.get(index - numColumns).prevState == "dead"))
					deadAround++;
				else if (cells.get(index - numColumns).cellState == "tenticle")
					nextToSlime ++;
				else if (cells.get(index - numColumns).cellState == "slime")
					nextToMould = true;
				else if (cells.get(index - numColumns).cellState == "food")
					foundFood = true;
				else if (cells.get(index - numColumns).cellState == "trace" && cells.get(index - numColumns).prevState == "trace")
				{
					traceAround++;
					up = true;
				}
 
			//calculate the minimum number of moves to reach the food source - check cells around food
			if(!foundEmpty)
			{ 
				if (cells.get(foodIndex + 1).cellState == "trace")
					if(minMoves == 0 || minMoves > cells.get(foodIndex + 1).count)
						minMoves = cells.get(foodIndex + 1).count;
				
				if (cells.get(foodIndex - 1).cellState == "trace")
					if(minMoves == 0 || minMoves > cells.get(foodIndex - 1).count)
						minMoves = cells.get(foodIndex - 1).count;
				
				if (cells.get(foodIndex + numColumns).cellState == "trace")
					if(minMoves == 0 || minMoves > cells.get(foodIndex + numColumns).count)
						minMoves = cells.get(foodIndex + numColumns).count;
				
				if (cells.get(foodIndex - numColumns).cellState == "trace")
					if(minMoves == 0 || minMoves > cells.get(foodIndex - numColumns).count)
						minMoves = cells.get(foodIndex - numColumns).count;
			}
			
			
			
			if (cells.get(index).cellState == "empty") 
			{
				foundEmpty = true;
				unfinished = true;
				if(traceAround > 0 || nextToMould)
				{
				    if(nextToMould)
				    {
				    	cells.set(index, new Cell("trace","empty",x,y, -2,1));
				    }
					else if(traceAround > 0)
					{
						if(right)
						{
					      cells.set(index, new Cell("trace","empty",x,y, index +1, cells.get(index+1).count+1)); 
					      //System.out.println(cells.get(index).previousCell);
						}
						else if(left) 
							cells.set(index, new Cell("trace","empty",x,y, index - 1,cells.get(index-1).count+1)); 
						else if(down)
							cells.set(index, new Cell("trace","empty",x,y, index + numColumns,cells.get(index+numColumns).count+1)); 
						else if(up)
							cells.set(index, new Cell("trace","empty",x,y, index - numColumns,cells.get(index-numColumns).count+1)); 
					}
				}
				else 
					cells.set(index, new Cell("empty","empty",x,y, -1,1)); 
			}
			/*
			else if (cells.get(index).cellState == "trace" && !foundEmpty) // If the cell we are on is a trace
			{
				unfinished = true;
				if (deadAround == 3 && nextToSlime == 0)
					{
					    cells.set(index, new Cell("dead","trace",x,y, index)); 
					}	
				
				
				else if ((deadAround == 2 && nextToSlime >= 1) || 
					  (deadAround == 2 && foundFood) || (deadAround >= 1 && nextToSlime >= 2))
					cells.set(index, new Cell("tenticle","trace",x,y,index)); // Then in the new
														// generation the slime
														// grows.
				
				else cells.set(index, new Cell("trace","trace",x,y,index));
			}
			*/
			
			else if (cells.get(index).cellState == "trace")
			{
				unfinished = true;
				if(!foundEmpty)
				{
				if(foundFood && cells.get(index).count == minMoves)
				{
					unfinished = false;
					prevIndex = cells.get(index).previousCell;
					cells.set(index, new Cell("tenticle","trace",x,y,prevIndex,cells.get(prevIndex).count));
					Cell thisCell = cells.get(index);
					while(prevIndex != -2)
					{
						//System.out.println(prevIndex);
						cells.set(prevIndex, new Cell("tenticle","trace",x,y,cells.get(prevIndex).previousCell,cells.get(prevIndex).count));
						Cell lastCell = cells.get(prevIndex);
						prevIndex = cells.get(prevIndex).previousCell;
						
					}				}
				else 
					cells.set(index, new Cell("dead","trace",x,y,cells.get(index).previousCell,1));
				}
				else
					cells.set(index, new Cell("trace","trace",x,y,cells.get(index).previousCell,cells.get(index).count));
			}

			else if (cells.get(index).cellState == "wall")	
				cells.set(index, new Cell("wall","wall",x,y,-1,1));
			else if (cells.get(index).cellState == "slime")	
				cells.set(index, new Cell("slime","slime",x,y,-1,1));
			else if (cells.get(index).cellState == "food")	
			{
				cells.set(index, new Cell("food","food",x,y,-1,1));
				foodIndex = index;
			}
			else if (cells.get(index).cellState == "dead")	
				cells.set(index, new Cell("dead","dead",x,y,-1,1));
			else if (cells.get(index).cellState == "tenticle")
			{
				cells.set(index, new Cell("tenticle","tenticle",x,y,-1,1));
				
			}

		}
		}
		//PrintCells(numRows, numColumns, cells); // Call the update cells method which
											// displays the cells.
        return(cells);
	}

	// This method prints out the cells
	private static void PrintCells(int numRows, int numColumns, ArrayList<Cell> cells) {
		for (int y = 0; y < numRows; y++) {
			for (int x = 0; x < numColumns; x++) // Loop through each cell
			{
				// In order to get the index of the cell based off of its X and
				// Y coords,
				// we use this handy formula: index = numColumns * y + x
				//int no = numColumns * y + x;	
				
				String state = cells.get(numColumns * y + x).cellState;
				if (state == "trace")
					System.out.print(Cell.traceChar);
				else if (state == "slime")
					System.out.print(Cell.slimeChar);
				else if (state == "wall")
					System.out.print(Cell.wallChar);
				else if (state == "food")
					System.out.print(Cell.foodChar);
				else if (state == "dead")
					System.out.print(Cell.deadChar);
				else if (state == "tenticle")
					System.out.print(Cell.tenticleChar);
				else if (state == "empty")
					System.out.print(Cell.emptyChar);
			}
		System.out.println();
		}

	}

	@SuppressWarnings("resource")
	@Override
	public void run() {

		//BufferedReader br = null;
		try {
		
			br = new BufferedReader(new FileReader(
					"/Users/ioana/Downloads/Conway's Game of Life/src/example.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				numRows++;
				numColumns = line.length();
				for (int i = 0; i < numColumns; i++) {
					char value = line.charAt(i);
					if (value == '@') {
						cells.add(new Cell("food","food",numRows*40,i*40,-1,1));
					} else if (value == '*') {
						cells.add(new Cell("wall","wall",numRows*40,i*40,-1,1));
					}else if (value == '0') {
						cells.add(new Cell("empty","empty",numRows*40,i*40,-1,1));
					} else if (value == '1') {
						cells.add(new Cell("slime","slime",numRows*40,i*40,-1,1));
					} else if (value == 'x') {
						cells.add(new Cell("dead","dead",numRows*40,i*40,-1,1));
					}
				}

			}
		/*
			char[][] maze = new char[numRows+2][numColumns+2];
			maze = MazeCreator.generateMaze(numRows,numColumns);
			numRows = numRows +2;
			numColumns = numColumns +2;
			for(int i=0;i<numRows;i++){
				for(int j=0;j<numColumns;j++)
					System.out.print(maze[i][j]);
				System.out.println();
				}
			for(int i=0;i<numRows;i++){
				for(int j=0;j<numColumns;j++)
				{
					if (maze[i][j] == '@') {
						cells.add(new Cell("food","food",numRows*40,i*40));
					} else if (maze[i][j] == '*') {
						cells.add(new Cell("wall","wall",numRows*40,i*40));
					}else if (maze[i][j] == '0') {
						cells.add(new Cell("empty","empty",numRows*40,i*40));
					} else if (maze[i][j] == '1') {
						cells.add(new Cell("slime","slime",numRows*40,i*40));
					} else if (maze[i][j] == 'x') {
						cells.add(new Cell("dead","dead",numRows*40,i*40));
					}
				}
			}*/

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(true)
		{
			while(unfinished)
			{
			cells = simulateStep(numRows, numColumns, cells);
			repaint();	
			try{
				Thread.sleep(1000);
			}catch(InterruptedException ex)
			{
			   Logger.getLogger(MouldModel.class.getName()).log(Level.SEVERE,null, ex);
			}
			}
			
		}
	}
}