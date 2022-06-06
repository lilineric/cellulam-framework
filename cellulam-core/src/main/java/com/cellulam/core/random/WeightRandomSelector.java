package com.cellulam.core.random;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * weight random selector
 *
 * @author eric.li
 * @date 2022-06-06 14:16
 */
public class WeightRandomSelector<T> extends AbstractRandomSelector<T> implements RandomSelector<T> {

    private List<Integer> dataIndex;

    private WeightRandomSelector() {
    }

    @Override
    public T next() {
        int index = this.dataIndex.get(RandomUtils.nextInt(0, this.dataIndex.size()));
        return this.data.get(index);
    }

    public static final class Builder<T> {
        private List<T> data;

        private List<Integer> index;

        private double weightSum = 0;

        private int WEIGHT_SCALE = 1000;

        private Builder() {
            this.data = Lists.newCopyOnWriteArrayList();
            this.index = Lists.newCopyOnWriteArrayList();
        }

        public static Builder builder() {
            return new Builder();
        }

        /**
         * The weight must sum to 1
         * Support 3 decimal places eg. 0.125
         *
         * @param data
         * @param weight
         * @return
         */
        public Builder data(T data, double weight) {
            if (weight > 1 && weight < 0) {
                throw new IllegalArgumentException("The weight must be between 0 and 1, value: " + weight);
            }

            this.data.add(data);

            for (int i = 0; i < weight * WEIGHT_SCALE; i++) {
                this.index.add(this.data.size() - 1);
            }
            this.weightSum = this.weightSum + weight;
            return this;
        }

        public WeightRandomSelector build() {
            if (weightSum != 1.0) {
               throw new IllegalArgumentException("The weight must sum to 1.0");
            }
            WeightRandomSelector weightRandomSelector = new WeightRandomSelector();
            weightRandomSelector.data = this.data;
            weightRandomSelector.dataIndex = this.index;
            return weightRandomSelector;
        }
    }
}
