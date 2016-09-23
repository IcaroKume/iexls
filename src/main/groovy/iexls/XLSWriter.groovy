package iexls

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.WorkbookUtil

/**
 * Created by icarokume on 21/09/16.
 */
class XLSWriter {

    OutputStream output

    XLSWriter(OutputStream output) {
        this.output = output
    }

    def write(DataWriter data) {
        Workbook wb = new HSSFWorkbook()

        String safeName = WorkbookUtil.createSafeSheetName(data.sheetName)

        Sheet sheet = wb.createSheet(safeName)

        // header
        int i = 0
        def header = sheet.createRow(i++)

        int j = 0
        CellStyle headerStyle = createStyleHeader(wb, data.headerStyle)
        data.headers.each {
            def cell = header.createCell(j)
            cell.setCellStyle(headerStyle)
            cell.setCellValue(it)
            sheet.autoSizeColumn(j++)
        }

        // rows
        data.rowValues.each { List r ->
            def row = sheet.createRow(i++)
            j = 0
            r.each {
                def cell = row.createCell(j++)
                CellStyle rowStyle = createStyleRow(wb, data.rowStyle, it)

                cell.setCellStyle(rowStyle)
                cell.setCellValue(it)
            }
        }

        wb.write(output);

    }

    protected createStyleHeader(Workbook wb, IexlsCellStyle cellStyle) {
        createStyle(wb, cellStyle, null)
    }

    protected createStyleRow(Workbook wb, IexlsCellStyle cellStyle, def value) {
        createStyle(wb, cellStyle, value)
    }

    protected CellStyle createStyle(Workbook wb, IexlsCellStyle cellStyle, def value) {
        CreationHelper createHelper = wb.getCreationHelper()

        def style = wb.createCellStyle()
        style.setFillForegroundColor(cellStyle.backgroundColor)
        style.setFillPattern(CellStyle.SOLID_FOREGROUND)
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        if (value in Date) {
            style.setDataFormat(
                    createHelper.createDataFormat().getFormat("m/d/yy"))
        }

        style
    }

}
