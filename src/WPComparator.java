import java.util.Comparator;


public class WPComparator implements Comparator<P2Waypoint> {

@Override
public int compare(P2Waypoint o1, P2Waypoint o2) {
	if (o1.f < o2.f){
		return -1;
	}
	if(o1.f > o2.f){
		return 1;
	}
	return 0;
	}
}
