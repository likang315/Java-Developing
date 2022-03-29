package com.atlantis.zeus.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


/**
 * 日志解析工具类
 *
 * @author likang02@corp.netease.com
 * @date 2022-03-29 16:40
 */
@Slf4j
public class LogParseUtil {

    /**
     * 按行读取日志
     *
     * @param path
     * @return
     */
    public static List<JSONObject> readLog(String path) {
        List<JSONObject> result = new LinkedList<>();
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(path), "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String rowStr;
            while (Objects.nonNull((rowStr = reader.readLine()))) {
                JSONObject obj = new JSONObject(rowStr);
                result.add(obj);
            }
        } catch (Exception e) {
            log.error("LogParseUtil_readLog: read log error !!!", e);
        } finally {
            try {
                if (Objects.nonNull(inputStreamReader)) {
                    inputStreamReader.close();
                }
                if (Objects.nonNull(reader)) {
                    reader.close();
                }
            } catch (IOException e) {
                log.error("LogParseUtil_readLog: stream close error !!!", e);
            }
        }

        return result;
    }


    public static void main(String[] args) {
        List<JSONObject> result = readLog("/Users/likang/Downloads/comment.log");
        List<List<Object>> data = new LinkedList<>();
        for (JSONObject object : result) {
            List<Object> row = new LinkedList<>();
            try {
                row.add(object.getString("docId"));
                row.add(object.getString("postId"));
                row.add(object.getString("sourceId"));
                row.add(object.getString("title"));
                row.add(object.getString("category"));
                row.add(object.getString("comment"));
                row.add(object.getString("score"));
                data.add(row);
            } catch (JSONException e) {
                continue;
            }
        }

        XSSFWorkbook book = new XSSFWorkbook();
        List<String> title = new ArrayList<>();
        title.add("docId");
        title.add("postId");
        title.add("sourceId");
        title.add("title");
        title.add("category");
        title.add("comment");
        title.add("score");

        ExcelUtil.writeExcel(Pair.of(title, data), "123", book, false);
        ExcelUtil.writeDataToExcel(book);

    }



}
