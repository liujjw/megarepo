import acm.program.*;

public class CelsiusFahrenheit extends ConsoleProgram {
	public void run(){
		double fahrenheit = readDouble("Fahrenheit: ");
		double celsius = (fahrenheit - 32.0) * ((double)5/(double)9);
		
		println("Celsius: " + celsius);
	}

}
