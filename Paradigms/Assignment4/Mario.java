//Robert Goss
//October 4, 2021
//Assigment 4
import java.awt.Image;
import java.awt.Graphics;
class Mario
{
    static int x;
    int y;
    int px, py;
    final int w = 60;
    final int h = 95;
    double vert_vel;
    int marioImageCount;
    static int marioScreenPos;
    Model model;
    static Image images[] = null;
    static int framesSinceGround;
    Mario()
    {
        x = 0;
        y = 450;
        px = 0;
        py = 450;
        marioScreenPos = 50;
        if(images == null)
        {
            images = new Image[5];
            images[0] = View.loadImage("mario1.png");
            images[1] = View.loadImage("mario2.png");
            images[2] = View.loadImage("mario3.png");
            images[3] = View.loadImage("mario4.png");
            images[4] = View.loadImage("mario5.png");
        }
        
    }


    void update()
    {
        py = y;
        if(framesSinceGround > 0)
        {
            vert_vel += 1.2;
        }
        if(y >= 500)
		{
			vert_vel = 0.0;
            y = 500; 
            framesSinceGround = 0;
        }
        if(Controller.canJump)
        {
            vert_vel -= 4;
        }
        y += vert_vel;
       
        if(y < 500)
        {
            framesSinceGround++;
        }
    }

    void draw(Graphics g)
    {
        g.drawImage(images[marioImageCount], x - Mario.x + marioScreenPos, y, null);
    }
}