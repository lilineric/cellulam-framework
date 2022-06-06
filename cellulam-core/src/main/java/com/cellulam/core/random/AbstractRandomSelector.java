package com.cellulam.core.random;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * @author eric.li
 * @date 2022-06-06 14:54
 */
public abstract class AbstractRandomSelector<T> implements RandomSelector<T> {
    protected List<T> data;

    @Override
    public T next() {
        return this.data.get(RandomUtils.nextInt(0, this.data.size()));
    }

    @Override
    public boolean hasNext() {
        return CollectionUtils.isNotEmpty(data);
    }
}
