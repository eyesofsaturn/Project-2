import java.util.ArrayList;


public class P2Waypoint {

	public int x, y, height, cost, gold, mapX, mapY, neighborCount;
	double f;
	public ArrayList<P2Waypoint> neighbors = new ArrayList<P2Waypoint>();
	public P2Waypoint pred;
	//Constructors
	public P2Waypoint(){
		double g = 0;
	}
	public P2Waypoint(int xval, int yval){
		x = xval;
		y = yval;
		height = 0;
		cost = 0;
		gold = 0;
		mapX=0;
		mapY=0;
	}
	public P2Waypoint(int xval, int yval, int heightval, int costval, int goldval, 
			int mapXval, int mapYval, int nCount)
	{
		x = xval;
		y = yval;
		height = heightval;
		cost = costval;
		gold = goldval;
		mapX = mapXval;
		mapY = mapYval;
		neighborCount = nCount;
	}
	
	//Accessible Methods
	public String toString(){
		return x+" " +y+" "+height+" "+cost+" "+gold+" "+mapX+" "+mapY;
	}
	public int hv(){
		String a = (Integer.toString(x)+Integer.toString(y));
		return Integer.parseInt(a);
	}
	public void addNeighbor(int x, int y){
		P2Waypoint z = new P2Waypoint(x, y);
		neighbors.add(z);
	}
	public void addNeighbor(P2Waypoint k){
		neighbors.add(k);
	}
}
