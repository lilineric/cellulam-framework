package com.cellulam.core.random;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author eric.li
 * @date 2022-06-06 15:18
 */
public class UniqueRandomSelector<T> extends AbstractRandomSelector<T> implements RandomSelector<T> {
    @Override
    public synchronized T next() {
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        T result = super.next();
        this.data.remove(result);
        return result;
    }

    public static final class Builder<T> {
        private List<T> data;

        public static Builder builder() {
            return new Builder();
        }

        private Builder() {
            this.data = Lists.newCopyOnWriteArrayList();
        }

        public Builder data(List<T> list) {
            list.parallelStream()
                    .forEach(x -> data(x));
            return this;
        }

        public Builder data(T data) {
            this.data.add(data);
            return this;
        }

        public UniqueRandomSelector<T> build() {
            UniqueRandomSelector<T> selector = new UniqueRandomSelector<>();
            selector.data = this.data;
            return selector;
        }
    }
}
