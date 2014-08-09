package markpeng.kaggle.sentiment;

public class TrainingDataEntry {
	private int phraseId = 0;
	private int sentenceId = 0;
	private String phrase = "";
	private int expectedSentiment = 0;
	private int predictedSentiment = 0;

	public int getPhraseId() {
		return phraseId;
	}

	public void setPhraseId(int phraseId) {
		this.phraseId = phraseId;
	}

	public int getSentenceId() {
		return sentenceId;
	}

	public void setSentenceId(int sentenceId) {
		this.sentenceId = sentenceId;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public int getExpectedSentiment() {
		return expectedSentiment;
	}

	public void setExpectedSentiment(int expectedSentiment) {
		this.expectedSentiment = expectedSentiment;
	}

	public int getPredictedSentiment() {
		return predictedSentiment;
	}

	public void setPredictedSentiment(int predictedSentiment) {
		this.predictedSentiment = predictedSentiment;
	}

	@Override
	public String toString() {
		return "TrainingDataEntry [phraseId=" + phraseId + ", sentenceId="
				+ sentenceId + ", phrase=" + phrase + ", expectedSentiment="
				+ expectedSentiment + ", predictedSentiment="
				+ predictedSentiment + "]";
	}

	
}
