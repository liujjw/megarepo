

import acm.program.*;
public class newAverageList extends ConsoleProgram {

   /* Runs the program */
   public void run() {
      println("This program averages a list of integers.");
      println("Enter values, one per line, using a blank line to signal end of list");
      
      int total = 0;
      int numberOfScores = 0;
      
      while (true) {
         String input = readLine(" ? ");
         if(input.equals("")) break;
         
         int value = Integer.parseInt(input);
         
         total += value;
         numberOfScores++;
      }
      
      int average = total / numberOfScores;
      println("The average is " + average + ".");
   }
}
