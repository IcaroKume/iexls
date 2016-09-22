package iexls

/**
 * Created by icarokume on 21/09/16.
 */
class DataWriter {

    List headers

    List rowValues

    def getValue(String header, Integer row) {
        rowValues[row][headers.indexOf(header)]
    }

    Map headerStyle = [:]

    Map rowStyle = [:]

    String sheetName
}
