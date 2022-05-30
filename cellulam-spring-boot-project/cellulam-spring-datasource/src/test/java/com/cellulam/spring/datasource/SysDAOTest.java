package com.cellulam.spring.datasource;

import com.cellulam.spring.datasource.test.BaseUnitTest;
import com.cellulam.spring.datasource.test.dal.SysDAO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SysDAOTest extends BaseUnitTest {

    @Autowired
    private SysDAO sysDAO;

    @Test
    public void testGetSysDate() {
        System.out.println(sysDAO.getSysDate());
    }
}
