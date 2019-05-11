// FracCalc Final
//
// Everett Quan

package utilities.math;

import java.util.Scanner;
import java.util.ArrayList;

public class FracCalc {

    public static void main(String[] args) {
    	Scanner console = new Scanner(System.in);
    	    	
    	while (true) {
    		System.out.print("Type out a problem to solve: ");
    	   	String problem = console.nextLine();
    	   	
	    	if (problem.toLowerCase().charAt(0)=='q') break;
	    	else System.out.println("The answer is "+produceAnswer(problem));
    	}
    	
    	console.close();
    }
        
    public static String produceAnswer(String input) {
    	if (input.contains("/0")||input.contains("/ 0")) {
    		System.out.println("ERROR: Cannot divide by zero.");
    		return "undefined";
    	}
    	
    	ArrayList<Character> operators = new ArrayList<>();
    	ArrayList<int[]> operands = new ArrayList<>();
    	
    	//declare operands and operators
    	while (input.length()>0) {
			String clause; 
			
    		if (input.indexOf(' ')==-1) clause = input; //the last term is all that's left
			else clause = input.substring(0, input.indexOf(' '));
    		
			//if not 0-9 unicode value						could be negative number
			if ((clause.charAt(0)<'0'||clause.charAt(0)>'9')&&clause.length()==1)
				operators.add(clause.charAt(0));
    		else {
    			int[] term = parse(clause);
    			term = improperize(term);
    			operands.add(term);
    		}
			
			
    		if (input.indexOf(' ')==-1) input = ""; //kinda sucky but this is how i fenceposted correctly
    		else input = input.substring(input.indexOf(' ')+1);
    	}
    	
    	//i initially accounted for operator precedence, 
    	//but the tests don't call for that...

    	/*
    	//multiplication/division first
    	for (int i=0; i<operators.size(); i++) {
    		char operator = operators.get(i);
    		if (operator=='*'||operator=='/') {
        		//it's already stored in this iteration so we don't need it anymore
    			operators.remove(i);
        		
    			//replace term 1 with the solution to this section
        		operands.set(i, solve(operands.get(i), operands.get(i+1), operator));
        		operands.remove(i+1); //remove term 2
        		
        		//since the individual problem was solved/condensed, repeat at the same index
        		i--;
    		}
    	}
    	
    	for (int i=0; i<operators.size(); i++) {
    		char operator = operators.get(i);
			operators.remove(i);
    		operands.set(i, solve(operands.get(i), operands.get(i+1), operator));
    		operands.remove(i+1);
    		System.out.println(i);
    		i--;
    	}
    	*/
    	
    	while (operators.size()>0) {
    		operands.set(0, solve(operands.get(0), operands.get(1), operators.get(0)));
    		operators.remove(0);
    		operands.remove(1);
    	}
    	
    	//final simplification
    	String answer = "";
    	int num = operands.get(0)[0], den = operands.get(0)[1];
    	
    	boolean negative = false;	//negatives are painful
    	if (num<0) {
    		num*= -1;
    		negative = true;
    	}
    	
    	int whl = num/den;	//create whole number
    	num%= den;
    	if (whl>0) {
    		answer+= whl;
    		if (num>0) answer+='_';
    	} else if (num==0) return "0";
    	
    	//fraction doesn't exist anymore
    	if (num!=0) {
    		//simplify
    		for (int i=2; i<=num; i++) {
	    		if (num%i==0&&den%i==0) {
	    			num/= i;
	    			den/= i;
	    			i--;
	    		}
	    	}
    		answer+= num+"/"+den;
    	}
    	
    	if (negative) answer = "-"+answer;
    	
    	return answer;
    }
    
    //separates term into its components
    private static int[] parse(String term) {
    	int[] numbers = {0,0,1};

    	//declare numbers (whole, numerator, denominator)
    	if (term.indexOf('_')>0) {
    		numbers[0] = Integer.parseInt(term.substring(0, term.indexOf('_')));
        	numbers[1] = Integer.parseInt(term.substring(term.indexOf('_')+1, term.indexOf('/')));
        	numbers[2] = Integer.parseInt(term.substring(term.indexOf('/')+1));
    	} else {
    		if (term.indexOf('/')>0) {
    			numbers[1] = Integer.parseInt(term.substring(0, term.indexOf('/')));
            	numbers[2] = Integer.parseInt(term.substring(term.indexOf('/')+1));
    		} else {
    			numbers[0] = Integer.parseInt(term);
    		}
    	}
    	
    	//for (int i=0; i<numbers.length; i++) System.out.println(numbers[i]);
    	//System.out.println();
    	
    	return numbers;
    }

    //changes (potential) complex fractions into improper ones so that they are easier to manipulate
    private static int[] improperize(int[] term) {
    	if (term[0]<0) term[1]*= -1;
    	term[1]+= term[0]*term[2];
    	return new int[] {term[1],term[2]};
    }
    
    //solve
    private static int[] solve(int[] term1, int[] term2, char operator) {
    	switch (operator) {
		case '-': 
			term2[0]*=-1; //addition but with negative numbers
		case '+':
			term1[0]*= term2[1];
			term2[0]*= term1[1];
			term1[1]*= term2[1]; //commonize denominator
			term1[0]+= term2[0];
			break;
		case '/':
			int temp = term2[0];
			term2[0] = term2[1];
			term2[1] = temp; //multiplying by the reciprocal
			if (term2[1]<0) {
				term2[0]*= -1;
				term2[1]*= -1;
			}
		case '*':
			term1[0]*= term2[0];
			term1[1]*= term2[1];
			break;
		default:
			System.out.println("ERROR: Invalid operator");
		}
    	
    	return term1;
    }
}