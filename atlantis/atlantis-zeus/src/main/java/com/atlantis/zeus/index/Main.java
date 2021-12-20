package com.atlantis.zeus.index;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试平常case
 *
 * @author kangkang.li@qunar.com
 * @date 2020-11-09 12:02
 */
@Slf4j
public class Main extends ClassLoader {

    static class TreeNode {
        private int val;
        private TreeNode left;
        private TreeNode right;

        private TreeNode(int val) {
            this.val = val;
        }
    }

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    static void solution(String str) {
        System.out.println(str.split("_")[1]);

    }


    public static void main(String[] args) {
        int[] array = {4, 2, 3, 1, 5, 6, 7};
        int[][] arr = {{917}, {558, 224, 24}, {691, 434, 234, 56}};
        solution("");
    }
}
