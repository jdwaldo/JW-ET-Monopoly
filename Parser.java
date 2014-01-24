import java.util.ArrayList;
public class Parser {
    private ArrayList<String> equation;
    private String[] numbers = {".","0","1","2","3","4","5","6","7","8","9"}; 
    private String[] operators = {"+", "-", "*", "/", "^", "sin", "cos", "tan","cot", "csc", "sec", "log", "ln"};
    ArrayList<Double> roots = new ArrayList<Double>();
    ArrayList<Double> asymptotes = new ArrayList<Double>();
    ArrayList<Double> important = new ArrayList<Double>();
    public Parser() {
        equation = new ArrayList<String>();
    }
 //    Constructor for Parser. Takes an input String and turns it into an ArrayList which seperates numbers and operators while keeping it in correct order.
    public Parser(String input) {
        String numberString = "";
	equation= new ArrayList<String>();
        for (int i = 0; i < input.length(); i++) {
            // Searches for operators of length 3. {sin,cos,tan,cot,csc,sec,log}
	    if ( i < input.length() -2 &&linSearch(operators, input.substring(i,i+3)) != -1) {
		if (numberString.length() > 0) {
		    equation.add(numberString);
		    numberString = "";
		}
		equation.add(operators[linSearch(operators,input.substring(i,i+3))]);
		i+= 2;
	    }
	    //Searchs for ln
	    else if (i < input.length() - 1 &&input.substring(i,i+2).equals("ln")) {
		if (numberString.length() > 0) {
		    equation.add(numberString);
		    numberString = "";
		}
		equation.add("ln");
		i++;
	    }
	    // Searches for pi
	    else if (i < input.length() - 1 &&input.substring(i,i+2).equals("pi")) {
                if (numberString.length() > 0) {
                    equation.add(numberString);
                    numberString = "";
                }
                equation.add("pi");
                i++;
            }
            // Searches for e
	    else if (input.substring(i,i+1).equals("e")) {
		if (numberString.length() > 0) {
                    equation.add(numberString);
                    numberString = "";
                }
                equation.add("e");
	    }
	    // Searches for numbers if it is a number add to numberString
            else if (linSearch(numbers, input.substring(i,i+1)) != -1) {
                numberString = numberString + input.substring(i,i+1);
            }
            // If it isnt a number and numberString isnt empty than add numberString then add the operator
            else if (numberString.length() != 0) {
                equation.add(numberString);
                equation.add(input.substring(i,i+1));
                numberString = "";
            }
            // If not an operator and numberString is empty just add operator
	    else {
		equation.add(input.substring(i,i+1));
	    }
	    // If number occurs at end of input String
	    if (input.length() - 1 == i && linSearch(numbers,input.substring(i,i+1)) !=  -1) {
		equation.add(numberString);
	    }
        }
    }
    // Prints out elements of equation
    public String toString() {
	String retString = "";
	for (int i = 0; i < equation.size(); i++) {
	    retString = retString + "[" + equation.get(i) + "]";
	}
	return retString;
    }
    
    // Finds last instance of open parens and first instance of closer parens
    public ArrayList<String> innerParens() {
        int openParens = 0;
        int closedParens = 0;
        for (int i = 0; i < equation.size(); i++) {
            if (equation.get(i).equals( "(")) {
                openParens = i;
            }
            if (equation.get(i).equals( ")")) {
                closedParens = i;
                break;
            }
        }
        if (closedParens == 0) {
            return equation;
        }
        else {
	    ArrayList<String> retArray = new ArrayList<String>();
	    for (int r = openParens + 1; r < closedParens; r++) {
		retArray.add(equation.get(r));
	    }
	    return retArray;
	}
    }
    // LinSearch for array
    public static int linSearch ( Comparable[] a, Comparable target ) {

        int tPos = -1;
        int i = 0;

        while ( i < a.length ) {
            if ( a[i].equals(target) ) {
                tPos = i;
                break;
            }
            i++;
        }
        return tPos;
    }

    // LinSearch for ArrayList
    public static int linSearch ( ArrayList<String> a, String target ) {

        int tPos = -1;
        int i = 0;

        while ( i < a.size() ) {
            if ( a.get(i).equals(target) ) {
                tPos = i;
                break;
            }
            i++;
        }
        return tPos;
    }
    
    public static int linSearch ( ArrayList<Double> a, Double target ) {

        int tPos = -1;
        int i = 0;

        while ( i < a.size() ) {
            if ( a.get(i).equals(target) ) {
                tPos = i;
                break;
            }
            i++;
        }
        return tPos;
    }
    // simplifystep takes a sub ArrayList from equation without any parenthesis returns a string to be added back into equation
    public String simplifyStep(ArrayList<String> step) {
	int size = step.size();
	while (size > 1) {
	    for (int i = 1; i < step.size(); i++) {
	    	// Checks to see if two numbers are side by side if so multiply. ie coefficients
		try {
		    Double a = Double.parseDouble(step.get(i-1));
		    Double b = Double.parseDouble(step.get(i));
		    step.add(i,"*");
		    i--;
		    size++;
		}
		catch (NumberFormatException e) {
		    continue;
		}
	    }
	    // searches for each operator with respect to order of operations
	    if (linSearch(step, "log") != -1) {
                int i = linSearch(step, "log");
                double answer = Math.log10(Double.parseDouble(step.get(i+1)));
                step.set(i, Double.toString(answer));
                step.remove(i+1);
                size-= 1;
            }
	    else if (linSearch(step, "ln") != -1) {
                int i = linSearch(step, "ln");
                double answer = Math.log(Double.parseDouble(step.get(i+1)));
                step.set(i, Double.toString(answer));
                step.remove(i+1);
                size-= 1;
            }
	    else if (linSearch(step, "sin") != -1) {
                int i = linSearch(step, "sin");
                double answer = Math.sin(Double.parseDouble(step.get(i+1)));
                step.set(i, Double.toString(answer));
                step.remove(i+1);
                size-= 1;
	    }
	    else if (linSearch(step, "sec") != -1) {
                int i = linSearch(step, "sec");
                double answer = 1 /Math.cos(Double.parseDouble(step.get(i+1)));
                step.set(i, Double.toString(answer));
                step.remove(i+1);
                size-= 1;
            }
	    else if (linSearch(step, "tan") != -1) {
                int i = linSearch(step, "tan");
                double answer = Math.tan(Double.parseDouble(step.get(i+1)));
                step.set(i, Double.toString(answer));
                step.remove(i+1);
                size-= 1;
            }
	    else if (linSearch(step, "cot") != -1) {
                int i = linSearch(step, "cot");
                double answer = 1 /Math.tan(Double.parseDouble(step.get(i+1)));
                step.set(i, Double.toString(answer));
                step.remove(i+1);
                size-= 1;
            }
	    else if (linSearch(step, "cos") != -1) {
                int i = linSearch(step, "cos");
                double answer = Math.cos(Double.parseDouble(step.get(i+1)));
                step.set(i, Double.toString(answer));
                step.remove(i+1);
                size-= 1;
            }
	    else if (linSearch(step, "csc") != -1) {
                int i = linSearch(step, "csc");
                double answer = 1/ Math.sin(Double.parseDouble(step.get(i+1)));
                step.set(i, Double.toString(answer));
                step.remove(i+1);
                size-= 1;
            }
	    else if (linSearch(step, "^") != -1) {
		int i = linSearch(step, "^");
		double answer = Math.pow(Double.parseDouble(step.get(i-1)), Double.parseDouble(step.get(i+1)));
		step.set(i, Double.toString(answer));
		step.remove(i-1);
		step.remove(i);
		size-= 2;
	    }
	    else if (linSearch(step, "*") != -1) {
		int i = linSearch(step, "*");
		double answer = Double.parseDouble(step.get(i-1)) * Double.parseDouble(step.get(i+1));
		step.set(i, Double.toString(answer));
		step.remove(i-1);
		step.remove(i);
		size-= 2;
	    }
	    else if (linSearch(step, "/") != -1) {
		int i = linSearch(step, "/");
		double answer = Double.parseDouble(step.get(i-1)) / Double.parseDouble(step.get(i+1));
		step.set(i, Double.toString(answer));
		step.remove(i-1);
		step.remove(i);
		size-= 2;
	    }
	    else if (linSearch(step,"+") != -1) {
		int i = linSearch(step, "+");
		double answer = Double.parseDouble(step.get(i-1)) + Double.parseDouble(step.get(i+1));
		step.set(i, Double.toString(answer));
		step.remove(i-1);
		step.remove(i);
		size-= 2;
	    }
	    else if (linSearch(step, "-") != -1) {
		int i = linSearch(step, "-");
                double answer = Double.parseDouble(step.get(i-1)) - Double.parseDouble(step.get(i+1));
                step.set(i, Double.toString(answer));
                step.remove(i-1);
                step.remove(i);
                size-= 2;
	    }
	}
	return step.get(0);
    }
    
    public Double simplify(double xcor) {
	// creates a duplicate so that equation is never changed
	ArrayList<String> retList= new ArrayList<String>();
	for (int i = 0; i < equation.size(); i++) {
	    retList.add(i,equation.get(i));
	}
	// substitutes variables and constancts for appropriate values
	for (int i = 0; i < equation.size(); i++) {
	    if (equation.get(i).equals("x")) {
		equation.set(i, Double.toString(xcor));
	    }
	    else if (equation.get(i).equals("e")) {
		equation.set(i, Double.toString(Math.E));
	    }
	    else if (equation.get(i).equals("pi")) {
		equation.set(i,Double.toString(Math.PI));
	    }
	}
	try {
	    while (equation.size() > 1) {
		int openParens = 0;
		int closedParens = 0;
		for (int i = 0; i < equation.size(); i++) {
		    if (equation.get(i).equals( "(")) {
			openParens = i;
		    }
		    if (equation.get(i).equals( ")")) {
			closedParens = i;
			break;
		    }
		}
		equation.add(openParens, simplifyStep(innerParens()));
		for (int i = openParens + 1 ; i <= closedParens + 1; i=i) {
		    equation.remove(i); 
		    closedParens--;
		}
	    }
	    Double retDouble = Double.parseDouble(equation.get(0));
	    equation.remove(0);
	    for (int i = 0; i < retList.size(); i++) {
		equation.add(i,retList.get(i));
	    }
	    // if a real solution
	    if (retDouble != Double.POSITIVE_INFINITY && retDouble != Double.NEGATIVE_INFINITY) {
	       return retDouble;
	    }
	    return retDouble;
	}
	// imaginary solutions return null
	catch (ArithmeticException ae) {
	    return null;
	}
    }
    // Given domain [a,b] find all roots and asympototes within range
    public void findImportant(double a, double b, double interval) {
	double starter = a;
	while (a < b) {
	    //if no real solution continue
	    if (simplify(a).equals(Double.NaN)) {
		a+= interval;
	    }
	    // else take sign and record
	    else {
		if (starter != a) {important.add(a);}
		double sign = Math.signum(simplify(a));
		double newSign= sign;
		while (a < b) {
		    if (newSign==sign) {
			a+= interval;
			if (simplify(a) == 0) {important.add(a);}
			newSign= Math.signum(simplify(a));
		    }
		    // Change of sign means cross x-axis or asymptote
		    else {
			sign = newSign;
			if (simplify(a) != Double.NaN) {important.add(a); }
		    }
		}
		// Refine solutions
		int importantSize = important.size();
		for (int i = 0; i < importantSize; i++) {
		    double accurate = -interval / 10.;
		    int counter = 0;
		    double startValue = 0;
		    while (counter < 8) {
			sign = Math.signum(simplify(important.get(i)));
			newSign = sign;
			startValue = important.get(i);
			while (sign == newSign) {
			    try {
				startValue += accurate;
				newSign= Math.signum(simplify(startValue));
			    }
			    catch (ArithmeticException ae) {
				break;
			    }
			}
			accurate = -1 * accurate / 10;
			sign = newSign;
			important.remove(i);
			important.add(i,startValue);
			counter++;
		    }
		}  
		// decide whether solution is a root or an asymptote and add to appropriate ArrayList
		for (int r = 0; r < important.size(); r++) {
		    if (simplify(important.get(r)) == 0) {roots.add(important.get(r));}
		    else if (Math.abs(simplify(important.get(r))) > 50) {
			asymptotes.add((double) Math.round(10000*important.get(r))/10000.);
		    }
		    else if (simplify(important.get(r)+.1).equals(Double.NaN) || simplify(important.get(r)-.1).equals(Double.NaN)) {
			asymptotes.add((double) Math.round(10000*important.get(r))/10000.);
		    }
		    else {roots.add((double) Math.round(10000*important.get(r))/10000.);}
		}
	    }
	}
    }
    // returns roots in given interval [a,b]
    public ArrayList<Double> getRoots(double a, double b) {
	int importantSize = important.size();
	int rootSize = roots.size();
	for (int i = 0; i < importantSize; i=i) {
	    important.remove(i);
	    importantSize--;
	}
	for (int i = 0; i < rootSize; i=i) {
	    roots.remove(i);
	    rootSize--;
	}
	findImportant(a,b,.1);
	return roots;
    }
    // returns asymptotes in given interval [a,b]
    public ArrayList<Double> getAsymptotes(double a,double b) {
	int importantSize = important.size();
	int asymptoteSize = asymptotes.size();
	for (int i = 0; i < important.size(); i=i) {
            important.remove(i);
	    importantSize--;
        }
	for (int i = 0; i < asymptotes.size(); i=i) {
            asymptotes.remove(i);
	    asymptoteSize--;
	}
	findImportant(a,b,.1);
	return asymptotes;
    }

    public static void main( String[] args) {
        String tester = "csc(x)log(x)/x";
	Parser test = new Parser(tester);
	System.out.println(test);
	System.out.println(test.simplify(1));
	System.out.println("Roots at" + test.getRoots(-5.,5.));
	System.out.println("Asymptotes at" + test.getAsymptotes(-5.,5.));
	System.out.println(test);
    }
}
