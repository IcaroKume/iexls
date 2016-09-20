package iexls

import spock.lang.Specification
/**
 * Created by icarokume on 04/09/16.
 */
class XLSReaderSpec extends Specification {

    def "test"() {
        given:
        def reader = new XLSReader(new FileInputStream(this.class.getResource('test1.xlsx').file))

        when:
        def result = reader.read()
        def data = result.first()

        then:
        data.headers.find {it == 'Nome'}

        data.rowValues.size() == 1
        data.getValue('Nome', 0) == 'John'
    }

}
