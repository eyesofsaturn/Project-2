import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class ComPract {
	HashMap<Integer, P2Waypoint> hmap = new HashMap<Integer, P2Waypoint>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WPComparator wpcomp = new WPComparator();
		PriorityQueue<P2Waypoint> open = new PriorityQueue<P2Waypoint>(20, wpcomp);
		P2Waypoint a = new P2Waypoint(100, 300);
		P2Waypoint b = new P2Waypoint(400, 600);
		a.f = 5;
		b.f = 10;
		open.add(b);
		open.add(a);
		System.out.println(open);
		
		
	}
	public void read(){
		   File waypoints = new File("waypointNeighbor.txt");
			Scanner wps=null;
			try {
				wps = new Scanner(waypoints);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.exit(0);
			}
			
	/*
	*/
			while(wps.hasNext()){ 
					P2Waypoint w = new P2Waypoint(wps.nextInt(),
							wps.nextInt(),
							wps.nextInt(),
							wps.nextInt(),
							wps.nextInt(),
							wps.nextInt(),
							wps.nextInt(),
							wps.nextInt());
					for(int i=0; i<w.neighborCount; i++){
						P2Waypoint m = new P2Waypoint(wps.nextInt(), wps.nextInt());
						w.addNeighbor(m);
					}
					
					//Add the waypoint to the hashMap.
					hmap.put(w.hv(), w);
			}
			
			wps.close();
			
			//The following algorithm takes the neighbor arraylist of waypoints 
			//(which are just raw x-y coords) and refreshes them with real waypoints
			//from the map.
			
			Iterator<Integer> it = hmap.keySet().iterator();
			while(it.hasNext()){
				P2Waypoint j = hmap.get(it.next());
				for(int i = 0; i<j.neighborCount;i++){
					P2Waypoint b = j.neighbors.remove(i); //get the neighbor.
					j.neighbors.add(wpSearch(b, hmap)); // search for a real neighbor on hmap and add to j.
				}
				hmap.put(j.hv(), j);
			}
			

			
			
	   }
	   public P2Waypoint wpSearch(int x, int y, HashMap<Integer, P2Waypoint> kmap){
		   String m = Integer.toString(x)+Integer.toString(y);
		   P2Waypoint d = kmap.get(m);
		   return d;
		   
	   }
	   
	   public P2Waypoint wpSearch(P2Waypoint m, HashMap<Integer, P2Waypoint> kmap){
		   P2Waypoint d = hmap.get(m.hv());
		   return d;
	   }

}
