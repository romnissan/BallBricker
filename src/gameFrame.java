import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.*;

public class gameFrame extends JFrame{
	gameFrame(){
		this.add(new gamePanel());
		this.setTitle("Ball Breaker");
		this.setBackground(Color.black);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		ImageIcon icon = new ImageIcon("C:\\Users\\romni\\Downloads\\logo.png");
		this.setIconImage(icon.getImage());
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}
}
