//Robert Goss
//October 21, 2021
//Assigment 5
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

class Controller implements ActionListener, MouseListener, KeyListener
{
	View view;
	Model model;
	Mario mario;
	ArrayList<Sprite> sprites;
	boolean keyLeft, keyRight, keyUp, keyDown;
	boolean keyS, keyL, keyE, keyC, keySpace, isMousePressed, isMouseReleased, isEditMode, isCoinMode;
	int pressedX, pressedY, releasedX, releasedY;
	static boolean spawnCoin;

	Controller(Model m)
	{
		model = m;
	}

	public void actionPerformed(ActionEvent e)
	{
		
	}
	void updateImageCount()
	{
		if(model.mario.marioImageCount < 4)
		{
			model.mario.marioImageCount++;
		}
		else
		{
			model.mario.marioImageCount = 0;
		}

	}
	void setView(View v)
	{
		view =  v;
	}

	public void mousePressed(MouseEvent e)
	{
		pressedX = e.getX() + model.mario.x - model.mario.marioScreenPos;
		pressedY = e.getY();
		isMousePressed = true;
	}

	public void mouseReleased(MouseEvent e) 
	{
		releasedX = e.getX() + model.mario.x - model.mario.marioScreenPos;
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
			case KeyEvent.VK_E: keyE = true; break;
			case KeyEvent.VK_C: keyC = true; break;
			case KeyEvent.VK_SPACE: keySpace = true; break;
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
			case KeyEvent.VK_E: keyE = false; break;
			case KeyEvent.VK_C: keyC = false; break;
			case KeyEvent.VK_SPACE: keySpace = false; break;
		}
	}

	public void keyTyped(KeyEvent e)
	{
	}

	void update()
	{
		model.mario.px = model.mario.x;
		model.mario.py = model.mario.y;
		if(keyRight)
		{
			model.mario.x += 6;
			updateImageCount();
			View.backgroudPos--;
		}
		if(keyLeft)
		{
			model.mario.x -= 6;
			updateImageCount();
			View.backgroudPos++;
		}
		model.mario.canJump = false;
		if(keySpace)
		{
			model.mario.canJump = true;
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
		//This toggles if you draw a normal brick or a coin brick
		if(keyC)
		{
			if(isCoinMode)
			{
				isCoinMode = false;
			}
			else
			{
				isCoinMode = true;
			}
		}
		//This toggles if you can or cannot draw a brick.
		if(keyE)
		{
			if(isEditMode)
			{
				isEditMode = false;
			}
			else
			{
				isEditMode = true;
			}
		}
		//Press E for edit mode
		if(isMousePressed && isMouseReleased)
		{
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
			if(isEditMode && !isCoinMode)
			{
				Brick b = new Brick(false, brickX, brickY, brickW, brickH, model);
				model.sprites.add(b);
			}
			if(isEditMode && isCoinMode)
			{
				Brick b = new Brick(5, true, brickX, brickY, brickW, brickH, model);
				model.sprites.add(b);
			}
			isMousePressed = false;
			isMouseReleased = false;
		}
	}

}
