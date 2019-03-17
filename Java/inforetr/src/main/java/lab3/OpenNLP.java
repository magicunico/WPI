package lab3;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OpenNLP {

    public static String LANG_DETECT_MODEL = "inforetr/models/langdetect-183.bin";
    public static String TOKENIZER_MODEL = "inforetr/models/en-token.bin";
    public static String SENTENCE_MODEL = "inforetr/models/en-sent.bin";
    public static String POS_MODEL = "inforetr/models/en-pos-maxent.bin";
    public static String CHUNKER_MODEL = "inforetr/models/en-chunker.bin";
    public static String LEMMATIZER_DICT = "inforetr/models/en-lemmatizer.dict";
    public static String NAME_MODEL = "inforetr/models/en-ner-person.bin";
    public static String ENTITY_XYZ_MODEL = "inforetr/models/en-ner-xyz.bin";

    public static void main(String[] args) throws IOException
    {
        OpenNLP openNLP = new OpenNLP();
        openNLP.run();
    }

    public void run() throws IOException
    {

//        languageDetection();
//         tokenization();
//         sentenceDetection();
//         posTagging();
//         lemmatization();
//         stemming();
//         chunking();
         nameFinding();
    }

    private void languageDetection() throws IOException
    {
        Path root = Paths.get(System.getProperty("user.dir")).resolve(LANG_DETECT_MODEL);
        File modelFile = root.toFile();
        System.out.println("FILE: " + modelFile.getAbsolutePath());

        LanguageDetectorModel model = new LanguageDetectorModel(modelFile);
        LanguageDetectorME languageDetectorME = new LanguageDetectorME(model);


        String text = "";
//        text = "cats";
//         text = "cats like milk";
//         text = "Many cats like milk because in some ways it reminds them of their
        // mother's milk.";
        // text = "The two things are not really related. Many cats like milk because in
        // some ways it reminds them of their mother's milk.";
        text = "The two things are not really related. Many cats like milk because in some ways it reminds them of their mother's milk. "
				+ "It is rich in fat and protein. They like the taste. They like the consistency . "
				+ "The issue as far as it being bad for them is the fact that cats often have difficulty digesting milk and so it may give them "
				+ "digestive upset like diarrhea, bloating and gas. After all, cow's milk is meant for baby calves, not cats. "
				+ "It is a fortunate quirk of nature that human digestive systems can also digest cow's milk. But humans and cats are not cows.";
        // text = "Many cats like milk because in some ways it reminds them of their
        // mother's milk. Le lait n'est pas forcï¿½ment mauvais pour les chats";
        // text = "Many cats like milk because in some ways it reminds them of their
        // mother's milk. Le lait n'est pas forcï¿½ment mauvais pour les chats. "
        // + "Der Normalfall ist allerdings der, dass Salonlï¿½wen Milch weder brauchen
        // noch gut verdauen kï¿½nnen.";

        Language language = languageDetectorME.predictLanguage(text);

        System.out.println("!!##!#!#!#!##       TUTUAJ");
        System.out.println(language.getLang() + " : " + language.getConfidence());
    }

    private void tokenization() throws IOException
    {
        TokenizerME tokenizerME = new TokenizerME(
                new TokenizerModel(Paths.get(System.getProperty("user.dir")).resolve(TOKENIZER_MODEL).toFile())
        );

        String text = "";

        text = "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
                + "but there may have been instances of domestication as early as the Neolithic from around 9500 years ago (7500 BC).";
		/*text = "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
				+ "but there may have been instances of domestication as early as the Neolithic from around 9,500 years ago (7,500 BC).";
		text = "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
		 + "but there may have been instances of domestication as early as the Neolithic from around 9 500 years ago ( 7 500 BC).";*/

		String[] string = tokenizerME.tokenize(text);
		double[] doubles = tokenizerME.getTokenProbabilities();

        for (int i = 0; i < string.length; i++)
        {
            System.out.println(string[i] + " : " + doubles[i]);
        }
    }

    private void sentenceDetection() throws IOException
    {
        SentenceModel sentenceModel = new SentenceModel(Paths.get(System.getProperty("user.dir")).resolve(SENTENCE_MODEL).toFile());
        SentenceDetectorME sentenceDetectorME = new SentenceDetectorME(sentenceModel);



        String text = "";

//        text = "Hi. How are you? Welcome to OpenNLP. "
//                + "We provide multiple built-in methods for Natural Language Processing.";
		text = "Hi. How are you?! Welcome to OpenNLP? "
				+ "We provide multiple built-in methods for Natural Language Processing.";
		text = "Hi. How are you? Welcome to OpenNLP.?? "
				+ "We provide multiple . built-in methods for Natural Language Processing.";
		text = "The interrobang, also known as the interabang (often represented by ?! or !?), "
				+ "is a nonstandard punctuation mark used in various written languages. "
				+ "It is intended to combine the functions of the question mark (?), or interrogative point, "
				+ "and the exclamation mark (!), or exclamation point, known in the jargon of printers and programmers as a \"bang\". ";

		//text = "You want some fuck.";

		String[] strings = sentenceDetectorME.sentDetect(text);
		double[] doubles = sentenceDetectorME.getSentenceProbabilities();


        for (int i = 0; i < strings.length; i++)
        {
            System.out.println(strings[i] + " : " + doubles[i]);
        }

    }

    private void posTagging() throws IOException {
        POSModel posModel = new POSModel(Paths.get(System.getProperty("user.dir")).resolve(POS_MODEL).toFile());
        POSTaggerME posTaggerME = new POSTaggerME(posModel);

        String[] sentence = new String[0];
//        sentence = new String[] { "Cats", "like", "milk" };
//		sentence = new String[]{"Cat", "is", "white", "like", "milk"};
//		sentence = new String[] { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
//				"built-in", "methods", "for", "Natural", "Language", "Processing" };
		sentence = new String[] { "She", "put", "the", "big", "knives", "on", "the", "table" };

		String[] tags = posTaggerME.tag(sentence);

        for (String tag : tags)
        {
            System.out.println("Tag:" + tag);
        }

    }

    private void lemmatization() throws IOException
    {
        DictionaryLemmatizer dictionaryLemmatizer = new DictionaryLemmatizer(Paths.get(System.getProperty("user.dir")).resolve(LEMMATIZER_DICT).toFile());
        PorterStemmer porterStemmer = new PorterStemmer();

        String[] text = new String[0];
        text = new String[] { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
                "built-in", "methods", "for", "Natural", "Language", "Processing" };
        String[] tags = new String[0];
        tags = new String[] { "NNP", "WRB", "VBP", "PRP", "VB", "TO", "VB", "PRP", "VB", "JJ", "JJ", "NNS", "IN", "JJ",
                "NN", "VBG" };

        String[] strings = dictionaryLemmatizer.lemmatize(text, tags);


        for (int i = 0; i < strings.length; i++)
        {
            String string2 = porterStemmer.stem(text[i]);
            System.out.println("Stem:" + string2);

            System.out.println("Lemma: " + strings[i]);
        }
    }

    private void stemming()
    {
        System.out.println("STEMMING");
        PorterStemmer porterStemmer = new PorterStemmer();

        String[] sentence = new String[0];
        sentence = new String[] { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
                "built-in", "methods", "for", "Natural", "Language", "Processing" };

        for (String s : sentence)
        {
            String stem = porterStemmer.stem(s);
            System.out.println(stem);
        }

    }

    private void chunking() throws IOException
    {
        ChunkerModel chunkerModel = new ChunkerModel(Paths.get(System.getProperty("user.dir")).resolve(CHUNKER_MODEL).toFile());
        ChunkerME chunkerME = new ChunkerME(chunkerModel);

        String[] sentence = new String[0];
        sentence = new String[] { "She", "put", "the", "big", "knives", "on", "the", "table" };

        String[] tags = new String[0];
        tags = new String[] { "PRP", "VBD", "DT", "JJ", "NNS", "IN", "DT", "NN" };

        String[] strings = chunkerME.chunk(sentence, tags);

        for (String string : strings)
        {
            System.out.println(string);
        }
    }

    private void nameFinding() throws IOException
    {
        TokenNameFinderModel tokenNameFinderModel = new TokenNameFinderModel(Paths.get(System.getProperty("user.dir")).resolve(NAME_MODEL).toFile());
        NameFinderME nameFinderME = new NameFinderME(tokenNameFinderModel);

        String text = "he idea of using computers to search for relevant pieces of information was popularized in the article "
                + "As We May Think by Vannevar Bush in 1945. It would appear that Bush was inspired by patents "
                + "for a 'statistical machine' - filed by Emanuel Goldberg in the 1920s and '30s - that searched for documents stored on film. "
                + "The first description of a computer searching for information was described by Holmstrom in 1948, "
                + "detailing an early mention of the Univac computer. Automated information retrieval systems were introduced in the 1950s: "
                + "one even featured in the 1957 romantic comedy, Desk Set. In the 1960s, the first large information retrieval research group "
                + "was formed by Gerard Salton at Cornell. By the 1970s several different retrieval techniques had been shown to perform "
                + "well on small text corpora such as the Cranfield collection (several thousand documents). Large-scale retrieval systems, "
                + "such as the Lockheed Dialog system, came into use early in the 1970s.";

        String[] s = text.split(" ");
        Span[] spans = nameFinderME.find(s);

        for (Span span : spans)
        {
            for (int i = span.getStart(); i < span.getEnd(); i++)
            {
                System.out.print(s[i] + " ");
            }
            System.out.println("");
        }

    }

}