//Robert Goss
//September 21, 2021
//Assigment 3
import java.util.ArrayList;
class Model
{
	int model_x;
	int model_y;
	int dest_x;
	int dest_y;
	int cameraPos;
	ArrayList<Brick> bricks;

	Model()
	{
		bricks = new ArrayList<Brick>();
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
    public void update()
	{
		// Move the turtle
		if(this.model_x < this.dest_x)
            this.model_x += Math.min(4, dest_x - model_x);
		else if(this.model_x > this.dest_x)
			this.model_x -= Math.min(4, model_x - dest_x);
		if(this.model_y < this.dest_y)
			this.model_y += Math.min(4, dest_y - model_y);
		else if(this.model_y > this.dest_y)
			this.model_y -= Math.min(4, model_y - dest_y);
	}

	public void setDestination(int x, int y)
	{
		this.dest_x = x;
		this.dest_y = y;
	}
}