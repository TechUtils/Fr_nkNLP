package in.techutils.cognition.nlp.lang.treebank;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import in.techutils.cognition.nlp.lang.Detection;
import in.techutils.cognition.nlp.lang.MessageCorpus;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import opennlp.tools.util.model.BaseModel;

public class TreebankProcessor {
	public static final String MODEL_PATH = "models/";
	private static SentenceDetectorME sentenceDetector;
	private static Tokenizer tokenizer;
	private static NameFinderME nameFinder, placeFinder, timeFinder, currencyFinder, orgFinder;
	private static Parser parser;

	public TreebankProcessor() {
		InputStream modelIn = null;
		try {
			{
				System.out.println("en-sent.bin:" + (new File(MODEL_PATH + "en-sent.bin")).exists());
				modelIn = new FileInputStream(MODEL_PATH + "en-sent.bin");
				BaseModel model = new SentenceModel(modelIn);
				sentenceDetector = new SentenceDetectorME((SentenceModel) model);
			}
			{
				System.out.println("en-token.bin:" + (new File(MODEL_PATH + "en-token.bin")).exists());
				modelIn = new FileInputStream(MODEL_PATH + "en-token.bin");
				BaseModel model = new TokenizerModel(modelIn);
				tokenizer = new TokenizerME((TokenizerModel) model);
			}
			{
				System.out.println("en-ner-person.bin:" + (new File(MODEL_PATH + "en-ner-person.bin")).exists());
				modelIn = new FileInputStream(MODEL_PATH + "en-ner-person.bin");
				BaseModel model = new TokenNameFinderModel(modelIn);
				nameFinder = new NameFinderME((TokenNameFinderModel) model);
			}
			{
				System.out.println("en-ner-location.bin:" + (new File(MODEL_PATH + "en-ner-location.bin")).exists());
				modelIn = new FileInputStream(MODEL_PATH + "en-ner-location.bin");
				BaseModel model = new TokenNameFinderModel(modelIn);
				placeFinder = new NameFinderME((TokenNameFinderModel) model);
			}
			{
				System.out.println("en-ner-date.bin:" + (new File(MODEL_PATH + "en-ner-date.bin")).exists());
				modelIn = new FileInputStream(MODEL_PATH + "en-ner-date.bin");
				BaseModel model = new TokenNameFinderModel(modelIn);
				timeFinder = new NameFinderME((TokenNameFinderModel) model);
			}
			{
				System.out.println("en-ner-money.bin:" + (new File(MODEL_PATH + "en-ner-money.bin")).exists());
				modelIn = new FileInputStream(MODEL_PATH + "en-ner-money.bin");
				BaseModel model = new TokenNameFinderModel(modelIn);
				currencyFinder = new NameFinderME((TokenNameFinderModel) model);
			}
			{
				System.out.println(
						"en-ner-organization.bin:" + (new File(MODEL_PATH + "en-ner-organization.bin")).exists());
				modelIn = new FileInputStream(MODEL_PATH + "en-ner-organization.bin");
				BaseModel model = new TokenNameFinderModel(modelIn);
				orgFinder = new NameFinderME((TokenNameFinderModel) model);
			}
			{
				System.out.println("en-parser-chunking:" + (new File(MODEL_PATH + "en-parser-chunking.bin")).exists());
				modelIn = new FileInputStream(MODEL_PATH + "en-parser-chunking.bin");
				BaseModel model = new ParserModel(modelIn);
				parser = ParserFactory.create((ParserModel) model);
			}

		} catch (Exception e) {
			System.out.println("Error in initialization: " + e);
		} finally {
			try {
				modelIn.close();
			} catch (Exception e2) {
			}
		}
	}

	public void processCorpus(MessageCorpus mc) {
		String corpus = mc.getCorpus();
		String[] statements = detectSentences(corpus);
		for (String statement : statements) {
			try {
				String[] names = detectObjects(statement, getNameFinder());
				String[] places = detectObjects(statement, getPlaceFinder());
				String[] times = detectObjects(statement, getTimeFinder());
				String[] currencies = detectObjects(statement, getCurrencyFinder());
				String[] orgs = detectObjects(statement, getOrgFinder());
				storeDetectedData(mc, "names", names);
				storeDetectedData(mc, "places", places);
				storeDetectedData(mc, "time", times);
				storeDetectedData(mc, "currency", currencies);
				storeDetectedData(mc, "org", orgs);
			} catch (Exception e) {
				e.printStackTrace();
			}

			mc.getParsed().addAll(parseStatement(statement));
		}
	}

	private void storeDetectedData(MessageCorpus mc, String type, String[] objects) {
		for (String data : objects) {
			mc.getDetections().add(new Detection(data, type));
		}
	}

	public List<Element> parseStatement(String statement) {
		Parse topParses[] = ParserTool.parseLine(statement, parser, 1);

		List<Element> response = new ArrayList<Element>();
		for (Parse parse : topParses) {
			for (Parse psc : parse.getChildren()) {
				response.add(processClause(psc));
			}
		}
		return response;
	}

	private Element processClause(Parse clause) {
		/**
		 * <pre>
		 * - find Subject
		 * - find Predicate
		 * - break Predicate
		 * </pre>
		 */
		Element element = null;

		if (clause.getChildCount() == 0) {

			element = new Element(clause.getType(), clause.getCoveredText());

		} else if ("1" == new String("1") && clause.getChildCount() == 1
				&& clause.getChildren()[0].getChildCount() == 1) {

			element = new Element(clause.getType(), clause.getCoveredText());

		} else {

			List<Element> elems = new ArrayList<Element>();
			Element e = null;
			for (Parse elem : clause.getChildren()) {
				e = processClause(elem);
				elems.add(e);
			}

			if (elems.size() == 1 && ("TK".equals(e.getType()))) {
				element = new Element(clause.getType(), e.getValue());
			} else {
				element = new Element(clause.getType(), elems);
			}

		}

		return element;
	}

	public String[] detectObjects(String sentence, NameFinderME finder) {
		String[] tokens = tokenize(sentence);
		Span nameSpans[] = finder.find(tokens);
		String[] detectedObjects = new String[nameSpans.length];
		for (int i = 0; i < nameSpans.length; i++) {
			Span span = nameSpans[i];
			detectedObjects[i] = "";
			for (int j = span.getStart(); j < span.getEnd(); j++) {
				detectedObjects[i] += tokens[j] + " ";
			}
			detectedObjects[i] = detectedObjects[i].trim();
		}
		finder.clearAdaptiveData();
		return detectedObjects;
	}

	public String[] tokenize(String corpus) {
		return tokenizer.tokenize(corpus);
	}

	public String[] detectSentences(String corpus) {
		String[] statements = sentenceDetector.sentDetect(corpus);
		return statements;
	}

	public SentenceDetectorME getSentenceDetector() {
		return sentenceDetector;
	}

	public Tokenizer getTokenizer() {
		return tokenizer;
	}

	public NameFinderME getNameFinder() {
		return nameFinder;
	}

	public NameFinderME getPlaceFinder() {
		return placeFinder;
	}

	public NameFinderME getTimeFinder() {
		return timeFinder;
	}

	public NameFinderME getCurrencyFinder() {
		return currencyFinder;
	}

	public NameFinderME getOrgFinder() {
		return orgFinder;
	}

	public Parser getParser() {
		return parser;
	}
}
