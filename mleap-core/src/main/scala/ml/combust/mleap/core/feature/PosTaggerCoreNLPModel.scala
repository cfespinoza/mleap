package ml.combust.mleap.core.feature

import edu.stanford.nlp.tagger.maxent.MaxentTagger
import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types._


case class PosTaggerCoreNLPModel(lang: String = "es", selectedTags: String = "") extends Model {

  val englishTagKeys: Map[String, Seq[String]] = Map("adjectives" -> Seq("JJ", "JJR", "JJS"),
    "conjunctions" -> Seq("CC"),
    "determiners" -> Seq("DT", "WDT", "PDT"),
    "punctuation" -> Seq(),
    "interjections" -> Seq("UH"),
    "nouns" -> Seq("NN","NNS", "NNP", "NNPS"),
    "pronouns" -> Seq("PRP","PRP$", "WP", "WP$"),
    "adverbs" -> Seq("RB","RBR", "RBS", "WRB"),
    "prepositions" -> Seq("IN", "TO", "RP"),
    "verbs" -> Seq("EX","MD", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ"),
    "dates" -> Seq(),
    "numerals" -> Seq("CD"),
    "other" -> Seq("FW", "LS", "POS", "SYM"))

  val spanishTagKeys: Map[String, Seq[String]] = Map("adjectives" -> Seq("ao0000", "aq0000"),
    "conjunctions" -> Seq("cc", "cs"),
    "determiners" -> Seq("da0000","dd0000","de0000","di0000","dn0000","do0000","dp0000","dt0000"),
    "punctuation" -> Seq("f0","faa","fat","fc","fca","fct","fd","fe","fg","fh","fia","fit","fp","fpa","fpt","fra","frc","fs","ft","fx","fz"),
    "interjections" -> Seq("i"),
    "nouns" -> Seq("nc00000","nc0n000","nc0p000","nc0s000","np00000"),
    "pronouns" -> Seq("p0000000","pd000000","pe000000","pi000000","pn000000","pp000000","pr000000","pt000000","px000000"),
    "adverbs" -> Seq("rg","rn"),
    "prepositions" -> Seq("sp000"),
    "verbs" -> Seq("va00000","vag0000","vaic000","vaif000","vaii000","vaip000","vais000","vam0000","van0000","vap0000","vasi000","vasp000","vmg0000","vmic000","vmif000","vmii000","vmip000","vmis000","vmm0000","vmn0000","vmp0000","vmsi000","vmsp000","vsg0000","vsic000","vsif000","vsii000","vsip000","vsis000","vsm0000","vsn0000","vsp0000","vssf000","vssi000","vssp000"),
    "dates" -> Seq("w"),
    "numerals" -> Seq("z0","zm","zu"),
    "other" -> Seq("word"))

  val (mapByLanguage, model) = getMapAndModelByLang(lang)
  val selectedTagList: Seq[String] = getSelectedTagList(mapByLanguage, selectedTags)

  def apply(sentenceStr: String): String = {
    if (selectedTagList.length > 0){
      // there are selected tags
      val tagger: MaxentTagger = new MaxentTagger(model)
      val taggedStr: String = tagger.tagString(sentenceStr)
      val taggedStrSeq: Seq[String] = taggedStr.split(" ").toSeq
      val filteredTaggedStrSeq: Seq[String] = taggedStrSeq.filter(tagged => selectedTagList.indexOf(tagged.split("_").last) > -1)
      val proccessedTaggedStr: Seq[String] = filteredTaggedStrSeq.map(_.split("_").head)
      proccessedTaggedStr.mkString(" ")
    }
    else {
      // there is not any selected tag, the string should be returned without filter nor proccess
      sentenceStr
    }
  }

  def getMapAndModelByLang(lang: String):  (Map[String, Seq[String]], String)   = {
    if (lang == "es"){
      return (spanishTagKeys, "edu/stanford/nlp/models/pos-tagger/spanish/spanish-distsim.tagger")
    }
    else if (lang == "en") {
      return (englishTagKeys, "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger")
    }
    return (Map[String, Seq[String]](), "")
  }

  def getSelectedTagList(keysMapByLang: Map[String, Seq[String]], selectedTags: String): Seq[String] = {
    val keysTagSelected: Seq[String] = selectedTags.split(",").toSeq
    if (keysTagSelected.length == 1 && keysTagSelected(0) == ""){
      // default value -> "", empty selected tags
      return Seq[String]()
    }
    val selectedKeysMap: Map[String, Seq[String]] = keysMapByLang filterKeys keysTagSelected.toSet
    val listTags = selectedKeysMap.flatMap(_._2)
    listTags.toSeq
  }

  override def inputSchema: StructType = StructType("input" -> ScalarType.String).get

  override def outputSchema: StructType = StructType("output" -> ScalarType.String).get
}


