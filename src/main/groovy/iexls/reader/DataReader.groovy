package iexls.reader

/**
 * Created by icarokume on 19/09/16.
 */
class DataReader {

    List headers

    List rowValues

    List<RowDescription> rowDescriptions

    List cellErrors

    String serviceName

    String sheetName

    def getValue(String header, Integer row) {
        rowValues[row][headers.indexOf(header)]
    }
}
