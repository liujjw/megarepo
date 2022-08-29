import acm.program.*;

public class hexToDecimal extends ConsoleProgram{
	
	private static final int SENTINEL = 0;
	public void run(){
		println("This program converts a user entered hexadecimal integer into its decimal form");
		println("Enter " + SENTINEL + " to quit.");
		
		while(true){
			String hexString = readLine("Hex number: ");
			if(hexString.equals("0")) break;

			int decimalFromHex = Integer.parseInt(hexString, 16);
			
			println(hexString + " in hex is = " + decimalFromHex + " in decimal.");
		}
	}
}
