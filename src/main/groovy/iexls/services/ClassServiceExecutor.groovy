package iexls.services

import iexls.reader.DataReader
import iexls.converter.DataConverter
import iexls.converter.TransformerFactory

/**
 * Created by icarokume on 27/09/16.
 */
class ClassServiceExecutor extends AbstractServiceExecutor {

    ClassServiceExecutor(ServiceFactory serviceFactory, TransformerFactory transformerFactory) {
        super(serviceFactory, transformerFactory)
    }

    @Override
    def convert(DataReader data, Class clazz, TransformerFactory transformerFactory) {
        return new DataConverter(transformerFactory).convert(data, clazz)
    }
}
