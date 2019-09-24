package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
	public static String delims1 = "/t*+-/()] ";

    public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
            // Filter through the groups
        String tempexp = expr.replaceAll("\\[", "[ ");
        StringTokenizer st=new StringTokenizer(tempexp, delims1); 

        while(st.hasMoreTokens()) { 
        	String var=st.nextToken();  
    		if (!var.equals("") && !Character.isDigit(var.charAt(0))) {
    			if (var.endsWith("[")) {
                    var = var.substring(0, var.length() - 1);
                    if (!arrays.contains(new Array(var))) { 
                    	int idx = 0;
                        while (idx < arrays.size() && var.length() < arrays.get(idx).name.length()) {
                            idx++;
                        }
                        
                        arrays.add(idx,new Array(var));
                    }
    			} else { 
                    if (!vars.contains(new Variable(var))) {    
                    	int idx = 0;
                        while (idx < vars.size() && var.length() < vars.get(idx).name.length()) {
                            idx++;
                        }
                        vars.add(idx, new Variable(var));
                    }
    			}    	
    		}
        
        }
        
    }
  
    public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) throws IOException {
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
   
    private static String cMatcher(String str, int start, char increase, char decrease) {
        int c = 1;
        // The index counter
        int count = start + 1;
        while (c > 0 && count < str.length()) {
            if (str.charAt(count) == increase) { // Increase C
                c++;
            } else if (str.charAt(count) == decrease) { // Decrease C
                c--;
            }
            count++;
        }
        return str.substring(start, count);
    }
   
    private static float simpleEvaluate(String op, float A, float B) {
        float number = 0;
        switch (op) {
            case "addition":
                number = A + B;
                break;
            case "subtract":
                number = A - B;
                break;
            case "multiply":
                number = A * B;
                break;
            case "divide":
                number = A / B;
                break;
            default:
                break;
        }
        return number;
    }
   
    private static String eval(String expr, String op1, String op2) {
        // Loop through both operations
        while (expr.contains(op1) || expr.contains(op2)) {
            // Find the correct operation, in linear order (if op1 occurs before op2, we will evaluate op1 first)
            String op = op1;
            if (expr.contains(op1) && expr.contains(op2)) {
                if (expr.indexOf(op1) > expr.indexOf(op2)) {
                    op = op2;
                }
            } else {
                if (expr.contains(op2)) {
                    op = op2;
                }
            }
            // Parse the expression to find A and B values
            String beg = expr.substring(0, expr.indexOf(op));
            String end = expr.substring(expr.indexOf(op) + op.length());
            String[] bArr = beg.split("addition|subtract|multiply|divide");
            String[] eArr = end.split("addition|subtract|multiply|divide");
            float A = Float.parseFloat(bArr[bArr.length - 1]);
            float B = Float.parseFloat(eArr[0]);
            // Evaluate A op B
            expr = expr.replace(bArr[bArr.length - 1] + op + eArr[0], "" + simpleEvaluate(op, A, B));
        }
        return expr;
    }
    
    public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
        // Remove the whitespace
        String tempexp = "";
        for (int i = 0; i < expr.length(); i++) {
        	char c=expr.charAt(i);
        	if (c != ' ' && c != '\t') {
                tempexp += c;
            }
        }
        // Convert the single variables to their actual numbered value
        for (int i = 0; i < vars.size(); i++) {
            while (tempexp.contains(vars.get(i).name)) {
                tempexp = tempexp.replace(vars.get(i).name, "" + vars.get(i).value);
            }
        }
        // Convert the array variables to their respective numbered value. Here, we need to recursively call evaluate, as we can have an expression as the array index
        for (int i = 0; i < arrays.size(); i++) {
            while (tempexp.contains(arrays.get(i).name)) {
                String subFull = cMatcher(tempexp, tempexp.indexOf(arrays.get(i).name) + arrays.get(i).name.length(), '[', ']');
                String sub = "" + subFull;
                if (sub.startsWith("[")) {
                    sub = sub.substring(1);
                }
                if (sub.endsWith("]")) {
                    sub = sub.substring(0, sub.length() - 1);
                }
                tempexp = tempexp.replace(arrays.get(i).name + subFull, "" + arrays.get(i).values[(int)evaluate(sub, vars, arrays)]);
            }
        }
        
        while (tempexp.contains("(")) {
            String subFull = cMatcher(tempexp, tempexp.indexOf("("), '(', ')');
            String sub = "" + subFull;
            if (sub.startsWith("(")) {
                sub = sub.substring(1);
            }
            if (sub.endsWith(")")) {
                sub = sub.substring(0, sub.length() - 1);
            }
            tempexp = tempexp.replace(subFull, "" + evaluate(sub, vars, arrays));
        }
        String[][] converter = {
           
            {"+", "addition"}, // convert the + symbol
            {"-", "subtract"}, // convert the - symbol
            {"*", "multiply"}, // convert the * symbol
            {"/", "divide"}, // convert the / symbol
        };
        
        
        for (int i = 0; i < converter.length; i++) {
            while (tempexp.contains(converter[i][0])) {
                tempexp = tempexp.replace(converter[i][0], converter[i][1]);
            }
        }
        tempexp = eval(tempexp, "multiply", "divide");        
        tempexp = eval(tempexp, "addition", "subtract");       
    	return Float.parseFloat(tempexp);
    	
    		
    }
}