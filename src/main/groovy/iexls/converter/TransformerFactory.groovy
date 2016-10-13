package iexls.converter

import iexls.reader.RowDescription

/**
 * Created by icarokume on 28/09/16.
 */
class TransformerFactory {

    List<DataTransformer> transformers

    TransformerFactory(List<DataTransformer> transformers) {
        this.transformers = transformers
    }

    def transform(def value, Class targetClass, def instance, RowDescription rowDescription, String column) {
        if (value == null) {
            return value
        }

        DataTransformer transformer =
                transformers.find { it.from() == value.class && it.to() == targetClass }

        if (!transformer) {
            return value
        }

        transformer.transform(value, instance, rowDescription, column)
    }

    def reverse(def value) {
        if (value == null) {
            return value
        }

        DataTransformer transformer =
                transformers.find { it.to() == value.class }

        if (!transformer) {
            return value
        }

        transformer.reverse(value)
    }
}
