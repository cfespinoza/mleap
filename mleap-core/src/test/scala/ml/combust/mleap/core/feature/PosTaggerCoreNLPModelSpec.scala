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

    val tokens = "Simplemente es una prueba"
    val tags = model.apply(tokens)

    System.out.println(tags)
    it ("has returned a filled list") {
      assert(tags.length > 0)
    }
  }

  describe("get tags from english") {
    val model = PosTaggerCoreNLPModel("en")

    val tokens = "This is a test"
    val tags = model.apply(tokens)

    System.out.println(tags)
    it ("has returned a filled list") {
      assert(tags.length > 0)
    }
  }
}
