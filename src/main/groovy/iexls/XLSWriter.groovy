package iexls

import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.CreationHelper
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.WorkbookUtil

/**
 * Created by icarokume on 21/09/16.
 */
class XLSWriter {

    def write(DataWriter data, OutputStream output) {
        Workbook wb = new HSSFWorkbook()
        CreationHelper createHelper = wb.getCreationHelper()

        String safeName = WorkbookUtil.createSafeSheetName(data.sheetName)

        Sheet sheet = wb.createSheet(safeName)

        // header
        int i = 0
        def header = sheet.createRow(i++)

        int j = 0
        data.headers.each {
            def cell = header.createCell(j++)
            cell.setCellValue(it)
        }

        // rows
        data.rowValues.each { List r ->
            def row = sheet.createRow(i++)
            r.each {
                def cell = row.createCell(i++)
                if (it in Date) {
                    CellStyle cellStyle = wb.createCellStyle()
                    cellStyle.setDataFormat(
                            createHelper.createDataFormat().getFormat("m/d/yy"))
                    cell.setCellStyle(cellStyle)
                }

                cell.setCellValue(it)
            }
        }

        wb.write(output);

    }

}
