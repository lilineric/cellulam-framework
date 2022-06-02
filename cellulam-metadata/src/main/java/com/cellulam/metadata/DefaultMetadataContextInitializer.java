package com.cellulam.metadata;

import com.cellulam.core.factories.PropertiesFactory;
import com.cellulam.core.utils.IpUtils;
import com.cellulam.db.conf.DbConfiguration;
import com.cellulam.db.conf.DbProperties;
import com.cellulam.metadata.common.Constants;
import com.cellulam.metadata.common.MetadataProperties;
import com.cellulam.metadata.dynamic.DbDynamicMetadataExplorer;
import com.cellulam.metadata.dynamic.DynamicMetadataExplorer;
import com.cellulam.metadata.exceptions.MetadataException;
import com.cellulam.metadata.worker.WorkerIdAssigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class DefaultMetadataContextInitializer implements MetadataContextInitializer {
    private Logger logger = LoggerFactory.getLogger(DefaultMetadataContextInitializer.class);

    private WorkerIdAssigner workerIdAssigner;

    private MetadataProperties properties;

    private DynamicMetadataExplorer dynamicMetadataExplorer = new DbDynamicMetadataExplorer();

    public DefaultMetadataContextInitializer(String propertiesResourceName, WorkerIdAssigner workerIdAssigner) {
        this(PropertiesFactory.getProperties(propertiesResourceName), workerIdAssigner);
    }

    public DefaultMetadataContextInitializer(WorkerIdAssigner workerIdAssigner) {
        this(PropertiesFactory.getProperties(Constants.DEFAULT_PROPERTIES_RESOURCE_NAME), workerIdAssigner);
    }

    public DefaultMetadataContextInitializer(Properties properties, WorkerIdAssigner workerIdAssigner) {
        this(MetadataProperties.loadFromProperties(properties), workerIdAssigner);
    }

    public DefaultMetadataContextInitializer(MetadataProperties properties, WorkerIdAssigner workerIdAssigner) {
        this.properties = properties;
        this.workerIdAssigner = workerIdAssigner;

        DbConfiguration.initConfiguration(DbProperties.Builder
                .builder()
                .dataSourceType(properties.getDataSourceType())
                .jdbcUrl(properties.getJdbcUrl())
                .jdbcUsername(properties.getJdbcUsername())
                .jdbcPassword(properties.getJdbcPassword())
                .mapperPackage("com.cellulam.metadata.dal.dao.mappers")
                .build());
    }

    public void setDynamicMetadataExplorer(DynamicMetadataExplorer dynamicMetadataExplorer) {
        this.dynamicMetadataExplorer = dynamicMetadataExplorer;
    }

    @Override
    public void initialize() {
        try {
            MetadataContext.context.setAppName(properties.getAppName());
            MetadataContext.context.setIp(IpUtils.getIp());
            MetadataContext.context.setPort(properties.getPort());
            MetadataContext.context.setWorkId(this.workerIdAssigner.assign());
            MetadataContext.context.setDynamicMetadataExplorer(dynamicMetadataExplorer);
            logger.info("MetadataContext Initialized: {}", MetadataContext.context.toString());
        } catch (Exception e) {
            throw new MetadataException("MetadataContext initialize failed.", e);
        }
    }
}
