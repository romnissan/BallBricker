import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.*;

public class Ball extends Rectangle{

	double speed = 10;
	double xVelocity = speed/Math.sqrt(2);
	double yVelocity = -Math.sqrt(speed*speed-xVelocity*xVelocity);
	Random random;
	
	Ball(int x, int y, int width, int height){
		super(x, y, width, height);
		setVelocity();
	}

	private void setVelocity() {
		random = new Random();
		if(random.nextInt(2)==1)
			xVelocity = -xVelocity;
	}
	public void move() {
		x = (int) (x+xVelocity);
		y = (int) (y+yVelocity);
	}
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillOval(x, y, width, height);
	}
}
