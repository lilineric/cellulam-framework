package com.cellulam.core.random;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * simple random selector
 *
 * @author eric.li
 * @date 2022-06-06 14:03
 */
public class SimpleRandomSelector<T> extends AbstractRandomSelector<T> implements RandomSelector<T> {
    private SimpleRandomSelector() {
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

        public SimpleRandomSelector<T> build() {
            SimpleRandomSelector<T> selector = new SimpleRandomSelector<>();
            selector.data = this.data;
            return selector;
        }
    }
}
