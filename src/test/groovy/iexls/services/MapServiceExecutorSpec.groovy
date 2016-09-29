package iexls.services

import iexls.DataReader
import iexls.converter.Convertible
import spock.lang.Specification

/**
 * Created by icarokume on 27/09/16.
 */
class MapServiceExecutorSpec extends Specification {

    def "test"() {
        given:
        def executor = new MapServiceExecutor(new SampleServiceFactory())
        def data = new DataReader(serviceName: 'SampleService',  headers: ['Name', 'Age'], rowValues: [['JJ', 13], ['KK', 10]])

        when:
        def result = executor.execute([data])

        then:
        result.success == 1
        result.fail == 1
        result.messages.size() == 1
        result.messages.first() == 'Error KK'
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

        def execute(Map instance) {
            if (instance.name == 'KK') {
                throw new RuntimeException('Error KK')
            }
            instance
        }
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
