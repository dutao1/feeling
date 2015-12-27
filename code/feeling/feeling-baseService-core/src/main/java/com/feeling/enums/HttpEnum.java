package com.feeling.enums;

/**
 * The Enum HttpEnum.
 */
public enum HttpEnum {
    GET(1, "GET"),
    POST(2, "POST"),
    PUT(3, "PUT"),
    DELETE(4, "DELETE")
    ;

    /** The value. */
    private final int value;

    /** The desc. */
    private final String desc;

    /**
     * Instantiates a new return status enum.
     * @param value the value
     * @param desc the desc
     */
    private HttpEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * Gets the value.
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the desc.
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
}
