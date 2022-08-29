import acm.program.*;

public class perfectNumbers extends ConsoleProgram{
	public void run(){
		for(int i = 1; i < 9999; i++){
			if(isPerfect(i)){
				println(i);
			}
		}
	}
	
	private boolean isPerfect(int n){
		int sum = 0;
		for(int i = 1; i < n; i++){
			if(n % i == 0 ){
				sum += i;
			}
		}
		if(sum == n){
			return true;
		}else{
			return false;
		}
	}
}
