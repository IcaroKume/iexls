package iexls.converter

import iexls.reader.DataReader
import iexls.writer.DataWriter

/**
 * Created by icarokume on 26/09/16.
 */
class DataConverter {

    TransformerFactory transformerFactory

    DataConverter(TransformerFactory transformerFactory) {
        this.transformerFactory = transformerFactory
    }

    public <T extends Convertible> List<T> convert(DataReader data, Class<T> clazz) {
        def rows = []

        def size = data.rowValues.size()
        size.times { Integer row ->
            T instance = clazz.newInstance()
            def headerMap = instance.mapHearder()
            headerMap.entrySet().each { entry ->
                setInstanceValue(entry, data, instance, instance, row)
            }
            rows << instance
        }

        rows
    }

    public <T extends Convertible> List<Map> convertToMap(DataReader data, Class<T> clazz) {
        def rows = []

        def size = data.rowValues.size()
        size.times { Integer row ->
            T type = clazz.newInstance()
            Map instance = [:]
            def headerMap = type.mapHearder()
            headerMap.entrySet().each { entry ->
                setInstanceValue(entry, data, instance, type, row)
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
                getRowValue(row[header.key])
            }
        }

        data
    }

    def getRowValue(def value) {
        transformerFactory.reverse(value)
    }

    void setInstanceValue(Map.Entry entry, DataReader data, def instance, Object type, Integer row) {
        def value = data.getValue(entry.value, row)
        def targetClass = type.class.getField(entry.key).type
        instance[entry.key] = transformerFactory.transform(value, targetClass)
    }

}
