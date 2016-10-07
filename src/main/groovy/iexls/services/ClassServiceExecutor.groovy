package iexls.services

import iexls.converter.DataConverter
import iexls.converter.TransformerFactory
import iexls.reader.DataReader

/**
 * Created by icarokume on 27/09/16.
 */
class ClassServiceExecutor extends AbstractServiceExecutor {

    ClassServiceExecutor(ServiceFactory serviceFactory, TransformerFactory transformerFactory) {
        super(serviceFactory, transformerFactory)
    }

    @Override
    def convert(DataReader data, Class clazz, TransformerFactory transformerFactory, Integer rowNumber) {
        return new DataConverter(transformerFactory).convert(data, clazz, rowNumber)
    }
}
