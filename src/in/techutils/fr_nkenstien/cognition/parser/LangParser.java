package in.techutils.fr_nkenstien.cognition.parser;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import in.techutils.cognition.nlp.lang.Entity;
import in.techutils.cognition.nlp.lang.MessageCorpus;
import launch.FrnkServlet;

@Path("/ParseService")
public class LangParser {

	@POST
	@Path("/parse")
	@Produces(MediaType.APPLICATION_JSON)
	public String parsedStatement(@FormParam("userid") String userId, @FormParam("text") String sentence) {

		System.out.println("User ID: " + userId);
		System.out.println("text: " + sentence);

		if (userId == null || sentence == null) {
			return "{\"response\":\"values are null\"}";
		}
		Entity source = new Entity(Entity.TYPE.USER);
		source.setName(userId);
		MessageCorpus mc = new MessageCorpus(sentence, source);
		FrnkServlet.getTreebankProcessor().processCorpus(mc);
		return mc.toString();
	}

}
