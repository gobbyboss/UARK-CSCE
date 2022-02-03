//Robert Goss
//October 21, 2021
//Assigment 5
import java.awt.Image;
import java.awt.Graphics;
class Brick extends Sprite
{
   Image image = null;
   Model model;
   int numCoins;
   boolean isCoinBrick;
   public Brick(boolean isCoinBrick1, int x1, int y1, int w1, int h1, Model m)
   {
          numCoins = 0;
          isCoinBrick = isCoinBrick1;
          x = x1;
          y = y1;
          w = w1;
          h = h1;
          loadImage();
          model = m;
   }
   public Brick(int numCoins1, boolean isCoinBrick1, int x1, int y1, int w1, int h1, Model m)
   {
          isCoinBrick = isCoinBrick1;
          x = x1;
          y = y1;
          w = w1;
          h = h1;
          loadImage();
          model = m;
          numCoins = numCoins1;
   }
  
   Brick(Json ob, Model m)
   {
        numCoins = (int)ob.getLong("numCoins");
        isCoinBrick = (boolean)ob.getBool("isCoinBrick");
        x = (int)ob.getLong("BrickX");
        y = (int)ob.getLong("BrickY");
        w = (int)ob.getLong("BrickW");
        h = (int)ob.getLong("BrickH");
        loadImage();
        model = m;
   }
   void loadImage()
   {
     if(image == null && !isCoinBrick)
     {
          image = View.loadImage("brick.png");
     }
     if(image == null && isCoinBrick)
     {
          image = View.loadImage("coinBlock.png");
     }
     
   }
   Json marshall()
   {
        Json ob = Json.newObject();
        ob.add("numCoins", numCoins);
        ob.add("isCoinBrick", isCoinBrick);
        ob.add("BrickX", this.x);
        ob.add("BrickY", this.y);
        ob.add("BrickW", this.w);
        ob.add("BrickH", this.h);
        return ob;
   }
   public boolean update() 
   {
     return true;
   }
   
   public void draw(Graphics g)
   {
     g.drawImage(image, x - model.mario.x + model.mario.marioScreenPos, y, w, h, null);
   }

   @Override
   public boolean isEmpty()
   {
        if(numCoins == 0)
        {
          return true;
        }
        else
        {
          numCoins--;
          if(numCoins == 0)
          {
               image = View.loadImage("emptyBlock.png");
          }
          return false;
        }
   }


   @Override
   boolean isBrick()
   {
        return true;
   }

   @Override
   boolean isCoinBrick()
   {
        if(isCoinBrick == true)
        {
             return true;
        }
        else
        {
             return false;
        }
   }
}