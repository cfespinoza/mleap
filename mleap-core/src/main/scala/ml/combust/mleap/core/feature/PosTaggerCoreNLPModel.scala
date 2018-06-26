package ml.combust.mleap.core.feature

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.simple._
import edu.stanford.nlp.tagger.maxent.MaxentTagger
import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types._

import scala.collection.JavaConversions


// , selectedTags: String
case class PosTaggerCoreNLPModel(lang: String = "es") extends Model {

  val props: Properties = new Properties()
  props.setProperty("tokenize.language", s"$lang")
  props.setProperty("annotators", "tokenize,ssplit,pos")
  props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/spanish/spanish-ud.tagger")
  props.setProperty("ssplit.isOneSentence", "true")
  props.setProperty("tokenize.class", "PTBTokenizer")
  var modelStr: String = "edu/stanford/nlp/models/pos-tagger/spanish/spanish.tagger"
  if (lang == "en") {
    props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger")
    modelStr = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger"
  }

  def apply(sentenceStr: String): Seq[String] = {
    /*
    val sentencePipeline: StanfordCoreNLP = new StanfordCoreNLP(props)
    val document: Annotation  = new Annotation(sentenceStr)
    sentencePipeline.annotate(document)
    document.get(CoreAnnotations.PartOfSpeechAnnotation.class)
    */

    val tagger: MaxentTagger = new MaxentTagger(modelStr)
    val taggedStr: String = tagger.tagString(sentenceStr)
    val tags: Seq[String] = taggedStr.split(" ").toSeq
    tags
}

override def inputSchema: StructType = StructType("input" -> ScalarType.String).get

override def outputSchema: StructType = StructType("output" -> ListType.String).get
}


