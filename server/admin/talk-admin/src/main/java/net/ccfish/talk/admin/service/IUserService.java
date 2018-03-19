package net.ccfish.talk.admin.service;

import net.ccfish.talk.admin.domain.ManagerUser;

/**
 * 用户操作接口
 *
 * @author hackyo
 * Created on 2017/12/3 11:53.
 */
public interface IUserService {

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 操作结果
     */
    String login(String username, String password);

    /**
     * 刷新密钥
     *
     * @param oldToken 原密钥
     * @return 新密钥
     */
    String refreshToken(String oldToken);

}