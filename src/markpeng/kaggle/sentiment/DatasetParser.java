package markpeng.kaggle.sentiment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import org.tartarus.snowball.ext.PorterStemmer;

public class DatasetParser {

	private String fileFolder = null;

	public DatasetParser(String fileFolder) {
		this.fileFolder = fileFolder;
	}

	public Hashtable<Integer, TrainingDataEntry> getTrainingData(String fileName)
			throws Exception {
		Hashtable<Integer, TrainingDataEntry> result = new Hashtable<Integer, TrainingDataEntry>();

		BufferedReader reader = new BufferedReader(new FileReader(fileFolder
				+ "/" + fileName));
		String line = null;
		int index = 0;

		int oneCount = 0;
		int twoCount = 0;
		int threeCount = 0;
		int fourCount = 0;
		int fiveCount = 0;

		while ((line = reader.readLine()) != null) {
			if (index > 0) {
				String[] data = line.split("\t");
				if (data != null) {
					int phraseId = Integer.parseInt(data[0]);
					int sentenceId = Integer.parseInt(data[1]);
					String phrase = data[2];
					int sentiment = Integer.parseInt(data[3]);

					// preprocessing
					phrase = phrase.toLowerCase();

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

					if (sentiment == 0)
						oneCount++;
					else if (sentiment == 1)
						twoCount++;
					else if (sentiment == 2)
						threeCount++;
					else if (sentiment == 3)
						fourCount++;
					else if (sentiment == 4)
						fiveCount++;

					TrainingDataEntry entry = new TrainingDataEntry();
					entry.setPhraseId(phraseId);
					entry.setSentenceId(sentenceId);
					entry.setPhrase(phrase);
					entry.setExpectedSentiment(sentiment);

					result.put(phraseId, entry);
				}
			}

			index++;
		}
		reader.close();

		System.out.println("[Statistics]");
		System.out.println("0 negative ratio:" + (double) oneCount
				/ (index + 1));
		System.out.println("1 somewhat negative ratio:" + (double) twoCount
				/ (index + 1));
		System.out.println("2 neutral ratio:" + (double) threeCount
				/ (index + 1));
		System.out.println("3 somewhat positive ratio:" + (double) fourCount
				/ (index + 1));
		System.out.println("4 positive ratio:" + (double) fiveCount
				/ (index + 1));

		return result;
	}

	public Hashtable<Integer, TestingDataEntry> getTestingData(String fileName)
			throws Exception {
		Hashtable<Integer, TestingDataEntry> result = new Hashtable<Integer, TestingDataEntry>();

		BufferedReader reader = new BufferedReader(new FileReader(fileFolder
				+ "/" + fileName));
		String line = null;
		int index = 0;

		while ((line = reader.readLine()) != null) {
			if (index > 0) {
				String[] data = line.split("\t");
				if (data != null) {
					int phraseId = Integer.parseInt(data[0]);
					int sentenceId = Integer.parseInt(data[1]);
					String phrase = data[2];

					// preprocessing
					phrase = phrase.toLowerCase();

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
					System.out.println("Original: " + phrase);
					phrase = newString.toString();
					System.out.println("Stemmed: " + phrase);

					TestingDataEntry entry = new TestingDataEntry();
					entry.setPhraseId(phraseId);
					entry.setSentenceId(sentenceId);
					entry.setPhrase(phrase);

					result.put(phraseId, entry);
				}
			}

			index++;
		}
		reader.close();

		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String fileFolder = "G:/Dropbox/Kaggle/Sentiment Analysis on Movie Reviews";
		DatasetParser parser = new DatasetParser(fileFolder);
		Hashtable<Integer, TrainingDataEntry> train = parser
				.getTrainingData("train.tsv");
		// Hashtable<Integer, TestingDataEntry> test =
		// parser.getTestingData("test.tsv");
		// for (Integer key : train.keySet())
		// System.out.println(train.get(key).toString());

	}
}
