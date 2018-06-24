package org.apache.spark.ml.mleap.feature

import ml.combust.mleap.core.feature.PosTaggerCoreNLPModel
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.param.shared.{HasInputCol, HasOutputCol}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset}

class PosTaggerCoreNLPSparkTransformer (override val uid: String, val model: PosTaggerCoreNLPModel) extends Transformer with HasInputCol with HasOutputCol {
  def this(model: PosTaggerCoreNLPModel) = this(uid = Identifiable.randomUID("postagger_corenlp"), model = model)

  def setInputCol(value: String): this.type = set(inputCol, value)
  def setOutputCol(value: String): this.type = set(outputCol, value)

  @org.apache.spark.annotation.Since("2.0.0")
  override def transform(dataset: Dataset[_]): DataFrame = {
    val posTaggerCoreNLPUdf = udf {
      (labels: Seq[String]) => model(labels)
    }

    dataset.withColumn($(outputCol), posTaggerCoreNLPUdf(dataset($(inputCol))))
  }

  override def copy(extra: ParamMap): Transformer = copyValues(new PosTaggerCoreNLPSparkTransformer(uid, model), extra)

  @DeveloperApi
  override def transformSchema(schema: StructType): StructType = {
    require(schema($(inputCol)).dataType.isInstanceOf[ArrayType],
      s"Input column must be of type StringType but got ${schema($(inputCol)).dataType}")
    val inputFields = schema.fields
    require(!inputFields.exists(_.name == $(outputCol)),
      s"Output column ${$(outputCol)} already exists.")
    StructType(schema.fields :+ StructField($(outputCol), ArrayType(StringType)))
  }
}

