package ml.combust.mleap.runtime.transformer.feature

import ml.combust.mleap.core.feature.PosTaggerCoreNLPModel
import ml.combust.mleap.core.types.NodeShape
import ml.combust.mleap.runtime.function.UserDefinedFunction
import ml.combust.mleap.runtime.transformer.{SimpleTransformer, Transformer}

case class PosTaggerCoreNLPMleapTransformer(override val uid: String = Transformer.uniqueName("core_nlp_mleap_transformer"),
                                            override val shape: NodeShape,
                                            override val model: PosTaggerCoreNLPModel) extends SimpleTransformer{

  override val exec: UserDefinedFunction = (tokenized_instance: String) => model(tokenized_instance)

}

