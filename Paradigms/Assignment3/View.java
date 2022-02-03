//Robert Goss
//September 21, 2021
//Assigment 3
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import javax.swing.JButton;
import java.awt.Color;


class View extends JPanel
{
	Model model;
	JButton b1;
	BufferedImage turtle_image;

	View(Controller c, Model m)
	{
		model = m;
		c.setView(this);
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(new Color(128, 255, 255));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		for(int i = 0; i < model.bricks.size(); i++)
		{
			Brick b = model.bricks.get(i);
			g.setColor(new Color(165,42,42));
			g.fillRect(b.x - model.cameraPos, b.y, b.w, b.h);
		}
	}

}
