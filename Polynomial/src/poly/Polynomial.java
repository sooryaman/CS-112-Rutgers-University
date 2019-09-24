package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	
	public static Node add(Node poly1, Node poly2) {
		Node p1=poly1;
		Node p2=poly2;
		float coeff = 0;
	    int degree = 0;
	    Node answer = null;
	    
	    while(p1 != null && p2 != null)
	    	{
	    	  if (p1.term.degree == p2.term.degree) {
	            degree = p1.term.degree;
	            coeff = p1.term.coeff + p2.term.coeff;
	            if(coeff!=0) {
	                answer=new Node(coeff, degree, answer);
	            }
	            p1 = p1.next;
	            p2 = p2.next;
	        } else if (p1.term.degree <p2.term.degree ) {        
	            degree = p1.term.degree;
	            coeff = p1.term.coeff;
	            answer=new Node(coeff,degree,answer);
	            p1 = p1.next;
	        } else if(p1.term.degree>p2.term.degree) {
	            degree = p2.term.degree;
	            coeff = p2.term.degree;
	            answer=new Node(coeff,degree,answer);
	            p2 = p2.next;
	        }
	}  
	    	while (p1!=null && p2==null) {
	    		degree = p1.term.degree;
	            coeff = p1.term.coeff;
	            answer=new Node(coeff,degree,answer);
	            p1 = p1.next;
	    	}
	    	while(p2!=null && p1==null) {
	    	    degree = p2.term.degree;
	            coeff = p2.term.coeff;
	            answer=new Node(coeff,degree,answer);
	            p2 = p2.next;
	    	}    
	        Node temp=null;
	        Node ptr=answer;
	        while (ptr!=null) 
	        {
	        	temp=new Node(ptr.term.coeff, ptr.term.degree,temp);
	        	ptr=ptr.next;
	        }
	        
	   	 return temp; 
	}
	
	public static Node multiply(Node poly1, Node poly2) { 
		
	  	if (poly1 == null || poly2 == null)
		        return null;

		    Node p1 = poly1;
		    Node p2 = poly2;

		    Node answer = null;
		    float coeff;
		    int degree;

		    int max = 0;
		    while (p1 != null) {
		        while (p2 != null) {
		            coeff = p1.term.coeff * p2.term.coeff;
		            degree = p1.term.degree + p2.term.degree;
		            answer = new Node(coeff, degree, answer);
		            if (degree > max)
		                max = degree;
		            p2 = p2.next;
		        }
		        p1 = p1.next;
		        p2 = poly2;
		    }

		    Node result = null;
		    for (int p = 0; p<= max; p++) {
		        Node temp = answer;
		        float terms = 0;
		        while (temp != null) {
		            if (temp.term.degree == p)
		                terms+=temp.term.coeff;
		            temp = temp.next;
		        }
		        if (terms != 0)
		            result = new Node(terms, p, result);
		    }
		    
		    Node finalResult=null;
	        Node ptr=result;
	        while (ptr!=null) 
	        {
	        	finalResult=new Node(ptr.term.coeff, ptr.term.degree,finalResult);
	        	ptr=ptr.next;
	        }
		    return finalResult; 
	}

	public static float evaluate(Node poly, float x) {
		float ans =0;
		for(Node current=poly;current!=null;current=current.next) {
			ans =(float) (ans+ current.term.coeff * Math.pow(x,current.term.degree));
		}
		return ans;
	}
	
	
	 
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}

