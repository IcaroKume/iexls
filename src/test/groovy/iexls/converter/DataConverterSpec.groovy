package iexls.converter

import iexls.reader.DataReader
import iexls.reader.RowDescription
import iexls.writer.Comment
import spock.lang.Specification

/**
 * Created by icarokume on 26/09/16.
 */
class DataConverterSpec extends Specification {

    def "test convert from data"() {
        given:
        def converter = new DataConverter(new TransformerFactory([]))
        def data = new DataReader(headers: ['Name', 'Age'], rowValues: [['JJ', 13], ['KK', 10]], rowDescriptions: [new RowDescription(), new RowDescription()])

        when:
        def result1 = converter.convert(data, Sample, 0)
        def result2 = converter.convert(data, Sample, 1)

        then:
        result1.name == 'JJ' && result1.age == 13
        result2.name == 'KK' && result2.age == 10
    }

    def "test convert from data to map"() {
        given:
        def converter = new DataConverter(new TransformerFactory([]))
        def data = new DataReader(headers: ['Name', 'Age'], rowValues: [['JJ', 13], ['KK', 10]], rowDescriptions: [new RowDescription(), new RowDescription()])

        when:
        def result1 = converter.convert(data, Sample, 0)
        def result2 = converter.convert(data, Sample, 1)

        then:
        result1.name == 'JJ' && result1.age == 13
        result2.name == 'KK' && result2.age == 10
    }

    def "test convert to data"() {
        given:
        def converter = new DataConverter(new TransformerFactory([]))
        def samples = [new Sample(name: 'JJ', age: 13), new Sample(name: 'KK', age: 10)]

        when:
        def result = converter.convert(samples, 'sheet')

        then:
        result.sheetName == 'sheet'
        result.headers.size() == 2
        result.headers == ['Name', 'Age']

        result.comments.size() == 2
        result.comments[0].comment == 'Nome completo'
        result.comments[0].right == 2
        result.comments[0].bottom == 2
        result.comments[1] == null

        result.getValue('Name', 0) == 'JJ'
        result.getValue('Age', 0) == 13
        result.getValue('Name', 1) == 'KK'
        result.getValue('Age', 1) == 10
    }

    def "test convert to data with converter"() {
        given:
        def converter = new DataConverter(new TransformerFactory([new SampleDataTransformer()]))
        def data = new DataReader(headers: ['Name', 'Age'], rowValues: [['JJ', '13']], rowDescriptions: [new RowDescription()])

        when:
        def result = converter.convert(data, Sample, 0)

        then:
        result.name == 'JJ' && result.age == 13
    }

    class SampleDataTransformer implements DataTransformer {

        @Override
        def transform(def value, def instance, RowDescription rowDescription, String column) {
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
        Map mapHeader() {
            return [name: 'Name', age: 'Age']
        }

        @Override
        Map mapCommet() {
            return ['Name': new Comment(comment:  'Nome completo', bottom: 2, right: 2)]
        }
    }
}
