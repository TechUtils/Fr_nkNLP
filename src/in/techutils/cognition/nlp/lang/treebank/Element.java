package in.techutils.cognition.nlp.lang.treebank;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Element {
	private String type, value;
	private List<Element> subElements;

	public Element(String t, String v) {
		type = t;
		value = v;
	}

	public Element(String t, List<Element> e) {
		subElements = new ArrayList<Element>();
		type = t;
		subElements.addAll(e);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Element> getSubElements() {
		if (subElements == null) {
			subElements = new ArrayList<Element>();
		}
		return subElements;
	}

	public void setSubElements(List<Element> subElements) {
		this.subElements = subElements;
	}

	@Override
	public String toString() {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.toJson(this);
	}
}
