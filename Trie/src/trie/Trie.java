package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {

		TrieNode root = new TrieNode(null, null, null);
		if (allWords.length == 0) {		
			return root;
		}
		Indexes firstTrieNodeSubstring = new Indexes(0, (short) 0, (short) (allWords[0].length()-1));
		root.firstChild = new TrieNode(firstTrieNodeSubstring, null, null);
		
		
		for(int index=1; index<allWords.length; index++) {
			
			TrieNode ptr = root.firstChild, prev = root.firstChild;
			String currWord = allWords[index];
			build(allWords, currWord, index, ptr, prev);
		}
		
		return root;
	}
	
	
	private static void build(String[] allWords, String word, int index, TrieNode ptr, TrieNode prev) {
		
		int match = 0;
		
		while (ptr != null) {
			
			int startIndex = ptr.substr.startIndex;
			int endIndex = ptr.substr.endIndex;
			
			if(startIndex > word.length()) {
				prev = ptr;
				ptr = ptr.sibling;
				continue;
			}
			
			match = commonPrefix(word, allWords, startIndex, endIndex, ptr);

			if(match != -1) {
				match += startIndex;
			}
			
			if(match == -1) { 
				prev = ptr;
				ptr = ptr.sibling;
			}
			else if(match == endIndex) { 
				prev = ptr;
				ptr = ptr.firstChild;
			}
			else if (match < endIndex) { 
				prev = ptr;
				updateTrie(prev, word, match, index);
				break;
			}
			
		}
		
		if (ptr == null) {
			Indexes indexes = new Indexes(index, (short)prev.substr.startIndex, (short)(word.length()-1));
			prev.sibling = new TrieNode(indexes, null, null);
		}
		
	}
	
	
	private static int commonPrefix(String word, String[] allWords, int startIndex, int endIndex, TrieNode ptr) {

		int ptrWordIndex = ptr.substr.wordIndex;
			
		String ptrWord = allWords[ptrWordIndex].substring(startIndex, endIndex+1);
		String insert = word.substring(startIndex);
		
		int match = 0;
		while (match < ptrWord.length() && match < insert.length() && ptrWord.charAt(match) == insert.charAt(match)) {
			match++;
		}
		return match-1;
			
	}
	

	private static void updateTrie(TrieNode prev, String word, int match, int index) {
				
		Indexes newWordIndexes = new Indexes(prev.substr.wordIndex, (short)(match+1), prev.substr.endIndex);
		prev.substr.endIndex = (short)match; 
		Indexes prevIndex = new Indexes((short)index, (short)(match+1), (short)(word.length()-1));
		prev.firstChild = new TrieNode(newWordIndexes, prev.firstChild, new TrieNode(prevIndex, null, null));
		
	}
		
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		
		if (root == null) {
			return null;
		}
		if (root.firstChild == null && root.substr == null) {
			return null;
		}
		
		ArrayList<TrieNode> completeWords = new ArrayList<>();
		ArrayList<TrieNode> temp = new ArrayList<>();
		
		TrieNode ptr = root;
		while (ptr != null) {
			
			if (ptr.substr == null) {
				ptr = ptr.firstChild;
			}
			
			String ptrWord = allWords[ptr.substr.wordIndex];
			
			if (beginsWith(ptrWord, prefix, ptr)) {
				
				//ptr is a word
				if (ptr.firstChild == null) {
					completeWords.add(ptr);
					ptr = ptr.sibling;
					
				}
				//ptr is a prefix
				else {
					temp = (completionList(ptr.firstChild, allWords, prefix));
					ptr = ptr.sibling;
				}
								
			}
			
			else {
				ptr = ptr.sibling;
			}
			
			if (temp != null && !completeWords.containsAll(temp)) {
				completeWords.addAll(temp);
			}
						
		}
		if (!completeWords.isEmpty()) {
			return completeWords;
		}
		else {
			return null;
		}
	}
	
	private static boolean beginsWith(String ptrWord, String prefix, TrieNode ptr) {
		String prefixInTrie = ptrWord.substring(0, ptr.substr.endIndex+1);
		int smallerWord = Math.min(prefix.length(), prefixInTrie.length());
		
		if (ptr.firstChild == null && prefix.length() > ptrWord.length()) {
			return false;
		}
		
		for (int i=0; i<smallerWord; i++) {
			if (ptrWord.charAt(i) != prefix.charAt(i)) {
				return false;
			}
		}
		
		return true;
	
	}
	
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
