package in.techutils.cognition.nlp.lang;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.GsonBuilder;

import in.techutils.cognition.nlp.lang.treebank.Element;

public class MessageCorpus {
	private final String msgId;
	private String corpus;
	private Entity source;
	private Date msgTime;
	private static int msgSeq;
	private List<Element> parsed = new ArrayList<Element>();
	private List<Detection> detections = new ArrayList<Detection>();

	public MessageCorpus() {
		msgId = "Msg#" + (msgSeq++);
	}

	public MessageCorpus(String corpus, Entity source) {
		this();
		this.corpus = corpus;
		this.source = source;
		this.msgTime = new Date();
	}

	public List<Detection> getDetections() {
		return detections;
	}

	public void setDetections(List<Detection> detections) {
		this.detections = detections;
	}

	public List<Element> getParsed() {
		return parsed;
	}

	public void setParsed(List<Element> parsed) {
		this.parsed = parsed;
	}

	public String getCorpus() {
		return corpus;
	}

	public void setCorpus(String corpus) {
		this.corpus = corpus;
	}

	public Date getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(Date msgTime) {
		this.msgTime = msgTime;
	}

	public Entity getSource() {
		return source;
	}

	public void setSource(Entity source) {
		this.source = source;
	}

	public String getMsgId() {
		return msgId;
	}

	@Override
	public String toString() {
		return new GsonBuilder().create().toJson(this);
	}
}
