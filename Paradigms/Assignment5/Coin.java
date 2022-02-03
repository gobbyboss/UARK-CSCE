import java.awt.Image;
import java.awt.Graphics;
class Coin extends Sprite
{
    static Image coin = null;
    Model model;
    double vert_vel;
    double x_vel;
    public Coin(int x1, int y1, int x_vel1, Model m)
    {
        vert_vel = -10;
        x = x1;
        x_vel = x_vel1;
        y = y1;
        w = 50;
        h = 50;
        if(coin == null)
        {
            coin = View.loadImage("coin.png");
        }
        model = m;
    }
    public boolean update() 
    {
        vert_vel += 1.2;
        y += vert_vel;
        x += x_vel;
        if(y > 1080)
        {
            return false;
        }
        return true;
    }
   
    public void draw(Graphics g)
    {    
        g.drawImage(coin, x - model.mario.x + model.mario.marioScreenPos, y, w, h, null);
    }

}