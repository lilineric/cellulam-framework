package com.cellulam.spring.datasource.test.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserDO implements Serializable {
    private Long id;
    private String name;
    private Integer age;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return String.format("[id:%s][name:%s][age:%s][createTime:%s][updateTime:%s]",
                this.id,
                this.name,
                this.age,
                this.createTime.format(DateTimeFormatter.ISO_DATE_TIME),
                        this.updateTime.format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
