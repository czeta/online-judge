package com.czeta.onlinejudge.enums;

import java.util.*;

/**
 * @EnumName SubmitStatus
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 16:54
 * @Version 1.0
 */
public enum SubmitStatus {
    ACCEPTED("Accepted", "通过"),
    WRONG_ANSWER("WrongAnswer", "答案错误"),
    TIME_LIMIT_EXCEEDED("TimeLimitExceeded", "超时"),
    OUTPUT_LIMIT_EXCEEDED("OutputLimitExceeded", "超过输出限制"),
    MEMORY_LIMIT_EXCEEDED("MemoryLimitExceeded", "超过内存限制"),
    RUNTIME_ERROR("RuntimeError", "运行时错误"),
    PRESENTATION_ERROR("PresentationError", "格式错误"),
    COMPILE_ERROR("CompileError", "编译错误"),
    PENDING("Pending", "正在评测"),
    SYSTEM_ERROR("SystemError", "系统出错");



    private String name;
    private String message;

    private SubmitStatus() {}
    private SubmitStatus(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static List<String> getEnumNameList() {
        List<String> list = new ArrayList<>();
        for (SubmitStatus p : SubmitStatus.values()) {
            list.add(p.getName());
        }
        return list;
    }

    public static Map<String, Integer> getStatisticMap() {
        Map<String, Integer> map = new HashMap<>();
        for (SubmitStatus s: SubmitStatus.values()) {
            map.put(s.getName(), 0);
        }
        return map;
    }

    public static boolean isContain(String name) {
        Set<String> set = new HashSet<>();
        for (SubmitStatus p : SubmitStatus.values()) {
            set.add(p.getName());
        }
        return set.contains(name);
    }
}
