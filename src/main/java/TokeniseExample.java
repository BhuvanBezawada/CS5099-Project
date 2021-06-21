import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;

public class TokeniseExample {

    public static void main(String[] args) {
        StanfordCoreNLP stanfordCoreNLP = BasicPipelineExample.getPipeline();
        String text = "This is an example sentence. Another sentence for testing!";

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
        sentenceList.forEach(e -> System.out.println(e.sentiment()));

    }
}
