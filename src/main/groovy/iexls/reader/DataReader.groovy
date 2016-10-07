package iexls.reader

/**
 * Created by icarokume on 19/09/16.
 */
class DataReader {

    List headers

    List rowValues

    List<RowDescription> rowDescriptions

    String serviceName

    def getValue(String header, Integer row) {
        rowValues[row][headers.indexOf(header)]
    }
}
