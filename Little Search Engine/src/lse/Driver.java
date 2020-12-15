package lse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Driver {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		LittleSearchEngine engine = new LittleSearchEngine();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter document file name => ");
		String docFile = br.readLine();
		System.out.print("Enter noise words file name => ");
		String noiseWordsFile = br.readLine();
		
		engine.makeIndex(docFile, noiseWordsFile);
		
//		System.out.println(engine.getKeyword("reading..?")); 
		
//		ArrayList<Occurrence> o = new ArrayList<Occurrence>();
//		String d = "";
//		o.add(new Occurrence(d, 12));
//		o.add(new Occurrence(d, 8));
//		o.add(new Occurrence(d, 7));
//		o.add(new Occurrence(d, 5));
//		o.add(new Occurrence(d, 3));
//		o.add(new Occurrence(d, 2));
//		o.add(new Occurrence(d, 6));
//		
//		System.out.println(engine.insertLastOccurrence(o));
//		System.out.println(o);
//		
		
		
//		for(String key: engine.keywordsIndex.keySet()) {
//			System.out.println(key + " " + engine.keywordsIndex.get(key));
//		}

		System.out.println("\n");
		engine.top5search("wild","simply");
		
	}

}