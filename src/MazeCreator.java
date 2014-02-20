import java.util.*;

public class MazeCreator {
	
	public static char[][] generateMaze(int r, int c)
    {
    	// dimensions of generated maze
    	//int r=8, c=8;
 
    	// build maze and initialize with only walls
        StringBuilder s = new StringBuilder(c);
        for(int x=0;x<c;x++)
        	s.append('*');
        char[][] maz = new char[r][c];
        char[][] maze = new char[r+2][c+2];
        for(int x=0;x<r;x++) maz[x] = s.toString().toCharArray();
 
        // select random point and open as start node
        Point st = new Point((int)(Math.random()*r),(int)(Math.random()*c),null);
        maz[st.r][st.c] = '1';
 
        // iterate through direct neighbors of node
        ArrayList<Point> frontier = new ArrayList<Point>();
        for(int x=-1;x<=1;x++)
        	for(int y=-1;y<=1;y++){
        		if(x==0 && y==0||x!=0 && y!=0)
        			continue;
        		try{
        			if(maz[st.r+x][st.c+y]=='0') continue;
        		}catch(Exception e){ // ignore ArrayIndexOutOfBounds
        			continue;
        		}
        		// add eligible points to frontier
        		frontier.add(new Point(st.r+x,st.c+y,st));
        	}
 
        Point last=null;
        while(!frontier.isEmpty()){
 
        	// pick current node at random
        	Point cu = frontier.remove((int)(Math.random()*frontier.size()));
        	Point op = cu.opposite();
        	try{
        		// if both node and its opposite are walls
        		if(maz[cu.r][cu.c]=='*'){
        			if(maz[op.r][op.c]=='*'){
 
        				// open path between the nodes
        				maz[cu.r][cu.c]='0';
        				maz[op.r][op.c]='0';
 
        				// store last node in order to mark it later
        				last = op;
 
        				// iterate through direct neighbors of node, same as earlier
        				for(int x=-1;x<=1;x++)
				        	for(int y=-1;y<=1;y++){
				        		if(x==0&&y==0||x!=0&&y!=0)
				        			continue;
				        		try{
				        			if(maz[op.r+x][op.c+y]=='0') continue;
				        		}catch(Exception e){
				        			continue;
				        		}
				        		frontier.add(new Point(op.r+x,op.c+y,op));
				        	}
        			}
        		}
        	}catch(Exception e){ // ignore NullPointer and ArrayIndexOutOfBounds
        	}
 
        	// if algorithm has resolved, mark end node
        	if(frontier.isEmpty())
        		maz[last.r][last.c]='@';
        }
 /*
		// print final maze
		for(int i=0;i<r;i++){
			for(int j=0;j<c;j++)
				System.out.print(maz[i][j]);
			System.out.println();
			}
		*/
		// create new maze with additional walls
		for(int i=0;i<c+2;i++){	
			maze[0][i]='*';
			maze[r+1][i]='*';
		}
		for(int i=0;i<r+2;i++){		
			maze[i][0]='*';
			maze[i][c+1]='*';
		}
		for(int i=1;i<r+1;i++){
			for(int j=1;j<c+1;j++)
			{
				maze[i][j]=maz[i-1][j-1];
			}
		}/*
        System.out.println("New maze");
		for(int i=0;i<r+2;i++){
			for(int j=0;j<c+2;j++)
				System.out.print(maze[i][j]);
			System.out.println();
			}*/
		return maze;
    }
	static class Point{
    	Integer r;
    	Integer c;
    	Point parent;
    	public Point(int x, int y, Point p){
    		r=x;c=y;parent=p;
    	}
    	// compute opposite node given that it is in the other direction from the parent
    	public Point opposite(){
    		if(this.r.compareTo(parent.r)!=0)
    			return new Point(this.r+this.r.compareTo(parent.r),this.c,this);
    		if(this.c.compareTo(parent.c)!=0)
    			return new Point(this.r,this.c+this.c.compareTo(parent.c),this);
    		return null;
    	}

   }
}
