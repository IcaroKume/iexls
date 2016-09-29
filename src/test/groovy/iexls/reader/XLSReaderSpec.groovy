package iexls.reader

import spock.lang.Specification

/**
 * Created by icarokume on 04/09/16.
 */
class XLSReaderSpec extends Specification {

    def "test xlsx"() {
        given:
        def reader = new XLSReader(new FileInputStream(this.class.getResource('../test1.xlsx').file))

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
        def reader = new XLSReader(new FileInputStream(this.class.getResource('../test2.xls').file))

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

    def "test xls multiple services"() {
        given:
        def reader = new XLSReader(new FileInputStream(this.class.getResource('../test3.xls').file))

        when:
        def result = reader.read()

        then:
        result.size() == 4
        def first = result.first()
        first.serviceName == 'Comum'
        first.headers.find {it == 'Nome'}
        first.headers.find {it == 'Idade'}
        first.rowValues.size() == 2
        first.getValue('Nome', 0) == 'John'
        first.getValue('Idade', 0) == 17
        first.getValue('Nome', 1) == 'Maria'
        first.getValue('Idade', 1) == 18

        def second = result[1]
        second.serviceName == 'Incomum'
        second.headers.find {it == 'Nome'}
        second.headers.find {it == 'Idade'}
        second.rowValues.size() == 1
        second.getValue('Nome', 0) == 'KKK'
        second.getValue('Idade', 0) == 110

        def third = result[2]
        third.serviceName == 'Raro'
        third.headers.find {it == 'Sobre'}
        third.rowValues.size() == 1
        third.getValue('Sobre', 0) == 'Magic'

        def forth = result[3]
        forth.serviceName == 'Comum'
        forth.headers.find {it == 'Nome'}
        forth.headers.find {it == 'Idade'}
        forth.rowValues.size() == 1
        forth.getValue('Nome', 0) == 'Luiz'
        forth.getValue('Idade', 0) == 33
    }

}
