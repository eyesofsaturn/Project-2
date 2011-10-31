/*

Rajwinder "Ricky" Sidhu
October 2011

*/

import java.awt.*;
import java.awt.event.*;  
import javax.swing.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class Project2SF  extends SimFrame   {
	private static final long serialVersionUID = 42L;

   @SuppressWarnings("unused")
private Project2SF app;
   // application variables;
   /** the actors "bots" of the simulation */
   HashMap<Integer, P2Waypoint> hmap = new HashMap<Integer, P2Waypoint>(400);
   P2Bot b;
   P2Waypoint botWP;
   P2Waypoint goal;
   P2Waypoint start;
   boolean goalreached = false;


   public static void main(String args[]) {
      Project2SF app = new Project2SF("Project2SF", "terrain282F11.png");
      app.start();  // start is inherited from SimFrame
      }

   public Project2SF(String frameTitle, String imageFile) {
      super(frameTitle, imageFile);
      // create menus
      JMenuBar menuBar = new JMenuBar();
      // set About and Usage menu items and listeners.
      aboutMenu = new JMenu("About");
      aboutMenu.setMnemonic('A');
      aboutMenu.setToolTipText(
        "Display information about this program");
      // create a menu item and the dialog it invoke 
      usageItem = new JMenuItem("Usage");
      authorItem = new JMenuItem("Author");
      usageItem.addActionListener( // anonymous inner class event handler
         new ActionListener() {        
         public void actionPerformed(ActionEvent event) {
            JOptionPane.showMessageDialog( Project2SF.this, 
               "Click anywhere on the map to create a bot at the nearest waypoint." +
               "\n The bot will immediately begin to navigate the map until it is" +
               "\n exhausted.",
               "Usage",   // dialog window's title
               JOptionPane.PLAIN_MESSAGE);
               }}
         );
      // create a menu item and the dialog it invokes
      authorItem.addActionListener(
         new ActionListener() {          
            public void actionPerformed(ActionEvent event) {
               JOptionPane.showMessageDialog( Project2SF.this, 
               "Ricky Sidhu \n" +
					"rajwinder.sidhu.842@my.csun.edu \n" +
					"Comp 282",
               "author",  // dialog window's title
               JOptionPane.INFORMATION_MESSAGE,
					//  author's picture 
               new ImageIcon("C:\\Users\\Mugen\\workspace\\P1Sidhu\\src\\author.png"));
               }}
         );
      // add menu items to menu 
      aboutMenu.add(usageItem);   // add menu item to menu
      aboutMenu.add(authorItem);
      menuBar.add(aboutMenu);
      setJMenuBar(menuBar);
      validate();  // resize layout managers
      // construct the application specific variables
      }

   public P2Waypoint nearestWaypoint(int x, int y){
	   P2Waypoint mP = new P2Waypoint(x,y);
	   Iterator<Integer> it = hmap.keySet().iterator();
	    int dist=1000000;					//Iterate through the map and find the closest waypoint
		int shortestdist=dist;				//to place the bot on.
		P2Waypoint closestWaypoint = new P2Waypoint();
		while (it.hasNext()){
			P2Waypoint o = hmap.get(it.next());
			int x2 = o.x;
			int y2 = o.y;
			dist = (int) (Math.pow(mP.x - x2, 2));
			dist+= (Math.pow(mP.y - y2, 2));
			dist = (int) Math.sqrt(dist);
			if (dist<shortestdist){
				shortestdist = dist;
				closestWaypoint = o;
			}
		}
		return closestWaypoint;
   }
/* plot()
 * 
 * the plot() method runs on the logic that if a waypoint has certain given properties,
 * it is possible that it may be a special location.
 * The possibilities are as listed:
 * 
 * 1. if the waypoint has a cost, it is a city. cities have blue markers of size 3.
 * 2. if the waypoint has a mapX/mapY pair of values, it is a map location. map locations have yellow markers of size 3.
 * 3. if the waypoint has a gold value, it is a treasure location. treasure locations have orange markers of size 4.
 * 4. if the waypoint satisfies NONE of these, it is merely a place, with a normal black marker (default size being 2).
 * 
 */
   public void plot(){
	   
	   Iterator<Integer> dit = hmap.keySet().iterator();
		while(dit.hasNext()){
			P2Waypoint w = hmap.get(dit.next());
			 //if (w.cost==0 && w.gold == 0 && w.mapX == 0){
			   Marker d = new Marker(w.x, w.y, Color.black);
			   animatePanel.addPermanentDrawable(d);
		 //  }
		   if(w.cost>0){
			 //blue, CITY,3
			   Marker b = new Marker(w.x, w.y, Color.blue);
			   b.setSize(3);
			   animatePanel.addPermanentDrawable(b);
		   }
		   else if(w.mapX>0){
			 //yellow, MAP,3
			   Marker s = new Marker(w.x, w.y, Color.yellow);
			   s.setSize(3);
			   animatePanel.addPermanentDrawable(s);
		   }
		   else if(w.gold>0){
			 //orange, GOLD, 4
			   Marker e = new Marker(w.x, w.y, Color.orange);
			   e.setSize(4);
			   animatePanel.addPermanentDrawable(e);
		   }
		}
		
	  
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
					animatePanel.addPermanentDrawable(new Connector(w.x, w.y, m.x, m.y, Color.black));
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
    
   public void setSimModel() {
      setStatus("Initial state of simulation");
      read();
      plot();
      
      	setStatus("Click anywhere to create the start location.");
	    waitForMousePosition();
	    botWP = nearestWaypoint((int) mousePosition.getX(), (int) mousePosition.getY());
		placeBot("Red", (int) botWP.x, 
				(int) botWP.y, Color.green);
		Marker startMarker = new Marker(botWP.x, botWP.y, Color.green);
		/*for(int s=0; s<botWP.neighbors.size(); s++){
			animatePanel.addTemporaryDrawable(new Marker(botWP.neighbors.get(s).x, botWP.neighbors.get(s).y,
					Color.cyan));
		}*/
		startMarker.setSize(5);
		animatePanel.addPermanentDrawable(startMarker);
		setStatus("Click anywhere to create the goal.");
		waitForMousePosition();
		goal = nearestWaypoint((int) mousePosition.getX(), (int) mousePosition.getY());
		Marker goalMarker = new Marker(goal.x, goal.y, Color.red);
		goalMarker.setSize(5);
		animatePanel.addPermanentDrawable(goalMarker);
		//animatePanel.addTemporaryDrawable(new Connector(botWP.x, botWP.y, goal.x, goal.y, Color.MAGENTA));
		
      }
   		
   private void placeBot(String name, int x, int y, Color color) {
	      b = new P2Bot(name, x, y, color);
	      animatePanel.addBot(b);
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
   
   public double distance(P2Waypoint o1, P2Waypoint o2){
	   double dist;
	   double xdist;
	   double ydist;
	   double hdist;
	   xdist = o1.x-o2.x;
	   xdist = Math.pow(xdist, 2);
	   ydist = o1.y-o2.y;
	   ydist = Math.pow(ydist, 2);
	   hdist = o1.height-o2.height;
	   hdist = Math.pow(hdist, 2);
	   dist = Math.sqrt(xdist+ydist+hdist);
	   return dist;
   }
   
   public double pathBack(P2Waypoint b){
	   if(b.pred==null){
		   return 0;
	   }
	   else {
		   return distance(b, b.pred) + pathBack(b.pred);
	   }
   }

   public synchronized boolean a_Star(P2Waypoint start, P2Waypoint end){ //this doesn't work right now
	   WPComparator wpcomp = new WPComparator();
	   PriorityQueue<P2Waypoint> open = new PriorityQueue<P2Waypoint>(20, wpcomp);
	   ArrayList<P2Waypoint> closed = new ArrayList<P2Waypoint>();
	   ArrayList<P2Waypoint> path = new ArrayList<P2Waypoint>();
	   P2Waypoint curr, goal, last;
	   curr = start;
	   last = null;
	   goal = end;
	   
	   if(curr.neighborCount==0){
		   setStatus("No neighbors");
		   return false;
	   }
	   open.add(curr);
	   while(open.peek() != goal){
		   last=curr;
		   curr = open.remove();
		   closed.add(curr);
		   for(int i = 0; i<curr.neighborCount; i++){
			  double cost = pathBack(curr)+distance(curr, curr.neighbors.get(i));
			  if( (open.contains(curr.neighbors.get(i)) )&& (cost < pathBack(curr.neighbors.get(i)))){
				  open.remove(curr.neighbors.get(i));
			  }
			  if(closed.contains((curr.neighbors.get(i))) && (cost < pathBack(curr.neighbors.get(i)))){
				  closed.remove(curr.neighbors.get(i));
			  }
			  if(!open.contains(curr.neighbors.get(i)) && !closed.contains(curr.neighbors.get(i)))
			  {
				  curr.neighbors.get(i).f=cost+distance(curr.neighbors.get(i), goal);
				  curr.neighbors.get(i).pred = curr;
				  open.add(curr.neighbors.get(i));
			  }
			  
		   }
	   }
	   
	   if(open.peek() == goal){
		   return true;
	   }
	   
	   return false;
   }
   
   
   
   
   public synchronized void simulateAlgorithm() {
	   	   
      while (runnable()) {
   	if(a_Star(botWP, goal)){
   		setStatus("Goal Reached");
   		animatePanel.setComponentState(false, false, false, false, true);
   		return;
   		}
   	else{
   		setStatus("Goal Unreachable");
   		animatePanel.setComponentState(false, false, false, false, true);
   		return;
   	}
       

      }
      checkStateToWait();
   }
}


