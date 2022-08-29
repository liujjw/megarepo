import acm.program.*;

public class runtimeGarbageCollection extends ConsoleProgram{
	
	public void run(){
		Runtime myRuntime = Runtime.getRuntime();
		long freeMem = myRuntime.freeMemory();
		println("Currently at " + freeMem + " bytes of free space.");
		
		for(int i = 0; i < 100000; i++){
			new Rational(i, i + 2);
		}
		println("Allocating objects in the heap...");
		freeMem = myRuntime.freeMemory();
		println("Now at " + freeMem + " bytes.");
		
		myRuntime.gc();
		println("Now cleaning.");
		long freeMemCleaned = myRuntime.freeMemory();
		println("Cleanded " + (freeMemCleaned - freeMem) + " bytes");

	}
	
}
