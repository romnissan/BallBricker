import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Powers extends Ball{
	double speed = 10;
	double xVelocity = speed/Math.sqrt(2);
	double yVelocity = -Math.sqrt(speed*speed-xVelocity*xVelocity);
	Random random;
	int id;
	
	public Powers(int x, int y, int width, int height) {
			super(x, y, width, height);
			setVelocity();
			id = random.nextInt(6);
		}

		protected void setVelocity() {
			random = new Random();
			if(random.nextInt(2)==1)
				xVelocity = -xVelocity;
		}
		
		public void move() {
			x = (int) (x+xVelocity);
			y = (int) (y+yVelocity);
		}
		
		public void draw(Graphics g) {
		    if (id < 3) {
		        g.setColor(Color.blue);
		    } else if (id == 5) {
		        g.setColor(Color.red);
		        g.fillRect(x, y, (int)(width*1.5), (int)(height*1.5));
		    } else {
		        g.setColor(Color.red);
		    }

		    g.fillRect(x, y, width, height);
		    g.setColor(Color.white);

		    if (id != 5) 
		        g.drawString("?", x + 5, y + 15);  // Adjust the y-coordinate for proper positioning
		    else {
		    	g.drawString("o", x + 5, y + 15);
		    	g.drawString("X", x + 5, y + 15);

		    }
		}
}
