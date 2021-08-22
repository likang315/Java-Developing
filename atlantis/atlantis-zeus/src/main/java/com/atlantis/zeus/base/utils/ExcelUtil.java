package com.atlantis.zeus.base.utils;

import org.apache.commons.lang3.tuple.Pair;
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
public class ExcelUtil {
    /**
     * 构造 多个sheet Excel 数据
     *
     * @param data sheet->title-[rows->cols]
     * @param sheetNames
     * @return
     * @throws IOException
     */
    public static OutputStream buildExcelFileStream(List<Pair<List<String>, List<List<Object>>>> data,
                                                    List<String> sheetNames) throws IOException {
        // xlsx 的 excel文档对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        for (int i = 0; i < data.size(); i++) {
            buildExcelFile(data.get(i), sheetNames.get(i), workbook, false);
        }

        OutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream;
    }


    /**
     * 构造Excel 数据
     *
     * @param data title-[rows->cols]
     * @param sheetName
     * @param workbook
     * @return
     */
    public static void buildExcelFile(Pair<List<String>, List<List<Object>>> data,
                                      String sheetName,
                                      XSSFWorkbook workbook,
                                      boolean isAppend){
        // 创建单元格类型
        XSSFCellStyle style = workbook.createCellStyle();
        buildExcelSheet(data.getLeft(), data.getRight(), workbook, style, sheetName, isAppend);
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
                                        XSSFCellStyle style,
                                        String sheetName,
                                        boolean isAppend) {
        XSSFRow row;
        XSSFSheet sheet;
        int lastRows = 0;
        if (isAppend) {
            sheet = workbook.getSheet(sheetName);
            lastRows = sheet.getLastRowNum();
        } else {
            // 创建工作表对象
            sheet = workbook.createSheet(sheetName);
            // 创建工作表的行
            row = sheet.createRow(0);
            // 循环建立表头
            for(int i = 0 ; i < title.size(); i++) {
                // 创建单元格
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(title.get(i));
                cell.setCellStyle(style);
            }
        }

        // 为每一行赋值
        for (int i = 0; i < beans.size(); i++) {
            row = sheet.createRow(lastRows + i + 1);

            List<Object> list = beans.get(i);
            for(int j = 0; j < list.size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellValue(String.valueOf(list.get(j)));
                cell.setCellStyle(style);
            }
        }
    }
}