//Robert Goss
//October 21, 2021
//Assigment 5
import javax.swing.JFrame;
import java.awt.Toolkit;

public class Game extends JFrame
{
	Model model = new Model();
	Mario mario = new Mario();
	Controller controller = new Controller(model);
	View view = new View(controller, model);

	public Game()
	{
		this.setTitle("Mario but Java!");
		this.setSize(1920, 1080);
		this.setFocusable(true);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		view.addMouseListener(controller);
		this.addKeyListener(controller);	
	}

	public void run()
	{
		while(true)
		{
			model.update();
			controller.update();
			view.repaint(); // Indirectly calls View.paintComponent
			Toolkit.getDefaultToolkit().sync(); // Updates screen

			// Go to sleep for 50 miliseconds
			try
			{
				Thread.sleep(40);
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public static void main(String[] args)
	{
		Game g = new Game();
		g.run();
	}
}
