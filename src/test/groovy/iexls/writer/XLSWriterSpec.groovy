package iexls.writer

import iexls.reader.DataReader
import iexls.reader.XLSReader
import spock.lang.Specification

/**
 * Created by icarokume on 21/09/16.
 */
class XLSWriterSpec extends Specification {

    def "test"() {
        given:
        def string = 'Luiz'
        def number = 17
        def date = new Date()
        def bool = true

        def dataWriter = new DataWriter(
                sheetName: 'sheet',
                headers: ['Nome', 'Idade', 'Nascimento', 'Brasileiro'],
                rowValues: [[string, number, date, bool]]
        )
        def output = new ByteArrayOutputStream()
        def writer = new XLSWriter(output)

        when:
        writer.write(dataWriter)
        def reader = new XLSReader(new ByteArrayInputStream(output.toByteArray()))
        List datas = reader.read('Pessoa')

        then:
        DataReader data = datas.first()
        data.sheetName == 'sheet'
        data.headers[0] == 'Nome'
        data.headers[1] == 'Idade'
        data.headers[2] == 'Nascimento'
        data.headers[3] == 'Brasileiro'
        data.getValue('Nome', 0) == string
        data.getValue('Idade', 0) == number
        data.getValue('Nascimento', 0) == date
        data.getValue('Brasileiro', 0) == bool
    }

    def "test create file"() {
        given:
        def string = 'Luiz Joao Pedro Paulo de Souza Pereira Forte'
        def number = 17
        def date = new Date()
        def bool = true

        def dataWriter = new DataWriter(
                headers: ['Nome', 'Idade', 'Nascimento', 'Brasileiro'],
                comments: [new Comment(comment: 'Nome completo da pessoa', right: 4, bottom: 4), null],
                rowValues: [[string, number, date, bool]]
        )
        def output = new FileOutputStream('build/testout.xls')
        def writer = new XLSWriter(output)

        when:
        writer.write(dataWriter)
        output.close()

        def reader = new XLSReader(new FileInputStream('build/testout.xls'))
        List datas = reader.read('Pessoa')

        then:
        DataReader data = datas.first()
        data.headers[0] == 'Nome'
        data.headers[1] == 'Idade'
        data.headers[2] == 'Nascimento'
        data.headers[3] == 'Brasileiro'
        data.getValue('Nome', 0) == string
        data.getValue('Idade', 0) == number
        data.getValue('Nascimento', 0) == date
        data.getValue('Brasileiro', 0) == bool
    }
}
