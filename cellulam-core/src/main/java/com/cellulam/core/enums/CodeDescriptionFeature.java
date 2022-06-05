package com.cellulam.core.enums;


/**
 * enum with code and description
 * @author eric.li
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
