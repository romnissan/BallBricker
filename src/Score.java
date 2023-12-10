import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Score extends Rectangle{

	int gameHeight;
	int gameWidth;
	int score = 0;
	
	Score(int gameHeight, int gameWidth){
		this.gameHeight=gameHeight;
		this.gameWidth = gameWidth;
	}
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Consolas", Font.PLAIN,20));
		g.drawString("Score: "+score, 10, 25);

	}
}
