//Robert Goss
//October 21, 2021
//Assigment 5
import java.awt.Graphics;

abstract class Sprite
{
    int x, y, w, h;
    abstract boolean update();
    abstract void draw(Graphics g);

    boolean isBrick()
    {
        return false;
    }
    boolean isMario()
    {
        return false;
    }
    boolean isCoinBrick()
    {
        return false;
    }

    boolean isEmpty()
    {
        return true;
    }

    public boolean isNotColliding(Sprite s)
    {
        if(this.x + this.w < s.x)
        {
            return true;
        }
        if(this.x > s.x + s.w)
        {
            return true;
        }
        if(this.y + this.h < s.y)
        {
            return true;
        }
        if(this.y > s.y + s.h)
        {
            return true;
        }
        return false;
    }
}