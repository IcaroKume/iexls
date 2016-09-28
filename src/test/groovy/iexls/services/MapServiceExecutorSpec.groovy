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
        def result = executor.execute(data)

        then:
        result.size() == 2
        result.find {it.name == 'JJ' && it.age == 13}
        result.find {it.name == 'KK' && it.age == 10}
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
