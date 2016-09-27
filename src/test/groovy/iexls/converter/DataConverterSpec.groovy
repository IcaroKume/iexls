package iexls.converter

import iexls.DataReader
import spock.lang.Specification

/**
 * Created by icarokume on 26/09/16.
 */
class DataConverterSpec extends Specification {

    def "test convert from data"() {
        given:
        def converter = new DataConverter()
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
        def converter = new DataConverter()
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

    class Sample implements Convertible {

        String name

        Integer age

        @Override
        Map mapHearder() {
            return [name: 'Name', age: 'Age']
        }
    }
}
