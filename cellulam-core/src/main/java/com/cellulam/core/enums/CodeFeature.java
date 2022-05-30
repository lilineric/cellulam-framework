package com.cellulam.core.enums;


/**
 * 带code的描述
 */
public interface CodeFeature {

    default void registerToHelper() {
        EnumUtils.register(getClass());
    }

    /**
     * 代码
     * @return
     */
    int getCode();

}
