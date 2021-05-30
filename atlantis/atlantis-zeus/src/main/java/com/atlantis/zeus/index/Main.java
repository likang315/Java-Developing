package com.atlantis.zeus.index;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 测试平常case
 *
 * @author kangkang.li@qunar.com
 * @date 2020-11-09 12:02
 */
@Slf4j
public class Main extends ClassLoader {
    ReentrantReadWriteLock

    private static void solution(int[] array) {
        Queue<String> queue = new LinkedList<>();
        Lock lock = new ReentrantLock();



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
