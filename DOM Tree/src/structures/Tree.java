package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		
		root=BuildRec();
	}
	
	private TagNode BuildRec() {
		int length;
		String a="";
		
		if (sc.hasNextLine()) {
			
			a=sc.nextLine();
		}
		else {
			return null;
		}
		length=a.length();
		boolean bool=false;
		if(a.charAt(0)=='<') {
			a=a.substring(1,length-1);
			if(a.charAt(0)=='/') {
				return null;
			}
			else {
				bool=true;
			}
		}
		TagNode temp=new TagNode(a,null,null);
		if(bool==true) {
		temp.firstChild=BuildRec();
		}
		temp.sibling=BuildRec();
		return temp;
	
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		replaceRec(oldTag, newTag, root); 
	}
	
	private void replaceRec(String oldTag, String newTag, TagNode current)
	{
		if (current == null)
			return;
		
		else if (current.tag.equals(oldTag))
			current.tag = newTag;
			
		replaceRec(oldTag, newTag, current.firstChild);
		replaceRec(oldTag, newTag, current.sibling);
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		BoldRec(row, root);
		}
	
	
	private void BoldRec(int row, TagNode current)
	{
		if (current == null)
			return;

		if (current.tag.equals("table"))
		{
			TagNode tableRow = current.firstChild;
			int counter = 0;
			while (counter < row-1)
			{
				if (tableRow.sibling != null)
					tableRow = tableRow.sibling;
				else throw new IllegalArgumentException();
				counter++;
			}
			TagNode col = tableRow.firstChild;
			while (col != null)
			{
				TagNode b = new TagNode("b", col.firstChild, null);
				col.firstChild = b;
				col = col.sibling;
			}
		}
		BoldRec(row, current.firstChild);
		BoldRec(row, current.sibling);
	}
	
	
	private boolean searchTag(String tag, TagNode current) {
		if (current==null) {
			return false;
		}
		if(current.tag.equals(tag)) {
			return true;
		}
		boolean found=(searchTag(tag, current.firstChild)|| searchTag(tag, current.sibling));
		return found; 
	}
			
		
	
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		if ((root == null) || (tag == null))
			return;

		else {
		while (searchTag(tag, root))
			removeRec(tag, root, root.firstChild);
	}
}
	
	private void removeRec(String tag, TagNode prev, TagNode current) {
		if(current==null ||prev==null) {
			return;
		}
		else if(current.tag.equals(tag)) {
			if (tag.equals("ol")|| tag.equals("ul")) {
				TagNode child= current.firstChild;
				while(child!=null) {
					if(child.tag.equals("li")) {
						child.tag="p";
					}
					child=child.sibling; 
				}
			}
			if(prev.firstChild==current) {
				prev.firstChild=current.firstChild;
				TagNode child=current.firstChild;  	
				while(child.sibling!=null) {
					child=child.sibling;
				}
				child.sibling=current.sibling;
			}
			else if(prev.sibling==current) {
				TagNode child = current.firstChild;
				while(child.sibling!=null) {
					child=child.sibling;
				}
				child.sibling=current.sibling; 
				prev.sibling=current.firstChild;
			
				
				}
			return; 
		}
			
			
		prev=current;
		removeRec(tag, prev, current.firstChild);
		removeRec(tag, prev, current.sibling);
		
	}
	private boolean specCheck(String str, String word) {

		 //we need to change this and compare with current.tag

		//word is what you are looking for

		//tag is what you are search

		word=word.toLowerCase();

		str=str.toLowerCase();

		int tlength = str.length();

		int wlength = word.length();

		int clength = wlength+1;

		

		if(tlength==wlength) {

			if(str.equals(word)) {

			return true;

			}

		}

		

		if(tlength==clength) {

			char c = str.charAt(tlength-1);

			if(c=='!'||c=='?'||c=='.'||c==';'|| c==':') {

				str=str.substring(0, tlength-1);

				

				if(str.equals(word)) {

					return true;

					}

			}	

		}

		

		

		return false;
		

		

	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		addTagRec(root, root.firstChild, word, tag);
	}
	
private void addTagRec(TagNode prev, TagNode current, String word, String tag) {
		
		// Base Case
		if (current == null)
			return;
		
		// Prevents nesting of two of the same tags
		if(prev != null && prev.tag.equals(tag)){
			return;
		}
		
		if (tag.equals("html") || tag.equals("body") || tag.equals("p") || tag.equals("em") || tag.equals("b") ||
				tag.equals("table") || tag.equals("tr") || tag.equals("td") || tag.equals("ol") || tag.equals("ul") || tag.equals("li")) {
		
		if(!current.tag.equals("html") && !current.tag.equals("body") && !current.tag.equals("p") && !current.tag.equals("em") && !current.tag.equals("b") &&
			!current.tag.equals("table") && !current.tag.equals("tr") && !current.tag.equals("td") && !current.tag.equals("ol") && !current.tag.equals("ul") && !current.tag.equals("li")) {
		
		// Investigate, if curr is plain text, see if there is a match.
		
		
			
			String[] arr = current.tag.split(" ");
			int len = arr.length;
			String before = "";
			String target = "";
			String after = "";
			TagNode temp = new TagNode(tag, null, null);
			// Check if there is only 1 word
			if (len == 1) {
				for (int i = 0; i < len; i++) {
					if (specCheck(arr[i], word)) {
						if (prev.firstChild == current) {
							if (current.sibling != null) {
								//System.out.println("hey1");
								prev.firstChild = temp;
								temp.firstChild = current;
								temp.sibling = current.sibling;
								current.sibling = null;
							}
							else {
								//System.out.println("hey2");
								prev.firstChild = temp;
								temp.firstChild = current;
							}
						}
						if (prev.sibling == current) {
							if (current.sibling != null) {
								//System.out.println("hey3");
								prev.sibling = temp;
								temp.firstChild = current;
								temp.sibling = current.sibling;
								current.sibling = null;
							}
							else {
								//System.out.println("hey4");
								prev.sibling = temp;
								temp.firstChild = current;
							}
						}
					}
				}	
			}
				else {
					TagNode head = null;
					TagNode tail = null;
					boolean beforeCheck = true;
					boolean targetCheck = true;
					boolean afterCheck = true;
					while (afterCheck == true) {
						TagNode beforeNode = new TagNode(null, null, null);
						TagNode targetNode = new TagNode(null, null, null);
						TagNode afterNode = new TagNode(null, null, null);
						before = "";
						target = "";
						after = "";
						for (int n = 0; n < len && (targetCheck == true); n++) {
							if (specCheck(arr[n], word)) {
								beforeCheck = false;
								targetCheck = false;
								//System.out.println("target, arr[n]: " + arr[n]);
								target = arr[n];
								targetNode.tag = target;
								// Store the rest of the string into an after node
								if (n != len - 1) {
								for (int m = n + 1; m < len; m++) {
									//System.out.println("length: " + len);
									//System.out.println("m: " + m);
									after = after + arr[m] + " ";
									//System.out.println("after: " + after);
								}
								afterNode.tag = after;
								}
							}
							else if (beforeCheck == true) {
								before = before + arr[n] + " ";
								beforeNode.tag = before;
								//System.out.println("before: " + before);
							}
						}
						
						// If there was never a match
						if (targetCheck == true){
							if (prev.firstChild == current)
								prev.firstChild = beforeNode;
							if (prev.sibling == current)
								prev.sibling = beforeNode;
							break;
						}
						
						if (beforeNode.tag != null && targetNode.tag != null && afterNode.tag != null) {
							beforeNode.sibling = temp;
							temp.firstChild = targetNode;
							temp.sibling = afterNode;
						}
						else if (beforeNode.tag != null && targetNode.tag != null) {
							beforeNode.sibling = temp;
							temp.firstChild = targetNode;
							
						}
						else if (afterNode.tag != null) {
							temp.firstChild = targetNode;
							temp.sibling = afterNode;
						}
						
						if (head == null && beforeNode.tag != null)
							head = beforeNode;
						else if (head == null && beforeNode.tag == null) {
							temp.firstChild = targetNode;
							head = temp;
						}
						else
							tail = targetNode;
						
						if (afterNode.tag != null) {
							afterCheck = true;
							arr = afterNode.tag.split(" ");
							len = arr.length;
							if (head == null && beforeNode.tag != null)
								head = beforeNode;
							else if (head == null && beforeNode.tag == null) {
								temp.firstChild = targetNode;
								head = temp;
							}
							else
								tail = afterNode;
						}
						else
							afterCheck = false;
					}
					
					if (prev.firstChild == current)
						prev.firstChild = head;
					else if (prev.sibling == current)
						prev.sibling = head;
					if(current.sibling == null) {
						
					}
					else {
						tail.sibling = current.sibling;
						current.sibling = null;
					}
				}

			}
		
	//	if(current.firstChild!=null || current.sibling!=null) {
		addTagRec(current, current.firstChild, word, tag);
		addTagRec(current, current.sibling,  word, tag);
	//	}
		
	}
		return;
}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}

