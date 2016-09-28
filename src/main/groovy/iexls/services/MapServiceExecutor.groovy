package iexls.services

import iexls.DataReader
import iexls.converter.DataConverter

/**
 * Created by icarokume on 27/09/16.
 */
class MapServiceExecutor extends AbstractServiceExecutor {

    MapServiceExecutor(ServiceFactory serviceFactory) {
        super(serviceFactory)
    }

    @Override
    def convert(DataReader data, Class clazz) {
        return new DataConverter().convertToMap(data, clazz)
    }
}
