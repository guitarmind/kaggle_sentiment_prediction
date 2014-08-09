package markpeng.kaggle.sentiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.tartarus.snowball.ext.PorterStemmer;

public class SentiWordNetParser {
	public HashMap<String, Double> dict = new HashMap<String, Double>();

	// Unit Test
	public static void main(String args[]) {
		String swnFilePath = "E:/SentiWordNet_3.0.0/SentiWordNet_3.0.0_20130122.txt";
		SentiWordNetParser parser = new SentiWordNetParser(swnFilePath);
		System.out.println(parser.findSumSentiment("Mojo"));
	}

	public double findSumSentiment(String phrase) {
		double sum = 0.0;
		for (String word : dict.keySet()) {
			if (phrase.contains(word))
				sum += dict.get(word);
		}

		return sum;
	}

	// Initialize the sentiwordnet file into a dictionary
	public SentiWordNetParser(String swnFilePath) {
		try {
			System.out.println("Reading : " + swnFilePath);
			BufferedReader reader = new BufferedReader(new FileReader(
					swnFilePath));
			String line = "";
			HashMap<String, Vector<Double>> _temp = new HashMap<String, Vector<Double>>();
			while ((line = reader.readLine()) != null) {
				// Skip parsing if line is empty or starts with a comment(#)
				if (line.isEmpty() || line.startsWith("#"))
					continue;
				// Segment into data string tokens
				// Algorithm to calculate the score is taken from the
				// sentiwordnet web site
				// http://sentiwordnet.isti.cnr.it/
				String data[] = line.split("\t");
				String words[] = data[4].split(" ");
				double score = Double.parseDouble(data[2])
						- Double.parseDouble(data[3]);
				// Java 5.0+ paradigm .... fix this later ???
				for (String w : words) {
					String[] w_n = w.split("#");
					// w_n[0] += "#" + data[0];
					int index = Integer.parseInt(w_n[1]) - 1;
					if (_temp.containsKey(w_n[0])) {
						Vector<Double> v = _temp.get(w_n[0]);
						if (index > v.size())
							for (int i = v.size(); i < index; i++)
								v.add(0.0);
						v.add(index, score);
						_temp.put(w_n[0], v);
					} else {
						Vector<Double> v = new Vector<Double>();
						for (int i = 0; i < index; i++)
							v.add(0.0);
						v.add(index, score);
						_temp.put(w_n[0], v);
					}
				} // End for
			} // End while
				// Now re-score and put into dictionary by words
			Set<String> temp = _temp.keySet();
			for (Iterator<String> iterator = temp.iterator(); iterator
					.hasNext();) {
				String word = (String) iterator.next();

				// if (word.contains("#"))
				// word = word.split("#")[0];

				// preprocessing
				String phrase = word.toLowerCase();

				// stemming
				StringBuffer newString = new StringBuffer();
				StringTokenizer tokenizer = new StringTokenizer(
						phrase.replaceAll("[^a-zA-Z\\s]", " "));
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					if (token.trim().length() > 0) {
						PorterStemmer stemer = new PorterStemmer();
						stemer.setCurrent(token);
						stemer.stem();
						String stemmedToken = stemer.getCurrent();

						if (!stemmedToken.equals(token)) {
							// System.out.println(token + " stemmed to "
							// + stemmedToken + "\n");
							token = stemmedToken;
						}
					}

					newString.append(token + " ");
				}
				// System.out.println("Original: " + phrase);
				phrase = newString.toString();
				// System.out.println("Stemmed: " + phrase);

				Vector<Double> v = _temp.get(word);
				double score = 0.0;
				double sum = 0.0;
				for (int i = 0; i < v.size(); i++)
					score += ((double) 1 / (double) (i + 1)) * v.get(i);
				for (int i = 1; i <= v.size(); i++)
					sum += (double) 1 / (double) i;
				score /= sum;
				// System.out.println("word is " + phrase + "(score: " + score
				// + ")");
				dict.put(phrase, Double.valueOf(score));
			}
			// Info + sanity check
			System.out.println("Sent Dictionary Size: " + dict.size());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
