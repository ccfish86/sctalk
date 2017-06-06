/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.mogujie.tt.utils;

import org.apache.commons.io.FilenameUtils;

/**
 * 缩略图文件名（URL）编辑
 * 
 * @author 袁贵
 * @since 2015/6/3
 */
public abstract class ThumbUrlUtils {

    /**
     * 缩略图文件URL/ID编辑 <br>
     * 取缩略图
     * 
     * @param orgiUrl 原图URL
     * @param width 文件宽
     * @param height 文件高
     * @return 缩略图URl
     */
    public static String getThumbUrl(String orgiUrl, int width, int height) {

        if (orgiUrl == null) {
            return null;
        }

        String extension = FilenameUtils.getExtension(orgiUrl);
        if (extension == null) {
            return orgiUrl;
        }

        if ("jpg".equalsIgnoreCase(extension)) {
            // 缩略图
        } else if ("png".equalsIgnoreCase(extension)) {
            // 缩略图
        } else {
            return orgiUrl;
        }

        int extensionPos = orgiUrl.length() - extension.length() - 1;

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(orgiUrl.substring(0, extensionPos));
        stringBuffer.append("_").append(width).append("x").append(height);
        stringBuffer.append(orgiUrl.substring(extensionPos));

        return stringBuffer.toString();
    }

    /**
     * 缩略图文件URL/ID编辑 <br>
     * 取原图
     * 
     * @param thumbUrl 缩略图
     * @return 原图URL
     */
    public static String getOrgiUrl(String thumbUrl) {

        if (thumbUrl == null) {
            return null;
        }

        String extension = FilenameUtils.getExtension(thumbUrl);
        if (extension == null) {
            return thumbUrl;
        }

        if ("jpg".equalsIgnoreCase(extension)) {
            // 缩略图
        } else if ("png".equalsIgnoreCase(extension)) {
            // 缩略图
        } else {
            return thumbUrl;
        }

        return thumbUrl.replaceAll("_\\d+x\\d+\\." + extension + "$", "." + extension);
    }
}
