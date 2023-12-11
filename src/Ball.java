import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.*;

public class Ball extends Rectangle{

	double speed = 10;
	double xVelocity;
	double yVelocity;
	Random random;
	
	Ball(int x, int y, int width, int height){
		super(x, y, width, height);
		setVelocity(speed);
		random = new Random();
		if(random.nextInt(2)==1)
			xVelocity = -xVelocity;
	}

	protected void setVelocity(double speed) {
		yVelocity = -Math.sqrt(speed*speed-xVelocity*xVelocity);
		xVelocity = speed/Math.sqrt(2);
		
	}
	public void move() {
		x = (int) (x+xVelocity);
		if(xVelocity==0) xVelocity++;
		y = (int) (y+yVelocity);
		if(yVelocity==0) yVelocity++;
	}
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillOval(x, y, width, height);
	}

//	public void remove() {
//		xVelocity=0;
//		yVelocity=0;
//		x=0;
//		y=0;
//		width=0;
//		height=0;
//	}
}
