package com.blt.talk.common.io.config;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * minio配置
 * 
 * @author yuan'gui
 * @version 1.0
 * @since 2020年1月6日
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig implements Serializable {

    private String endpoint;

    private int port = 9000;

    /**
     * 公用数据，图片文档等
     */
    private String defaultBucket = "public";

    /**
     * 授权用人脸等数据
     */
    private String authBucket = "auth";

    private String accessKey;

    private String secretKey;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDefaultBucket() {
        return defaultBucket;
    }

    public void setDefaultBucket(String defaultBucket) {
        this.defaultBucket = defaultBucket;
    }

    public String getAuthBucket() {
        return authBucket;
    }

    public void setAuthBucket(String authBucket) {
        this.authBucket = authBucket;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
