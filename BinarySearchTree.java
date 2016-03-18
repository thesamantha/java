
public class BinarySearchTree {
	
	Element root;		//root of search tree
	int nodeCount;		//total count of nodes in tree
	
	
	class Element {
		
		int value;
		
		Element right;
		Element left;
		
		public Element(int value) {
			this.value = value;
		}
	}
	
		/**
		 * Inserts the element into the tree
		 * if the element is equal to a current node, we go to that element's left 
		 */
	
		public void addElement(Element element) {
			
			Element current = root;
			
			if(current == null) {		//if the tree doesn't exist, make the element to be inserted the root of the tree
				root = element;
				return;		//leave because we're done
			}
			
			while(current != null) {

				if(element.value <= current.value) {
					
					if(current.left == null) {
						current.left = element;
						return;		//leave because otherwise we'll enter the loop again
					}
					
					else
						current = current.left;
				}
				
				else if(element.value > current.value) {
					
					if(current.right == null) {
						current.right = element;
						return;
					}
					
					else
						current = current.right;
				}
			}
		}
		
		
	
		public void add(int element) {
			
			Element el = new Element(element);
			addElement(el);
		}
		
		/**
		 * traverses the tree recursively pre-order
		 * @param root
		 * @return String representation of binary tree pre-order
		 */
		public String preOrderRecursive(Element root) {
			
			//essentially go to the left until we reach a null node, at which point we return to caller, and then go to the right
			String nodeRep = "";
			
			if(root == null) {
				return "";
			}
			//get value of current node, and add it to the string which will get the values of subsequent nodes
			nodeRep += (root.value) + ";";
			nodeCount++;	//we can count the number of nodes every time we make it past the if(current == null) check, as this function visits every node
			
			
			nodeRep += (preOrderRecursive(root.left));
			nodeRep += (preOrderRecursive(root.right));
			
			return nodeRep;			
		}
		
		/**
		 * removes an element with the specified value, giving the children of the deleted node to the deleted node's parent
		 * when necessary
		 * @param val
		 * @return true if value was contained in the tree (which means it was deleted)
		 */
		public boolean removeRecursive(Element element, int val) {
			
			boolean itsIn = false;
			
			if(element == null) {		//covers both the case in which the tree is empty as well as the case
				return false;			//in which we've reached the last node without finding the element
			}
			
			//we need to look at the child of the current, so that we can transfer its children to the current should it have children
			if(element.left != null && element.left.value == val) {		
				
				if(element.left.left != null && element.left.right != null)	{		//two child scenario
					element.left = element.left.left;	//child's left child becomes grandparent's left child
					element.left.left.right = element.left.right;	//child's right child becomes child's left child's right child
				}
				
				//in both cases in which the left child has only one child, we transfer that child to the grandparent's left
				else if(element.left.left != null) {	//regardless of its position in relation to the parent
					element.left = element.left.left;
				}
				
				else if(element.left.right != null) {
					element.left = element.left.right;
				}
				
				else {	//if the child has no children
					element.left = null;		//simply break off connection to the child
				}
				return true;	//tell the caller we've found the node to be deleted
			}
			
			else if(element.right != null && element.right.value == val) {		
					
				if(element.right.left != null && element.right.right != null)	{		//two child scenario
					element.right = element.right.right;	//child's left child becomes grandparent's left child
					element.right.right.left = element.right.left;	//child's right child becomes child's left child's right child
				}
				
				//in both cases in which the right child has only one child, we transfer that child to the grandparent's right
				else if(element.right.left != null) {	//regardless of its position in relation to the parent
					element.right = element.right.left;
				}
				
				else if(element.right.left != null) {
					element.right = element.right.left;
				}
				
				else {	//if the child has no children
					element.right = null;		//simply break off connection to the child
				}
				return true;		//let the caller know we've found the node to be deleted
			}
			
			
			itsIn = removeRecursive(element.left, val);
			
			if(itsIn)			//if it was found in the left, we're done so return true
				return true;
			else				//otherwise, let's check in the right. we'll only get here if the last left node was null, so if 
				itsIn = removeRecursive(element.right, val);	//the right node is null as well, then we've finished the tree
																		//without finding the specified element
			
			return itsIn;
			
			
		}
		
		public boolean containsRecursive(Element element, int val) {
			
			//see if the element is equal to the value of the current node. if not, go to that node's left. if that node
			//has no left, go to its right. if it has no right, the element is not in the tree.
			
			boolean itsIn = false;
			
			if(element == null) {		//covers both the case in which the tree is empty as well as the case
				return false;			//in which we've reached the last node without finding the element
			}
			
			if(element.value == val) {
				return true;
			}
			
			
			itsIn = containsRecursive(element.left, val);
			
			if(itsIn)			//if it was found in the left, we're done so return true
				return true;
			else				//otherwise, let's check in the right. we'll only get here if the last left node was null, so if 
				itsIn = containsRecursive(element.right, val);	//the right node is null as well, then we've finished the tree
																		//without finding the specified element
			
			return itsIn;
		}
		
		/**
		 * traverses the tree recursively in-order
		 * @param root
		 * @return String representation of binary tree in-order
		 */
		public String inOrderRecursive(Element root) {
			
			String nodeRep = "";
			//we need to go all the way to the left, grab the value, and then return and go to the right
			
			if(root == null) {
				return "";
			}
			
			nodeRep += inOrderRecursive(root.left);
			nodeRep += root.value + ";";
			nodeRep += inOrderRecursive(root.right);
			
			return nodeRep;
		}
		
		
		
		/**
		 * Removes a single instance of the specified element from this tree.
		 * @return true if an element was removed
		 */
		public boolean remove(int element){
			
			//first check if we want to delete the root
			if(element == root.value) {
				
				if(root.right != null) {
					Element tmp = root;		//if the root has a right child, make that child the root
					
					//if the root had a left child
					if(tmp.left != null) {
						addElement(tmp.left);		//add it to the tree where it belongs. its children are still in the correct positions, so we just need to worry about placing left.
					}
				}
				
				else if (root.left != null) {		//if root has a left child (and in this case no right), make the left child the root
					root= root.left;
				}
				
				else { 		//root had no children
					root = null;
				}
				
				return true;
			}
			
			//otherwise, we didn't want to delete the root and can proceed to finding the node we did want to delete and deleting it
			return removeRecursive(root, element);
		}
		
		/**
		 * Returns true if this collection contains the specified element and false otherwise
		 */
		public boolean contains(int element){
			
			return containsRecursive(root, element);
		}
		
		/**
		 * Returns the number of elements in this tree
		 */
		public int size(){

			nodeCount = 0;		//reset nodeCount every time size() is called, as we're not sure when preOrderRecursive has been called
			preOrderRecursive(root);	//pass with root so that we count every node (including root)
			return nodeCount;
		}
		
		/**
		 * Returns the elements in the tree as a String, according to an in-order traversal, separated by ";"
		 * Output format could for instance be '1;3;3;7'
		 */
		public String inOrderTraversal(){
			return inOrderRecursive(root);
		}
		
		/**
		 * Returns the elements in the tree as a String, according to an pre-order traversal, separated by ";"
		 */
		public String preOrderTraversal(){
			
			return preOrderRecursive(root);
		}

}
