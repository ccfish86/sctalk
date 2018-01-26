/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package net.ccfish.talk.admin.config;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.CacheConfiguration;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * 分布式缓存配置，分布式锁及消息
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableCaching
public class HazelCastConfigration {

    private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

    private final DiscoveryClient discoveryClient;

    private Registration registration;

    public HazelCastConfigration(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Autowired(required = false)
    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    @PreDestroy
    public void destroy() {
        log.info("Closing Cache Manager");
        Hazelcast.shutdownAll();
    }

    @Bean
    public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
        log.debug("Starting HazelcastCacheManager");
        CacheManager cacheManager =
                new com.hazelcast.spring.cache.HazelcastCacheManager(hazelcastInstance);
        return cacheManager;
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        log.debug("Configuring Hazelcast");
        HazelcastInstance hazelCastInstance =
                Hazelcast.getHazelcastInstanceByName("talk-admin");
        if (hazelCastInstance != null) {
            log.debug("Hazelcast already initialized");
            return hazelCastInstance;
        }
        Config config = new Config();
        config.setInstanceName("message-server");
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        if (this.registration == null) {
            log.warn("No discovery service is set up, Hazelcast cannot create a cluster.");
        } else {
            // The serviceId is by default the application's name, see Spring Boot's
            // eureka.instance.appname property
            String serviceId = registration.getServiceId();
            log.debug("Configuring Hazelcast clustering for instanceId: {}", serviceId);
            config.getNetworkConfig().setPort(5701);
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
            for (ServiceInstance instance : discoveryClient.getInstances(serviceId)) {
                String clusterMember = instance.getHost() + ":5701";
                log.debug("Adding Hazelcast (prod) cluster member " + clusterMember);
                config.getNetworkConfig().getJoin().getTcpIpConfig().addMember(clusterMember);
            }
        }
        config.getMapConfigs().put("default", initializeDefaultMapConfig());
        return Hazelcast.newHazelcastInstance(config);
    }

    private MapConfig initializeDefaultMapConfig() {
        MapConfig mapConfig = new MapConfig();

        /*
         * Number of backups. If 1 is set as the backup-count for example, then all entries of the
         * map will be copied to another JVM for fail-safety. Valid numbers are 0 (no backup), 1, 2,
         * 3.
         */
        mapConfig.setBackupCount(1);

        /*
         * Valid values are: NONE (no eviction), LRU (Least Recently Used), LFU (Least Frequently
         * Used). NONE is the default.
         */
        mapConfig.setEvictionPolicy(EvictionPolicy.LRU);

        /*
         * Maximum size of the map. When max size is reached, map is evicted based on the policy
         * defined. Any integer between 0 and Integer.MAX_VALUE. 0 means Integer.MAX_VALUE. Default
         * is 0.
         */
        mapConfig
                .setMaxSizeConfig(new MaxSizeConfig(0, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE));

        return mapConfig;
    }

}
