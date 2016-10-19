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
        Integer success = 0
        Integer fail = 0
        List<Map<Integer, Integer>> rowsWithError = []
        List<String> messages = []

        dataReader.size().times { data ->
            def ser = serviceFactory.serviceMap()[dataReader[data].serviceName]
            if (!ser) {
                return null
            }
            def service = serviceFactory.createService(ser.clazz)
            dataReader[data].rowValues.size().times { rowNumber ->
                try {
                    def instance = convert dataReader[data], ser.clazzDef, transformerFactory, rowNumber
                    if (instance) {
                        def result = service.invokeMethod(ser.method, instance)
                        success++
                    }
                } catch (Exception ex) {
                    fail++
                    def errorMessage = getErrorMessage(ex, dataReader[data].serviceName, dataReader[data].rowDescriptions[rowNumber])
                    messages << errorMessage
                    rowsWithError << new Tuple(data, rowNumber, errorMessage)

                }
            }
        }

        createServiceResult(success, fail, messages, rowsWithError)
    }

    abstract convert(DataReader data, Class clazz, TransformerFactory transformerFactory, Integer rowNumber)

    def createServiceResult(Integer success, Integer fail, List<String> messages, List<Map<Integer, Integer>> rowsWithError) {
        new ServiceResult(success: success, fail: fail, messages: messages, rowsWithError: rowsWithError)
    }

    String getErrorMessage(Exception ex, String serviceName, RowDescription rowDescription) {
        "${rowDescription.sheetName} ${rowDescription.rowNumber}: ${ex.getMessage()}"
    }

}
