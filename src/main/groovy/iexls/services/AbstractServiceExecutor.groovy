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

    def execute(DataReader data) {
        def ser = serviceFactory.serviceMap()[data.serviceName]
        if (!ser) {
            return  null
        }
        def service = serviceFactory.createService(ser.clazz)
        convert(data, ser.clazzDef).collect {
            service.invokeMethod(ser.method, it)
        }

    }

    abstract convert(DataReader data, Class clazz)
}
