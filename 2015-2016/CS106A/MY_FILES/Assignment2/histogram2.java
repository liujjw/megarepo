import acm.program.*;
import java.io.*;

public class histogram2 extends ConsoleProgram{
	
	public void run(){
		initHistogram();
		insertIntoHistogram();
		displayHistogram();
	}
	
	private void initHistogram(){
		histogramArray = new int[10];
		for(int i = 0; i < histogramArray.length; i++){
			histogramArray[i] = 0;
		}
	}
	
	private void insertIntoHistogram(){
		BufferedReader rd = null;
		while(rd == null){
			String file = readLine("Give me a file name: ");
			try{
				rd = new BufferedReader(new FileReader(file));
			}catch(Exception e){
				println("Invalid file");
			}
		}
		
		while(true){
			try{
				String score = rd.readLine();
				if(score == null) break;
				int intScore = Integer.parseInt(score);
				if(intScore < 100 && intScore > 0){
					intScore = intScore / 10;
					histogramArray[intScore]++;
				}
				println("Parse issue");
			}catch(IOException e){
				println("IOException");
			}
		}
	}
	
	private void displayHistogram(){
		for(int i = 0; i < histogramArray.length; i++){
			String label;
			switch(i){
			case 0:
				label = "00-09: ";
				break;
			case 10:
				label = "100: ";
				break;
			default:
				label = (i * 10) + "-" + (10 * i + 9);
				break;
			}
			String stars = createStars(histogramArray[i]);
			println(label + ": " + stars);
		}
	}
	
	private String createStars(int n) {
		String stars = "";
		for (int i = 0; i < n; i++) {
		         stars += "*";
		      }
		      return stars;
		   }
	
	
	
	int[] histogramArray;
}