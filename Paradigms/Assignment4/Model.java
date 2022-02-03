//Robert Goss
//October 4, 2021
//Assigment 4
import java.util.ArrayList;
import java.util.Iterator;
class Model
{
	int model_x, model_y;
	ArrayList<Brick> bricks;
	Mario mario;

	Model()
	{
		bricks = new ArrayList<Brick>();
		mario = new Mario();
	}

	Json marshall()
	{
		Json ob = Json.newObject();
		Json list = Json.newList();
		
		for(int i = 0; i < bricks.size(); i++)
		{
			list.add(bricks.get(i).marshall());
		}
		ob.add("bricks", list);
		return ob;
	}

	void unmarshall(Json ob)
	{
		for(int i = 0; i < bricks.size(); i++)
		{
			bricks.remove(i);
		}
		bricks = new ArrayList<Brick>();
		Json tmpList = ob.get("bricks");
		for(int i = 0; i < tmpList.size(); i++)
		{
			bricks.add(new Brick(tmpList.get(i)));
		}
	}
	public void collisionCheck()
    {
		Iterator<Brick> it = bricks.iterator();
	
        while(it.hasNext())
        {
			Brick b = it.next();
			if(!isNotColliding(b))
			{
				int marioLeft = Mario.x;
				int marioRight = Mario.x + mario.w;
				int marioTop = mario.y;
				int marioBottom = mario.y + mario.h;
				
				int pMarioLeft = mario.px;
				int pMarioRight = mario.px + mario.w;
				int pMarioTop = mario.py;
				int pMarioBottom = mario.py + mario.h;

				int brickLeft = b.x;
				int brickRight = b.x + b.w;
				int brickTop = b.y;
				int brickBottom = b.y + b.h;

				
				if((marioRight >= brickLeft) && (pMarioRight <= brickLeft))
				{
					Mario.x = brickLeft - mario.w;
				}
				else if((marioLeft <= brickRight) && (pMarioLeft >= brickRight))
				{
					Mario.x = brickRight;
				}
				else if((marioBottom >= brickTop) && (marioBottom <= brickBottom) && (pMarioBottom <= brickTop))
				{
					mario.y = b.y - mario.h;
					Mario.framesSinceGround = 0;
				}
				else if((marioTop <= brickBottom)  && (marioTop >= brickTop) && (pMarioTop >= brickBottom))
				{
					mario.y = b.y + b.h;
					mario.vert_vel += 1.2;
				}
				
			}
        }
      
	}
	boolean isNotColliding(Brick b)
	{
		if(Mario.x + mario.w < b.x)
		{
			return true;
		}
		if(Mario.x > b.x + b.w)
		{
			return true;
		}
		if(mario.y + mario.h < b.y)
		{	
			return true;
		}
		if(mario.y > b.y + b.h)
		{
			return true;
		}
		return false;
		
	}

    public void update()
	{
		mario.update();
		collisionCheck();
	}
}