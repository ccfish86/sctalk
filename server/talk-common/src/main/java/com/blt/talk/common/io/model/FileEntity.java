package com.blt.talk.common.io.model;

import java.io.InputStream;

public class FileEntity {

    private String fileName;
    private long length;
    private String contentType;
    private InputStream content;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "FileEntity [fileName=" + fileName + ", contentType=" + contentType + "]";
    }

}
