import acm.program.*;

public class ThisOldMan extends ConsoleProgram{
	public void run(){
		for(int i = 1; i <= 10; i++){
			println("This old man, he played " + i + ".");
			String rhyme;
			switch(i){
			case 1:
				rhyme = "thumb";
				break;
			case 2:
				rhyme = "shoe";
				break;
			case 3:
				rhyme = "knee";
				break;
			default:
				rhyme = "Seeyasuckers";
				break;
			}
			println("He played knick-knack on my " + rhyme + ".");
		}
	}

}
