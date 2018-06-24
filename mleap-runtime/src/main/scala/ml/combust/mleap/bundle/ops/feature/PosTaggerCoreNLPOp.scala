package ml.combust.mleap.bundle.ops.feature

import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, _}
import ml.combust.bundle.op.OpModel
import ml.combust.mleap.bundle.ops.MleapOp
import ml.combust.mleap.core.feature.PosTaggerCoreNLPModel
import ml.combust.mleap.runtime.MleapContext
import ml.combust.mleap.runtime.transformer.feature.PosTaggerCoreNLPMleapTransformer

class PosTaggerCoreNLPOp extends MleapOp[PosTaggerCoreNLPMleapTransformer, PosTaggerCoreNLPModel]{
  /** Type class for the underlying model.
    */
  override val Model: OpModel[MleapContext, PosTaggerCoreNLPModel] = new OpModel[MleapContext, PosTaggerCoreNLPModel] {
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
    override def store(model: Model, obj: PosTaggerCoreNLPModel)(implicit context: BundleContext[MleapContext]): Model = {
      val lang = obj.lang
      model.withValue("language", Value.string(lang))
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
    override def load(model: Model)(implicit context: BundleContext[MleapContext]): PosTaggerCoreNLPModel = {
      val lang = model.value("language")
      PosTaggerCoreNLPModel(lang.getString)
    }
  }

  /** Get the underlying model of the node.
    *
    * @param node node object
    * @return underlying model object
    */
  override def model(node: PosTaggerCoreNLPMleapTransformer): PosTaggerCoreNLPModel = node.model
}

