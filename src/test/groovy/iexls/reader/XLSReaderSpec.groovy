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

        data.rowDescriptions.size() == 2
        data.rowDescriptions[0].sheetName == 'Sheet1'
        data.rowDescriptions[0].rowNumber == 2
        data.rowDescriptions[1].sheetName == 'Sheet1'
        data.rowDescriptions[1].rowNumber == 3
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

        data.rowDescriptions.size() == 2
        data.rowDescriptions[0].sheetName == 'Sheet1'
        data.rowDescriptions[0].rowNumber == 2
        data.rowDescriptions[1].sheetName == 'Sheet1'
        data.rowDescriptions[1].rowNumber == 3
    }

    def "test xls without some columns"() {
        given:
        def reader = new XLSReader(new FileInputStream(this.class.getResource('../test4.xls').file))

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
        data.getValue('Idade', 1) == null
        data.getValue('Nascimento', 1) == new Date(99, 04, 18)
        data.getValue('Brasileiro', 1) == null
        data.getValue('Soma', 1) == null
        data.getValue('Soma data', 1) == new Date(99, 04, 21)
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

        first.rowDescriptions.size() == 2
        first.rowDescriptions[0].sheetName == 'Sheet1'
        first.rowDescriptions[0].rowNumber == 2
        first.rowDescriptions[1].sheetName == 'Sheet1'
        first.rowDescriptions[1].rowNumber == 3

        def second = result[1]
        second.serviceName == 'Incomum'
        second.headers.find {it == 'Nome'}
        second.headers.find {it == 'Idade'}
        second.rowValues.size() == 1
        second.getValue('Nome', 0) == 'KKK'
        second.getValue('Idade', 0) == 110

        second.rowDescriptions.size() == 1
        second.rowDescriptions[0].sheetName == 'Sheet1'
        second.rowDescriptions[0].rowNumber == 4

        def third = result[2]
        third.serviceName == 'Raro'
        third.headers.find {it == 'Sobre'}
        third.rowValues.size() == 1
        third.getValue('Sobre', 0) == 'Magic'

        third.rowDescriptions.size() == 1
        third.rowDescriptions[0].sheetName == 'Sheet1'
        third.rowDescriptions[0].rowNumber == 7

        def forth = result[3]
        forth.serviceName == 'Comum'
        forth.headers.find {it == 'Nome'}
        forth.headers.find {it == 'Idade'}
        forth.rowValues.size() == 1
        forth.getValue('Nome', 0) == 'Luiz'
        forth.getValue('Idade', 0) == 33

        forth.rowDescriptions.size() == 1
        forth.rowDescriptions[0].sheetName == 'Sheet1'
        forth.rowDescriptions[0].rowNumber == 10
    }

    def "test xls errors"() {
        given:
        def reader = new XLSReader(new FileInputStream(this.class.getResource('../test-cell-error.xls').file))

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

        data.rowValues.size() == 1

        data.getValue('Nome', 0) == 'John'
        data.getValue('Idade', 0) == 'aaa'
        data.getValue('Nascimento', 0) == new Date(99, 04, 11)
        data.getValue('Brasileiro', 0) == true
        data.getValue('Soma', 0) == null
        data.getValue('Soma data', 0) == new Date(99, 04, 14)

        data.rowDescriptions.size() == 1
        data.rowDescriptions[0].sheetName == 'Sheet1'
        data.rowDescriptions[0].rowNumber == 2

        data.cellErrors.size() == 1
        data.cellErrors.contains('E2')
    }

    def "test test-cell-after-header-limit"() {
        given:
        def reader = new XLSReader(new FileInputStream(this.class.getResource('../test-cell-after-header-limit.xls').file))

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

        data.rowValues[0].size() == 6
        data.getValue('Nome', 0) == 'John'
        data.getValue('Idade', 0) == 17
        data.getValue('Nascimento', 0) == new Date(99, 04, 11)
        data.getValue('Brasileiro', 0) == true
        data.getValue('Soma', 0) == 3
        data.getValue('Soma data', 0) == new Date(99, 04, 14)

        data.rowValues[1].size() == 6
        data.getValue('Nome', 1) == 'Maria'
        data.getValue('Idade', 1) == 18
        data.getValue('Nascimento', 1) == new Date(99, 04, 14)
        data.getValue('Brasileiro', 1) == false
        data.getValue('Soma', 1) == 5
        data.getValue('Soma data', 1) == new Date(99, 04, 18)

        data.rowDescriptions.size() == 2
        data.rowDescriptions[0].sheetName == 'Sheet1'
        data.rowDescriptions[0].rowNumber == 2
        data.rowDescriptions[1].sheetName == 'Sheet1'
        data.rowDescriptions[1].rowNumber == 3
    }
}
