package com.blt.talk.common.constant;

/**
 * DB常数类
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class DBConstant {

    /**性别
     * 1. 男性 2.女性
     * */
    public static final int SEX_MAILE = 1;
    public static final int SEX_FEMALE = 2;

    /**msgType*/
    public static final int  MSG_TYPE_SINGLE_TEXT = 0x01;
    public static final int  MSG_TYPE_SINGLE_AUDIO = 0x02;
    public static final int  MSG_TYPE_GROUP_TEXT = 0x11;
    public static final int  MSG_TYPE_GROUP_AUDIO = 0x12;

    /**msgDisplayType
     * 保存在DB中，与服务端一致，图文混排也是一条
     * 1. 最基础的文本信息
     * 2. 纯图片信息
     * 3. 语音
     * 4. 图文混排
     * */
    public static final int SHOW_ORIGIN_TEXT_TYPE = 1;
    public static final int  SHOW_IMAGE_TYPE = 2;
    public static final int  SHOW_AUDIO_TYPE = 3;
    public static final int  SHOW_MIX_TEXT = 4;
    public static final int  SHOW_GIF_TYPE = 5;


    public static final String DISPLAY_FOR_IMAGE = "[图片]";
    public static final String DISPLAY_FOR_MIX = "[图文消息]";
    public static final String DISPLAY_FOR_AUDIO = "[语音]";
    public static final String DISPLAY_FOR_ERROR = "[未知消息]";


    /**sessionType*/
    public static final int  SESSION_TYPE_SINGLE = 1;
    public static final int  SESSION_TYPE_GROUP = 2;
    public static final int SESSION_TYPE_ERROR= 3;

    /**user status
     * 1. 试用期 2. 正式 3. 离职 4.实习
     * */
    public static final int  USER_STATUS_PROBATION = 1;
    public static final int  USER_STATUS_OFFICIAL = 2;
    public static final int  USER_STATUS_LEAVE = 3;
    public static final int  USER_STATUS_INTERNSHIP =4;

   /**group type*/
   public static final int  GROUP_TYPE_NORMAL = 1;
   public static final int  GROUP_TYPE_TEMP = 2;

    /**group status
     * 1: shield  0: not shield
     * */

    public static final byte  GROUP_STATUS_ONLINE = 0;
    public static final byte  GROUP_STATUS_SHIELD = 1;

//    /**group change Type*/
//    public static final byte  GROUP_MODIFY_TYPE_ADD= 0;
//    public static final byte  GROUP_MODIFY_TYPE_DEL =1;

    /** 删除状态（群/Session/Relation/消息等通用、统一）*/
    public static final byte  DELETE_STATUS_OK= 0;
    public static final byte  DELETE_STATUS_DELETE =1;

    /** 无效的ID值 */
    public static final long INVALIAD_VALUE = 0L;
    
}
