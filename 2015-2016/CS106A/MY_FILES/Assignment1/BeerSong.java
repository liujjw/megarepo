import acm.program.*;

public class BeerSong extends ConsoleProgram{
	private static final int BEER = 99;
	
	public void run(){
		for(int i = BEER; i > 0; i--){
			if(i == 1){
				println(i + " bottle of beer on the wall.");
				println(i + " bottle of beer.");
			}else{
				println(i + " bottles of beer on the wall.");
				println(i + " bottles of beer.");
			}
			println("You take one down, pass it around");
			println((i - 1) + " bottles of beer on the wall.");
			println("");
		}
		println("No more bottles of beer on the wall, no more drinks for us all.");
	}

}
