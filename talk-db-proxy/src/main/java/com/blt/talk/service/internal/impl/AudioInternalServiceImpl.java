/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal.impl;

import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.service.internal.AudioInternalService;
import com.blt.talk.service.jpa.entity.IMAudio;
import com.blt.talk.service.jpa.repository.IMAudioRepository;

import net.mikesu.fastdfs.FastdfsClient;
import net.mikesu.fastdfs.FastdfsClientFactory;
import net.mikesu.fastdfs.data.BufferFile;

/**
 * 语音处理Service
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Service
public class AudioInternalServiceImpl implements AudioInternalService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IMAudioRepository audioRepository;
    
    /* (non-Javadoc)
     * @see com.blt.talk.service.internal.AudioInternalService#saveAudioInfo(long, long, int, com.google.protobuf.ByteString)
     */
    @Override
    public long saveAudioInfo(long fromId, long toId, int time, byte[] content) {

        int realLen = content.length - 4;
        
        String fileName = fromId + CommonUtils.currentTimeSeconds() + ".spx";
        
        if (realLen <= 0) {
            return DBConstant.INVALIAD_VALUE;
        }

        // 录音长度
        int costTime = CommonUtils.byteArray2int(Arrays.copyOf(content, 4));
        
        // 上传文件
        try {
            FastdfsClient fastdfsClient = FastdfsClientFactory.getFastdfsClient();
            BufferFile fdfsFile = new BufferFile();
            fdfsFile.setName(fileName);
            fdfsFile.setFiledata(content);
            String fileId = fastdfsClient.upload(fdfsFile);

            String path = fileId;
            // 存DB
            IMAudio audio = new IMAudio();
            audio.setFromId(fromId);
            audio.setToId(toId);
            audio.setCreated(time);
            audio.setDuration(costTime);
            audio.setPath(path);
            audio.setSize(realLen);
            
            audio = audioRepository.save(audio);

            return audio.getId();
        } catch (Exception e) {
            logger.warn("语音上传失败！", e);
        }
        
        return DBConstant.INVALIAD_VALUE;
    }

    /* (non-Javadoc)
     * @see com.blt.talk.service.internal.AudioInternalService#readAudioInfo(long)
     */
    @Override
    public byte[] readAudioInfo(long id) {

        IMAudio audio = audioRepository.findOne(id);
        if (audio == null) {
            return null;
        }
        
        // 下载文件
        try {
            FastdfsClient fastdfsClient = FastdfsClientFactory.getFastdfsClient();
            BufferFile audioFile = fastdfsClient.download(audio.getPath());

            return audioFile.getFiledata();
        } catch (Exception e) {
            logger.warn("语音读取失败！", e);
        }
        
        return null;
    }

    /* (non-Javadoc)
     * @see com.blt.talk.service.internal.AudioInternalService#readAudios(java.util.List)
     */
    @Override
    public List<byte[]> readAudios(List<Long> audioIds) {
        
        List<byte[]> audioResult = new ArrayList<>();
                
        for (Long id: audioIds) {
            audioResult.add(readAudioInfo(id));
        }
        
        return audioResult;
    }

}
