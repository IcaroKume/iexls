package iexls.reader

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator
import org.apache.poi.xssf.usermodel.XSSFWorkbook

/**
 * Created by icarokume on 19/09/16.
 */
//http://poi.apache.org/spreadsheet/quick-guide.html#Iterator
//https://bitbucket.org/jmine_team/jmine-tec
//https://gist.github.com/jkeam/4083570
class XLSReader {

    FormulaEvaluator formulaEvaluator

    Workbook workbook

    XLSReader(InputStream input) {
         workbook = WorkbookFactory.create(input);

        if(workbook instanceof HSSFWorkbook) {
            formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
        } else if (workbook instanceof XSSFWorkbook) {
            formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
        } else {
            throw new RuntimeException("Invalid workbook type, for now we only support XSSF or HSSF workbooks.");
        }
    }

    List<DataReader> read (String serviceName) {
        def sheetData = workbook.sheetIterator().collect {
            extractData it, serviceName
        }
        sheetData.findAll {it != null}
    }

    List<DataReader> read () {
        def sheetData = workbook.sheetIterator().collect {
            extractData it
        }
        sheetData.findAll {it != null}.flatten()
    }

    private DataReader extractData(Sheet sheet, String serviceName) {
        def rows = sheet.rowIterator()
        if (!rows.hasNext()) {
            return null
        }

        def headerRow = rows.next()

        def headers = extractRow headerRow

        def rowValues = rows.collect {
            extractRow it
        }

        new DataReader(serviceName: serviceName, headers: headers, rowValues: rowValues)
    }

    private List extractData(Sheet sheet) {
        def rows = sheet.rowIterator()
        if (!rows.hasNext()) {
            return null
        }

        List data = []
        DataReader currentData
        String currentServiceName
        def headers

        rows.each {
            def firstCell = it.getCell(0)
            def secondCell = it.getCell(1)
            if ((firstCell == null || firstCell.getCellType() == Cell.CELL_TYPE_BLANK) && secondCell?.getCellType() != Cell.CELL_TYPE_BLANK ) {
                headers = extractRow it
            } else if ((firstCell && firstCell.getCellType() != Cell.CELL_TYPE_BLANK) && (secondCell && secondCell.getCellType() != Cell.CELL_TYPE_BLANK) ) {
                def row = extractRow(it)

                def serviceName = row.first()
                row.remove(0)
                if (serviceName != currentServiceName) {
                    currentServiceName = serviceName
                    currentData = new DataReader(serviceName: currentServiceName, headers: headers, rowValues: [row])
                    data << currentData
                } else {
                    currentData.rowValues << row
                }
            }
        }

        data
    }

    private List extractRow(Row row) {
        row.collect {
            getValue it
        }
    }

    private def getValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString()
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue()
                } else {
                    return cell.getNumericCellValue()
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue()
            case Cell.CELL_TYPE_ERROR:
                return '' // subir uma expection???
            case Cell.CELL_TYPE_FORMULA:
                return resolveFormula(cell)
            default:
                return ''
        }
    }

    private def resolveFormula(Cell cell) {
        def cellValue = formulaEvaluator.evaluate(cell)
        if (!cellValue) {
            return ''
        }
        switch (cellValue.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cellValue.getStringValue()?.trim();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return DateUtil.getJavaDate(cellValue.getNumberValue())
                } else {
                    return cell.getNumericCellValue()
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return Boolean.toString(cellValue.getBooleanValue());
            case Cell.CELL_TYPE_ERROR:
                return ''
            default:
                return ''
        }
    }
}
