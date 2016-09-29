package iexls.writer

import org.apache.poi.ss.usermodel.IndexedColors

/**
 * Created by icarokume on 21/09/16.
 */
class DataWriter {

    List headers

    List rowValues

    def getValue(String header, Integer row) {
        rowValues[row][headers.indexOf(header)]
    }

    IexlsCellStyle headerStyle = new IexlsCellStyle(backgroundColor: IndexedColors.GREY_25_PERCENT.getIndex())

    IexlsCellStyle rowStyle = new IexlsCellStyle(backgroundColor: IndexedColors.WHITE.getIndex())

    String sheetName
}
