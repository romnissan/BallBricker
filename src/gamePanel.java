import java.awt.event.*;
import java.util.Random;
import java.awt.*;
import javax.swing.JPanel;

public class gamePanel extends JPanel implements Runnable {
	//fields
	static final int GAME_HEIGHT = 600;
	static final int GAME_WIDTH = 1000;
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
	static final int BALL_DIAMETER = 20;
	static final int PADDLE_HEIGHT = 10;
	static final int PADDLE_WIDTH = 160;
	static final int BRICK_HEIGHT = 30;
	static final int BRICK_WIDTH = 50;
	static final int NUMBERS_OF_BRICKS = 1;
	static boolean isWon=false;
	static boolean start=true;
	static boolean running=true;
	Image image;
	Thread thread;
	Graphics graphics;
	Random random;
	Ball ball;
	Paddle paddle;
	Bricks[] bricks = new Bricks[NUMBERS_OF_BRICKS];
	Score score;
	
	//constructor
	gamePanel(){
		ball = new Ball(GAME_WIDTH/2, GAME_HEIGHT-3*PADDLE_HEIGHT-BALL_DIAMETER, BALL_DIAMETER, BALL_DIAMETER);
		paddle = new Paddle(GAME_WIDTH/2, GAME_HEIGHT-3*PADDLE_HEIGHT,PADDLE_WIDTH,PADDLE_HEIGHT);
		newBricks(bricks);
		score = new Score(GAME_HEIGHT,GAME_WIDTH);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(SCREEN_SIZE);
		thread = new Thread(this);
	}
	private void newBricks(Bricks[] bricks) {
		for (int i = 0; i < bricks.length; i++) {
			bricks[i] = new Bricks(i,NUMBERS_OF_BRICKS, GAME_WIDTH,GAME_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT);
		}
	}
	//methods
	public void checkCollisions() {
		//stop paddle at the adges
		if(paddle.x<=0)
			paddle.x = 0;
		if(paddle.x>=GAME_WIDTH-PADDLE_WIDTH)
			paddle.x = GAME_WIDTH-PADDLE_WIDTH;
		//ball meet paddle
		if(ball.y<=GAME_HEIGHT-3*PADDLE_HEIGHT) {
			if(ball.intersects(paddle)) {
				 ball.xVelocity = (ball.x-paddle.x-PADDLE_WIDTH/2)*(ball.speed*200/PADDLE_WIDTH)/100;
				 ball.yVelocity = -Math.sqrt(Math.abs((ball.speed*ball.speed)-(ball.xVelocity*ball.xVelocity)));
			}
		}
		
		if(ball.y>=GAME_HEIGHT) {
			running = false;
			draw(graphics);
		}
		//ball meet upper screen
		if(ball.y<=0)
			ball.yVelocity = -ball.yVelocity;
		//ball meet sides borders
		if(ball.x<=0)
			ball.xVelocity = -ball.xVelocity;
		if(ball.x>=GAME_WIDTH)
			ball.xVelocity = -ball.xVelocity;
		//ball meet brick
		for (int i = 0; i < bricks.length; i++) {
			int count = 0;
			if(bricks[i]!=null) {
				if(ball.intersects(bricks[i])) {
					score.score += 15;
					if(ball.y>=bricks[i].y+bricks[i].height/2)
						ball.yVelocity = -ball.yVelocity;
					else if(ball.y<=bricks[i].y-bricks[i].height/2)
						ball.yVelocity = -ball.yVelocity;
					else
						ball.xVelocity = -ball.xVelocity;
					bricks[i] = null;
				}
			}
			else count++;
			if(count == bricks.length) {
				isWon = true;
				draw(graphics);
			}
		}
		
	}
	
	public void draw(Graphics g) {
		if(isWon) youWon(g);
		else if (running) {
			score.draw(g);
			paddle.draw(g);
			ball.draw(g);
			for (int i = 0; i < bricks.length; i++) {
				if (bricks[i]!=null)
					bricks[i].draw(g);
			}
		}
		else gameOver(g);
	}
	
	public void move() {
		if(start) {
			paddle.move();
			ball.move();
		}
	}
	public void paint(Graphics g) {
		image = createImage(getWidth(), getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0, 0, this);
	}
	public void run() {
		//game loop
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		while(start) {
			long now = System.nanoTime();
			delta +=(now - lastTime)/ns;
			lastTime = now;
			if(delta>=1) {
				move();
				checkCollisions();
				repaint();
				delta--;
			}
		}
	}
	
	public void youWon(Graphics g) {
		g.setColor(Color.GREEN);
		g.setFont(new Font("Ink Free", Font.LAYOUT_LEFT_TO_RIGHT, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("YOU WON!", (GAME_WIDTH - metrics.stringWidth("YOU WON!"))/2, GAME_HEIGHT/2 );
	}
	
	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.LAYOUT_LEFT_TO_RIGHT, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (GAME_WIDTH - metrics.stringWidth("Game Over"))/2, GAME_HEIGHT/2 );
	}
	
	public class AL extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				start = true;
				thread.start();	
			}
				else
					paddle.keyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			paddle.keyReleased(e);
		}
	}
}

