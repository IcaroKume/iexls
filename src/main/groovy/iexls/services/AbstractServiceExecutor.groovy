package iexls.services

import iexls.DataReader

/**
 * Created by icarokume on 27/09/16.
 */
abstract class AbstractServiceExecutor {

    ServiceFactory serviceFactory

    AbstractServiceExecutor(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory
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
            convert(data, ser.clazzDef).each { instance ->
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

    abstract convert(DataReader data, Class clazz)

    def createServiceResult(Integer success, Integer fail, List<String> messages) {
        new ServiceResult(success: success, fail: fail, messages: messages)
    }

    void addErrorWarningMessage(List<String> messages, Exception ex, String serviceName) {
        messages << ex.getMessage()
    }

    void addSuccessMessage(List<String> messages, def result, String serviceName) {

    }
}
