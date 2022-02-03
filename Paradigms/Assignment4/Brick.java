//Robert Goss
//October 4, 2021
//Assigment 4
import java.awt.Image;
import java.awt.Graphics;

class Brick
{
   static Image image = null;
   int x, y, w, h;
   
   Brick(int x, int y, int w, int h)
   {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        loadImage();
   }
   Brick(Json ob)
   {
        x = (int)ob.getLong("BrickX");
        y = (int)ob.getLong("BrickY");
        w = (int)ob.getLong("BrickW");
        h = (int)ob.getLong("BrickH");
        loadImage();
   }
   void loadImage()
   {
     if(image == null)
     {
          image = View.loadImage("brick.png");
     }
   }
   Json marshall()
   {
        Json ob = Json.newObject();
        ob.add("BrickX",this.x);
        ob.add("BrickY", this.y);
        ob.add("BrickW", this.w);
        ob.add("BrickH", this.h);
        return ob;
   }

   
   void draw(Graphics g, int screenLocation)
   {
    g.drawImage(image, x - Mario.x + screenLocation, y, w, h, null);
   }
}