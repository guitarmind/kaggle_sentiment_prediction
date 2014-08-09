package markpeng.kaggle.sentiment;

public class TestingDataEntry {
	private int phraseId = 0;
	private int sentenceId = 0;
	private String phrase = "";
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

	public int getPredictedSentiment() {
		return predictedSentiment;
	}

	public void setPredictedSentiment(int predictedSentiment) {
		this.predictedSentiment = predictedSentiment;
	}

	@Override
	public String toString() {
		return "TestingDataEntry [phraseId=" + phraseId + ", sentenceId="
				+ sentenceId + ", phrase=" + phrase + ", predictedSentiment="
				+ predictedSentiment + "]";
	}

}
