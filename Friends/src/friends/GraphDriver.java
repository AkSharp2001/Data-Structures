package friends;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GraphDriver {

	public static void main(String[] args) throws FileNotFoundException {

		Graph graph = new Graph(new Scanner(new File("test")));
	
		System.out.println(Friends.shortestChain(graph, "sam","aparna"));
		
//		System.out.println("Rutgers: " + Friends.cliques(graph, "rutgers"));
//		System.out.println("Penn State: " + Friends.cliques(graph, "penn state"));
//		System.out.println("UCLA: " + Friends.cliques(graph, "ucla"));
//		System.out.println("Cornell: " + Friends.cliques(graph, "cornell"));
//		
//		System.out.println(Friends.connectors(graph));
		

	}

}
