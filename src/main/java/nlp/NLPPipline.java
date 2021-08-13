package nlp;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

/**
 * NLP Pipeline
 * Code adapted from: https://www.youtube.com/watch?v=pUwNTGyxIOc&list=PLK0V_H0fCvPjKcUPjdXNZ6k4LueCJjsBN&index=3
 */
public class NLPPipline {

    // Instance variables
    private static StanfordCoreNLP stanfordCoreNLP;
    private static final Properties properties;
    private static final String PROPERTY_NAMES = "tokenize, ssplit, parse, pos, lemma, ner, sentiment";

    // Static initialisation of properties - only occurs once
    static {
        properties = new Properties();
        properties.setProperty("annotators", PROPERTY_NAMES);
    }

    /**
     * Get the NLP pipeline.
     * @return The NLP pipeline.
     */
    public static StanfordCoreNLP getPipeline() {
        if (stanfordCoreNLP == null) {
            stanfordCoreNLP = new StanfordCoreNLP(properties);
        }
        return stanfordCoreNLP;
    }
}

