import acm.graphics.*;

public class drawJ extends GCompound{
	public drawJ(){
		add(new GLine(0, 0, 15, 0));
		add(new GLine(7.5, 0, 7.5, 12.5));
		add(new GArc(-1.4, 7.5, 9.0, 9.0, 0, -180));
	}
}