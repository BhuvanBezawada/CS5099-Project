package nlp;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;

public class TokeniseExample {

    public static void main(String[] args) {
        StanfordCoreNLP stanfordCoreNLP = BasicPipelineExample.getPipeline();
        String text = "This is an excellent piece of work. This is a good piece of work. Your work can be improved. The code needs some improvement. This is not a good quality piece of work.";

        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);

        // Tokenize
//        List<CoreLabel> coreLabelList = coreDocument.tokens();
//        coreLabelList.forEach(e -> {
//            System.out.println(e.originalText());
//            System.out.println(e.tag());
//        });

        // Sentence and sentiment
        List<CoreSentence> sentenceList = coreDocument.sentences();
        sentenceList.forEach(e -> System.out.println( e + " - " + e.sentiment()));

    }
}
