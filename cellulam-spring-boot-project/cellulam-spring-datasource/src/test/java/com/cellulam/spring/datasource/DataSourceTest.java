package com.cellulam.spring.datasource;

import com.cellulam.spring.datasource.test.BaseUnitTest;
import com.cellulam.spring.datasource.test.dal.SysDAO;
import com.cellulam.spring.datasource.test.dal.UserDAO;
import com.cellulam.spring.datasource.test.po.UserDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class DataSourceTest extends BaseUnitTest {

    @Autowired
    private UserDAO userDao;

    @Autowired
    private SysDAO sysDAO;

    @Test
    public void startTest() {

        LocalDateTime currentTime = sysDAO.getSysDate();

        UserDO userDO = new UserDO();
        userDO.setName("Mike");
        userDO.setAge(28);
        userDO.setCreateTime(currentTime);
        userDO.setUpdateTime(currentTime);
        userDao.addUser(userDO);

        System.out.println(userDO);

        UserDO user = userDao.getById(userDO.getId());
        System.out.println(user);

//        Assert.assertTrue(user.getCreateTime().equals(userDO.getCreateTime()));
//        Assert.assertTrue(user.getUpdateTime().equals(userDO.getUpdateTime()));
        Assert.assertEquals(userDO.getId(), user.getId());
        Assert.assertEquals(userDO.getName(), user.getName());
        Assert.assertEquals(userDO.getAge(), user.getAge());

        currentTime = sysDAO.getSysDate();
        user.setUpdateTime(currentTime);
        user.setName("Tom");
        user.setAge(31);
        userDao.updateUser(user);

        UserDO user2 = userDao.getById(userDO.getId());
//        Assert.assertTrue(user.getCreateTime().equals(user2.getCreateTime()));
//        Assert.assertTrue(user.getUpdateTime().equals(user2.getUpdateTime()));
        Assert.assertEquals(user2.getId(), user.getId());
        Assert.assertEquals(user2.getName(), user.getName());
        Assert.assertEquals(user2.getAge(), user.getAge());
        System.out.println(user2);

    }
}
