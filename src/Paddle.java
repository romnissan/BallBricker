import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Paddle extends Rectangle {
	//fields
	int xVelocity=0;
	int speed = 10;
		
	public Paddle(int x, int y, int width, int height) {
		super(x-width/2,y,width,height);
	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			xVelocity = -speed;
			move();
		}
		if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
			xVelocity=speed;
			move();
		}
	}
//	private void setDirection(int direction) {
//		xVelocity=direction;
//	}
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			xVelocity = 0;
			move();
		}
		if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
			xVelocity = 0;
			move();
		}
	}
	public void move() {
		this.x = x + xVelocity;
	}
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
	}
}
