package com.blt.talk.common.io;

import java.io.IOException;
import java.io.InputStream;

import com.blt.talk.common.io.model.FileEntity;

/**
 * MINIO文件访问接口
 *
 * @author yuan'gui
 * @version 1.0
 * @since 2019年10月15日
 */
public interface MinioClientComponent {

    /**
     * 图片上传接口
     * <br>
     * 仅供用户头像，反馈，工单拍照等可以公开访问的图片
     *
     * @param inputStream 图片内容
     * @param fileName    文件名
     * @return 图片URL
     * @throws 图片上传异常
     */
    String saveImage(InputStream inputStream, String fileName) throws IOException;

    /**
     * 图片上传接口
     * <br>
     * 仅供用户头像，反馈，工单拍照等可以公开访问的图片
     *
     * @param inputStream 图片内容
     * @param fileName    文件名
     * @param contentType 文件格式
     * @return 图片URL
     * @throws IOException 图片上传异常
     */
    String saveImage(InputStream inputStream, String fileName, String contentType) throws IOException;

    /**
     * 以指定文件名上传图片文件
     * 
     * @param inputStream  图片内容
     * @param objectName 文件名
     * @return  图片URL
     * @throws IOException 图片上传异常
     */
    String updateImage(InputStream inputStream, String objectName) throws IOException;
    
    /**
     * 图片上传接口
     * <br>
     * 供用户授权等场合用
     *
     * @param inputStream 图片内容
     * @param fileName    文件名
     * @return 图片UUID
     * @throws 图片上传异常
     */
    String saveAuthImage(InputStream inputStream, String fileName) throws IOException;

    /**
     * 图片上传接口
     * <br>
     * 供用户授权等场合用
     *
     * @param inputStream 图片内容
     * @param fileName    文件名
     * @param contentType 文件格式
     * @return 图片UUID
     * @throws 图片上传异常
     */
    String saveAuthImage(InputStream inputStream, String fileName, String contentType) throws IOException;

    /**
     * 授权用图片访问接口
     *
     * @param objectName 图片UUID
     * @return 图片内容
     * @throws IOException
     */
    InputStream getAuthImage(String objectName) throws IOException;

    /**
     * 文件上传接口
     * <br>
     * 可供公开访问，不需要登录使用的
     *
     * @param inputStream 文件内容
     * @param fileName    文件名
     * @param contentType 文件格式
     * @return 文件URL
     * @throws 文件上传异常
     */
    String saveFile(InputStream inputStream, String fileName, String contentType) throws IOException;

    /**
     * 文件上传接口
     * <br>
     * 供特定用户使用的文件
     *
     * @param inputStream 文件内容
     * @param fileName    文件名
     * @param contentType 文件格式
     * @return 文件UUID
     * @throws 文件上传异常
     */
    String saveAuthFile(InputStream inputStream, String fileName, String contentType) throws IOException;

    /**
     * 文件访问接口
     *
     * @param objectName 文件UUID
     * @param expires    有效期
     * @return 文件下载地址
     * @throws IOException
     */
    String getAuthFile(String objectName, Integer expires) throws IOException;

    /**
     * 下载文件
     * @param name 文件名
     * @return
     * @throws IOException
     */
    FileEntity getAuthFileContent(String name) throws IOException;
}
