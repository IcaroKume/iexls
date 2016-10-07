package iexls.converter

import iexls.reader.RowDescription

/**
 * Created by icarokume on 28/09/16.
 */
interface DataTransformer {

    def transform(def value, def instance, RowDescription rowDescription)

    def reverse(def value)

    Class from()

    Class to()
}
