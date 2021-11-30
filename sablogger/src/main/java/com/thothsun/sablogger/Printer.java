package com.thothsun.sablogger;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class Printer {

    private final ThreadLocal<String> localTag = new ThreadLocal<>();
    private final ThreadLocal<Integer> localStackCount = new ThreadLocal<>();
    private String defaultTag = "sablogger";

    Printer setTag(String customTag) {
        localTag.set(customTag);
        return this;
    }

    Printer stacks(int count) {
        localStackCount.set(count);
        return this;
    }

    void init(String tag) {
        this.defaultTag = tag;
    }

    // TODO: 2021/11/30 dist file log
    public void v(Object... logs) {
        print(Priority.VERBOSE, logs);
    }

    public void d(Object... logs) {
        print(Priority.DEBUG, logs);
    }

    public void i(Object... logs) {
        print(Priority.INFO, logs);
    }

    public void w(Object... logs) {
        print(Priority.WARN, logs);
    }

    public void e(Object... logs) {
        print(Priority.ERROR, logs);
    }

    public void wtf(Object... logs) {
        print(Priority.ASSERT, logs);
    }

    private synchronized void print(int priority, Object... logs) {
        String tag = getTag();
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = getStackOffset(trace);//3

        int methodCount = getStackCount();
        int start = stackOffset + methodCount;
        if (start >= trace.length) {
            start = trace.length - 1;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Object log : logs) {
            if (log.getClass().isArray()) {
                stringBuilder.append(arrayToString(log)).append(" ");
            } else {
                stringBuilder.append(log).append(" ");
            }
        }

        if (methodCount == 1) {
            log(priority, tag, '[' + Thread.currentThread().getName() + " thread] " + getStack(trace[start]) + ": " + stringBuilder);
        } else {
            for (int i = start; i > stackOffset; i--) {
                log(priority, tag, (i == start ? ('[' + Thread.currentThread().getName() + " thread] ") : ("           -> ")) + getStack(trace[i]) + (i != stackOffset + 1 ? ("") : (": " + stringBuilder)));
            }
        }
    }

    private String getStack(StackTraceElement target) {
        return getSimpleClassName(target.getClassName()) + '.' + target.getMethodName() + '(' + target.getFileName() + ":" + target.getLineNumber() + ')';
    }

    private String arrayToString(Object array) {
        String result = array.toString();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return result;
        }
        switch (array.getClass().getTypeName()) {
            case "byte[]":
                result = Arrays.toString((byte[]) array);
                break;
            case "short[]":
                result = Arrays.toString((short[]) array);
                break;
            case "int[]":
                result = Arrays.toString((int[]) array);
                break;
            case "long[]":
                result = Arrays.toString((long[]) array);
                break;
            case "float[]":
                result = Arrays.toString((float[]) array);
                break;
            case "double[]":
                result = Arrays.toString((double[]) array);
                break;
            case "char[]":
                result = Arrays.toString((char[]) array);
                break;
            case "boolean[]":
                result = Arrays.toString((boolean[]) array);
                break;
            case "Object[]":
                result = Arrays.toString((Object[]) array);
                break;
        }
        return result;
    }

    private void log(int priority, String tag, String log) {
        Log.println(priority, tag == null ? defaultTag : tag, log);
    }

    private int getStackOffset(@NonNull StackTraceElement[] trace) {
        final int MIN_STACK_OFFSET = 2;
        for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(Logger.class.getName()) && !name.equals(Printer.class.getName())) {
                return i - 1;
            }
        }
        return -1;
    }

    private String getSimpleClassName(@NonNull String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private String getTag() {
        String tag = localTag.get();
        if (tag != null) {
            localTag.remove();
            return tag;
        }
        return null;
    }

    private int getStackCount() {
        Integer stackCount = localStackCount.get();
        if (stackCount != null) {
            localStackCount.remove();
            return stackCount;
        }
        return 1;
    }
}
