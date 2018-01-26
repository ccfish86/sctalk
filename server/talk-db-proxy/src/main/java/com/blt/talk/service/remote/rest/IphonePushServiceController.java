package com.blt.talk.service.remote.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.param.IosPushReq;
import com.blt.talk.common.param.UserToken;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.service.config.PushServerInfo;
import com.blt.talk.service.internal.UserTokenService;

import javapns.Push;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;

/**
 * 推送相关处理
 * 
 * @author 袁贵
 * @version 1.1
 * @since 1.1
 */
@RequestMapping("/push")
@RestController
public class IphonePushServiceController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PushServerInfo pushServerInfo;
    @Autowired
    private UserTokenService userTokenService;

    @PostMapping(value = "/toUsers")
    public Callable<BaseModel<?>> sendToUsers(@RequestBody IosPushReq message) {

        return new Callable<BaseModel<?>>() {
            @Override
            public BaseModel<?> call() throws Exception {

                String certKeyPath = pushServerInfo.getIos().getCertKeyPath();
                String keyPassword = pushServerInfo.getIos().getCertPassword();
                
                try {
                    
                    List<String> tokens = new ArrayList<String>();
                    
                    for (UserToken user: message.getUserTokenList()) {
                        String token;
                        if (user.getUserToken() != null) {
                            token = user.getUserToken();
                        } else {
                            token = userTokenService.getToken(user.getUserId());
                        }
                        if (token != null && token.startsWith("ios:")) {
                            tokens.add(token.substring(4));
                        }
                    }
                    
                    if (!tokens.isEmpty()) {

                        PushNotificationPayload payload =
                                PushNotificationPayload.alert(message.getContent());
                        payload.addSound("default");
                        payload.addCustomDictionary("time", CommonUtils.currentTimeSeconds());
                        payload.addCustomDictionary("msg_type", message.getMsgType());
                        payload.addCustomDictionary("from_id", message.getFromId());
                        
                        // 群组时
                        if (CommonUtils.isGroup(message.getMsgType())) {
                            payload.addCustomDictionary("group_id", message.getGroupId());
                        }

                        PushedNotifications apnResults;
                        if (pushServerInfo.isTestMode()) {
                            apnResults =
                                    Push.payload(payload, certKeyPath, keyPassword, false, 30, tokens);
                        } else {
                            apnResults =
                                    Push.payload(payload, certKeyPath, keyPassword, true, 30, tokens);
                        }
                        if (apnResults != null) {
                            for (PushedNotification apnResult : apnResults) {
                                // ResponsePacket responsePacket = apnResult.getResponse();
                                if (apnResult.isSuccessful()) {
                                    logger.debug("推送结果：成功");
                                    // logger.debug("推送结果：",
                                    // responsePacket.getStatus(),responsePacket.getMessage());
                                } else {
                                    logger.debug("推送结果：失败");
                                }
                            }
                        }
                    }

                    return new BaseModel<Integer>();
                } catch (Exception e) {

                    logger.error("Iphone 推送失败！", e);
                    throw new Exception("推送失败！");
                }
            }
        };
    }
    
    /**
     * 用户device token取得
     * @param userId
     * @return
     * @since  3.0
     */
    @GetMapping(value = "/deviceToken")
    BaseModel<String> userDeviceToken(@RequestParam("userId") long userId){
        String userToken = userTokenService.getToken(userId);
        BaseModel<String> result = new BaseModel<>();
        result.setData(userToken);
        return result;
    }

}
