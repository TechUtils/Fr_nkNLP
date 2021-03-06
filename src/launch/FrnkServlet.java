package launch;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.techutils.cognition.nlp.lang.treebank.TreebankProcessor;

public class FrnkServlet extends HttpServlet {
	public FrnkServlet() {
		System.out.println("Loading frank into the system...");
		getTreebankProcessor();
	}

	private static TreebankProcessor t = null;

	synchronized public static TreebankProcessor getTreebankProcessor() {
		if (t == null) {
			t = new TreebankProcessor();
		}
		return t;
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletOutputStream out = resp.getOutputStream();
		out.write("hello heroku".getBytes());
		out.flush();
		out.close();
	}
}
