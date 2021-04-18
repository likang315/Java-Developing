package com.atlantis.zeus.index;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

/**
 * 测试平常case
 *
 * @author kangkang.li@qunar.com
 * @date 2020-11-09 12:02
 */
@Slf4j
public class Main {

    private static void solution(int[] array) {
        Map<String, String> map = new TreeMap<>();


    }





    public static void main(String[] args) {
        try {
            int[] array = {1, 2, 5, 7, 3};
            solution(array);
        } catch (Exception e) {
            log.info("Main_main: ", e);
        }
    }

}
