package com.cellulam.metadata;

import com.cellulam.metadata.dynamic.DynamicMetadataExplorer;
import com.cellulam.metadata.exceptions.MetadataException;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 元数据 Context
 */
@ToString
@Data
public class MetadataContext {
    private MetadataContext() {
        this.ext = Maps.newConcurrentMap();
    }

    public final static MetadataContext context = new MetadataContext();

    private DynamicMetadataExplorer dynamicMetadataExplorer;

    private String appName;

    private String ip;

    private String port;

    private long workId;

    private Map<String, Object> ext;

    public void addExtendInfo(String key, Object value) {
        this.ext.put(key, value);
    }

    public LocalDateTime getSysTime() {
        this.checkDynamicMetadataExplorer();
        return dynamicMetadataExplorer.getSysTime();
    }

    public long getSysTimestamp() {
        return dynamicMetadataExplorer.getSysTimestamp();
    }

    private void checkDynamicMetadataExplorer() {
        if(dynamicMetadataExplorer == null) {
            throw new MetadataException("DynamicMetadataExplorer hadn't been set");
        }
    }

}
