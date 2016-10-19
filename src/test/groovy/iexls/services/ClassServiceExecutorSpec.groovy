package iexls.services

import iexls.converter.TransformerFactory
import iexls.reader.DataReader
import iexls.converter.Convertible
import iexls.reader.RowDescription
import spock.lang.Specification

/**
 * Created by icarokume on 27/09/16.
 */
class ClassServiceExecutorSpec extends Specification {

    def "test"() {
        given:
        def executor = new ClassServiceExecutor(new SampleServiceFactory(), new TransformerFactory([]))
        def data = new DataReader(
                serviceName: 'SampleService',
                headers: ['Name', 'Age', 'BLA'],
                rowValues: [['JJ', 13], ['KK', 10], [null, null, 'BLA']],
                rowDescriptions: [new RowDescription(rowNumber: 2, sheetName: 'name'),
                                  new RowDescription(rowNumber: 3, sheetName: 'name'),
                                  new RowDescription(rowNumber: 4, sheetName: 'name')]
        )

        when:
        def result = executor.execute([data])

        then:
        result.success == 1
        result.fail == 1
        result.messages.size() == 1
        result.messages.first() == 'name 3: Error KK'
        result.rowsWithError == [new Tuple(0, 1, 'name 3: Error KK')]
    }

    class SampleServiceFactory implements ServiceFactory {
        @Override
        Map serviceMap() {
            return ['SampleService': [clazz: SampleService, method: 'execute', clazzDef: Sample]]
        }

        @Override
        def createService(Class serviceClass) {
            return serviceClass.newInstance()
        }
    }

    class SampleService {

        def execute(Sample instance) {
            if (instance.name == 'KK') {
                throw new RuntimeException('Error KK')
            }
            instance
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
            return [:]
        }
    }
}
