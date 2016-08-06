package in.techutils.fr_nkenstien.cognition.parser;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import in.techutils.cognition.nlp.lang.Entity;
import in.techutils.cognition.nlp.lang.MessageCorpus;
import in.techutils.cognition.nlp.lang.treebank.TreebankProcessor;

@Path("/ParseService")
public class LangParser {

	private static TreebankProcessor t = null;

	synchronized private TreebankProcessor getTreebankProcessor() {
		if (t == null) {
			t = new TreebankProcessor();
		}
		return t;
	}

	@POST
	@Path("/parse")
	@Produces(MediaType.APPLICATION_JSON)
	public String parsedStatement(String userId, String sentence) {
		Entity source = new Entity(Entity.TYPE.USER);
		source.setName(userId);
		MessageCorpus mc = new MessageCorpus(sentence, source);
		getTreebankProcessor().processCorpus(mc);
		return mc.toString();
	}

}
