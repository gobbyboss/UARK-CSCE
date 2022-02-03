//Robert Goss
//September 21, 2021
//Assigment 3
class Brick
{
   int x;
   int y;
   int w;
   int h;
   Brick(int x, int y, int w, int h)
   {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
   }
   Brick(Json ob)
   {
        x = (int)ob.getLong("BrickX");
        y = (int)ob.getLong("BrickY");
        w = (int)ob.getLong("BrickW");
        h = (int)ob.getLong("BrickH");
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
}