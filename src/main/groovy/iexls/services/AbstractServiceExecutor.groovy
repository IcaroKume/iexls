package iexls.services

import iexls.converter.TransformerFactory
import iexls.reader.DataReader
import iexls.reader.RowDescription

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
        Integer row = 0
        Integer success = 0
        Integer fail = 0
        List<String> messages = []

        dataReader.each { data ->
            def ser = serviceFactory.serviceMap()[data.serviceName]
            if (!ser) {
                return null
            }
            def service = serviceFactory.createService(ser.clazz)
            data.rowValues.size().times { rowNumber ->
                try {
                    def instance = convert data, ser.clazzDef, transformerFactory, rowNumber
                    if (instance) {
                        def result = service.invokeMethod(ser.method, instance)
                        success++
                        addSuccessMessage(messages, result, data.serviceName, data.rowDescriptions[row])
                    }
                } catch (Exception ex) {
                    fail++
                    addErrorWarningMessage(messages, ex, data.serviceName, data.rowDescriptions[row])
                }
                row++
            }
        }

        createServiceResult(success, fail, messages)
    }

    abstract convert(DataReader data, Class clazz, TransformerFactory transformerFactory, Integer rowNumber)

    def createServiceResult(Integer success, Integer fail, List<String> messages) {
        new ServiceResult(success: success, fail: fail, messages: messages)
    }

    void addErrorWarningMessage(List<String> messages, Exception ex, String serviceName, RowDescription rowDescription) {
        messages << "${rowDescription.sheetName} ${rowDescription.rowNumber}: ${ex.getMessage()}"
    }

    void addSuccessMessage(List<String> messages, def result, String serviceName, RowDescription rowDescription) {

    }
}
