package markpeng.kaggle.sentiment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class SentimentPrediction {
	String fileFolder = "G:/Dropbox/Kaggle/Sentiment Analysis on Movie Reviews";
	private String swnFilePath = "E:/SentiWordNet_3.0.0/SentiWordNet_3.0.0_20130122.txt";
	private SentiWordNetParser sentiwordNet = new SentiWordNetParser(
			swnFilePath);
	private DatasetParser parser = new DatasetParser(fileFolder);

	public void doTraining() throws Exception {
		StringBuffer log = new StringBuffer();

		long startTime = Calendar.getInstance().getTimeInMillis();

		Hashtable<Integer, TrainingDataEntry> train = parser
				.getTrainingData("train.tsv");
		// Hashtable<Integer, TrainingDataEntry> train = parser
		// .getTrainingData("train_small_1000.tsv");
		int matchedCount = 0;
		int totalCount = 0;
		for (Integer phraseId : train.keySet()) {
			TrainingDataEntry entry = train.get(phraseId);
			String phrase = entry.getPhrase();
			int expectedSentiment = entry.getExpectedSentiment();

			log.append("[Predicting phrase: " + phrase + "]" + "\n");
			// System.out.println("Predicting phrase: " + phrase);

			int predictedSentiment = 0;
			double sumSentiment = sentiwordNet.findSumSentiment(phrase);

			StringTokenizer tokenizer = new StringTokenizer(phrase);
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken().trim();
				if (token.length() > 0) {
					// if (token.equals("not") || token.equals("rather")
					// || token.equals("but") || token.equals("none")) {
					if (token.equals("not") || token.equals("rather")
							|| token.equals("but")) {
						// reverse the sentiment due to negation
						sumSentiment = sumSentiment * -1;
					}
				}
			}

			if (sumSentiment == 0)
				predictedSentiment = 2;
			else if (sumSentiment >= 0.75)
				predictedSentiment = 4;
			else if (sumSentiment < 0.75 && sumSentiment > 0.25)
				// positive
				predictedSentiment = 3;
			else if (sumSentiment <= 0.25 && sumSentiment >= -0.25)
				// neutral
				predictedSentiment = 2;
			else if (sumSentiment < -0.25 && sumSentiment >= -0.75)
				// somewhat negative
				predictedSentiment = 1;
			else if (sumSentiment < -0.75)
				// negative
				predictedSentiment = 0;

			// log.append("Expected sentiment type:" + expectedSentiment +
			// "\n");
			// // System.out.println("Expected sentiment type:" +
			// // expectedSentiment);
			// log.append("Predicted sentiment score:" + sumSentiment + "\n");
			// // System.out.println("Predicted sentiment score:" +
			// sumSentiment);
			// log.append("Predicted sentiment type:" + predictedSentiment
			// + "\n\n");
			// System.out.println("Predicted sentiment type:" +
			// predictedSentiment);

			if (predictedSentiment == expectedSentiment)
				matchedCount++;
			else {
				log.append("Expected sentiment type:" + expectedSentiment
						+ "\n");
				// System.out.println("Expected sentiment type:" +
				// expectedSentiment);
				log.append("Predicted sentiment score:" + sumSentiment + "\n");
				// System.out.println("Predicted sentiment score:" +
				// sumSentiment);
				log.append("Predicted sentiment type:" + predictedSentiment
						+ "\n\n");
			}

			if (totalCount % 1000 == 0) {
				System.out.println("Processed count: " + totalCount);
				// write log
				writeToFile(log.toString(), "trainig_output.txt");
				log.setLength(0);
			}

			totalCount++;
		} // end of for loop

		System.out.println(log.toString());

		long endTime = Calendar.getInstance().getTimeInMillis();
		double elapseTime = (endTime - startTime) / (1000 * 60);
		log.append("Time spent:" + elapseTime + " minutes." + "\n");
		System.out.println("Time spent:" + elapseTime + " minutes.");

		double accurarcy = ((double) matchedCount / totalCount) * 100;
		log.append("Accuracy: " + accurarcy + " %" + "\n");
		System.out.println("Accuracy: " + accurarcy + " %");

		// write log
		writeToFile(log.toString(), "trainig_output.txt");
		log.setLength(0);
	}

	public void doTesting() throws Exception {
		StringBuffer output = new StringBuffer();
		output.append("PhraseId,Sentiment\n");

		StringBuffer log = new StringBuffer();

		long startTime = Calendar.getInstance().getTimeInMillis();

		Hashtable<Integer, TestingDataEntry> train = parser
				.getTestingData("test.tsv");
		int totalCount = 0;
		for (Integer phraseId : train.keySet()) {
			TestingDataEntry entry = train.get(phraseId);
			String phrase = entry.getPhrase();

			log.append("[Predicting phrase: " + phrase + "]" + "\n");
			// System.out.println("Predicting phrase: " + phrase);

			int predictedSentiment = 0;
			double sumSentiment = sentiwordNet.findSumSentiment(phrase);

			StringTokenizer tokenizer = new StringTokenizer(phrase);
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken().trim();
				if (token.length() > 0) {
					// if (token.equals("not") || token.equals("rather")
					// || token.equals("but") || token.equals("none")) {
					if (token.equals("not") || token.equals("rather")
							|| token.equals("but")) {
						// reverse the sentiment due to negation
						sumSentiment = sumSentiment * -1;
					}
				}
			}

			if (sumSentiment == 0)
				predictedSentiment = 2;
			else if (sumSentiment >= 0.75)
				predictedSentiment = 4;
			else if (sumSentiment < 0.75 && sumSentiment > 0.25)
				// positive
				predictedSentiment = 3;
			else if (sumSentiment <= 0.25 && sumSentiment >= -0.25)
				// neutral
				predictedSentiment = 2;
			else if (sumSentiment < -0.25 && sumSentiment >= -0.75)
				// somewhat negative
				predictedSentiment = 1;
			else if (sumSentiment < -0.75)
				// negative
				predictedSentiment = 0;

			if (totalCount % 1000 == 0) {
				System.out.println("Processed count: " + totalCount);
				// write log
				writeToFile(log.toString(), "testing_output.txt");
				log.setLength(0);
			}

			output.append(phraseId + "," + predictedSentiment + "\n");

			totalCount++;
		} // end of for loop

		System.out.println(log.toString());

		long endTime = Calendar.getInstance().getTimeInMillis();
		double elapseTime = (endTime - startTime) / (1000 * 60);
		log.append("Time spent:" + elapseTime + " minutes." + "\n");
		System.out.println("Time spent:" + elapseTime + " minutes.");

		// write log
		writeToFile(log.toString(), "testing_output.txt");
		log.setLength(0);

		// write submission file
		writeToFile(output.toString(), "submission.csv");
		output.setLength(0);
	}

	private void writeToFile(String text, String logFilePath) throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath,
				true));
		writer.write(text.toString());
		writer.flush();
		writer.close();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SentimentPrediction tester = new SentimentPrediction();
		// tester.doTraining();
		tester.doTesting();
	}

}
