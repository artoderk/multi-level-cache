package com.arto.cache.common;

/**
 * Created by xiong.jie on 2017-07-10.
 */
enum CheckResultEnum {

    SUCCESS(100, "成功"),

    SYSTEM_ERROR(201, "通用错误"),

    JSON_FORMAT_ERROR(202, "JOSN格式有误"),

    TEMPLATE_NOT_EXIST(203, "模板ID不存在"),

    USER_NOT_EXIST(204, "用户不存在"),

    MISS_ARGS(205, "参数为空或不正确");

    /**
     * @param code
     * @param reason
     */
    private CheckResultEnum(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    private int    code;

    private String reason;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
