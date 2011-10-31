import java.awt.Color;
import java.awt.Point;


public class P2Bot extends Bot {
	public int strength;
	private int lastX, lastY;  
	public P2Bot(String label, Point pt, Color colorValue) {
		super(label, pt, colorValue);
		// TODO Auto-generated constructor stub
	}

	public P2Bot(String name, int x, int y, Color color) {
		super(name, x, y, color);
		lastX = x;
		lastY = y;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void reset() {
		super.reset();
		lastX = (int) point.getX(); 
		lastY = (int) point.getY();
		}
	public void move() {
		// TODO Auto-generated method stub
	}

}