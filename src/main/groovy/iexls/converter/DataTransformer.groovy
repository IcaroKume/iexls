package iexls.converter

/**
 * Created by icarokume on 28/09/16.
 */
interface DataTransformer {

    def transform(def value)

    Class from()

    Class to()
}
