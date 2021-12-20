package com.atlantis.zeus.base.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Shell 工具类
 *
 * @author likang02@corp.netease.com
 * @date 2021-12-20 17:11
 */
@Slf4j
public class ShellUtil {

    /**
     * 执行Shell命令
     *
     * @param command "cd /" + " && " + "ls -l | grep '2112'"
     * @return 是否执行成功
     */
    public static boolean execute(String command) {
        try {
            Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", command});
            return true;
        } catch (Exception e) {
            log.error("ShellUtil_execute: execute error, e: ", e);
        }

        return false;
    }

}
