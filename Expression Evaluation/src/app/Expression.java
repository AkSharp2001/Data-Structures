package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/

    	expr = expr.replaceAll("\\+", " ");
    	expr = expr.replaceAll("-", " ");
    	expr = expr.replaceAll("\\*", " ");
    	expr = expr.replaceAll("/", " ");
    	expr = expr.replaceAll("\\(", " ");
    	expr = expr.replaceAll("\\)", " ");
    	expr = expr.replaceAll("\\]", " ");
    	expr = expr.replaceAll("\\[", " [ ");  
    	expr = expr.replaceAll("\\d", "");  
    	
    	expr = expr.trim();
    	    	
    	String[] expression = expr.split("\\s+");
    	
    	//System.out.println(Arrays.toString(expression));
    	
    	for (int i=0; i<=expression.length-1; i++) {
        	if ((i != expression.length-1) && !arrays.contains(new Array(expression[i])) && (expression[i+1]).equals("[")) {
        		arrays.add(new Array(expression[i]));
        	}
        	else if (!vars.contains(new Variable(expression[i])) && !arrays.contains(new Array(expression[i])) && !(expression[i]).equals("[") && !expression[i].equals("")) {
        		vars.add(new Variable(expression[i]));
        	}
    	}
//    	System.out.println(arrays);
//    	System.out.println(vars);
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {

    	Stack<String> operands = new Stack<String>();
		Stack<Character> operators = new Stack<Character>();
		expr = expr.replaceAll("\\s+", "");
		expr = "(" + expr + ")";
		
		int ptr = 0;

		while (ptr < expr.length()) {
			char c = expr.charAt(ptr);
			if (delims.contains(Character.toString(c))) {
				if (c == '(' || c == '[') {  
					operators.push(c);
					ptr++;
				} else if (c == ')' || c == ']') {  
					while (!operators.isEmpty()) {
						char op = operators.peek();
						if (op == '(') {  
							operators.pop();
							break;
						}
						operands.push(math(operands, operators, arrays));
						if (op == '[') {  
							break;
						}
					}
					ptr++;
				} else if (!operators.isEmpty() && !(operators.peek() == '(' || operators.peek() == '[' || precedence(c, operators.peek()))) {  
					operands.push(math(operands, operators, arrays));
				} else {
					operators.push(c);
					ptr++;
				}
				continue;
			}

			String varName = "";
			char currentChar = ' ';
			for (int i = 0; i < expr.length() - ptr; i++) {
				currentChar = expr.charAt(ptr + i);
				if (delims.contains(Character.toString(currentChar))) {
					break;
				}
				varName += currentChar + "";
			}

			if (currentChar == '[' || Character.isDigit(varName.charAt(0))) {
				operands.push(varName);
			} else {
				operands.push(Float.toString(getVariableValue(varName, vars)));
			}

			ptr += varName.length();
		}

		while (operands.size() > 1) {
			operands.push(math(operands, operators, arrays));
		}
		return Float.parseFloat(operands.pop());
	}
    	
    	
    private static boolean precedence(char c, char operator) {
		return (operator == '-' || operator == '+') && (c == '*' || c == '/');
	}

	private static String math(Stack<String> operands,  Stack<Character> operators,  ArrayList<Array> arrays) {
		char op = operators.pop();
		String second = operands.pop();
		String first = operands.pop();
		switch (op) {
		case '+':
			return Float.toString(Float.parseFloat(first) + Float.parseFloat(second));
		case '-':
			return Float.toString(Float.parseFloat(first) - Float.parseFloat(second));
		case '*':
			return Float.toString(Float.parseFloat(first) * Float.parseFloat(second));
		case '/':
			return Float.toString(Float.parseFloat(first) / Float.parseFloat(second));
		case '[':
			int index = (int)Float.parseFloat(second);
			return Float.toString(arrays.get(arrays.indexOf(new Array(first))).values[index]);
		}
		return "0";
	}

	private static float getVariableValue(String name, ArrayList<Variable> vars) {
		return vars.get(vars.indexOf(new Variable(name))).value;
	}  
	
}