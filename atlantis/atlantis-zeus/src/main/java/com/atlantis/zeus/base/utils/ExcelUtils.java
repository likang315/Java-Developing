package com.atlantis.zeus.base.utils;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Excel 工具类
 *
 * @author kangkang.li@qunar.com
 * @date 2020-11-12 15:59
 */
public class ExcelUtils {
    /**
     * 构键文件OutPutStream
     *
     * @param titles
     * @param data Sheets->rows->columns
     * @param sheetName
     * @return
     * @throws IOException
     */
    public static OutputStream buildExcelFileStream(List<List<String>> titles,
                                                    List<List<List<Object>>> data,
                                                    List<String> sheetName)
            throws IOException {
        // xlsx 的 excel文档对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建单元格类型
        XSSFCellStyle style = workbook.createCellStyle();
        for (int index = 0; index < titles.size(); index++) {
            buildExcelSheet(titles.get(index), data.get(index) , workbook, style, sheetName.get(index));
        }
        OutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream;
    }

    /**
     * 构键Sheet，方便用于构键多个Sheet
     *
     * @param title
     * @param beans
     * @param workbook
     * @param style
     * @param sheetName
     * @return
     */
    private static void buildExcelSheet(List<String> title,
                                        List<List<Object>> beans,
                                        XSSFWorkbook workbook,
                                        XSSFCellStyle style, String sheetName) {
        // 创建工作表对象
        XSSFSheet sheet = workbook.createSheet(sheetName);
        // 创建工作表的行
        XSSFRow row = sheet.createRow(0);

        // 循环建立表头
        for(int i = 0 ; i < title.size(); i++) {
            // 创建单元格
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(title.get(i));
            cell.setCellStyle(style);
        }
        // 为每一行赋值
        for (int i = 0; i < beans.size(); i++) {
            row = sheet.createRow(i + 1);

            List<Object> list = beans.get(i);
            for(int j = 0; j < list.size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellValue(String.valueOf(list.get(j)));
                cell.setCellStyle(style);
            }
        }
    }
}