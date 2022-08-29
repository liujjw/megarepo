
import acm.graphics.*;

public class peaceCompound extends GCompound{
	public peaceCompound(double x, double y, double radius){
		GLine disarmament = new GLine(x, (y - radius), x, y + radius);
		add(disarmament);
		
		GOval circle = new GOval((x - radius), (y - radius), radius * 2, radius * 2);
		add(circle);
		
		/*For the calculations in these for the N in semaphore, would we have used the basic trig funcs/ratios sin cos and tan to find 
		 * the length of one of the lines for the N? Say we know the angle formed ~ 30 degrees, and one leg of triangle is the radius of the circle,
		 * then we can use ...... shit no this stuff only works with right triangles..... but WAIT, we can make a right triangle by extending the radius 
		 * leg to meet with the other leg of the N */
		
		/*NVM we can make a 45-45-90 triangle instead from both sides of semaphore N, given that the hypotenuse will only be a fraction of the radius tho*/
		/*also we only have lengths and lines are not defined like that, unless we use polar gline, say we calculate the length of one semaphore N leg and 
		 * decide to go 30 degrees in that direction */
		GLine nuclear1 = new GLine(x, y + (0.1 * radius), (x - (0.67 * radius)), (y + (0.75 * radius)));
		add(nuclear1);
		
		GLine nuclear2 = new GLine(x, y + (0.1 * radius), (x + (0.67 * radius)), (y + (0.75 * radius)));
		add(nuclear2);
	}
}

/*
 * 		GLine nuclear1 = new GLine(x, y + (0.1 * radius), (x - (0.67 * radius)), (y + (0.75 * radius)));
		add(nuclear1);
		
		GLine nuclear2 = new GLine(x, y + (0.1 * radius), (x + (0.67 * radius)), (y + (0.75 * radius)));
		add(nuclear2);
 * */
