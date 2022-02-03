//Robert Goss
//October 21, 2021
//Assigment 5
import java.util.ArrayList;
import java.util.Iterator;
class Model
{
	ArrayList<Sprite> sprites;
	Mario mario;
	
	Model()
	{
		sprites = new ArrayList<Sprite>();
		mario = new Mario();
		sprites.add(mario);
		Json j = Json.load("map.json");
		this.unmarshall(j);
	}

	Json marshall()
	{ 
		for(int i = 0; i < sprites.size(); i++)
		{
			sprites.remove(i);
		}
		Json ob = Json.newObject();
		Json list = Json.newList();
		ob.add("bricks", list);
		for(int i = 0; i < sprites.size(); i++)
		{
			Sprite s = sprites.get(i);
			if(s.isBrick())
			{
				Brick b = (Brick)s;
				list.add(b.marshall());
			}
		}
		ob.add("bricks", list);
		return ob;
	}

	void unmarshall(Json ob)
	{
		
		sprites = new ArrayList<Sprite>();
		mario = new Mario();
		sprites.add(mario);
		Json tmpList = ob.get("bricks");
		for(int i = 0; i < tmpList.size(); i++)
		{
			sprites.add(new Brick(tmpList.get(i), this));
		}
	}


    public void update()
	{
		ArrayList<Sprite> coins = new ArrayList<>();
		Iterator<Sprite> it = sprites.iterator();
		while(it.hasNext())
		{
			Sprite s = it.next();
			boolean spriteExists = s.update();
			if(!spriteExists)
			{
				it.remove();
				break;
			}
			if(s.isBrick() && !mario.isNotColliding(s))
			{
				mario.collisionFix(s);
				if(mario.hitCoinBlock)
				{
					if(!s.isEmpty())
					{
						coins.add((Brick)s);
					}
				}
			}
		}
		for(int i = 0; i < coins.size(); i++)
		{
			int max = 10;
			int min = -10;
			int range = max - min + 1;
			int randVelX = (int)(Math.random() * range) + min;
			Coin c = new Coin( coins.get(i).x, coins.get(i).y - coins.get(i).h,randVelX, this);
			sprites.add(c);
		}
	}
}