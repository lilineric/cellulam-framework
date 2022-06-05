package com.cellulam.core.enums;


/**
 * enum with code
 * @author eric.li
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
