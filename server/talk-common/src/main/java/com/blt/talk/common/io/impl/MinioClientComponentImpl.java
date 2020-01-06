package com.blt.talk.common.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xmlpull.v1.XmlPullParserException;

import com.blt.talk.common.io.MinioClientComponent;
import com.blt.talk.common.io.config.MinioConfig;
import com.blt.talk.common.io.model.FileEntity;
import com.google.api.client.util.Value;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.NoResponseException;
import io.minio.http.Method;

/**
 * 文件存储
 * 
 * @author yuan'gui
 * @version 1.0
 * @since 2020年1月4日
 */
@Component
public class MinioClientComponentImpl implements MinioClientComponent {

    @Autowired(required = false)
    private MinioConfig minioConfig;

    private MinioClient minioClient;

    @Value("classpath:public-policy.json")
    private Resource publicPolicy;

    private Logger logger = LoggerFactory.getLogger(MinioClientComponentImpl.class);

    @Override
    public String saveImage(InputStream inputStream, String fileName) throws IOException {
        // 文件后缀
        String contentType = getContentType(fileName);
        return saveImage(inputStream, fileName, contentType);
    }

    @Override
    public String saveAuthImage(InputStream inputStream, String fileName) throws IOException {
        String contentType = getContentType(fileName);
        return saveAuthImage(inputStream, fileName, contentType);
    }

    @Override
    public InputStream getAuthImage(String objectName) throws IOException {
        String bucketName = minioConfig.getAuthBucket();
        try {
            return minioClient.getObject(bucketName, objectName);
        } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
                | NoResponseException | ErrorResponseException | InternalException | InvalidArgumentException
                | InvalidResponseException | XmlPullParserException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String saveImage(InputStream inputStream, String fileName, String contentType) throws IOException {
        long size = inputStream.available();
        // 文件后缀
        String objectName = editFileName(fileName);
        try {
            String bucketName = minioConfig.getDefaultBucket();
            touchBucket(bucketName, true);
            minioClient.putObject(minioConfig.getDefaultBucket(), objectName, inputStream, size, null, null,
                    contentType);
            return minioClient.getObjectUrl(bucketName, objectName);
        } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | NoResponseException
                | ErrorResponseException | InternalException | InvalidArgumentException | InsufficientDataException
                | InvalidResponseException | XmlPullParserException e) {
            throw new IOException(e);
        } catch (MinioException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String updateImage(InputStream inputStream, String objectName) throws IOException {
        long size = inputStream.available();
        // 文件后缀
        try {
            String bucketName = minioConfig.getDefaultBucket();
            touchBucket(bucketName, true);
            minioClient.putObject(minioConfig.getDefaultBucket(), objectName, inputStream, size, null, null,
                    getContentType(objectName));
            return minioClient.getObjectUrl(bucketName, objectName);
        } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | NoResponseException
                | ErrorResponseException | InternalException | InvalidArgumentException | InsufficientDataException
                | InvalidResponseException | XmlPullParserException e) {
            throw new IOException(e);
        } catch (MinioException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String saveAuthImage(InputStream inputStream, String fileName, String contentType) throws IOException {
        long size = inputStream.available();
        // 文件后缀
        String objectName = editFileName(fileName);
        try {
            String bucketName = minioConfig.getAuthBucket();
            touchBucket(bucketName);
            minioClient.putObject(bucketName, objectName, inputStream, size, null, null, contentType);
            return objectName;
        } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | NoResponseException
                | ErrorResponseException | InternalException | InvalidArgumentException | InsufficientDataException
                | InvalidResponseException | XmlPullParserException e) {
            throw new IOException(e);
        } catch (MinioException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String saveFile(InputStream inputStream, String fileName, String contentType) throws IOException {
        long size = inputStream.available();
        // 文件后缀
        String objectName = editFileName(fileName);
        try {
            String bucketName = minioConfig.getDefaultBucket();
            touchBucket(bucketName, true);
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "utf-8"));
            minioClient.putObject(minioConfig.getDefaultBucket(), objectName, inputStream, size, headerMap, null,
                    contentType);
            return minioClient.getObjectUrl(bucketName, objectName);
        } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | NoResponseException
                | ErrorResponseException | InternalException | InvalidArgumentException | InsufficientDataException
                | InvalidResponseException | XmlPullParserException e) {
            throw new IOException(e);
        } catch (MinioException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String saveAuthFile(InputStream inputStream, String fileName, String contentType) throws IOException {
        long size = inputStream.available();
        // 文件后缀
        String objectName = editFileName(fileName);
        try {
            String bucketName = minioConfig.getAuthBucket();
            touchBucket(bucketName);

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "utf-8"));
            minioClient.putObject(bucketName, objectName, inputStream, size, headerMap, null, contentType);
            return objectName;
        } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | NoResponseException
                | ErrorResponseException | InternalException | InvalidArgumentException | InsufficientDataException
                | InvalidResponseException | XmlPullParserException e) {
            throw new IOException(e);
        } catch (MinioException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String getAuthFile(String objectName, Integer expires) throws IOException {
        try {
            return minioClient.getPresignedObjectUrl(Method.GET, minioConfig.getAuthBucket(), objectName, expires,
                    null);
        } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
                | NoResponseException | ErrorResponseException | InternalException | InvalidExpiresRangeException
                | InvalidResponseException | XmlPullParserException e) {
            throw new IOException(e);
        }
    }

//    @Override
//    public Object upload(InputStream inputStream, String suffix, String contentType)
//            throws MinioException, InvalidKeyException, NoSuchAlgorithmException, IOException, XmlPullParserException {
//        String objectName = UUID.randomUUID().toString() + "." +  suffix;
//        return upload(minioConfig.getDefaultBucket(), objectName, inputStream, contentType);
//    }
//
//    @Override
//    public Object upload(String bucketName, String objectName, InputStream inputStream, String contentType)
//            throws MinioException, InvalidKeyException, NoSuchAlgorithmException, IOException, XmlPullParserException {
//        long size = inputStream.available();
//        touchBucket(bucketName);
//        minioClient.putObject(bucketName, objectName, inputStream, size, null, null, contentType);
//        return minioClient.getObjectUrl(bucketName, objectName);
//    }

    private void touchBucket(String name)
            throws MinioException, InvalidKeyException, NoSuchAlgorithmException, IOException, XmlPullParserException {
        // 如存储桶不存在，创建之。
        touchBucket(name, false);
    }

    private void touchBucket(String name, boolean isPublic)
            throws MinioException, InvalidKeyException, NoSuchAlgorithmException, IOException, XmlPullParserException {
        // 如存储桶不存在，创建之。
        boolean found = minioClient.bucketExists(name);

        if (!found) {
            // 创建存储桶。
            minioClient.makeBucket(name);

            if (isPublic) {
                String policy = IOUtils.toString(publicPolicy.getInputStream(), Charset.forName("UTF-8"));
                minioClient.setBucketPolicy(name, policy);
            }
        }
    }

    private String editFileName(String fileName) {

        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;

        String suffix = FilenameUtils.getExtension(fileName);
        String ruuid = UUID.randomUUID().toString();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(y).append('/').append(m).append('/').append(ruuid);
        if (!StringUtils.isEmpty(suffix)) {
            stringBuffer.append('.').append(suffix);
        }
        return stringBuffer.toString();
    }

    private String getContentType(String fileName) {
        String suffix = FilenameUtils.getExtension(fileName);

        if ("jpg".equalsIgnoreCase(suffix) || "jpeg".equalsIgnoreCase(suffix)) {
            return "image/jpeg";
        } else if ("png".equalsIgnoreCase(suffix)) {
            return "image/png";
        } else if ("webp".equalsIgnoreCase(suffix)) {
            return "image/webp";
        } else if ("gif".equalsIgnoreCase(suffix)) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }
// 处理配置更新后，即时生效
//    // 使用此方法监听事件
//    @EventListener
//    public void eventListener(RefreshEvent event) {
//        logger.debug("@EventListener {}", event.getSource());
//        // 更新minioClient
//        init();
//    }

    @PostConstruct
    public void init() {
        try {
            logger.info("init minio client start");
            if (minioConfig == null) {
                logger.warn("MINIO未正确配置，文件上传服务将不可用");
            } else {
                minioClient = new MinioClient(minioConfig.getEndpoint(), minioConfig.getPort(),
                        minioConfig.getAccessKey(), minioConfig.getSecretKey());
            }
        } catch (InvalidEndpointException | InvalidPortException e) {
            logger.warn("MINIO未正确配置，文件上传服务将不可用", e);
        } finally {
            logger.info("init minio client end");
        }
    }

    @Override
    public FileEntity getAuthFileContent(String name) throws IOException {

        try {
            ObjectStat stat = minioClient.statObject(minioConfig.getAuthBucket(), name);
            InputStream inputStream = minioClient.getObject(minioConfig.getAuthBucket(), name);

            FileEntity file = new FileEntity();
            file.setLength(stat.length());
            file.setFileName(stat.name()); // TODO 可以通过httpHeaders来处理文件上传时的文件名
            file.setContentType(stat.contentType());
            file.setContent(inputStream);
            return file;

        } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | NoResponseException
                | ErrorResponseException | InternalException | InvalidArgumentException | InsufficientDataException
                | InvalidResponseException | XmlPullParserException e) {
            throw new IOException(e);
        }
    }

}
