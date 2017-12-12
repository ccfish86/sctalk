/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.message.server.config.MessageServerConfig;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.mikesu.fastdfs.FastdfsClient;
import net.mikesu.fastdfs.FastdfsClientFactory;
import net.mikesu.fastdfs.data.BufferFile;

/**
 * 文件上传
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@RestController
public class FileUploadController {

    private static String imageServer;
    
    public FileUploadController(MessageServerConfig messageServerConfig) {
        imageServer = messageServerConfig.getFileServer();
    }
    
    @PostMapping(path = "/upload")
    public BaseModel<FileUploadRsp> upload(HttpServletRequest request) {

        if (request instanceof MultipartRequest) {
            MultipartRequest multipartRequest = (MultipartRequest)request;
            
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            
            if (fileMap.size() > 0) {
                try {
                    FastdfsClient fastdfsClient = FastdfsClientFactory.getFastdfsClient();
                    String fileId=null;
                    for (MultipartFile file : fileMap.values()) {
                        if (file.getSize() > 0) {
                            BufferFile fdfsFile = new BufferFile();
                            fdfsFile.setName(file.getOriginalFilename());
                            fdfsFile.setFiledata(file.getBytes());
                            fileId = fastdfsClient.upload(fdfsFile);

                            break;
                        }
                    }
                    
                    BaseModel<FileUploadRsp> rsp = new BaseModel<>();
                    FileUploadRsp fileRsp = new FileUploadRsp(fileId);
                    rsp.setData(fileRsp);
                    return rsp;
                } catch (Exception e) {
                    BaseModel<FileUploadRsp> rsp = new BaseModel<>();
                    rsp.setCode(1);
                    rsp.setMsg("图片上传失败！");
                    return rsp;
                }
            } else {
                BaseModel<FileUploadRsp> rsp = new BaseModel<>();
                rsp.setCode(1);
                rsp.setMsg("请选择图片上传！");
                return rsp;
            }
        } else {
            BaseModel<FileUploadRsp> rsp = new BaseModel<>();
            rsp.setCode(1);
            rsp.setMsg("请选择图片上传！");
            return rsp;
        }
    }

    public static class FileUploadForm {

        private MultipartFile boundary;

        /**
         * @return the boundary
         */
        public MultipartFile getBoundary() {
            return boundary;
        }

        /**
         * @param boundary the boundary to set
         */
        public void setBoundary(MultipartFile boundary) {
            this.boundary = boundary;
        }
    }
    
    public static class FileUploadRsp {
        private String path;

        /**
         * @param string
         */
        public FileUploadRsp(String path) {
            // TODO Auto-generated constructor stub
            this.path= path;
        }

        /**
         * @return the url
         */
        @JsonSerialize(using = FileUrlJsonSerializer.class)
        public String getUrl() {
            return path;
        }
    }
    
    public static class FileUrlJsonSerializer extends JsonSerializer<String>  {

        @Override
        public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider provider)
                throws IOException, JsonProcessingException {

            if (value == null || value.length() == 0) {
                jsonGenerator.writeString(value);
            } else {
                jsonGenerator.writeString(imageServer + value.trim());
            }
        }
    }
}

