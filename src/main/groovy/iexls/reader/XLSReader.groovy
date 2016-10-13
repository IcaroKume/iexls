package iexls.reader

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.CellReference
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

        if (workbook instanceof HSSFWorkbook) {
            formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
        } else if (workbook instanceof XSSFWorkbook) {
            formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
        } else {
            throw new RuntimeException("Invalid workbook type, for now we only support XSSF or HSSF workbooks.");
        }
    }

    List<DataReader> read(String serviceName) {
        def sheetData = workbook.sheetIterator().collect {
            extractData it, serviceName
        }
        sheetData.findAll { it != null }
    }

    List<DataReader> read() {
        def sheetData = workbook.sheetIterator().collect {
            extractData it
        }
        sheetData.findAll { it != null }.flatten()
    }

    private DataReader extractData(Sheet sheet, String serviceName) {
        def cellErrors = []
        def rows = sheet.rowIterator()
        if (!rows.hasNext()) {
            return null
        }

        def headerRow = rows.next()

        def headers = extractRow headerRow, cellErrors

        if (isEmpty(headers)) {
            return null
        }

        def rowValues = []
        def rowDescriptions = []

        rows.each {
            def newRow = extractRow(it, cellErrors)
            if (!isEmpty(newRow)) {
                rowValues << newRow
                rowDescriptions << new RowDescription(rowNumber: it.rowNum + 1, sheetName: sheet.sheetName)
            }
        }

        new DataReader(
                sheetName: sheet.sheetName,
                serviceName: serviceName,
                headers: headers,
                rowValues: rowValues,
                rowDescriptions: rowDescriptions,
                cellErrors: cellErrors
        )
    }

    private boolean isEmpty(List row) {
        row.count { it != null } == 0
    }

    private List extractData(Sheet sheet) {
        def cellErrors = []
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
            if ((firstCell == null || firstCell.getCellType() == Cell.CELL_TYPE_BLANK) && secondCell?.getCellType() != Cell.CELL_TYPE_BLANK) {
                headers = extractRow it, cellErrors
                headers.remove(0)
            } else if ((firstCell && firstCell.getCellType() != Cell.CELL_TYPE_BLANK) && (secondCell && secondCell.getCellType() != Cell.CELL_TYPE_BLANK)) {
                def row = extractRow(it, cellErrors)
                def rowDescription = new RowDescription(rowNumber: it.rowNum + 1, sheetName: sheet.sheetName)

                def serviceName = row.first()
                row.remove(0)
                if (serviceName != currentServiceName) {
                    currentServiceName = serviceName
                    currentData = new DataReader(serviceName: currentServiceName, headers: headers, rowValues: [row], rowDescriptions: [rowDescription], cellErrors: cellErrors)
                    data << currentData
                } else {
                    currentData.rowValues << row
                    currentData.rowDescriptions << rowDescription
                }
            }
        }

        data
    }

    private List extractRow(Row row, List cellErrors) {
        (0..row.lastCellNum).collect {
            getValue row.getCell(it), cellErrors
        }
    }

    private def getValue(Cell cell, List cellErrors) {
        if (cell == null) {
            return null
        }
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
                CellReference cellRef = new CellReference(cell.row.getRowNum(), cell.getColumnIndex());
                cellErrors << (cellRef.cellRefParts[2] + cellRef.cellRefParts[1])
                return null
            case Cell.CELL_TYPE_FORMULA:
                return resolveFormula(cell, cellErrors)
            default:
                return null
        }
    }

    private def resolveFormula(Cell cell, List cellErrors) {
        def cellValue = formulaEvaluator.evaluate(cell)
        if (!cellValue) {
            return null
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
                return cellValue.getBooleanValue();
            case Cell.CELL_TYPE_ERROR:
                CellReference cellRef = new CellReference(cell.row.getRowNum(), cell.getColumnIndex());
                cellErrors << (cellRef.cellRefParts[2] + cellRef.cellRefParts[1])
                return null
            default:
                return null
        }
    }
}
