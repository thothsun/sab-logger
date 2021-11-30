package com.thothsun.sablogger;

import androidx.annotation.NonNull;

public class Logger {
    private Logger() {
    }

    private static final Printer printer = new Printer();

    /**
     * 设置全局tag（可选，默认为sablogger）
     *
     * @param tag 全局tag
     */
    public static void init(@NonNull String tag) {
        printer.init(tag);
    }

    /**
     * 设置单个tag（可选，不设置则使用全局tag）
     *
     * @param tag 单个tag
     */
    public static Printer tag(@NonNull String tag) {
        return printer.setTag(tag);
    }

    /**
     * 设置显示的堆栈方法数（可选，默认为1）
     *
     * @param stackCount 堆栈方法数
     */
    public static Printer stacks(@NonNull int stackCount) {
        return printer.stacks(stackCount);
    }

    public static void v(@NonNull Object... logs) {
        printer.v(logs);
    }

    public static void d(@NonNull Object... logs) {
        printer.d(logs);
    }

    public static void i(@NonNull Object... logs) {
        printer.i(logs);
    }

    public static void w(@NonNull Object... logs) {
        printer.w(logs);
    }

    public static void e(@NonNull Object... logs) {
        printer.e(logs);
    }

    public static void wtf(@NonNull Object... logs) {
        printer.wtf(logs);
    }
}
