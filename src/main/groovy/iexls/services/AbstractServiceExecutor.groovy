package iexls.services

import iexls.reader.DataReader
import iexls.converter.TransformerFactory

/**
 * Created by icarokume on 27/09/16.
 */
abstract class AbstractServiceExecutor {

    ServiceFactory serviceFactory

    TransformerFactory transformerFactory

    AbstractServiceExecutor(ServiceFactory serviceFactory, TransformerFactory transformerFactory) {
        this.serviceFactory = serviceFactory
        this.transformerFactory = transformerFactory
    }

    def execute(List<DataReader> dataReader) {
        Integer success = 0
        Integer fail = 0
        List<String> messages = []

        dataReader.each { data ->
            def ser = serviceFactory.serviceMap()[data.serviceName]
            if (!ser) {
                return  null
            }
            def service = serviceFactory.createService(ser.clazz)
            convert(data, ser.clazzDef, transformerFactory).each { instance ->
                try {
                    def result = service.invokeMethod(ser.method, instance)
                    success++
                    addSuccessMessage(messages, result, data.serviceName)
                } catch (Exception ex) {
                    fail++
                    addErrorWarningMessage(messages, ex, data.serviceName)
                }

            }
        }

        createServiceResult(success, fail, messages)
    }

    abstract convert(DataReader data, Class clazz, TransformerFactory transformerFactory)

    def createServiceResult(Integer success, Integer fail, List<String> messages) {
        new ServiceResult(success: success, fail: fail, messages: messages)
    }

    void addErrorWarningMessage(List<String> messages, Exception ex, String serviceName) {
        messages << ex.getMessage()
    }

    void addSuccessMessage(List<String> messages, def result, String serviceName) {

    }
}
