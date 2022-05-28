package com.cellulam.core.utils.test;

import com.cellulam.core.utils.InetAddressUtil;
import org.junit.Test;

public class InetAddressUtilTest {

    @Test
    public void testGetLocalPort() {
       System.out.println(InetAddressUtil.getLocalIPSilence());
    }

    @Test
    public void testGetHostname() {
        System.out.println(InetAddressUtil.getHostnameSilence());
    }
}
