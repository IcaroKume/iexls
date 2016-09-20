package iexls

/**
 * Created by icarokume on 19/09/16.
 */
class Data {

    List headers

    List rowValues

    def getValue(String header, Integer row) {
        rowValues[row][headers.indexOf(header)]
    }
}
