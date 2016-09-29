package iexls.converter

/**
 * Created by icarokume on 28/09/16.
 */
class TransformerFactory {

    List<DataTransformer> transformers

    TransformerFactory(List<DataTransformer> transformers) {
        this.transformers = transformers
    }

    def transform(def value, Class targetClass) {
        DataTransformer transformer =
                transformers.find {it.from() == value.class && it.to() == targetClass}

        if (!transformer) {
            return  value
        }

        transformer.transform(value)
    }
}
