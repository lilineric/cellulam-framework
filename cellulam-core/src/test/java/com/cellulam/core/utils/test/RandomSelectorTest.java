package com.cellulam.core.utils.test;

import com.cellulam.core.random.RandomSelector;
import com.cellulam.core.random.SimpleRandomSelector;
import com.cellulam.core.random.UniqueRandomSelector;
import com.cellulam.core.random.WeightRandomSelector;
import com.cellulam.core.utils.UUIDUtils;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author eric.li
 * @date 2022-06-06 15:25
 */
public class RandomSelectorTest {


    @Test
    public void testSimpleRandom() {
        List<User> users = this.initUsers();
        RandomSelector<User> randomSelector = SimpleRandomSelector.Builder
                .builder()
                .data(users)
                .build();
        System.out.println(randomSelector.next());
        System.out.println(randomSelector.next());
        System.out.println(randomSelector.next());
        System.out.println(randomSelector.next());
    }

    @Test
    public void testWeightRandom() {
        List<User> users = this.initUsers();
        RandomSelector<User> randomSelector = WeightRandomSelector.Builder
                .builder()
                .data(users.get(0), 0.2)
                .data(users.get(1), 0.5)
                .data(users.get(2), 0.3)
                .build();
        System.out.println(randomSelector.next());
        System.out.println(randomSelector.next());
        System.out.println(randomSelector.next());
        System.out.println(randomSelector.next());
        System.out.println(randomSelector.next());
        System.out.println(randomSelector.next());
        System.out.println(randomSelector.next());
        System.out.println(randomSelector.next());
        System.out.println(randomSelector.next());
    }

    @Test
    public void testUniqueRandom() {
        List<User> users = this.initUsers();
        RandomSelector<User> randomSelector = UniqueRandomSelector.Builder
                .builder()
                .data(users)
                .build();

        while (randomSelector.hasNext()) {
            System.out.println(randomSelector.next());
        }
    }

    private List<User> initUsers() {
        List<User> users = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            users.add(User.builder()
                    .id(System.currentTimeMillis() + RandomUtils.nextInt())
                    .name("name-" + UUIDUtils.randomUUID32())
                    .build());
        }
        return users;
    }

    @ToString
    @Builder
    @Data
    public static final class User {
        private String name;
        private Long id;
    }
}
