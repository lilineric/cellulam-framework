package com.cellulam.metadata.test;

import com.cellulam.metadata.DefaultMetadataContextInitializer;
import com.cellulam.metadata.MetadataContextInitializer;
import com.cellulam.metadata.MetadataContext;
import com.cellulam.metadata.common.Constants;
import com.cellulam.metadata.worker.DbWorkerIdAssigner;
import org.junit.Test;

import java.util.Properties;

public class MetadataContextTest {

    @Test
    public void testMetaContext() {
        Properties properties = new Properties();
        properties.setProperty(Constants.APP_NAME, "metadata-test");
        properties.setProperty(Constants.SERVER_PORT, "3412");
        properties.setProperty(Constants.JDBC_URL, "jdbc:mysql://localhost:3306/db_meta?characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8");
        properties.setProperty(Constants.JDBC_USERNAME, "root");
        properties.setProperty(Constants.JDBC_PASSWORD, "root123");

        MetadataContextInitializer metaContextInitializer = new DefaultMetadataContextInitializer(
                properties,
                new DbWorkerIdAssigner()
        );

//        metaContextInitializer.initialize();
        System.out.println(MetadataContext.context.toString());
        System.out.println(MetadataContext.context.getSysTime());
        System.out.println(MetadataContext.context.getSysTimestamp());
    }


    @Test
    public void testMetaContextFromResource() {
        MetadataContextInitializer metaContextInitializer = new DefaultMetadataContextInitializer(
                new DbWorkerIdAssigner()
        );

//        metaContextInitializer.initialize();
        System.out.println(MetadataContext.context.toString());
        System.out.println(MetadataContext.context.getSysTime());
        System.out.println(MetadataContext.context.getSysTimestamp());
    }

    @Test
    public void testMetaContextFromResource2() {
        MetadataContextInitializer metaContextInitializer = new DefaultMetadataContextInitializer(
                "dev/cellulam-metadata.properties",
                new DbWorkerIdAssigner()
        );

        metaContextInitializer.initialize();
        System.out.println(MetadataContext.context.toString());
        System.out.println(MetadataContext.context.getSysTime());
        System.out.println(MetadataContext.context.getSysTimestamp());
    }
}
