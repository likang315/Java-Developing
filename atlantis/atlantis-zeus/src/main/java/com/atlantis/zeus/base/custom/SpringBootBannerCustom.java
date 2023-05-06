package com.atlantis.zeus.base.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;
import org.springframework.util.StreamUtils;

import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 自定义 SpringBoot Banner
 *
 * @author likang02@corp.netease.com
 * @date 2023/5/6 09:49
 */
@Slf4j
public class SpringBootBannerCustom implements Banner {

    private static final String SPRING_BOOT = "    :: Spring Boot ::   ";

    private static final int STRAP_LINE_SIZE = 42;

    private static final String BANNER_PATH = "/spring/banner.txt";

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        try {
            BufferedInputStream bs = new BufferedInputStream(
                    Objects.requireNonNull(sourceClass.getResourceAsStream(BANNER_PATH)));
            String banner = StreamUtils.copyToString(bs,
                    environment.getProperty("spring.banner.charset", Charset.class, StandardCharsets.UTF_8));
            out.println(banner);

            String version = SpringBootVersion.getVersion();
            version = (version != null) ? " (v" + version + ")" : "";
            StringBuilder padding = new StringBuilder();
            while (padding.length() < STRAP_LINE_SIZE - (version.length() + SPRING_BOOT.length())) {
                padding.append(" ");
            }
            out.println(AnsiOutput.toString(AnsiColor.GREEN, SPRING_BOOT, AnsiColor.DEFAULT, padding.toString(),
                    AnsiStyle.FAINT, version));
            out.println();
        }
        catch (Exception ex) {
            log.warn("Banner not printable: {} ({}: '{}')", BANNER_PATH, ex.getClass(), ex.getMessage());
        }
    }
}
