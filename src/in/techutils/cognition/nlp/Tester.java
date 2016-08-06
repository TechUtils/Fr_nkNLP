package in.techutils.cognition.nlp;

import java.util.Calendar;
import java.util.Scanner;

import in.techutils.cognition.nlp.lang.Entity;
import in.techutils.cognition.nlp.lang.MessageCorpus;
import in.techutils.cognition.nlp.lang.treebank.TreebankProcessor;

public class Tester {
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(System.in);
		TreebankProcessor t = new TreebankProcessor();
		while (true) {
			System.out.println("Enter message:");
			String corpus = s.nextLine();// "Alice saw Bob"
			long start = Calendar.getInstance().getTimeInMillis();
			if ("".equals(corpus)) {
				break;
			}
			Entity source = new Entity(Entity.TYPE.USER);
			source.setName("Alex");
			MessageCorpus mc = new MessageCorpus(corpus, source);
			t.processCorpus(mc);
			System.out.println(mc);
			long end = Calendar.getInstance().getTimeInMillis();
			System.out.println((end - start) + " ms");
		}
		s.close();
		// String corpus = "There was a boy. His name
		// was
		// Alex Mathews";
		// String corpus = "This is it";
		// "My Name is Alex Mathews, and I work
		// for myself. I get a salary of Rs.
		// 5000. ";//
		// String corpus = "My birth place is India. ";//
		// String corpus = "I get a salary of Rs. 5000. ";//
		// String corpus = "I work for Oracle India pvt. " ;//
		// String corpus = "Oracle India pvt. is located in Hyderabad,
		// India.";//

	}

}
