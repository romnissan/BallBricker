import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Bricks extends Rectangle{
	Random random = new Random();
	int r = random.nextInt(256);
	int G = random.nextInt(256);
	int b = random.nextInt(256);
	public Bricks(int place, int numberOfBricks, int gameWidth, int gameHeight, int width, int height) {
		super(((place*(width+10))%(gameWidth-width)+10), ((place*(width+10)/(gameWidth-width)))*(height+10)+50, width, height);
		//super((place*(width+10)+50)%gameWidth-width , ((place*(width+10))/(gameWidth-width))*(height+10)+50,width,height);
	}
	public void draw(Graphics g) {
		g.setColor(new Color(r, G, b));
		g.fillRect(x, y, width, height);
	}
}
