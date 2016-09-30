package iexls.converter

import iexls.reader.DataReader
import spock.lang.Specification

/**
 * Created by icarokume on 26/09/16.
 */
class DataConverterSpec extends Specification {

    def "test convert from data"() {
        given:
        def converter = new DataConverter(new TransformerFactory([]))
        def data = new DataReader(headers: ['Name', 'Age'], rowValues: [['JJ', 13], ['KK', 10]])

        when:
        def result = converter.convert(data, Sample)

        then:
        result.size() == 2
        result.find {it.name == 'JJ' && it.age == 13}
        result.find {it.name == 'KK' && it.age == 10}
    }

    def "test convert from data to map"() {
        given:
        def converter = new DataConverter(new TransformerFactory([]))
        def data = new DataReader(headers: ['Name', 'Age'], rowValues: [['JJ', 13], ['KK', 10]])

        when:
        def result = converter.convert(data, Sample)

        then:
        result.size() == 2
        result.find {it.name == 'JJ' && it.age == 13}
        result.find {it.name == 'KK' && it.age == 10}
    }

    def "test convert to data"() {
        given:
        def converter = new DataConverter(new TransformerFactory([]))
        def samples = [new Sample(name: 'JJ', age: 13), new Sample(name: 'KK', age: 10)]

        when:
        def result = converter.convert(samples)

        then:
        result.headers.size() == 2
        result.headers == ['Name', 'Age']
        result.getValue('Name', 0) == 'JJ'
        result.getValue('Age', 0) == 13
        result.getValue('Name', 1) == 'KK'
        result.getValue('Age', 1) == 10
    }

    def "test convert to data with converter"() {
        given:
        def converter = new DataConverter(new TransformerFactory([new SampleDataTransformer()]))
        def data = new DataReader(headers: ['Name', 'Age'], rowValues: [['JJ', '13']])

        when:
        def result = converter.convert(data, Sample)

        then:
        result.size() == 1
        result.find {it.name == 'JJ' && it.age == 13}
    }

    class SampleDataTransformer implements DataTransformer {

        @Override
        def transform(def value) {
            Integer.valueOf(value)
        }

        @Override
        def reverse(def Object value) {
            return null
        }

        @Override
        Class from() {
            return String
        }

        @Override
        Class to() {
            return Integer
        }
    }

    public class Sample implements Convertible {

        public String name

        public Integer age

        @Override
        Map mapHearder() {
            return [name: 'Name', age: 'Age']
        }
    }
}
