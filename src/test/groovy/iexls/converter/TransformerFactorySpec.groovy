package iexls.converter

import iexls.reader.RowDescription
import spock.lang.Specification

/**
 * Created by icarokume on 29/09/16.
 */
class TransformerFactorySpec extends Specification {

    def "test without data transformer"() {
        given:
        def factory = new TransformerFactory([])

        when:
        def result = factory.transform(1, String, null, null, null)

        then:
        result == 1
        result != '1'
    }

    def "test with data transformer"() {
        given:
        def factory = new TransformerFactory([new SampleDataTransformer()])

        when:
        def result = factory.transform(1, String, null, null, null)

        then:
        result == '1'
        result != 1
    }

    def "test null with data transformer"() {
        given:
        def factory = new TransformerFactory([new SampleDataTransformer()])

        when:
        def result = factory.transform(null, String, null, null, null)

        then:
        result == null
    }

    def "test reverse without data transformer"() {
        given:
        def factory = new TransformerFactory([])

        when:
        def result = factory.reverse('1')

        then:
        result == '1'
        result != 1
    }

    def "test reverse with data transformer"() {
        given:
        def factory = new TransformerFactory([new SampleDataTransformer()])

        when:
        def result = factory.reverse('1')

        then:
        result == 1
        result != '1'
    }

    def "test null reverse with data transformer"() {
        given:
        def factory = new TransformerFactory([new SampleDataTransformer()])

        when:
        def result = factory.reverse(null)

        then:
        result == null
    }

    class SampleDataTransformer implements DataTransformer {

        @Override
        def transform(def value, def instance, RowDescription rowDescription, String column) {
            value.toString()
        }

        @Override
        def reverse(def value) {
            return new Integer(value)
        }

        @Override
        Class from() {
            return Integer
        }

        @Override
        Class to() {
            return String
        }
    }
}
