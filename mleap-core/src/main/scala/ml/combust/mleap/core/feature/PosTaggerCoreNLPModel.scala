package ml.combust.mleap.core.feature

import java.util.Properties

import edu.stanford.nlp.simple._
import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types._

import scala.collection.JavaConversions



case class PosTaggerCoreNLPModel(lang: String = "es") extends Model {

  val props: Properties = new Properties()
  props.setProperty("tokenize.language", s"$lang")
  props.setProperty("ssplit.isOneSentence", "true")
  props.setProperty("tokenize.class", "PTBTokenizer")

  def apply(sentenceStr: String): Seq[String] = {
    val sentence: Sentence = new Sentence(sentenceStr, props)
    val tags: java.util.List[String] = sentence.posTags()
    JavaConversions.asScalaBuffer(tags).toList
  }

  override def inputSchema: StructType = StructType("input" -> ScalarType.String).get

  override def outputSchema: StructType = StructType("output" -> ListType.String).get
}


