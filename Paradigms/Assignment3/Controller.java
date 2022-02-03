//Robert Goss
//September 21, 2021
//Assigment 3
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class Controller implements ActionListener, MouseListener, KeyListener
{
	View view;
	Model model;
	boolean keyLeft;
	boolean keyRight;
	boolean keyUp;
	boolean keyDown;
	boolean keyS;
	boolean keyL;
	boolean isMousePressed;
	boolean isMouseReleased;
	int pressedX;
	int pressedY;
	int releasedX;
	int releasedY;

	Controller(Model m)
	{
		model = m;
	}

	public void actionPerformed(ActionEvent e)
	{
		
	}

	void setView(View v)
	{
		view =  v;
	}

	public void mousePressed(MouseEvent e)
	{
		pressedX = e.getX();
		pressedY = e.getY();
		isMousePressed = true;
	}

	public void mouseReleased(MouseEvent e) 
	{
		releasedX = e.getX();
		releasedY = e.getY();
		isMouseReleased = true;
	}

	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) {    }

	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: keyRight = true; break;
			case KeyEvent.VK_LEFT: keyLeft = true; break;
			case KeyEvent.VK_UP: keyUp = true; break;
			case KeyEvent.VK_DOWN: keyDown = true; break;
			case KeyEvent.VK_S: keyS = true; break;
			case KeyEvent.VK_L: keyL = true; break;
		}
	}

	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: keyRight = false; break;
			case KeyEvent.VK_LEFT: keyLeft = false; break;
			case KeyEvent.VK_UP: keyUp = false; break;
			case KeyEvent.VK_DOWN: keyDown = false; break;
			case KeyEvent.VK_S: keyS = false; break;
			case KeyEvent.VK_L: keyL = false; break;
		}
	}

	public void keyTyped(KeyEvent e)
	{
	}

	void update()
	{
		if(keyRight)
		{
			model.cameraPos += 4;
		}
		if(keyLeft)
		{
		 model.cameraPos -= 4;
		}
		if(keyS)
		{
			model.marshall().save("map.json");
		}
		if(keyL)
		{
			Json j = Json.load("map.json");
			model.unmarshall(j);
		}
		if(isMousePressed && isMouseReleased)
		{
			//This code finds the bottom left point of the rectangle and does that math to make the brick from there
			int brickX, brickY, brickW, brickH;
			if(pressedX > releasedX)
			{
				brickX = releasedX;
				brickW = pressedX - releasedX;
			}
			else
			{
				brickX = pressedX;
				brickW = releasedX - pressedX;
			}
			if(pressedY > releasedY)
			{
				brickY = releasedY;
				brickH = pressedY - releasedY;
			}
			else
			{
				brickY = pressedY;
				brickH = releasedY - pressedY;
			}
			Brick b = new Brick(brickX, brickY, brickW, brickH);
			model.bricks.add(b);
			isMousePressed = false;
			isMouseReleased = false;
		}
	}

}
