package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		if (g == null || p1 == null || p2 == null) {
			return null;
		}

        boolean[] visited = new boolean[g.members.length];
        Queue<Person> people = new Queue<>(); 
        people.enqueue(g.members[g.map.get(p1)]); 
        Queue<ArrayList<String>> chains = new Queue<>(); 
        ArrayList<String> first = new ArrayList<>(); 
        first.add(p1);
        chains.enqueue(first); 
        
        while(!people.isEmpty()) {
            Person person = people.dequeue(); 
            visited[g.map.get(person.name)] = true; 
            ArrayList<String> list = chains.dequeue(); 
            Friend friend = g.members[g.map.get(person.name)].first; 
            while(friend != null) { 
	            if(!visited[friend.fnum]) {
	                ArrayList<String> shortChain = new ArrayList<>(list); 
	                String name = g.members[friend.fnum].name; 
	                shortChain.add(name); 

	                if(name.equals(p2)) return shortChain; 
	            
		            people.enqueue(g.members[friend.fnum]); 
		            chains.enqueue(shortChain); 
	            }
	            friend = friend.next; 
            }
        }
        
        return null;
	}
	

	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		if(g == null || school == null){
			return null;
		}
		
		ArrayList<ArrayList<String>> cliqueList= new ArrayList<>();
		boolean[] visited = new boolean[g.members.length];
		return BFS(g, g.members[0], cliqueList, visited, school);
		
	}
	
	private static ArrayList<ArrayList<String>> BFS(Graph g, Person start, ArrayList<ArrayList<String>> cliqueList, boolean[] visited, String school){
		
		ArrayList<String> cliquesResults = new ArrayList<String>();
		Queue<Person> queue = new Queue<>();
		queue.enqueue(start);
		visited[g.map.get(start.name)] = true;
		
		if(start.school == null || !start.school.equals(school)){
			
			queue.dequeue();
			
			for(int i = 0; i<visited.length; i++){
				if(!visited[i]){
					return BFS(g, g.members[i], cliqueList, visited, school);
				}
			}
		}
		
		Person currPerson = new Person();
		
		while(!queue.isEmpty()){
			
			currPerson = queue.dequeue();
			
			Friend friend = currPerson.first;
			cliquesResults.add(currPerson.name);
			
			while(friend != null){
				
				if(!visited[friend.fnum]) {
					
					if(g.members[friend.fnum].school != null) {
						if(g.members[friend.fnum].school.equals(school)) {
							queue.enqueue(g.members[friend.fnum]);
						}
					}

					visited[friend.fnum] = true;
				}
				friend = friend.next;
			}
		}
		
		if(!cliquesResults.isEmpty()) {
			cliqueList.add(cliquesResults);
		}
		
		for(int i = 0; i < visited.length; i++) {
			if(!visited[i]) {
				return BFS(g, g.members[i], cliqueList, visited, school);
			}
		}
		
		return cliqueList;
	}


	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		if(g == null) {
			return null;
		}
		
		ArrayList<String> connectors = new ArrayList<String>();
		boolean[] visited = new boolean[g.members.length];
		
		ArrayList<String> predecessor = new ArrayList<String>();
		
		int[] DFSnums= new int[g.members.length];
		
		int[] before = new int[g.members.length];
		
		for(int i=0; i < g.members.length; i++){
			if(!visited[i]) {
				connectors = DFS(connectors, g, g.members[i], new int[] {0,0}, DFSnums, before, predecessor, visited);
			}
		}
		
		return connectors;
		
	}
	private static ArrayList<String> DFS(ArrayList<String> connectors, Graph g, Person start, int[] count, int[] DFSnums, int[] before, ArrayList<String> predecessor, boolean[] visited){
		
		visited[g.map.get(start.name)] = true;
		
		Friend friend = start.first;
		
		DFSnums[g.map.get(start.name)] = count[0];
		before[g.map.get(start.name)] = count[1];
		
		while(friend != null) {
			
			if(!visited[friend.fnum]) {
				
				count[0]++;
				count[1]++;
				
				connectors = DFS(connectors, g, g.members[friend.fnum], count, DFSnums, before, predecessor, visited);
				
				if(DFSnums[g.map.get(start.name)] <= before[friend.fnum]) {
					
					if((!connectors.contains(start.name) && predecessor.contains(start.name)) || (!connectors.contains(start.name) && DFSnums[g.map.get(start.name)] != 0)) {
						connectors.add(start.name);
					}
				}
				else {
					
					if(before[g.map.get(start.name)] > before[friend.fnum]) {
						before[g.map.get(start.name)] = before[friend.fnum];
					}
					
				}	
				predecessor.add(start.name);
			}
			else {
				
				if(before[g.map.get(start.name)] > DFSnums[friend.fnum]) {
					before[g.map.get(start.name)] = DFSnums[friend.fnum];	
				}
				
			}
			friend = friend.next;
		}

		return connectors;
	}

}
