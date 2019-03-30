package com.xxx.ide.enums;

public enum ResultTypeEnum {
    ok(200), fail(400), error(500);
    private Integer typeCode;

    ResultTypeEnum(Integer code) {
        this.typeCode = code;
    }

    public Integer getTypeCode() {
        return typeCode;
    }
}
