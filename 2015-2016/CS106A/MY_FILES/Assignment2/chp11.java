import java.lang;

public int findIntInArray(int[] array, int key){
	for(int i = 0; i < array.length; i++){
		if(array[i] == key) return i;
	}
	return -1;
}

public class test() extends ConsoleProgram{
	public void run(){
		int city1 = findStringIndex("City #1: ");
		int city2 = findStringIndex("City #2: ");

		println(mileAgeTable[city1][city2] + " miles ");
	}

	private int findStringIndex(String prompt){
		String city = readLine(prompt);
		int index = -1;
		while(index == -1){
			index = binarySearch(city);
			if(index == -1){
				city = readLine("Try again: ");
			}
		}
		return index;

	}

	private int binarySearch(String city){
		int lower = 0;
		int upper = cityNames.length - 1;

		while(lower <= upper){

			int middle = (lower + upper) / 2;

			if(cityName[middle].equals(city)) return middle;
			else if(cityNames[middle].compareTo(city) < 0) lower = middle + 1;
			else if(cityNames[middle].compareTo(city) > 0) upper = middle - 1;
		}

		return -1;
	}
}

      private int[][] mileageTable = {
         {   0,1108, 708,1430, 732, 791,2191, 663, 854, 748,2483,2625},
         {1108,   0, 994,1998, 799,1830,3017,1520, 222, 315,3128,3016},
         { 708, 994,   0,1021, 279,1091,2048,1397, 809, 785,2173,2052},
         {1430,1998,1021,   0,1283,1034,1031,2107,1794,1739,1255,1341},
         { 732, 799, 279,1283,   0,1276,2288,1385, 649, 609,2399,2327},
         { 791,1830,1091,1034,1276,   0,1541,1190,1610,1511,1911,2369},
         {2191,3017,2048,1031,2288,1541,   0,2716,2794,2703, 387,1134},
         { 663,1520,1397,2107,1385,1190,2716,   0,1334,1230,3093,3303},
         { 854, 222, 809,1794, 649,1610,2794,1334,   0, 101,2930,2841},
         { 748, 315, 785,1739, 609,1511,2703,1230, 101,   0,2902,2816},
         {2483,3128,2173,1255,2399,1911, 387,3093,2930,2902,   0, 810},
         {2625,3016,2052,1341,2327,2369,1134,3303,2841,2816, 810,   0},
};

         private String[] cityNames = {
            "Atlanta",
            "Boston",
            "Chicago",
            "Denver",
            "Detroit",
            "Houston",
            "Los Angeles",
            "Miami",
            "New York",
            "Philadelphia",
            "San Francisco",
            "Seattle",
};

System.currentTimeMillisecs();