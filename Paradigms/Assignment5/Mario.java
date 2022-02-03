//Robert Goss
//October 21, 2021
//Assigment 5
import java.awt.Image;
import java.awt.Graphics;
class Mario extends Sprite
{
    int px, py;
    double vert_vel;
    int marioImageCount;
    int marioScreenPos;
    static Image images[] = null;
    int framesSinceGround;
    boolean canJump;
    boolean hitCoinBlock;
    Mario()
    {
        x = 0;
        y = 450;
        w = 60;
        h = 95;
        px = 0;
        py = 450;
        marioScreenPos = 100;
        vert_vel = 0;
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

    void collisionFix(Sprite s)
    {
        hitCoinBlock = false;
        if(this.x + this.w >= s.x && this.px + this.w <= s.x)
        {
            this.x = s.x - this.w;
        }
        else if(this.x <= s.x + s.w && this.px >= s.x + s.w)
        {
            this.x = s.x + s.w;
        }
        else if(this.y + this.h >= s.y && this.y + this.h <= s.y + s.h && this.py + this.h <= s.y)
        {
            this.y = s.y - this.h;
            framesSinceGround = 0;
        }
        else if(this.y <= s.y + s.h && this.y >= s.y && this.py >= s.y + s.h)
        {
            this.y = s.y + s.h + 5;
            vert_vel = 5;
            canJump = false;
            if(s.isCoinBrick())
            {
                hitCoinBlock = true;
            }
        }
    }

    public boolean update()
    {
       
       if(framesSinceGround > 0)
       {
            vert_vel += 1.2;
       }
        if(y >= 500)
        {
            y = 500;
            vert_vel = 0;
            framesSinceGround = 0;
        }
        if(canJump && framesSinceGround < 5)
        {
            vert_vel -= 4.5;
        }
      
        framesSinceGround++;
        y += vert_vel;
        return true;
    }

    void draw(Graphics g)
    {
        g.drawImage(images[marioImageCount], marioScreenPos, y, null);
    }
    
    @Override
    boolean isMario()
    {
        return true;
    }
}