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

    public <T extends Convertible> T convert(DataReader data, Class<T> clazz, Integer rowNumber) {
        T instance = clazz.newInstance()
        def headerMap = instance.mapHeader()
        headerMap.entrySet().each { entry ->
            setInstanceValue(entry, data, instance, instance, rowNumber)
        }
        instance
    }

    public <T extends Convertible> Map convertToMap(DataReader data, Class<T> clazz, Integer rowNumber) {
        T type = clazz.newInstance()
        Map instance = [:]
        def headerMap = type.mapHeader()
        headerMap.entrySet().each { entry ->
            setInstanceValue(entry, data, instance, type, rowNumber)
        }
        instance
    }

    public <T extends Convertible> DataWriter convert(List<T> rows, String sheetName) {
        if (!rows) {
            return null
        }

        def data = new DataWriter()

        data.sheetName = sheetName
        data.headers = rows.first().mapHeader().values().asList()

        data.comments = data.headers.collect {
            rows.first().mapCommet()[it]
        }

        data.rowValues = rows.collect { row ->
            row.mapHeader().entrySet().collect { header ->
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
        instance[entry.key] = transformerFactory.transform(value, targetClass, instance, data.rowDescriptions[row], entry.value)
    }

}
