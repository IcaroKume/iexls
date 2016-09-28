package iexls.converter

import iexls.DataReader
import iexls.DataWriter

/**
 * Created by icarokume on 26/09/16.
 */
class DataConverter {

    public <T extends Convertible> List<T> convert(DataReader data, Class<T> clazz) {
        def rows = []

        def size = data.rowValues.size()
        size.times { row ->
            T instance = clazz.newInstance()
            def headerMap = instance.mapHearder()
            headerMap.entrySet().each { entry ->
                instance[entry.key] = data.getValue(entry.value, row)
            }
            rows << instance
        }

        rows
    }

    public <T extends Convertible> List<Map> convertToMap(DataReader data, Class<T> clazz) {
        def rows = []

        def size = data.rowValues.size()
        size.times { row ->
            T type = clazz.newInstance()
            Map instance = [:]
            def headerMap = type.mapHearder()
            headerMap.entrySet().each { entry ->
                instance[entry.key] = data.getValue(entry.value, row)
            }
            rows << instance
        }

        rows
    }

    public <T extends Convertible> DataWriter convert(List<T> rows) {
        if (!rows) {
            return null
        }

        def data = new DataWriter()

        data.headers = rows.first().mapHearder().values().asList()
        data.rowValues = rows.collect { row ->
            row.mapHearder().entrySet().collect { header ->
                row[header.key]
            }
        }

        data
    }
}
