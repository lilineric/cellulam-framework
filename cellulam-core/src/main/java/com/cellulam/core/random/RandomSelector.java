package com.cellulam.core.random;

/**
 * @author eric.li
 * @date 2022-06-06 14:02
 */
public interface RandomSelector<T> {
    T next();
    boolean hasNext();
}
