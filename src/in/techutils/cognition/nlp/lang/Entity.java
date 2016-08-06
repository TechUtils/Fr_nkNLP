package in.techutils.cognition.nlp.lang;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class Entity implements Serializable {
	public enum TYPE {
		USER, MSG, RELATION, OBJECT, ATTRIBUTE;
	}

	private static final String ID_STR = "EntityID#";
	private static int idSeq = 0;

	public Entity(TYPE t) {
		this.id = ID_STR + (idSeq++);
		this.msgs = new HashSet<Entity>();
		this.type = t;
	}

	private String id;
	private String name;
	private TYPE type;

	private HashMap<String, Object> attribs;
	private Set<Entity> msgs;

	public String getId() {
		return id;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String getName() {
		return name == null ? "Unknown" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, Object> getAttribs() {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.putAll(attribs);
		return hm;
	}

	public void setAttribs(String attrib, Object e) {
		this.attribs.put(attrib, e);
	}

	public Set<Entity> getMsgs() {
		Set<Entity> es = new HashSet<Entity>();
		es.addAll(msgs);
		return es;
	}

	public void addMsg(String msg) {
		Entity m = new Entity(TYPE.MSG);
		m.setAttribs("msg", msg);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Entity)) {
			return false;
		}
		Entity object = (Entity) obj;
		if (this.getType().equals(object.getType())) {
			if (TYPE.MSG.equals(this.getType())) {
				boolean test = object.getAttribs().get("msg") != null;
				test = test && this.getAttribs().get("msg") != null;
				test = test && this.getAttribs().get("msg").equals(object.getAttribs().get("msg"));
				return test;
			}
			return super.equals(obj);
		} else {
			return false;
		}
	}
}