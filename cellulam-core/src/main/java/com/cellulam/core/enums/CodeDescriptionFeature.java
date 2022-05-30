package com.cellulam.core.enums;


/**
 * 带code和描述的枚举 <br>
 */
public interface CodeDescriptionFeature extends CodeFeature {

    default void registerToHelper() {
        EnumUtils.register(getClass());
    }

    /**
     * 描述
     *
     * @return
     */
    String getDescription();

}
