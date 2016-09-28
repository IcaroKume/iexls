package iexls

import spock.lang.Specification
/**
 * Created by icarokume on 04/09/16.
 */
class XLSReaderSpec extends Specification {

    def "test xlsx"() {
        given:
        def reader = new XLSReader(new FileInputStream(this.class.getResource('test1.xlsx').file))

        when:
        def result = reader.read('Pessoa')
        def data = result.first()

        then:
        data.serviceName == 'Pessoa'

        data.headers.find {it == 'Nome'}
        data.headers.find {it == 'Idade'}
        data.headers.find {it == 'Nascimento'}
        data.headers.find {it == 'Brasileiro'}
        data.headers.find {it == 'Soma'}
        data.headers.find {it == 'Soma data'}

        data.rowValues.size() == 2

        data.getValue('Nome', 0) == 'John'
        data.getValue('Idade', 0) == 17
        data.getValue('Nascimento', 0) == new Date(99, 04, 11)
        data.getValue('Brasileiro', 0) == true
        data.getValue('Soma', 0) == 3
        data.getValue('Soma data', 0) == new Date(99, 04, 14)

        data.getValue('Nome', 1) == 'Maria'
        data.getValue('Idade', 1) == 18
        data.getValue('Nascimento', 1) == new Date(99, 04, 14)
        data.getValue('Brasileiro', 1) == false
        data.getValue('Soma', 1) == 5
        data.getValue('Soma data', 1) == new Date(99, 04, 18)
    }

    def "test xls"() {
        given:
        def reader = new XLSReader(new FileInputStream(this.class.getResource('test2.xls').file))

        when:
        def result = reader.read('Pessoa')
        def data = result.first()

        then:
        data.serviceName == 'Pessoa'

        data.headers.find {it == 'Nome'}
        data.headers.find {it == 'Idade'}
        data.headers.find {it == 'Nascimento'}
        data.headers.find {it == 'Brasileiro'}
        data.headers.find {it == 'Soma'}
        data.headers.find {it == 'Soma data'}

        data.rowValues.size() == 2

        data.getValue('Nome', 0) == 'John'
        data.getValue('Idade', 0) == 17
        data.getValue('Nascimento', 0) == new Date(99, 04, 11)
        data.getValue('Brasileiro', 0) == true
        data.getValue('Soma', 0) == 3
        data.getValue('Soma data', 0) == new Date(99, 04, 14)

        data.getValue('Nome', 1) == 'Maria'
        data.getValue('Idade', 1) == 18
        data.getValue('Nascimento', 1) == new Date(99, 04, 14)
        data.getValue('Brasileiro', 1) == false
        data.getValue('Soma', 1) == 5
        data.getValue('Soma data', 1) == new Date(99, 04, 18)
    }

}
