//Robert Goss
//October 21, 2021
//Assigment 5
import javax.swing.JPanel;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Image;

class View extends JPanel
{
	Model model;
	Image backgroundImage;
	static int backgroudPos;
	View(Controller c, Model m)
	{
		model = m;
		c.setView(this);
		backgroundImage = loadImage("background.png");
	}

	static Image loadImage(String filename)
	{
		Image readImage = null;
		try
		{
			readImage = ImageIO.read(new File(filename));
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			System.exit(1);
		}	
		return readImage;
	}

	public void paintComponent(Graphics g)
	{
		g.drawImage(backgroundImage, backgroudPos, 0, this.getWidth() * 2, this.getHeight() * 2, null);
		g.setColor(Color.gray);
		g.fillRect(0, 596, this.getWidth(), this.getHeight());
		for(int i = 0; i < model.sprites.size(); i++)
		{
			Sprite s = model.sprites.get(i);
			s.draw(g);
		}
	}
}
