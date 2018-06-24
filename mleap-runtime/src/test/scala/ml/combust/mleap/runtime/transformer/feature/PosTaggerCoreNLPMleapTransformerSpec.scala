package ml.combust.mleap.runtime.transformer.feature

import ml.combust.mleap.core.feature.PosTaggerCoreNLPModel
import ml.combust.mleap.core.types._
import ml.combust.mleap.runtime.{LeapFrame, LocalDataset, Row}
import org.scalatest.FunSpec

class PosTaggerCoreNLPMleapTransformerSpec extends FunSpec {
  val schema = StructType(Seq(StructField("tokenized_instance", ScalarType.String))).get
  // val dataset = LocalDataset(Seq(Row(Seq("Esto", "es", "una", "simple", "prueba")), Row(Seq("Pyxis", "gusta", "y", "funciona", "muy", "bien"))))
  val dataset = LocalDataset(Seq(Row("Esto es una simple prueba"), Row("Pyxis gusta y funciona muy bien")))
  val frame = LeapFrame(schema, dataset)

  val transformer = PosTaggerCoreNLPMleapTransformer(
    shape = NodeShape().withStandardInput("tokenized_instance").withStandardOutput("tagged_sentence"),
    model = PosTaggerCoreNLPModel("es")
  )

  describe ("#transform") {
    it("tagged a tokenized sentence") {
      val data = transformer.transform(frame).get.dataset
      System.out.println(data)
      assert(data.toList.length == 2)
    }
  }

  describe("input/output schema") {
    it("has the correct inputs and outputs") {
      assert(transformer.schema.fields ==
        Seq(StructField("tokenized_instance", ScalarType.String),
          StructField("tagged_sentence", ListType.String)))
    }
  }

}
