package ml.combust.mleap.core.feature

import ml.combust.mleap.core.types.{ListType, ScalarType, StructField}
import org.scalatest.FunSpec

class PosTaggerCoreNLPModelSpec extends FunSpec  {
  describe("core nlp model") {
    val model = PosTaggerCoreNLPModel()

    it("has the right input schema") {
      assert(model.inputSchema.fields ==
        Seq(StructField("input", ScalarType.String)))
    }

    it("has the right output schema") {
      assert(model.outputSchema.fields ==
        Seq(StructField("output", ListType.String)))
    }
  }

  describe("get tags from spanish") {
    val model = PosTaggerCoreNLPModel("es")

    val tokens = "Estoy en la tienda de harry potter en la sección de slytherin y se acerca  una chica española y empieza a decir 'yo creo que soy slytherin, yo soy mala'. Madre mía la gente se piensa que ser slytherin es ser malo. Además si no sabes que casa eres, probablemente no seas slytherin"
    val tags = model.apply(tokens)

    System.out.println(tags)
    it ("has returned a filled list") {
      assert(tags.length > 0)
    }
  }

  describe("get tags from english") {
    val model = PosTaggerCoreNLPModel("en")

    val tokens = "From Wayne to Kane: England excel after ditching Mr Big Stuff mentality"
    val tags = model.apply(tokens)

    System.out.println(tags)
    it ("has returned a filled list") {
      assert(tags.length > 0)
    }
  }
}
