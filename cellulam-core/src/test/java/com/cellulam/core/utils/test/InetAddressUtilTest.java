package com.cellulam.core.utils.test;

import com.cellulam.core.utils.InetAddressUtis;
import org.junit.Test;

public class InetAddressUtilTest {

    @Test
    public void testGetLocalPort() {
       System.out.println(InetAddressUtis.getLocalIPSilence());
    }

    @Test
    public void testGetHostname() {
        System.out.println(InetAddressUtis.getHostnameSilence());
    }
}
