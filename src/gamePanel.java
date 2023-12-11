import java.awt.event.*;
import java.util.LinkedList;
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
	static final int NUMBERS_OF_BRICKS = 20;
	static final int POWER_WIDTH = 20;
	static final int POWER_HEIGHT = 20;
	static boolean isWon=false;
	static boolean start=true;
	static boolean running=true;
	static boolean powers=false;
	Powers power;
	Image image;
	Thread thread;
	Graphics graphics;
	Random random;
	LinkedList<Ball> balls = new LinkedList<>();
	Paddle paddle;
	Bricks[] bricks = new Bricks[NUMBERS_OF_BRICKS];
	Score score;
	
	//constructor
	gamePanel(){
		Ball ball = new Ball(GAME_WIDTH/2, GAME_HEIGHT-3*PADDLE_HEIGHT-BALL_DIAMETER, BALL_DIAMETER, BALL_DIAMETER);
		//LinkedList<Ball> balls = new LinkedList<>();
		balls.add(ball);
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
		//stop paddle at the edges
		if(paddle.x<=0)
			paddle.x = 0;
		if(paddle.x>=GAME_WIDTH-PADDLE_WIDTH)
			paddle.x = GAME_WIDTH-PADDLE_WIDTH;
		for (Ball b : balls) {
		//ball meet paddle
		if(b.y<=GAME_HEIGHT-3*PADDLE_HEIGHT) {
			if(b.intersects(paddle)) {
				 b.xVelocity = (b.x-paddle.x-PADDLE_WIDTH/2)*(b.speed*200/PADDLE_WIDTH)/100;
				 b.yVelocity = -Math.sqrt(Math.abs((b.speed*b.speed)-(b.xVelocity*b.xVelocity)));
			}
		}
		
		if(b.y>=GAME_HEIGHT && balls.size()==1) {
			running = false;
			draw(graphics);
		}
		else if(b.y>=GAME_HEIGHT)
			balls.remove(b);
		
		//ball meet upper screen
			if(b.y<=0)
				b.yVelocity = -b.yVelocity;
			if(power!=null && power.y<=0)
				power.yVelocity = -power.yVelocity;
			//ball meet sides borders
			if(b.x<=0)
				b.xVelocity = -b.xVelocity;
			if(power!=null && power.x<=0)
				power.xVelocity = -power.xVelocity;
			if(b.x>=GAME_WIDTH)
				b.xVelocity = -b.xVelocity;
			if(power!=null && power.x>=GAME_WIDTH)
				power.xVelocity = -power.xVelocity;
			//ball meet brick
			for (int i = 0; i < bricks.length; i++) {
				int count = 0;
				if(bricks[i]!=null) {
					if(b.intersects(bricks[i])) {
						score.score += 15;
						if(b.y>=bricks[i].y+bricks[i].height/2)
							b.yVelocity = -b.yVelocity;
						else if(b.y<=bricks[i].y-bricks[i].height/2)
							b.yVelocity = -b.yVelocity;
						else
							b.xVelocity = -b.xVelocity;
						if (score.score%75 == 0) {
							power = new Powers(bricks[i].x, bricks[i].y, POWER_WIDTH, POWER_HEIGHT);
							powers = true;
						}
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
		//paddle meet power
		if(power!=null && power.intersects(paddle)) {
			switch (power.id) {
			case 0:
				paddle.width += paddle.width + paddle.width/4;
				break;
			case 1:
				for (Ball ball : balls) {
					ball.speed = ball.speed*0.7;
					ball.setVelocity(ball.speed);
				}
					break;
			case 2:
				Ball ball2 = new Ball(paddle.x, paddle.y, BALL_DIAMETER,BALL_DIAMETER);
				balls.add(ball2);
				break;
			case 3:
				paddle.width = paddle.width - paddle.width/4;
				break;
			case 4:
				for (Ball ball : balls) {
					ball.speed = ball.speed*1.5;
					ball.setVelocity(ball.speed);
				}
					break;
			case 5:
				running = false;
				break;
			}
			powers=false;
			power=null;
		}
	}
	
	public void draw(Graphics g) {
		if(isWon) youWon(g);
		else if (running) {
			score.draw(g);
			if(powers)
				power.draw(g);
			paddle.draw(g);
			if(!balls.isEmpty())
				for (Ball ball : balls) {
					ball.draw(g);
				}
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
			for (Ball ball : balls) 
				ball.move();
			if(powers)
				power.move();
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

