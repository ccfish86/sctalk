/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author 袁贵
 * @version 3.0
 * @since  3.0
 */
@Component
@ConfigurationProperties(prefix="talk.push")
public class PushServerInfo {
    
    private Boolean testMode;
    
    private final Ios ios = new Ios();
    
    /**
     * @return the testMode
     */
    public Boolean isTestMode() {
        return testMode;
    }

    /**
     * @param testMode the testMode to set
     */
    public void setTestMode(Boolean testMode) {
        this.testMode = testMode;
    }

    /**
     * @return the ios
     */
    public Ios getIos() {
        return ios;
    }

    public static class Ios {
        private String certKeyPath;
        private String certPassword;

        /**
         * @return the certKeyPath
         */
        public String getCertKeyPath() {
            return certKeyPath;
        }
        /**
         * @param certKeyPath the certKeyPath to set
         */
        public void setCertKeyPath(String certKeyPath) {
            this.certKeyPath = certKeyPath;
        }
        /**
         * @return the certPassword
         */
        public String getCertPassword() {
            return certPassword;
        }
        /**
         * @param certPassword the certPassword to set
         */
        public void setCertPassword(String certPassword) {
            this.certPassword = certPassword;
        }
        
    }
}
