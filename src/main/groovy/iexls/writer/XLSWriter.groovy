package iexls.writer

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

        CellStyle headerStyle = createStyleHeader(wb, data.headerStyle)

        def countHeaders = data.headers.size()
        countHeaders.times {
            def cell = header.createCell(it)
            cell.setCellStyle(headerStyle)
            cell.setCellValue(data.headers[it])

            def comment = data.comments ? data.comments[it] : null

            if (comment) {
                Drawing drawing = sheet.createDrawingPatriarch()
                CreationHelper createHelper = wb.getCreationHelper()

                ClientAnchor anchor = createHelper.createClientAnchor();
                anchor.setCol1(cell.getColumnIndex());
                anchor.setCol2(cell.getColumnIndex()+comment.right);
                anchor.setRow1(header.getRowNum());
                anchor.setRow2(header.getRowNum()+comment.bottom);

                org.apache.poi.ss.usermodel.Comment cellComment = drawing.createCellComment(anchor)

                RichTextString str = createHelper.createRichTextString(comment.comment);
                cellComment.setString(str);
                cell.setCellComment(cellComment);
            }

        }

        CellStyle defaultRowStyle = createStyleRow(wb, data.rowStyle)
        CellStyle dateRowStyle = createDateStyleRow(wb, data.rowStyle)

        // rows
        data.rowValues.each { List r ->
            def row = sheet.createRow(i++)
            def j = 0
            r.each {
                def cell = row.createCell(j++)

                def rowStyle = it in Date ? dateRowStyle : defaultRowStyle

                cell.setCellStyle(rowStyle)
                cell.setCellValue(it)
            }
        }

        countHeaders.times {
            sheet.autoSizeColumn(it)
        }

        wb.write(output);

    }

    protected createStyleHeader(Workbook wb, IexlsCellStyle cellStyle) {
        createStyle(wb, cellStyle, null)
    }

    protected createStyleRow(Workbook wb, IexlsCellStyle cellStyle) {
        createStyle(wb, cellStyle, null)
    }

    protected createDateStyleRow(Workbook wb, IexlsCellStyle cellStyle) {
        createStyle(wb, cellStyle, Date)
    }

    protected CellStyle createStyle(Workbook wb, IexlsCellStyle cellStyle, Class clazz) {
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

        if (clazz == Date) {
            style.setDataFormat(
                    createHelper.createDataFormat().getFormat("m/d/yy"))
        }

        style
    }

}
