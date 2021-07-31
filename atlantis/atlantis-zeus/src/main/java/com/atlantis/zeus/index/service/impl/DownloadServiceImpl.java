package com.atlantis.zeus.index.service.impl;

import com.atlantis.zeus.base.constant.NumberConstants;
import com.atlantis.zeus.base.utils.ExcelUtil;
import com.atlantis.zeus.base.utils.NetworkUtil;
import com.atlantis.zeus.index.dao.readonly.CommonReadMapper;
import com.atlantis.zeus.index.pojo.vo.DownloadVO;
import com.atlantis.zeus.index.service.DownloadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author likang02@corp.netease.com
 * @date 2021-07-31 14:46
 */
@Slf4j
@Service
public class DownloadServiceImpl implements DownloadService {

    /**
     * 分页关键字
     */
    private static final String LIMIT = "limit";

    /**
     * 页面大小
     */
    private static final int PAGE_SIZE = 5000;

    /**
     * 文件路径
     */
    private static final String LOCATION = "data.xlsx";


    @Resource
    private CommonReadMapper commonReadMapper;

    @Override
    public DownloadVO downloadExcel(String tableName, List<String> fields, String where) {
        DownloadVO vo = new DownloadVO().setIp(NetworkUtil.queryIpAddress());
        if (where.contains(LIMIT)) {
            log.error("DownloadServiceImpl_downloadExcel: query page not contains limit word");
            return vo;
        }

        int count;
        try {
            count = commonReadMapper.queryCount(tableName, where);
            vo.setCount(count);
            // 最多下载100000条数据防止OOM
            count = count > NumberConstants.ONE_HUNDRED_THOUSAND ? NumberConstants.ONE_HUNDRED_THOUSAND : count;
            int pageSum = (count + PAGE_SIZE - 1) / PAGE_SIZE;

            XSSFWorkbook book = pageQuery(tableName, fields, where, pageSum, count);
            boolean result = writeDataToExcel(book);

            return vo.setNosUrl(String.valueOf(result));
        } catch (Exception e) {
            log.error("ExcelDownloadService_queryData: tableName: {}, where: {}, exp: {}", tableName, where, e);
        }

        return vo.setNosUrl(String.valueOf(Boolean.FALSE));
    }

    /**
     * 分页查询 防止数据库压力过大
     *
     * @param tableName
     * @param fields
     * @param where
     * @param pageSum
     * @param count
     * @return
     */
    public XSSFWorkbook pageQuery(String tableName, List<String> fields, String where, int pageSum, int count) {
        XSSFWorkbook book = new XSSFWorkbook();
        for (int i = 0; i < pageSum; i++) {
            // 字段顺序无法控制
            Map<String, Map<String, String>> result = commonReadMapper.query(tableName, fields, where, PAGE_SIZE, i * PAGE_SIZE);
            ExcelUtil.buildExcelFile(convert(result), tableName + "_" + count, book,
                    !Objects.equals(i, NumberConstants.ZERO));
        }

        return book;
    }

    /**
     * 将数据库格式的数据转换成Excel格式
     *
     * @param input
     * @return
     */
    private Pair<List<String>, List<List<Object>>> convert(Map<String, Map<String, String>> input) {
        List<String> title = new LinkedList<>();
        List<List<Object>> rows = new LinkedList<>();
        List<Object> cols;

        for (Map.Entry entry : input.entrySet()) {
            Map<String, String> map = (Map<String, String>) entry.getValue();
            // 初始化表头
            if (CollectionUtils.isEmpty(title)) {
                for (Map.Entry inner : map.entrySet()) {
                    title.add(inner.getKey().toString());
                }
            }

            cols = new LinkedList<>();
            for (Map.Entry inner : map.entrySet()) {
                cols.add(inner.getValue());
            }
            rows.add(cols);
        }

        return Pair.of(title, rows);
    }


    /**
     * 将数据写入到 Excel 中
     *
     * @param book
     * @return
     */
    private Boolean writeDataToExcel(XSSFWorkbook book) {
        FileOutputStream os = null;
        try {
            File file = new File(LOCATION);
            // 不可重试性错误
            if (file.exists() && !file.delete()) {
                return Boolean.FALSE;
            }
            if (!file.createNewFile()) {
                return Boolean.FALSE;
            }
            os = new FileOutputStream(file.getAbsolutePath());
            book.write(os);
            // 统一上传到静态资源服务器上

            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("ExcelDownloadService_writeDataToExcel: exp: ", e);
            return Boolean.FALSE;
        } finally {
            try {
                if (Objects.nonNull(os)) {
                    os.close();
                }
            } catch (IOException e) {
                log.error("ExcelDownloadService_writeDataToExcel: exp: ", e);
            }
        }
    }
}
