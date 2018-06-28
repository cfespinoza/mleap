package org.apache.spark.ml.bundle.extension.ops.feature

import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl._
import ml.combust.bundle.op.{OpModel, OpNode}
import ml.combust.mleap.core.feature.PosTaggerCoreNLPModel
import org.apache.spark.ml.bundle.SparkBundleContext
import org.apache.spark.ml.mleap.feature.PosTaggerCoreNLPSparkTransformer

class PosTaggerCoreNLPOp extends OpNode[SparkBundleContext, PosTaggerCoreNLPSparkTransformer, PosTaggerCoreNLPModel] {
  /** Type class for the underlying model.
    */
  override val Model: OpModel[SparkBundleContext, PosTaggerCoreNLPModel] = new OpModel[SparkBundleContext, PosTaggerCoreNLPModel] {
    /** Class of the model.
      */
    override val klazz: Class[PosTaggerCoreNLPModel] = classOf[PosTaggerCoreNLPModel]

    /** Get the name of the model.
      *
      * @return name of the model
      */
    override def opName: String = "core_nlp_mleap_transformer"

    /** Store the model.
      *
      * Store all standard parameters to the model's attribute list.
      * Store all non-standard parameters like a decision tree to files.
      *
      * Attributes saved to the writable model will be serialized for you
      * to JSON or Protobuf depending on the selected [[ml.combust.bundle.serializer.SerializationFormat]].
      *
      * @param model   writable model to store model attributes in
      * @param obj     object to be stored in Bundle.ML
      * @param context bundle context for custom types
      * @return writable model to be serialized
      */
    override def store(model: Model, obj: PosTaggerCoreNLPModel)(implicit context: BundleContext[SparkBundleContext]): Model = {
      val lang = obj.lang
      val selectedTags = obj.selectedTags
      model.withValue("language", Value.string(lang)).withValue("selectedTags", Value.string(selectedTags))
    }

    /** Load the model.
      *
      * Load all standard parameters from the model attributes.
      * Load all non-standard parameters like decision trees from the custom files.
      *
      * @param model   model and attributes read from Bundle.ML
      * @param context bundle context for custom types
      * @return reconstructed ML model from the model and context
      */
    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): PosTaggerCoreNLPModel = {
      val lang = model.value("language")
      val selectedTags = model.value("selectedTags")
      PosTaggerCoreNLPModel(lang.getString, selectedTags.getString)
    }
  }
  /** Class of the node.
    */
  override val klazz: Class[PosTaggerCoreNLPSparkTransformer] = classOf[PosTaggerCoreNLPSparkTransformer]

  /** Get the unique name for this node.
    *
    * @param node node object
    * @return unique name of the node
    */
  override def name(node: PosTaggerCoreNLPSparkTransformer): String = node.uid

  /** Get the underlying model of the node.
    *
    * @param node node object
    * @return underlying model object
    */
  override def model(node: PosTaggerCoreNLPSparkTransformer): PosTaggerCoreNLPModel = node.model

  /** Get the shape of the node.
    *
    * @param node node object
    * @return shape of the node
    */
  override def shape(node: PosTaggerCoreNLPSparkTransformer)(implicit context: BundleContext[SparkBundleContext]): NodeShape = NodeShape().withStandardIO(node.getInputCol, node.getOutputCol)

  /** Load a node from Bundle.ML data.
    *
    * @param node    read-only node for attributes
    * @param model   deserialized model for the node
    * @param context bundle context for custom types
    * @return deserialized node object
    */
  override def load(node: Node, model: PosTaggerCoreNLPModel)(implicit context: BundleContext[SparkBundleContext]): PosTaggerCoreNLPSparkTransformer = {
    new PosTaggerCoreNLPSparkTransformer(uid = node.name, model = model).
      setInputCol(node.shape.standardInput.name).
      setOutputCol(node.shape.standardOutput.name)
  }
}