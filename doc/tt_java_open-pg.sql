-- ************************************************************
-- 根据TeamTalk开发的JAVA（spring cloud）版服务器用/Postgresql版
-- Version 2017-06-02
--
-- Database: teamtalk
-- Generation Time: 2017-06-01 16:45:30
-- ************************************************************
--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.13
-- Dumped by pg_dump version 9.4.0
-- Started on 2017-06-01 16:45:30

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 7 (class 2615 OID 1033616)
-- Name: teamtalk; Type: SCHEMA; Schema: -; Owner: teamtalk
--

CREATE SCHEMA teamtalk;


ALTER SCHEMA teamtalk OWNER TO teamtalk;

SET search_path = teamtalk, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 1033619)
-- Name: im_admin; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_admin (
    id integer NOT NULL,
    uname character varying(40) NOT NULL,
    pwd character varying(256) NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_admin OWNER TO teamtalk;

--
-- TOC entry 3282 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN im_admin.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_admin.id IS '更新时间´';


--
-- TOC entry 3283 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN im_admin.uname; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_admin.uname IS '用户名';


--
-- TOC entry 3284 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN im_admin.pwd; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_admin.pwd IS '密码';


--
-- TOC entry 3285 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN im_admin.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_admin.status IS '用户状态 0 :正常 1:删除 可扩展';


--
-- TOC entry 3286 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN im_admin.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_admin.created IS '创建时间´';


--
-- TOC entry 3287 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN im_admin.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_admin.updated IS '更新时间´';


--
-- TOC entry 171 (class 1259 OID 1033617)
-- Name: im_admin_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_admin_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_admin_id_seq OWNER TO teamtalk;

--
-- TOC entry 3288 (class 0 OID 0)
-- Dependencies: 171
-- Name: im_admin_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_admin_id_seq OWNED BY im_admin.id;


--
-- TOC entry 174 (class 1259 OID 1033630)
-- Name: im_audio; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_audio (
    id bigint NOT NULL,
    from_id bigint NOT NULL,
    to_id bigint NOT NULL,
    path character varying(255),
    size bigint DEFAULT 0 NOT NULL,
    duration bigint DEFAULT 0 NOT NULL,
    created bigint NOT NULL
);


ALTER TABLE im_audio OWNER TO teamtalk;

--
-- TOC entry 3289 (class 0 OID 0)
-- Dependencies: 174
-- Name: COLUMN im_audio.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_audio.id IS '创建时间';


--
-- TOC entry 3290 (class 0 OID 0)
-- Dependencies: 174
-- Name: COLUMN im_audio.from_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_audio.from_id IS '发送者Id';


--
-- TOC entry 3291 (class 0 OID 0)
-- Dependencies: 174
-- Name: COLUMN im_audio.to_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_audio.to_id IS '接收者Id';


--
-- TOC entry 3292 (class 0 OID 0)
-- Dependencies: 174
-- Name: COLUMN im_audio.path; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_audio.path IS '语音存储的地址';


--
-- TOC entry 3293 (class 0 OID 0)
-- Dependencies: 174
-- Name: COLUMN im_audio.size; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_audio.size IS '文件大小';


--
-- TOC entry 3294 (class 0 OID 0)
-- Dependencies: 174
-- Name: COLUMN im_audio.duration; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_audio.duration IS '语音时长';


--
-- TOC entry 3295 (class 0 OID 0)
-- Dependencies: 174
-- Name: COLUMN im_audio.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_audio.created IS '创建时间';


--
-- TOC entry 173 (class 1259 OID 1033628)
-- Name: im_audio_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_audio_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_audio_id_seq OWNER TO teamtalk;

--
-- TOC entry 3296 (class 0 OID 0)
-- Dependencies: 173
-- Name: im_audio_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_audio_id_seq OWNED BY im_audio.id;


--
-- TOC entry 176 (class 1259 OID 1033641)
-- Name: im_depart; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_depart (
    id bigint NOT NULL,
    depart_name character varying(64) NOT NULL,
    priority bigint DEFAULT 0 NOT NULL,
    parent_id bigint NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    created bigint NOT NULL,
    updated bigint NOT NULL
);


ALTER TABLE im_depart OWNER TO teamtalk;

--
-- TOC entry 3297 (class 0 OID 0)
-- Dependencies: 176
-- Name: COLUMN im_depart.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_depart.id IS '部门id';


--
-- TOC entry 3298 (class 0 OID 0)
-- Dependencies: 176
-- Name: COLUMN im_depart.depart_name; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_depart.depart_name IS '部门名称';


--
-- TOC entry 3299 (class 0 OID 0)
-- Dependencies: 176
-- Name: COLUMN im_depart.priority; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_depart.priority IS '显示优先级';


--
-- TOC entry 3300 (class 0 OID 0)
-- Dependencies: 176
-- Name: COLUMN im_depart.parent_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_depart.parent_id IS '上级部门id';


--
-- TOC entry 3301 (class 0 OID 0)
-- Dependencies: 176
-- Name: COLUMN im_depart.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_depart.status IS '状态';


--
-- TOC entry 3302 (class 0 OID 0)
-- Dependencies: 176
-- Name: COLUMN im_depart.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_depart.created IS '创建时间';


--
-- TOC entry 3303 (class 0 OID 0)
-- Dependencies: 176
-- Name: COLUMN im_depart.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_depart.updated IS '更新时间';


--
-- TOC entry 175 (class 1259 OID 1033639)
-- Name: im_depart_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_depart_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_depart_id_seq OWNER TO teamtalk;

--
-- TOC entry 3304 (class 0 OID 0)
-- Dependencies: 175
-- Name: im_depart_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_depart_id_seq OWNED BY im_depart.id;


--
-- TOC entry 177 (class 1259 OID 1033651)
-- Name: im_discovery; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_discovery (
    id bigint NOT NULL,
    item_name character varying(64) NOT NULL,
    item_url character varying(64) NOT NULL,
    item_priority bigint NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    created bigint NOT NULL,
    updated bigint NOT NULL
);


ALTER TABLE im_discovery OWNER TO teamtalk;

--
-- TOC entry 3305 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN im_discovery.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_discovery.id IS 'id';


--
-- TOC entry 3306 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN im_discovery.item_name; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_discovery.item_name IS '名称';


--
-- TOC entry 3307 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN im_discovery.item_url; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_discovery.item_url IS 'URL';


--
-- TOC entry 3308 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN im_discovery.item_priority; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_discovery.item_priority IS '显示优先级';


--
-- TOC entry 3309 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN im_discovery.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_discovery.status IS '状态';


--
-- TOC entry 3310 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN im_discovery.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_discovery.created IS '创建时间';


--
-- TOC entry 3311 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN im_discovery.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_discovery.updated IS '更新时间';


--
-- TOC entry 179 (class 1259 OID 1033660)
-- Name: im_group; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_group (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    avatar character varying(256) NOT NULL,
    creator bigint DEFAULT 0 NOT NULL,
    type smallint DEFAULT 1::numeric NOT NULL,
    user_cnt bigint DEFAULT 0 NOT NULL,
    status smallint DEFAULT 1::numeric NOT NULL,
    version bigint DEFAULT 1::numeric NOT NULL,
    last_chated bigint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_group OWNER TO teamtalk;

--
-- TOC entry 3312 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN im_group.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group.id IS '创建时间';


--
-- TOC entry 3313 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN im_group.name; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group.name IS '群名称';


--
-- TOC entry 3314 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN im_group.avatar; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group.avatar IS '群头像';


--
-- TOC entry 3315 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN im_group.creator; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group.creator IS '创建者用户id';


--
-- TOC entry 3316 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN im_group.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group.type IS '群组类型，1-固定;2-临时群';


--
-- TOC entry 3317 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN im_group.user_cnt; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group.user_cnt IS '成员人数';


--
-- TOC entry 3318 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN im_group.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group.status IS '是否删除,0-正常，1-删除';


--
-- TOC entry 3319 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN im_group.version; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group.version IS '群版本号';


--
-- TOC entry 3320 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN im_group.last_chated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group.last_chated IS '最后聊天时间';


--
-- TOC entry 3321 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN im_group.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group.updated IS '更新时间';


--
-- TOC entry 3322 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN im_group.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group.created IS '创建时间';


--
-- TOC entry 178 (class 1259 OID 1033658)
-- Name: im_group_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_group_id_seq OWNER TO teamtalk;

--
-- TOC entry 3323 (class 0 OID 0)
-- Dependencies: 178
-- Name: im_group_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_group_id_seq OWNED BY im_group.id;


--
-- TOC entry 181 (class 1259 OID 1033681)
-- Name: im_group_member; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_group_member (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    status smallint DEFAULT 1::numeric NOT NULL,
    created bigint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_group_member OWNER TO teamtalk;

--
-- TOC entry 3324 (class 0 OID 0)
-- Dependencies: 181
-- Name: COLUMN im_group_member.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_member.id IS '更新时间';


--
-- TOC entry 3325 (class 0 OID 0)
-- Dependencies: 181
-- Name: COLUMN im_group_member.group_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_member.group_id IS '群Id';


--
-- TOC entry 3326 (class 0 OID 0)
-- Dependencies: 181
-- Name: COLUMN im_group_member.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_member.user_id IS '用户id';


--
-- TOC entry 3327 (class 0 OID 0)
-- Dependencies: 181
-- Name: COLUMN im_group_member.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_member.status IS '是否退出群，0-正常，1-已退出';


--
-- TOC entry 3328 (class 0 OID 0)
-- Dependencies: 181
-- Name: COLUMN im_group_member.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_member.created IS '创建时间';


--
-- TOC entry 3329 (class 0 OID 0)
-- Dependencies: 181
-- Name: COLUMN im_group_member.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_member.updated IS '更新时间';


--
-- TOC entry 180 (class 1259 OID 1033679)
-- Name: im_group_member_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_group_member_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_group_member_id_seq OWNER TO teamtalk;

--
-- TOC entry 3330 (class 0 OID 0)
-- Dependencies: 180
-- Name: im_group_member_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_group_member_id_seq OWNED BY im_group_member.id;


--
-- TOC entry 183 (class 1259 OID 1033695)
-- Name: im_group_message_0; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_group_message_0 (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096) NOT NULL,
    type smallint DEFAULT 2::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_group_message_0 OWNER TO teamtalk;

--
-- TOC entry 3331 (class 0 OID 0)
-- Dependencies: 183
-- Name: COLUMN im_group_message_0.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_0.id IS '创建时间';


--
-- TOC entry 3332 (class 0 OID 0)
-- Dependencies: 183
-- Name: COLUMN im_group_message_0.group_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_0.group_id IS '用户的关系id';


--
-- TOC entry 3333 (class 0 OID 0)
-- Dependencies: 183
-- Name: COLUMN im_group_message_0.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_0.user_id IS '发送用户的id';


--
-- TOC entry 3334 (class 0 OID 0)
-- Dependencies: 183
-- Name: COLUMN im_group_message_0.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_0.msg_id IS '消息ID';


--
-- TOC entry 3335 (class 0 OID 0)
-- Dependencies: 183
-- Name: COLUMN im_group_message_0.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_0.content IS '消息内容';


--
-- TOC entry 3336 (class 0 OID 0)
-- Dependencies: 183
-- Name: COLUMN im_group_message_0.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_0.type IS '群消息类型,101为群语音,2为文本';


--
-- TOC entry 3337 (class 0 OID 0)
-- Dependencies: 183
-- Name: COLUMN im_group_message_0.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_0.status IS '消息状态';


--
-- TOC entry 3338 (class 0 OID 0)
-- Dependencies: 183
-- Name: COLUMN im_group_message_0.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_0.updated IS '更新时间';


--
-- TOC entry 3339 (class 0 OID 0)
-- Dependencies: 183
-- Name: COLUMN im_group_message_0.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_0.created IS '创建时间';


--
-- TOC entry 182 (class 1259 OID 1033693)
-- Name: im_group_message_0_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_group_message_0_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_group_message_0_id_seq OWNER TO teamtalk;

--
-- TOC entry 3340 (class 0 OID 0)
-- Dependencies: 182
-- Name: im_group_message_0_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_group_message_0_id_seq OWNED BY im_group_message_0.id;


--
-- TOC entry 185 (class 1259 OID 1033712)
-- Name: im_group_message_1; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_group_message_1 (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096) NOT NULL,
    type smallint DEFAULT 2::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_group_message_1 OWNER TO teamtalk;

--
-- TOC entry 3341 (class 0 OID 0)
-- Dependencies: 185
-- Name: COLUMN im_group_message_1.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_1.id IS '创建时间';


--
-- TOC entry 3342 (class 0 OID 0)
-- Dependencies: 185
-- Name: COLUMN im_group_message_1.group_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_1.group_id IS '用户的关系id';


--
-- TOC entry 3343 (class 0 OID 0)
-- Dependencies: 185
-- Name: COLUMN im_group_message_1.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_1.user_id IS '发送用户的id';


--
-- TOC entry 3344 (class 0 OID 0)
-- Dependencies: 185
-- Name: COLUMN im_group_message_1.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_1.msg_id IS '消息ID';


--
-- TOC entry 3345 (class 0 OID 0)
-- Dependencies: 185
-- Name: COLUMN im_group_message_1.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_1.content IS '消息内容';


--
-- TOC entry 3346 (class 0 OID 0)
-- Dependencies: 185
-- Name: COLUMN im_group_message_1.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_1.type IS '群消息类型,101为群语音,2为文本';


--
-- TOC entry 3347 (class 0 OID 0)
-- Dependencies: 185
-- Name: COLUMN im_group_message_1.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_1.status IS '消息状态';


--
-- TOC entry 3348 (class 0 OID 0)
-- Dependencies: 185
-- Name: COLUMN im_group_message_1.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_1.updated IS '更新时间';


--
-- TOC entry 3349 (class 0 OID 0)
-- Dependencies: 185
-- Name: COLUMN im_group_message_1.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_1.created IS '创建时间';


--
-- TOC entry 184 (class 1259 OID 1033710)
-- Name: im_group_message_1_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_group_message_1_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_group_message_1_id_seq OWNER TO teamtalk;

--
-- TOC entry 3350 (class 0 OID 0)
-- Dependencies: 184
-- Name: im_group_message_1_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_group_message_1_id_seq OWNED BY im_group_message_1.id;


--
-- TOC entry 187 (class 1259 OID 1033729)
-- Name: im_group_message_2; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_group_message_2 (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096) NOT NULL,
    type smallint DEFAULT 2::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_group_message_2 OWNER TO teamtalk;

--
-- TOC entry 3351 (class 0 OID 0)
-- Dependencies: 187
-- Name: COLUMN im_group_message_2.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_2.id IS '创建时间';


--
-- TOC entry 3352 (class 0 OID 0)
-- Dependencies: 187
-- Name: COLUMN im_group_message_2.group_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_2.group_id IS '用户的关系id';


--
-- TOC entry 3353 (class 0 OID 0)
-- Dependencies: 187
-- Name: COLUMN im_group_message_2.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_2.user_id IS '发送用户的id';


--
-- TOC entry 3354 (class 0 OID 0)
-- Dependencies: 187
-- Name: COLUMN im_group_message_2.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_2.msg_id IS '消息ID';


--
-- TOC entry 3355 (class 0 OID 0)
-- Dependencies: 187
-- Name: COLUMN im_group_message_2.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_2.content IS '消息内容';


--
-- TOC entry 3356 (class 0 OID 0)
-- Dependencies: 187
-- Name: COLUMN im_group_message_2.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_2.type IS '群消息类型,101为群语音,2为文本';


--
-- TOC entry 3357 (class 0 OID 0)
-- Dependencies: 187
-- Name: COLUMN im_group_message_2.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_2.status IS '消息状态';


--
-- TOC entry 3358 (class 0 OID 0)
-- Dependencies: 187
-- Name: COLUMN im_group_message_2.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_2.updated IS '更新时间';


--
-- TOC entry 3359 (class 0 OID 0)
-- Dependencies: 187
-- Name: COLUMN im_group_message_2.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_2.created IS '创建时间';


--
-- TOC entry 186 (class 1259 OID 1033727)
-- Name: im_group_message_2_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_group_message_2_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_group_message_2_id_seq OWNER TO teamtalk;

--
-- TOC entry 3360 (class 0 OID 0)
-- Dependencies: 186
-- Name: im_group_message_2_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_group_message_2_id_seq OWNED BY im_group_message_2.id;


--
-- TOC entry 189 (class 1259 OID 1033746)
-- Name: im_group_message_3; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_group_message_3 (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096) NOT NULL,
    type smallint DEFAULT 2::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_group_message_3 OWNER TO teamtalk;

--
-- TOC entry 3361 (class 0 OID 0)
-- Dependencies: 189
-- Name: COLUMN im_group_message_3.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_3.id IS '创建时间';


--
-- TOC entry 3362 (class 0 OID 0)
-- Dependencies: 189
-- Name: COLUMN im_group_message_3.group_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_3.group_id IS '用户的关系id';


--
-- TOC entry 3363 (class 0 OID 0)
-- Dependencies: 189
-- Name: COLUMN im_group_message_3.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_3.user_id IS '发送用户的id';


--
-- TOC entry 3364 (class 0 OID 0)
-- Dependencies: 189
-- Name: COLUMN im_group_message_3.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_3.msg_id IS '消息ID';


--
-- TOC entry 3365 (class 0 OID 0)
-- Dependencies: 189
-- Name: COLUMN im_group_message_3.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_3.content IS '消息内容';


--
-- TOC entry 3366 (class 0 OID 0)
-- Dependencies: 189
-- Name: COLUMN im_group_message_3.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_3.type IS '群消息类型,101为群语音,2为文本';


--
-- TOC entry 3367 (class 0 OID 0)
-- Dependencies: 189
-- Name: COLUMN im_group_message_3.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_3.status IS '消息状态';


--
-- TOC entry 3368 (class 0 OID 0)
-- Dependencies: 189
-- Name: COLUMN im_group_message_3.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_3.updated IS '更新时间';


--
-- TOC entry 3369 (class 0 OID 0)
-- Dependencies: 189
-- Name: COLUMN im_group_message_3.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_3.created IS '创建时间';


--
-- TOC entry 188 (class 1259 OID 1033744)
-- Name: im_group_message_3_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_group_message_3_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_group_message_3_id_seq OWNER TO teamtalk;

--
-- TOC entry 3370 (class 0 OID 0)
-- Dependencies: 188
-- Name: im_group_message_3_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_group_message_3_id_seq OWNED BY im_group_message_3.id;


--
-- TOC entry 191 (class 1259 OID 1033763)
-- Name: im_group_message_4; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_group_message_4 (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096) NOT NULL,
    type smallint DEFAULT 2::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_group_message_4 OWNER TO teamtalk;

--
-- TOC entry 3371 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN im_group_message_4.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_4.id IS '创建时间';


--
-- TOC entry 3372 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN im_group_message_4.group_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_4.group_id IS '用户的关系id';


--
-- TOC entry 3373 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN im_group_message_4.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_4.user_id IS '发送用户的id';


--
-- TOC entry 3374 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN im_group_message_4.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_4.msg_id IS '消息ID';


--
-- TOC entry 3375 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN im_group_message_4.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_4.content IS '消息内容';


--
-- TOC entry 3376 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN im_group_message_4.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_4.type IS '群消息类型,101为群语音,2为文本';


--
-- TOC entry 3377 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN im_group_message_4.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_4.status IS '消息状态';


--
-- TOC entry 3378 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN im_group_message_4.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_4.updated IS '更新时间';


--
-- TOC entry 3379 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN im_group_message_4.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_4.created IS '创建时间';


--
-- TOC entry 190 (class 1259 OID 1033761)
-- Name: im_group_message_4_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_group_message_4_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_group_message_4_id_seq OWNER TO teamtalk;

--
-- TOC entry 3380 (class 0 OID 0)
-- Dependencies: 190
-- Name: im_group_message_4_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_group_message_4_id_seq OWNED BY im_group_message_4.id;


--
-- TOC entry 193 (class 1259 OID 1033780)
-- Name: im_group_message_5; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_group_message_5 (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096) NOT NULL,
    type smallint DEFAULT 2::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_group_message_5 OWNER TO teamtalk;

--
-- TOC entry 3381 (class 0 OID 0)
-- Dependencies: 193
-- Name: COLUMN im_group_message_5.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_5.id IS '创建时间';


--
-- TOC entry 3382 (class 0 OID 0)
-- Dependencies: 193
-- Name: COLUMN im_group_message_5.group_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_5.group_id IS '用户的关系id';


--
-- TOC entry 3383 (class 0 OID 0)
-- Dependencies: 193
-- Name: COLUMN im_group_message_5.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_5.user_id IS '发送用户的id';


--
-- TOC entry 3384 (class 0 OID 0)
-- Dependencies: 193
-- Name: COLUMN im_group_message_5.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_5.msg_id IS '消息ID';


--
-- TOC entry 3385 (class 0 OID 0)
-- Dependencies: 193
-- Name: COLUMN im_group_message_5.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_5.content IS '消息内容';


--
-- TOC entry 3386 (class 0 OID 0)
-- Dependencies: 193
-- Name: COLUMN im_group_message_5.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_5.type IS '群消息类型,101为群语音,2为文本';


--
-- TOC entry 3387 (class 0 OID 0)
-- Dependencies: 193
-- Name: COLUMN im_group_message_5.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_5.status IS '消息状态';


--
-- TOC entry 3388 (class 0 OID 0)
-- Dependencies: 193
-- Name: COLUMN im_group_message_5.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_5.updated IS '更新时间';


--
-- TOC entry 3389 (class 0 OID 0)
-- Dependencies: 193
-- Name: COLUMN im_group_message_5.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_5.created IS '创建时间';


--
-- TOC entry 192 (class 1259 OID 1033778)
-- Name: im_group_message_5_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_group_message_5_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_group_message_5_id_seq OWNER TO teamtalk;

--
-- TOC entry 3390 (class 0 OID 0)
-- Dependencies: 192
-- Name: im_group_message_5_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_group_message_5_id_seq OWNED BY im_group_message_5.id;


--
-- TOC entry 195 (class 1259 OID 1033797)
-- Name: im_group_message_6; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_group_message_6 (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096) NOT NULL,
    type smallint DEFAULT 2::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_group_message_6 OWNER TO teamtalk;

--
-- TOC entry 3391 (class 0 OID 0)
-- Dependencies: 195
-- Name: COLUMN im_group_message_6.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_6.id IS '创建时间';


--
-- TOC entry 3392 (class 0 OID 0)
-- Dependencies: 195
-- Name: COLUMN im_group_message_6.group_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_6.group_id IS '用户的关系id';


--
-- TOC entry 3393 (class 0 OID 0)
-- Dependencies: 195
-- Name: COLUMN im_group_message_6.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_6.user_id IS '发送用户的id';


--
-- TOC entry 3394 (class 0 OID 0)
-- Dependencies: 195
-- Name: COLUMN im_group_message_6.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_6.msg_id IS '消息ID';


--
-- TOC entry 3395 (class 0 OID 0)
-- Dependencies: 195
-- Name: COLUMN im_group_message_6.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_6.content IS '消息内容';


--
-- TOC entry 3396 (class 0 OID 0)
-- Dependencies: 195
-- Name: COLUMN im_group_message_6.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_6.type IS '群消息类型,101为群语音,2为文本';


--
-- TOC entry 3397 (class 0 OID 0)
-- Dependencies: 195
-- Name: COLUMN im_group_message_6.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_6.status IS '消息状态';


--
-- TOC entry 3398 (class 0 OID 0)
-- Dependencies: 195
-- Name: COLUMN im_group_message_6.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_6.updated IS '更新时间';


--
-- TOC entry 3399 (class 0 OID 0)
-- Dependencies: 195
-- Name: COLUMN im_group_message_6.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_6.created IS '创建时间';


--
-- TOC entry 194 (class 1259 OID 1033795)
-- Name: im_group_message_6_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_group_message_6_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_group_message_6_id_seq OWNER TO teamtalk;

--
-- TOC entry 3400 (class 0 OID 0)
-- Dependencies: 194
-- Name: im_group_message_6_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_group_message_6_id_seq OWNED BY im_group_message_6.id;


--
-- TOC entry 197 (class 1259 OID 1033814)
-- Name: im_group_message_7; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_group_message_7 (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096) NOT NULL,
    type smallint DEFAULT 2::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_group_message_7 OWNER TO teamtalk;

--
-- TOC entry 3401 (class 0 OID 0)
-- Dependencies: 197
-- Name: COLUMN im_group_message_7.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_7.id IS '创建时间';


--
-- TOC entry 3402 (class 0 OID 0)
-- Dependencies: 197
-- Name: COLUMN im_group_message_7.group_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_7.group_id IS '用户的关系id';


--
-- TOC entry 3403 (class 0 OID 0)
-- Dependencies: 197
-- Name: COLUMN im_group_message_7.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_7.user_id IS '发送用户的id';


--
-- TOC entry 3404 (class 0 OID 0)
-- Dependencies: 197
-- Name: COLUMN im_group_message_7.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_7.msg_id IS '消息ID';


--
-- TOC entry 3405 (class 0 OID 0)
-- Dependencies: 197
-- Name: COLUMN im_group_message_7.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_7.content IS '消息内容';


--
-- TOC entry 3406 (class 0 OID 0)
-- Dependencies: 197
-- Name: COLUMN im_group_message_7.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_7.type IS '群消息类型,101为群语音,2为文本';


--
-- TOC entry 3407 (class 0 OID 0)
-- Dependencies: 197
-- Name: COLUMN im_group_message_7.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_7.status IS '消息状态';


--
-- TOC entry 3408 (class 0 OID 0)
-- Dependencies: 197
-- Name: COLUMN im_group_message_7.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_7.updated IS '更新时间';


--
-- TOC entry 3409 (class 0 OID 0)
-- Dependencies: 197
-- Name: COLUMN im_group_message_7.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_7.created IS '创建时间';


--
-- TOC entry 196 (class 1259 OID 1033812)
-- Name: im_group_message_7_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_group_message_7_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_group_message_7_id_seq OWNER TO teamtalk;

--
-- TOC entry 3410 (class 0 OID 0)
-- Dependencies: 196
-- Name: im_group_message_7_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_group_message_7_id_seq OWNED BY im_group_message_7.id;


--
-- TOC entry 199 (class 1259 OID 1033831)
-- Name: im_group_message_8; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_group_message_8 (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096) NOT NULL,
    type smallint DEFAULT 2::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_group_message_8 OWNER TO teamtalk;

--
-- TOC entry 3411 (class 0 OID 0)
-- Dependencies: 199
-- Name: COLUMN im_group_message_8.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_8.id IS '创建时间';


--
-- TOC entry 3412 (class 0 OID 0)
-- Dependencies: 199
-- Name: COLUMN im_group_message_8.group_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_8.group_id IS '用户的关系id';


--
-- TOC entry 3413 (class 0 OID 0)
-- Dependencies: 199
-- Name: COLUMN im_group_message_8.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_8.user_id IS '发送用户的id';


--
-- TOC entry 3414 (class 0 OID 0)
-- Dependencies: 199
-- Name: COLUMN im_group_message_8.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_8.msg_id IS '消息ID';


--
-- TOC entry 3415 (class 0 OID 0)
-- Dependencies: 199
-- Name: COLUMN im_group_message_8.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_8.content IS '消息内容';


--
-- TOC entry 3416 (class 0 OID 0)
-- Dependencies: 199
-- Name: COLUMN im_group_message_8.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_8.type IS '群消息类型,101为群语音,2为文本';


--
-- TOC entry 3417 (class 0 OID 0)
-- Dependencies: 199
-- Name: COLUMN im_group_message_8.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_8.status IS '消息状态';


--
-- TOC entry 3418 (class 0 OID 0)
-- Dependencies: 199
-- Name: COLUMN im_group_message_8.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_8.updated IS '更新时间';


--
-- TOC entry 3419 (class 0 OID 0)
-- Dependencies: 199
-- Name: COLUMN im_group_message_8.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_8.created IS '创建时间';


--
-- TOC entry 198 (class 1259 OID 1033829)
-- Name: im_group_message_8_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_group_message_8_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_group_message_8_id_seq OWNER TO teamtalk;

--
-- TOC entry 3420 (class 0 OID 0)
-- Dependencies: 198
-- Name: im_group_message_8_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_group_message_8_id_seq OWNED BY im_group_message_8.id;


--
-- TOC entry 201 (class 1259 OID 1033848)
-- Name: im_group_message_9; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_group_message_9 (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096) NOT NULL,
    type smallint DEFAULT 2::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_group_message_9 OWNER TO teamtalk;

--
-- TOC entry 3421 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN im_group_message_9.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_9.id IS '创建时间';


--
-- TOC entry 3422 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN im_group_message_9.group_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_9.group_id IS '用户的关系id';


--
-- TOC entry 3423 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN im_group_message_9.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_9.user_id IS '发送用户的id';


--
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN im_group_message_9.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_9.msg_id IS '消息ID';


--
-- TOC entry 3425 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN im_group_message_9.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_9.content IS '消息内容';


--
-- TOC entry 3426 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN im_group_message_9.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_9.type IS '群消息类型,101为群语音,2为文本';


--
-- TOC entry 3427 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN im_group_message_9.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_9.status IS '消息状态';


--
-- TOC entry 3428 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN im_group_message_9.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_9.updated IS '更新时间';


--
-- TOC entry 3429 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN im_group_message_9.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_group_message_9.created IS '创建时间';


--
-- TOC entry 200 (class 1259 OID 1033846)
-- Name: im_group_message_9_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_group_message_9_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_group_message_9_id_seq OWNER TO teamtalk;

--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 200
-- Name: im_group_message_9_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_group_message_9_id_seq OWNED BY im_group_message_9.id;


--
-- TOC entry 203 (class 1259 OID 1033865)
-- Name: im_message_0; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_message_0 (
    id bigint NOT NULL,
    relate_id bigint NOT NULL,
    user_id bigint NOT NULL,
    to_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096),
    type smallint DEFAULT 1::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint NOT NULL,
    created bigint NOT NULL
);


ALTER TABLE im_message_0 OWNER TO teamtalk;

--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN im_message_0.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_0.id IS '创建时间';


--
-- TOC entry 3432 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN im_message_0.relate_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_0.relate_id IS '用户的关系id';


--
-- TOC entry 3433 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN im_message_0.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_0.user_id IS '发送用户的id';


--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN im_message_0.to_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_0.to_id IS '接收用户的id';


--
-- TOC entry 3435 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN im_message_0.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_0.msg_id IS '消息ID';


--
-- TOC entry 3436 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN im_message_0.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_0.content IS '消息内容';


--
-- TOC entry 3437 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN im_message_0.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_0.type IS '消息类型';


--
-- TOC entry 3438 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN im_message_0.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_0.status IS '0正常 1被删除';


--
-- TOC entry 3439 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN im_message_0.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_0.updated IS '更新时间';


--
-- TOC entry 3440 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN im_message_0.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_0.created IS '创建时间';


--
-- TOC entry 202 (class 1259 OID 1033863)
-- Name: im_message_0_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_message_0_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_message_0_id_seq OWNER TO teamtalk;

--
-- TOC entry 3441 (class 0 OID 0)
-- Dependencies: 202
-- Name: im_message_0_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_message_0_id_seq OWNED BY im_message_0.id;


--
-- TOC entry 205 (class 1259 OID 1033881)
-- Name: im_message_1; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_message_1 (
    id bigint NOT NULL,
    relate_id bigint NOT NULL,
    user_id bigint NOT NULL,
    to_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096),
    type smallint DEFAULT 1::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint NOT NULL,
    created bigint NOT NULL
);


ALTER TABLE im_message_1 OWNER TO teamtalk;

--
-- TOC entry 3442 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN im_message_1.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_1.id IS '创建时间';


--
-- TOC entry 3443 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN im_message_1.relate_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_1.relate_id IS '用户的关系id';


--
-- TOC entry 3444 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN im_message_1.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_1.user_id IS '发送用户的id';


--
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN im_message_1.to_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_1.to_id IS '接收用户的id';


--
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN im_message_1.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_1.msg_id IS '消息ID';


--
-- TOC entry 3447 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN im_message_1.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_1.content IS '消息内容';


--
-- TOC entry 3448 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN im_message_1.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_1.type IS '消息类型';


--
-- TOC entry 3449 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN im_message_1.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_1.status IS '0正常 1被删除';


--
-- TOC entry 3450 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN im_message_1.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_1.updated IS '更新时间';


--
-- TOC entry 3451 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN im_message_1.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_1.created IS '创建时间';


--
-- TOC entry 204 (class 1259 OID 1033879)
-- Name: im_message_1_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_message_1_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_message_1_id_seq OWNER TO teamtalk;

--
-- TOC entry 3452 (class 0 OID 0)
-- Dependencies: 204
-- Name: im_message_1_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_message_1_id_seq OWNED BY im_message_1.id;


--
-- TOC entry 207 (class 1259 OID 1033897)
-- Name: im_message_2; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_message_2 (
    id bigint NOT NULL,
    relate_id bigint NOT NULL,
    user_id bigint NOT NULL,
    to_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096),
    type smallint DEFAULT 1::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint NOT NULL,
    created bigint NOT NULL
);


ALTER TABLE im_message_2 OWNER TO teamtalk;

--
-- TOC entry 3453 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN im_message_2.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_2.id IS '创建时间';


--
-- TOC entry 3454 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN im_message_2.relate_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_2.relate_id IS '用户的关系id';


--
-- TOC entry 3455 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN im_message_2.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_2.user_id IS '发送用户的id';


--
-- TOC entry 3456 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN im_message_2.to_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_2.to_id IS '接收用户的id';


--
-- TOC entry 3457 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN im_message_2.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_2.msg_id IS '消息ID';


--
-- TOC entry 3458 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN im_message_2.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_2.content IS '消息内容';


--
-- TOC entry 3459 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN im_message_2.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_2.type IS '消息类型';


--
-- TOC entry 3460 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN im_message_2.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_2.status IS '0正常 1被删除';


--
-- TOC entry 3461 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN im_message_2.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_2.updated IS '更新时间';


--
-- TOC entry 3462 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN im_message_2.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_2.created IS '创建时间';


--
-- TOC entry 206 (class 1259 OID 1033895)
-- Name: im_message_2_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_message_2_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_message_2_id_seq OWNER TO teamtalk;

--
-- TOC entry 3463 (class 0 OID 0)
-- Dependencies: 206
-- Name: im_message_2_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_message_2_id_seq OWNED BY im_message_2.id;


--
-- TOC entry 209 (class 1259 OID 1033913)
-- Name: im_message_3; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_message_3 (
    id bigint NOT NULL,
    relate_id bigint NOT NULL,
    user_id bigint NOT NULL,
    to_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096),
    type smallint DEFAULT 1::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint NOT NULL,
    created bigint NOT NULL
);


ALTER TABLE im_message_3 OWNER TO teamtalk;

--
-- TOC entry 3464 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN im_message_3.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_3.id IS '创建时间';


--
-- TOC entry 3465 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN im_message_3.relate_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_3.relate_id IS '用户的关系id';


--
-- TOC entry 3466 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN im_message_3.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_3.user_id IS '发送用户的id';


--
-- TOC entry 3467 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN im_message_3.to_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_3.to_id IS '接收用户的id';


--
-- TOC entry 3468 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN im_message_3.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_3.msg_id IS '消息ID';


--
-- TOC entry 3469 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN im_message_3.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_3.content IS '消息内容';


--
-- TOC entry 3470 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN im_message_3.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_3.type IS '消息类型';


--
-- TOC entry 3471 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN im_message_3.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_3.status IS '0正常 1被删除';


--
-- TOC entry 3472 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN im_message_3.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_3.updated IS '更新时间';


--
-- TOC entry 3473 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN im_message_3.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_3.created IS '创建时间';


--
-- TOC entry 208 (class 1259 OID 1033911)
-- Name: im_message_3_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_message_3_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_message_3_id_seq OWNER TO teamtalk;

--
-- TOC entry 3474 (class 0 OID 0)
-- Dependencies: 208
-- Name: im_message_3_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_message_3_id_seq OWNED BY im_message_3.id;


--
-- TOC entry 211 (class 1259 OID 1033929)
-- Name: im_message_4; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_message_4 (
    id bigint NOT NULL,
    relate_id bigint NOT NULL,
    user_id bigint NOT NULL,
    to_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096),
    type smallint DEFAULT 1::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint NOT NULL,
    created bigint NOT NULL
);


ALTER TABLE im_message_4 OWNER TO teamtalk;

--
-- TOC entry 3475 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN im_message_4.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_4.id IS '创建时间';


--
-- TOC entry 3476 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN im_message_4.relate_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_4.relate_id IS '用户的关系id';


--
-- TOC entry 3477 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN im_message_4.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_4.user_id IS '发送用户的id';


--
-- TOC entry 3478 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN im_message_4.to_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_4.to_id IS '接收用户的id';


--
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN im_message_4.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_4.msg_id IS '消息ID';


--
-- TOC entry 3480 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN im_message_4.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_4.content IS '消息内容';


--
-- TOC entry 3481 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN im_message_4.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_4.type IS '消息类型';


--
-- TOC entry 3482 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN im_message_4.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_4.status IS '0正常 1被删除';


--
-- TOC entry 3483 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN im_message_4.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_4.updated IS '更新时间';


--
-- TOC entry 3484 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN im_message_4.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_4.created IS '创建时间';


--
-- TOC entry 210 (class 1259 OID 1033927)
-- Name: im_message_4_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_message_4_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_message_4_id_seq OWNER TO teamtalk;

--
-- TOC entry 3485 (class 0 OID 0)
-- Dependencies: 210
-- Name: im_message_4_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_message_4_id_seq OWNED BY im_message_4.id;


--
-- TOC entry 213 (class 1259 OID 1033945)
-- Name: im_message_5; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_message_5 (
    id bigint NOT NULL,
    relate_id bigint NOT NULL,
    user_id bigint NOT NULL,
    to_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096),
    type smallint DEFAULT 1::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint NOT NULL,
    created bigint NOT NULL
);


ALTER TABLE im_message_5 OWNER TO teamtalk;

--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN im_message_5.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_5.id IS '创建时间';


--
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN im_message_5.relate_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_5.relate_id IS '用户的关系id';


--
-- TOC entry 3488 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN im_message_5.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_5.user_id IS '发送用户的id';


--
-- TOC entry 3489 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN im_message_5.to_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_5.to_id IS '接收用户的id';


--
-- TOC entry 3490 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN im_message_5.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_5.msg_id IS '消息ID';


--
-- TOC entry 3491 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN im_message_5.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_5.content IS '消息内容';


--
-- TOC entry 3492 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN im_message_5.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_5.type IS '消息类型';


--
-- TOC entry 3493 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN im_message_5.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_5.status IS '0正常 1被删除';


--
-- TOC entry 3494 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN im_message_5.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_5.updated IS '更新时间';


--
-- TOC entry 3495 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN im_message_5.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_5.created IS '创建时间';


--
-- TOC entry 212 (class 1259 OID 1033943)
-- Name: im_message_5_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_message_5_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_message_5_id_seq OWNER TO teamtalk;

--
-- TOC entry 3496 (class 0 OID 0)
-- Dependencies: 212
-- Name: im_message_5_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_message_5_id_seq OWNED BY im_message_5.id;


--
-- TOC entry 215 (class 1259 OID 1033961)
-- Name: im_message_6; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_message_6 (
    id bigint NOT NULL,
    relate_id bigint NOT NULL,
    user_id bigint NOT NULL,
    to_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096),
    type smallint DEFAULT 1::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint NOT NULL,
    created bigint NOT NULL
);


ALTER TABLE im_message_6 OWNER TO teamtalk;

--
-- TOC entry 3497 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN im_message_6.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_6.id IS '创建时间';


--
-- TOC entry 3498 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN im_message_6.relate_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_6.relate_id IS '用户的关系id';


--
-- TOC entry 3499 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN im_message_6.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_6.user_id IS '发送用户的id';


--
-- TOC entry 3500 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN im_message_6.to_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_6.to_id IS '接收用户的id';


--
-- TOC entry 3501 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN im_message_6.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_6.msg_id IS '消息ID';


--
-- TOC entry 3502 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN im_message_6.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_6.content IS '消息内容';


--
-- TOC entry 3503 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN im_message_6.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_6.type IS '消息类型';


--
-- TOC entry 3504 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN im_message_6.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_6.status IS '0正常 1被删除';


--
-- TOC entry 3505 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN im_message_6.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_6.updated IS '更新时间';


--
-- TOC entry 3506 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN im_message_6.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_6.created IS '创建时间';


--
-- TOC entry 214 (class 1259 OID 1033959)
-- Name: im_message_6_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_message_6_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_message_6_id_seq OWNER TO teamtalk;

--
-- TOC entry 3507 (class 0 OID 0)
-- Dependencies: 214
-- Name: im_message_6_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_message_6_id_seq OWNED BY im_message_6.id;


--
-- TOC entry 217 (class 1259 OID 1033977)
-- Name: im_message_7; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_message_7 (
    id bigint NOT NULL,
    relate_id bigint NOT NULL,
    user_id bigint NOT NULL,
    to_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096),
    type smallint DEFAULT 1::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint NOT NULL,
    created bigint NOT NULL
);


ALTER TABLE im_message_7 OWNER TO teamtalk;

--
-- TOC entry 3508 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN im_message_7.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_7.id IS '创建时间';


--
-- TOC entry 3509 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN im_message_7.relate_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_7.relate_id IS '用户的关系id';


--
-- TOC entry 3510 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN im_message_7.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_7.user_id IS '发送用户的id';


--
-- TOC entry 3511 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN im_message_7.to_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_7.to_id IS '接收用户的id';


--
-- TOC entry 3512 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN im_message_7.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_7.msg_id IS '消息ID';


--
-- TOC entry 3513 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN im_message_7.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_7.content IS '消息内容';


--
-- TOC entry 3514 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN im_message_7.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_7.type IS '消息类型';


--
-- TOC entry 3515 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN im_message_7.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_7.status IS '0正常 1被删除';


--
-- TOC entry 3516 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN im_message_7.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_7.updated IS '更新时间';


--
-- TOC entry 3517 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN im_message_7.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_7.created IS '创建时间';


--
-- TOC entry 216 (class 1259 OID 1033975)
-- Name: im_message_7_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_message_7_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_message_7_id_seq OWNER TO teamtalk;

--
-- TOC entry 3518 (class 0 OID 0)
-- Dependencies: 216
-- Name: im_message_7_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_message_7_id_seq OWNED BY im_message_7.id;


--
-- TOC entry 219 (class 1259 OID 1033993)
-- Name: im_message_8; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_message_8 (
    id bigint NOT NULL,
    relate_id bigint NOT NULL,
    user_id bigint NOT NULL,
    to_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096),
    type smallint DEFAULT 1::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint NOT NULL,
    created bigint NOT NULL
);


ALTER TABLE im_message_8 OWNER TO teamtalk;

--
-- TOC entry 3519 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN im_message_8.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_8.id IS '创建时间';


--
-- TOC entry 3520 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN im_message_8.relate_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_8.relate_id IS '用户的关系id';


--
-- TOC entry 3521 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN im_message_8.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_8.user_id IS '发送用户的id';


--
-- TOC entry 3522 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN im_message_8.to_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_8.to_id IS '接收用户的id';


--
-- TOC entry 3523 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN im_message_8.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_8.msg_id IS '消息ID';


--
-- TOC entry 3524 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN im_message_8.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_8.content IS '消息内容';


--
-- TOC entry 3525 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN im_message_8.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_8.type IS '消息类型';


--
-- TOC entry 3526 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN im_message_8.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_8.status IS '0正常 1被删除';


--
-- TOC entry 3527 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN im_message_8.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_8.updated IS '更新时间';


--
-- TOC entry 3528 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN im_message_8.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_8.created IS '创建时间';


--
-- TOC entry 218 (class 1259 OID 1033991)
-- Name: im_message_8_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_message_8_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_message_8_id_seq OWNER TO teamtalk;

--
-- TOC entry 3529 (class 0 OID 0)
-- Dependencies: 218
-- Name: im_message_8_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_message_8_id_seq OWNED BY im_message_8.id;


--
-- TOC entry 221 (class 1259 OID 1034009)
-- Name: im_message_9; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_message_9 (
    id bigint NOT NULL,
    relate_id bigint NOT NULL,
    user_id bigint NOT NULL,
    to_id bigint NOT NULL,
    msg_id bigint NOT NULL,
    content character varying(4096),
    type smallint DEFAULT 1::numeric NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    updated bigint NOT NULL,
    created bigint NOT NULL
);


ALTER TABLE im_message_9 OWNER TO teamtalk;

--
-- TOC entry 3530 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN im_message_9.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_9.id IS '创建时间';


--
-- TOC entry 3531 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN im_message_9.relate_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_9.relate_id IS '用户的关系id';


--
-- TOC entry 3532 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN im_message_9.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_9.user_id IS '发送用户的id';


--
-- TOC entry 3533 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN im_message_9.to_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_9.to_id IS '接收用户的id';


--
-- TOC entry 3534 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN im_message_9.msg_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_9.msg_id IS '消息ID';


--
-- TOC entry 3535 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN im_message_9.content; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_9.content IS '消息内容';


--
-- TOC entry 3536 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN im_message_9.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_9.type IS '消息类型';


--
-- TOC entry 3537 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN im_message_9.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_9.status IS '0正常 1被删除';


--
-- TOC entry 3538 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN im_message_9.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_9.updated IS '更新时间';


--
-- TOC entry 3539 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN im_message_9.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_message_9.created IS '创建时间';


--
-- TOC entry 220 (class 1259 OID 1034007)
-- Name: im_message_9_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_message_9_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_message_9_id_seq OWNER TO teamtalk;

--
-- TOC entry 3540 (class 0 OID 0)
-- Dependencies: 220
-- Name: im_message_9_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_message_9_id_seq OWNED BY im_message_9.id;


--
-- TOC entry 223 (class 1259 OID 1034025)
-- Name: im_recent_session; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_recent_session (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    peer_id bigint NOT NULL,
    type smallint DEFAULT 0,
    status smallint DEFAULT 0,
    created bigint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_recent_session OWNER TO teamtalk;

--
-- TOC entry 3541 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN im_recent_session.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_recent_session.id IS '更新时间';


--
-- TOC entry 3542 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN im_recent_session.user_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_recent_session.user_id IS '用户id';


--
-- TOC entry 3543 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN im_recent_session.peer_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_recent_session.peer_id IS '对方id';


--
-- TOC entry 3544 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN im_recent_session.type; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_recent_session.type IS '类型，1-用户,2-群组';


--
-- TOC entry 3545 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN im_recent_session.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_recent_session.status IS '用户:0-正常, 1-用户A删除,群组:0-正常, 1-被删除';


--
-- TOC entry 3546 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN im_recent_session.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_recent_session.created IS '创建时间';


--
-- TOC entry 3547 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN im_recent_session.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_recent_session.updated IS '更新时间';


--
-- TOC entry 222 (class 1259 OID 1034023)
-- Name: im_recent_session_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_recent_session_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_recent_session_id_seq OWNER TO teamtalk;

--
-- TOC entry 3548 (class 0 OID 0)
-- Dependencies: 222
-- Name: im_recent_session_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_recent_session_id_seq OWNED BY im_recent_session.id;


--
-- TOC entry 225 (class 1259 OID 1034039)
-- Name: im_relation_ship; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_relation_ship (
    id bigint NOT NULL,
    small_id bigint NOT NULL,
    big_id bigint NOT NULL,
    status smallint DEFAULT 0,
    created bigint DEFAULT 0 NOT NULL,
    updated bigint DEFAULT 0 NOT NULL
);


ALTER TABLE im_relation_ship OWNER TO teamtalk;

--
-- TOC entry 3549 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN im_relation_ship.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_relation_ship.id IS '更新时间';


--
-- TOC entry 3550 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN im_relation_ship.small_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_relation_ship.small_id IS '用户A的id';


--
-- TOC entry 3551 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN im_relation_ship.big_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_relation_ship.big_id IS '用户B的id';


--
-- TOC entry 3552 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN im_relation_ship.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_relation_ship.status IS '用户:0-正常, 1-用户A删除,群组:0-正常, 1-被删除';


--
-- TOC entry 3553 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN im_relation_ship.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_relation_ship.created IS '创建时间';


--
-- TOC entry 3554 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN im_relation_ship.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_relation_ship.updated IS '更新时间';


--
-- TOC entry 224 (class 1259 OID 1034037)
-- Name: im_relation_ship_id_seq; Type: SEQUENCE; Schema: teamtalk; Owner: teamtalk
--

CREATE SEQUENCE im_relation_ship_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE im_relation_ship_id_seq OWNER TO teamtalk;

--
-- TOC entry 3555 (class 0 OID 0)
-- Dependencies: 224
-- Name: im_relation_ship_id_seq; Type: SEQUENCE OWNED BY; Schema: teamtalk; Owner: teamtalk
--

ALTER SEQUENCE im_relation_ship_id_seq OWNED BY im_relation_ship.id;


--
-- TOC entry 226 (class 1259 OID 1034049)
-- Name: im_user; Type: TABLE; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE TABLE im_user (
    id bigint NOT NULL,
    sex smallint DEFAULT 0 NOT NULL,
    name character varying(32) NOT NULL,
    domain character varying(32) NOT NULL,
    nick character varying(32) NOT NULL,
    password character varying(256) NOT NULL,
    salt character varying(4) NOT NULL,
    phone character varying(11) NOT NULL,
    email character varying(64) NOT NULL,
    avatar character varying(255),
    depart_id bigint NOT NULL,
    status smallint DEFAULT 0,
    created bigint NOT NULL,
    updated bigint NOT NULL,
    push_shield_status smallint DEFAULT 0 NOT NULL,
    sign_info character varying(128) NOT NULL
);


ALTER TABLE im_user OWNER TO teamtalk;

--
-- TOC entry 3556 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.id IS '用户id';


--
-- TOC entry 3557 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.sex; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.sex IS '1男2女0未知';


--
-- TOC entry 3558 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.name; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.name IS '用户名';


--
-- TOC entry 3559 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.domain; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.domain IS '拼音';


--
-- TOC entry 3560 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.nick; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.nick IS '花名,绰号等';


--
-- TOC entry 3561 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.password; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.password IS '密码';


--
-- TOC entry 3562 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.salt; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.salt IS '混淆码';


--
-- TOC entry 3563 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.phone; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.phone IS '手机号码';


--
-- TOC entry 3564 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.email; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.email IS 'email';


--
-- TOC entry 3565 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.avatar; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.avatar IS '自定义用户头像';


--
-- TOC entry 3566 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.depart_id; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.depart_id IS '所属部门Id';


--
-- TOC entry 3567 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.status IS '1. 试用期 2. 正式 3. 离职 4.实习';


--
-- TOC entry 3568 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.created; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.created IS '创建时间';


--
-- TOC entry 3569 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.updated; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.updated IS '更新时间';


--
-- TOC entry 3570 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.push_shield_status; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.push_shield_status IS '0关闭勿扰 1开启勿扰';


--
-- TOC entry 3571 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN im_user.sign_info; Type: COMMENT; Schema: teamtalk; Owner: teamtalk
--

COMMENT ON COLUMN im_user.sign_info IS '个性签名';


--
-- TOC entry 2876 (class 2604 OID 1033622)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_admin ALTER COLUMN id SET DEFAULT nextval('im_admin_id_seq'::regclass);


--
-- TOC entry 2880 (class 2604 OID 1033633)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_audio ALTER COLUMN id SET DEFAULT nextval('im_audio_id_seq'::regclass);


--
-- TOC entry 2883 (class 2604 OID 1033644)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_depart ALTER COLUMN id SET DEFAULT nextval('im_depart_id_seq'::regclass);


--
-- TOC entry 2887 (class 2604 OID 1033663)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_group ALTER COLUMN id SET DEFAULT nextval('im_group_id_seq'::regclass);


--
-- TOC entry 2896 (class 2604 OID 1033684)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_group_member ALTER COLUMN id SET DEFAULT nextval('im_group_member_id_seq'::regclass);


--
-- TOC entry 2900 (class 2604 OID 1033698)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_group_message_0 ALTER COLUMN id SET DEFAULT nextval('im_group_message_0_id_seq'::regclass);


--
-- TOC entry 2905 (class 2604 OID 1033715)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_group_message_1 ALTER COLUMN id SET DEFAULT nextval('im_group_message_1_id_seq'::regclass);


--
-- TOC entry 2910 (class 2604 OID 1033732)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_group_message_2 ALTER COLUMN id SET DEFAULT nextval('im_group_message_2_id_seq'::regclass);


--
-- TOC entry 2915 (class 2604 OID 1033749)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_group_message_3 ALTER COLUMN id SET DEFAULT nextval('im_group_message_3_id_seq'::regclass);


--
-- TOC entry 2920 (class 2604 OID 1033766)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_group_message_4 ALTER COLUMN id SET DEFAULT nextval('im_group_message_4_id_seq'::regclass);


--
-- TOC entry 2925 (class 2604 OID 1033783)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_group_message_5 ALTER COLUMN id SET DEFAULT nextval('im_group_message_5_id_seq'::regclass);


--
-- TOC entry 2930 (class 2604 OID 1033800)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_group_message_6 ALTER COLUMN id SET DEFAULT nextval('im_group_message_6_id_seq'::regclass);


--
-- TOC entry 2935 (class 2604 OID 1033817)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_group_message_7 ALTER COLUMN id SET DEFAULT nextval('im_group_message_7_id_seq'::regclass);


--
-- TOC entry 2940 (class 2604 OID 1033834)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_group_message_8 ALTER COLUMN id SET DEFAULT nextval('im_group_message_8_id_seq'::regclass);


--
-- TOC entry 2945 (class 2604 OID 1033851)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_group_message_9 ALTER COLUMN id SET DEFAULT nextval('im_group_message_9_id_seq'::regclass);


--
-- TOC entry 2950 (class 2604 OID 1033868)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_message_0 ALTER COLUMN id SET DEFAULT nextval('im_message_0_id_seq'::regclass);


--
-- TOC entry 2953 (class 2604 OID 1033884)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_message_1 ALTER COLUMN id SET DEFAULT nextval('im_message_1_id_seq'::regclass);


--
-- TOC entry 2956 (class 2604 OID 1033900)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_message_2 ALTER COLUMN id SET DEFAULT nextval('im_message_2_id_seq'::regclass);


--
-- TOC entry 2959 (class 2604 OID 1033916)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_message_3 ALTER COLUMN id SET DEFAULT nextval('im_message_3_id_seq'::regclass);


--
-- TOC entry 2962 (class 2604 OID 1033932)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_message_4 ALTER COLUMN id SET DEFAULT nextval('im_message_4_id_seq'::regclass);


--
-- TOC entry 2965 (class 2604 OID 1033948)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_message_5 ALTER COLUMN id SET DEFAULT nextval('im_message_5_id_seq'::regclass);


--
-- TOC entry 2968 (class 2604 OID 1033964)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_message_6 ALTER COLUMN id SET DEFAULT nextval('im_message_6_id_seq'::regclass);


--
-- TOC entry 2971 (class 2604 OID 1033980)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_message_7 ALTER COLUMN id SET DEFAULT nextval('im_message_7_id_seq'::regclass);


--
-- TOC entry 2974 (class 2604 OID 1033996)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_message_8 ALTER COLUMN id SET DEFAULT nextval('im_message_8_id_seq'::regclass);


--
-- TOC entry 2977 (class 2604 OID 1034012)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_message_9 ALTER COLUMN id SET DEFAULT nextval('im_message_9_id_seq'::regclass);


--
-- TOC entry 2980 (class 2604 OID 1034028)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_recent_session ALTER COLUMN id SET DEFAULT nextval('im_recent_session_id_seq'::regclass);


--
-- TOC entry 2985 (class 2604 OID 1034042)
-- Name: id; Type: DEFAULT; Schema: teamtalk; Owner: teamtalk
--

ALTER TABLE ONLY im_relation_ship ALTER COLUMN id SET DEFAULT nextval('im_relation_ship_id_seq'::regclass);


--
-- TOC entry 3223 (class 0 OID 1033619)
-- Dependencies: 172
-- Data for Name: im_admin; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_admin (id, uname, pwd, status, created, updated) FROM stdin;
\.


--
-- TOC entry 3572 (class 0 OID 0)
-- Dependencies: 171
-- Name: im_admin_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_admin_id_seq', 1, false);


--
-- TOC entry 3225 (class 0 OID 1033630)
-- Dependencies: 174
-- Data for Name: im_audio; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_audio (id, from_id, to_id, path, size, duration, created) FROM stdin;
\.


--
-- TOC entry 3573 (class 0 OID 0)
-- Dependencies: 173
-- Name: im_audio_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_audio_id_seq', 1, false);


--
-- TOC entry 3227 (class 0 OID 1033641)
-- Dependencies: 176
-- Data for Name: im_depart; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_depart (id, depart_name, priority, parent_id, status, created, updated) FROM stdin;
\.


--
-- TOC entry 3574 (class 0 OID 0)
-- Dependencies: 175
-- Name: im_depart_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_depart_id_seq', 1, false);


--
-- TOC entry 3228 (class 0 OID 1033651)
-- Dependencies: 177
-- Data for Name: im_discovery; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_discovery (id, item_name, item_url, item_priority, status, created, updated) FROM stdin;
\.


--
-- TOC entry 3230 (class 0 OID 1033660)
-- Dependencies: 179
-- Data for Name: im_group; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_group (id, name, avatar, creator, type, user_cnt, status, version, last_chated, updated, created) FROM stdin;
\.


--
-- TOC entry 3575 (class 0 OID 0)
-- Dependencies: 178
-- Name: im_group_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_group_id_seq', 1, false);


--
-- TOC entry 3232 (class 0 OID 1033681)
-- Dependencies: 181
-- Data for Name: im_group_member; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_group_member (id, group_id, user_id, status, created, updated) FROM stdin;
\.


--
-- TOC entry 3576 (class 0 OID 0)
-- Dependencies: 180
-- Name: im_group_member_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_group_member_id_seq', 1, false);


--
-- TOC entry 3234 (class 0 OID 1033695)
-- Dependencies: 183
-- Data for Name: im_group_message_0; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_group_message_0 (id, group_id, user_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3577 (class 0 OID 0)
-- Dependencies: 182
-- Name: im_group_message_0_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_group_message_0_id_seq', 1, false);


--
-- TOC entry 3236 (class 0 OID 1033712)
-- Dependencies: 185
-- Data for Name: im_group_message_1; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_group_message_1 (id, group_id, user_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3578 (class 0 OID 0)
-- Dependencies: 184
-- Name: im_group_message_1_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_group_message_1_id_seq', 1, false);


--
-- TOC entry 3238 (class 0 OID 1033729)
-- Dependencies: 187
-- Data for Name: im_group_message_2; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_group_message_2 (id, group_id, user_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3579 (class 0 OID 0)
-- Dependencies: 186
-- Name: im_group_message_2_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_group_message_2_id_seq', 1, false);


--
-- TOC entry 3240 (class 0 OID 1033746)
-- Dependencies: 189
-- Data for Name: im_group_message_3; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_group_message_3 (id, group_id, user_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3580 (class 0 OID 0)
-- Dependencies: 188
-- Name: im_group_message_3_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_group_message_3_id_seq', 1, false);


--
-- TOC entry 3242 (class 0 OID 1033763)
-- Dependencies: 191
-- Data for Name: im_group_message_4; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_group_message_4 (id, group_id, user_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3581 (class 0 OID 0)
-- Dependencies: 190
-- Name: im_group_message_4_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_group_message_4_id_seq', 1, false);


--
-- TOC entry 3244 (class 0 OID 1033780)
-- Dependencies: 193
-- Data for Name: im_group_message_5; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_group_message_5 (id, group_id, user_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3582 (class 0 OID 0)
-- Dependencies: 192
-- Name: im_group_message_5_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_group_message_5_id_seq', 1, false);


--
-- TOC entry 3246 (class 0 OID 1033797)
-- Dependencies: 195
-- Data for Name: im_group_message_6; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_group_message_6 (id, group_id, user_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3583 (class 0 OID 0)
-- Dependencies: 194
-- Name: im_group_message_6_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_group_message_6_id_seq', 1, false);


--
-- TOC entry 3248 (class 0 OID 1033814)
-- Dependencies: 197
-- Data for Name: im_group_message_7; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_group_message_7 (id, group_id, user_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3584 (class 0 OID 0)
-- Dependencies: 196
-- Name: im_group_message_7_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_group_message_7_id_seq', 1, false);


--
-- TOC entry 3250 (class 0 OID 1033831)
-- Dependencies: 199
-- Data for Name: im_group_message_8; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_group_message_8 (id, group_id, user_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3585 (class 0 OID 0)
-- Dependencies: 198
-- Name: im_group_message_8_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_group_message_8_id_seq', 1, false);


--
-- TOC entry 3252 (class 0 OID 1033848)
-- Dependencies: 201
-- Data for Name: im_group_message_9; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_group_message_9 (id, group_id, user_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3586 (class 0 OID 0)
-- Dependencies: 200
-- Name: im_group_message_9_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_group_message_9_id_seq', 1, false);


--
-- TOC entry 3254 (class 0 OID 1033865)
-- Dependencies: 203
-- Data for Name: im_message_0; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_message_0 (id, relate_id, user_id, to_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3587 (class 0 OID 0)
-- Dependencies: 202
-- Name: im_message_0_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_message_0_id_seq', 1, false);


--
-- TOC entry 3256 (class 0 OID 1033881)
-- Dependencies: 205
-- Data for Name: im_message_1; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_message_1 (id, relate_id, user_id, to_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3588 (class 0 OID 0)
-- Dependencies: 204
-- Name: im_message_1_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_message_1_id_seq', 1, false);


--
-- TOC entry 3258 (class 0 OID 1033897)
-- Dependencies: 207
-- Data for Name: im_message_2; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_message_2 (id, relate_id, user_id, to_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3589 (class 0 OID 0)
-- Dependencies: 206
-- Name: im_message_2_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_message_2_id_seq', 1, false);


--
-- TOC entry 3260 (class 0 OID 1033913)
-- Dependencies: 209
-- Data for Name: im_message_3; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_message_3 (id, relate_id, user_id, to_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3590 (class 0 OID 0)
-- Dependencies: 208
-- Name: im_message_3_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_message_3_id_seq', 1, false);


--
-- TOC entry 3262 (class 0 OID 1033929)
-- Dependencies: 211
-- Data for Name: im_message_4; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_message_4 (id, relate_id, user_id, to_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3591 (class 0 OID 0)
-- Dependencies: 210
-- Name: im_message_4_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_message_4_id_seq', 1, false);


--
-- TOC entry 3264 (class 0 OID 1033945)
-- Dependencies: 213
-- Data for Name: im_message_5; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_message_5 (id, relate_id, user_id, to_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3592 (class 0 OID 0)
-- Dependencies: 212
-- Name: im_message_5_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_message_5_id_seq', 1, false);


--
-- TOC entry 3266 (class 0 OID 1033961)
-- Dependencies: 215
-- Data for Name: im_message_6; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_message_6 (id, relate_id, user_id, to_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3593 (class 0 OID 0)
-- Dependencies: 214
-- Name: im_message_6_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_message_6_id_seq', 1, false);


--
-- TOC entry 3268 (class 0 OID 1033977)
-- Dependencies: 217
-- Data for Name: im_message_7; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_message_7 (id, relate_id, user_id, to_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3594 (class 0 OID 0)
-- Dependencies: 216
-- Name: im_message_7_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_message_7_id_seq', 1, false);


--
-- TOC entry 3270 (class 0 OID 1033993)
-- Dependencies: 219
-- Data for Name: im_message_8; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_message_8 (id, relate_id, user_id, to_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3595 (class 0 OID 0)
-- Dependencies: 218
-- Name: im_message_8_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_message_8_id_seq', 1, false);


--
-- TOC entry 3272 (class 0 OID 1034009)
-- Dependencies: 221
-- Data for Name: im_message_9; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_message_9 (id, relate_id, user_id, to_id, msg_id, content, type, status, updated, created) FROM stdin;
\.


--
-- TOC entry 3596 (class 0 OID 0)
-- Dependencies: 220
-- Name: im_message_9_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_message_9_id_seq', 1, false);


--
-- TOC entry 3274 (class 0 OID 1034025)
-- Dependencies: 223
-- Data for Name: im_recent_session; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_recent_session (id, user_id, peer_id, type, status, created, updated) FROM stdin;
\.


--
-- TOC entry 3597 (class 0 OID 0)
-- Dependencies: 222
-- Name: im_recent_session_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_recent_session_id_seq', 1, false);


--
-- TOC entry 3276 (class 0 OID 1034039)
-- Dependencies: 225
-- Data for Name: im_relation_ship; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_relation_ship (id, small_id, big_id, status, created, updated) FROM stdin;
\.


--
-- TOC entry 3598 (class 0 OID 0)
-- Dependencies: 224
-- Name: im_relation_ship_id_seq; Type: SEQUENCE SET; Schema: teamtalk; Owner: teamtalk
--

SELECT pg_catalog.setval('im_relation_ship_id_seq', 1, false);


--
-- TOC entry 3277 (class 0 OID 1034049)
-- Dependencies: 226
-- Data for Name: im_user; Type: TABLE DATA; Schema: teamtalk; Owner: teamtalk
--

COPY im_user (id, sex, name, domain, nick, password, salt, phone, email, avatar, depart_id, status, created, updated, push_shield_status, sign_info) FROM stdin;
\.


--
-- TOC entry 2993 (class 2606 OID 1033627)
-- Name: im_admin_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_admin
    ADD CONSTRAINT im_admin_pkey PRIMARY KEY (id);


--
-- TOC entry 2996 (class 2606 OID 1033637)
-- Name: im_audio_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_audio
    ADD CONSTRAINT im_audio_pkey PRIMARY KEY (id);


--
-- TOC entry 3000 (class 2606 OID 1033648)
-- Name: im_depart_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_depart
    ADD CONSTRAINT im_depart_pkey PRIMARY KEY (id);


--
-- TOC entry 3003 (class 2606 OID 1033656)
-- Name: im_discovery_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_discovery
    ADD CONSTRAINT im_discovery_pkey PRIMARY KEY (id);


--
-- TOC entry 3012 (class 2606 OID 1033689)
-- Name: im_group_member_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_group_member
    ADD CONSTRAINT im_group_member_pkey PRIMARY KEY (id);


--
-- TOC entry 3016 (class 2606 OID 1033707)
-- Name: im_group_message_0_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_group_message_0
    ADD CONSTRAINT im_group_message_0_pkey PRIMARY KEY (id);


--
-- TOC entry 3020 (class 2606 OID 1033724)
-- Name: im_group_message_1_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_group_message_1
    ADD CONSTRAINT im_group_message_1_pkey PRIMARY KEY (id);


--
-- TOC entry 3024 (class 2606 OID 1033741)
-- Name: im_group_message_2_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_group_message_2
    ADD CONSTRAINT im_group_message_2_pkey PRIMARY KEY (id);


--
-- TOC entry 3028 (class 2606 OID 1033758)
-- Name: im_group_message_3_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_group_message_3
    ADD CONSTRAINT im_group_message_3_pkey PRIMARY KEY (id);


--
-- TOC entry 3032 (class 2606 OID 1033775)
-- Name: im_group_message_4_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_group_message_4
    ADD CONSTRAINT im_group_message_4_pkey PRIMARY KEY (id);


--
-- TOC entry 3036 (class 2606 OID 1033792)
-- Name: im_group_message_5_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_group_message_5
    ADD CONSTRAINT im_group_message_5_pkey PRIMARY KEY (id);


--
-- TOC entry 3040 (class 2606 OID 1033809)
-- Name: im_group_message_6_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_group_message_6
    ADD CONSTRAINT im_group_message_6_pkey PRIMARY KEY (id);


--
-- TOC entry 3044 (class 2606 OID 1033826)
-- Name: im_group_message_7_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_group_message_7
    ADD CONSTRAINT im_group_message_7_pkey PRIMARY KEY (id);


--
-- TOC entry 3048 (class 2606 OID 1033843)
-- Name: im_group_message_8_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_group_message_8
    ADD CONSTRAINT im_group_message_8_pkey PRIMARY KEY (id);


--
-- TOC entry 3052 (class 2606 OID 1033860)
-- Name: im_group_message_9_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_group_message_9
    ADD CONSTRAINT im_group_message_9_pkey PRIMARY KEY (id);


--
-- TOC entry 3007 (class 2606 OID 1033676)
-- Name: im_group_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_group
    ADD CONSTRAINT im_group_pkey PRIMARY KEY (id);


--
-- TOC entry 3057 (class 2606 OID 1033875)
-- Name: im_message_0_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_message_0
    ADD CONSTRAINT im_message_0_pkey PRIMARY KEY (id);


--
-- TOC entry 3062 (class 2606 OID 1033891)
-- Name: im_message_1_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_message_1
    ADD CONSTRAINT im_message_1_pkey PRIMARY KEY (id);


--
-- TOC entry 3067 (class 2606 OID 1033907)
-- Name: im_message_2_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_message_2
    ADD CONSTRAINT im_message_2_pkey PRIMARY KEY (id);


--
-- TOC entry 3072 (class 2606 OID 1033923)
-- Name: im_message_3_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_message_3
    ADD CONSTRAINT im_message_3_pkey PRIMARY KEY (id);


--
-- TOC entry 3077 (class 2606 OID 1033939)
-- Name: im_message_4_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_message_4
    ADD CONSTRAINT im_message_4_pkey PRIMARY KEY (id);


--
-- TOC entry 3082 (class 2606 OID 1033955)
-- Name: im_message_5_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_message_5
    ADD CONSTRAINT im_message_5_pkey PRIMARY KEY (id);


--
-- TOC entry 3087 (class 2606 OID 1033971)
-- Name: im_message_6_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_message_6
    ADD CONSTRAINT im_message_6_pkey PRIMARY KEY (id);


--
-- TOC entry 3092 (class 2606 OID 1033987)
-- Name: im_message_7_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_message_7
    ADD CONSTRAINT im_message_7_pkey PRIMARY KEY (id);


--
-- TOC entry 3097 (class 2606 OID 1034003)
-- Name: im_message_8_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_message_8
    ADD CONSTRAINT im_message_8_pkey PRIMARY KEY (id);


--
-- TOC entry 3102 (class 2606 OID 1034019)
-- Name: im_message_9_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_message_9
    ADD CONSTRAINT im_message_9_pkey PRIMARY KEY (id);


--
-- TOC entry 3106 (class 2606 OID 1034034)
-- Name: im_recent_session_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_recent_session
    ADD CONSTRAINT im_recent_session_pkey PRIMARY KEY (id);


--
-- TOC entry 3109 (class 2606 OID 1034047)
-- Name: im_relation_ship_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_relation_ship
    ADD CONSTRAINT im_relation_ship_pkey PRIMARY KEY (id);


--
-- TOC entry 3114 (class 2606 OID 1034059)
-- Name: im_user_pkey; Type: CONSTRAINT; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

ALTER TABLE ONLY im_user
    ADD CONSTRAINT im_user_pkey PRIMARY KEY (id);


--
-- TOC entry 2994 (class 1259 OID 1033638)
-- Name: im_audio_idx_from_id_to_id; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_audio_idx_from_id_to_id ON im_audio USING btree (from_id, to_id);


--
-- TOC entry 2997 (class 1259 OID 1033649)
-- Name: im_depart_idx_depart_name; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_depart_idx_depart_name ON im_depart USING btree (depart_name);


--
-- TOC entry 2998 (class 1259 OID 1033650)
-- Name: im_depart_idx_priority_status; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_depart_idx_priority_status ON im_depart USING btree (priority, status);


--
-- TOC entry 3001 (class 1259 OID 1033657)
-- Name: im_discovery_idx_item_name; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_discovery_idx_item_name ON im_discovery USING btree (item_name);


--
-- TOC entry 3004 (class 1259 OID 1033678)
-- Name: im_group_idx_creator; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_idx_creator ON im_group USING btree (creator);


--
-- TOC entry 3005 (class 1259 OID 1033677)
-- Name: im_group_idx_name; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_idx_name ON im_group USING btree (name);


--
-- TOC entry 3008 (class 1259 OID 1033692)
-- Name: im_group_member_idx_group_id_updated; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_member_idx_group_id_updated ON im_group_member USING btree (group_id, updated);


--
-- TOC entry 3009 (class 1259 OID 1033690)
-- Name: im_group_member_idx_group_id_user_id_status; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_member_idx_group_id_user_id_status ON im_group_member USING btree (group_id, user_id, status);


--
-- TOC entry 3010 (class 1259 OID 1033691)
-- Name: im_group_member_idx_user_id_status_updated; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_member_idx_user_id_status_updated ON im_group_member USING btree (user_id, status, updated);


--
-- TOC entry 3013 (class 1259 OID 1033709)
-- Name: im_group_message_0_idx_group_id_msg_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_0_idx_group_id_msg_id_status_created ON im_group_message_0 USING btree (group_id, msg_id, status, created);


--
-- TOC entry 3014 (class 1259 OID 1033708)
-- Name: im_group_message_0_idx_group_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_0_idx_group_id_status_created ON im_group_message_0 USING btree (group_id, status, created);


--
-- TOC entry 3017 (class 1259 OID 1033726)
-- Name: im_group_message_1_idx_group_id_msg_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_1_idx_group_id_msg_id_status_created ON im_group_message_1 USING btree (group_id, msg_id, status, created);


--
-- TOC entry 3018 (class 1259 OID 1033725)
-- Name: im_group_message_1_idx_group_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_1_idx_group_id_status_created ON im_group_message_1 USING btree (group_id, status, created);


--
-- TOC entry 3021 (class 1259 OID 1033743)
-- Name: im_group_message_2_idx_group_id_msg_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_2_idx_group_id_msg_id_status_created ON im_group_message_2 USING btree (group_id, msg_id, status, created);


--
-- TOC entry 3022 (class 1259 OID 1033742)
-- Name: im_group_message_2_idx_group_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_2_idx_group_id_status_created ON im_group_message_2 USING btree (group_id, status, created);


--
-- TOC entry 3025 (class 1259 OID 1033760)
-- Name: im_group_message_3_idx_group_id_msg_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_3_idx_group_id_msg_id_status_created ON im_group_message_3 USING btree (group_id, msg_id, status, created);


--
-- TOC entry 3026 (class 1259 OID 1033759)
-- Name: im_group_message_3_idx_group_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_3_idx_group_id_status_created ON im_group_message_3 USING btree (group_id, status, created);


--
-- TOC entry 3029 (class 1259 OID 1033777)
-- Name: im_group_message_4_idx_group_id_msg_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_4_idx_group_id_msg_id_status_created ON im_group_message_4 USING btree (group_id, msg_id, status, created);


--
-- TOC entry 3030 (class 1259 OID 1033776)
-- Name: im_group_message_4_idx_group_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_4_idx_group_id_status_created ON im_group_message_4 USING btree (group_id, status, created);


--
-- TOC entry 3033 (class 1259 OID 1033794)
-- Name: im_group_message_5_idx_group_id_msg_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_5_idx_group_id_msg_id_status_created ON im_group_message_5 USING btree (group_id, msg_id, status, created);


--
-- TOC entry 3034 (class 1259 OID 1033793)
-- Name: im_group_message_5_idx_group_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_5_idx_group_id_status_created ON im_group_message_5 USING btree (group_id, status, created);


--
-- TOC entry 3037 (class 1259 OID 1033811)
-- Name: im_group_message_6_idx_group_id_msg_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_6_idx_group_id_msg_id_status_created ON im_group_message_6 USING btree (group_id, msg_id, status, created);


--
-- TOC entry 3038 (class 1259 OID 1033810)
-- Name: im_group_message_6_idx_group_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_6_idx_group_id_status_created ON im_group_message_6 USING btree (group_id, status, created);


--
-- TOC entry 3041 (class 1259 OID 1033828)
-- Name: im_group_message_7_idx_group_id_msg_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_7_idx_group_id_msg_id_status_created ON im_group_message_7 USING btree (group_id, msg_id, status, created);


--
-- TOC entry 3042 (class 1259 OID 1033827)
-- Name: im_group_message_7_idx_group_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_7_idx_group_id_status_created ON im_group_message_7 USING btree (group_id, status, created);


--
-- TOC entry 3045 (class 1259 OID 1033845)
-- Name: im_group_message_8_idx_group_id_msg_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_8_idx_group_id_msg_id_status_created ON im_group_message_8 USING btree (group_id, msg_id, status, created);


--
-- TOC entry 3046 (class 1259 OID 1033844)
-- Name: im_group_message_8_idx_group_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_8_idx_group_id_status_created ON im_group_message_8 USING btree (group_id, status, created);


--
-- TOC entry 3049 (class 1259 OID 1033862)
-- Name: im_group_message_9_idx_group_id_msg_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_9_idx_group_id_msg_id_status_created ON im_group_message_9 USING btree (group_id, msg_id, status, created);


--
-- TOC entry 3050 (class 1259 OID 1033861)
-- Name: im_group_message_9_idx_group_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_group_message_9_idx_group_id_status_created ON im_group_message_9 USING btree (group_id, status, created);


--
-- TOC entry 3053 (class 1259 OID 1033878)
-- Name: im_message_0_idx_from_id_to_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_0_idx_from_id_to_id_created ON im_message_0 USING btree (user_id, to_id, status);


--
-- TOC entry 3054 (class 1259 OID 1033876)
-- Name: im_message_0_idx_relate_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_0_idx_relate_id_status_created ON im_message_0 USING btree (relate_id, status, created);


--
-- TOC entry 3055 (class 1259 OID 1033877)
-- Name: im_message_0_idx_relate_id_status_msg_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_0_idx_relate_id_status_msg_id_created ON im_message_0 USING btree (relate_id, status, msg_id, created);


--
-- TOC entry 3058 (class 1259 OID 1033894)
-- Name: im_message_1_idx_from_id_to_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_1_idx_from_id_to_id_created ON im_message_1 USING btree (user_id, to_id, status);


--
-- TOC entry 3059 (class 1259 OID 1033892)
-- Name: im_message_1_idx_relate_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_1_idx_relate_id_status_created ON im_message_1 USING btree (relate_id, status, created);


--
-- TOC entry 3060 (class 1259 OID 1033893)
-- Name: im_message_1_idx_relate_id_status_msg_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_1_idx_relate_id_status_msg_id_created ON im_message_1 USING btree (relate_id, status, msg_id, created);


--
-- TOC entry 3063 (class 1259 OID 1033910)
-- Name: im_message_2_idx_from_id_to_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_2_idx_from_id_to_id_created ON im_message_2 USING btree (user_id, to_id, status);


--
-- TOC entry 3064 (class 1259 OID 1033908)
-- Name: im_message_2_idx_relate_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_2_idx_relate_id_status_created ON im_message_2 USING btree (relate_id, status, created);


--
-- TOC entry 3065 (class 1259 OID 1033909)
-- Name: im_message_2_idx_relate_id_status_msg_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_2_idx_relate_id_status_msg_id_created ON im_message_2 USING btree (relate_id, status, msg_id, created);


--
-- TOC entry 3068 (class 1259 OID 1033926)
-- Name: im_message_3_idx_from_id_to_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_3_idx_from_id_to_id_created ON im_message_3 USING btree (user_id, to_id, status);


--
-- TOC entry 3069 (class 1259 OID 1033924)
-- Name: im_message_3_idx_relate_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_3_idx_relate_id_status_created ON im_message_3 USING btree (relate_id, status, created);


--
-- TOC entry 3070 (class 1259 OID 1033925)
-- Name: im_message_3_idx_relate_id_status_msg_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_3_idx_relate_id_status_msg_id_created ON im_message_3 USING btree (relate_id, status, msg_id, created);


--
-- TOC entry 3073 (class 1259 OID 1033942)
-- Name: im_message_4_idx_from_id_to_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_4_idx_from_id_to_id_created ON im_message_4 USING btree (user_id, to_id, status);


--
-- TOC entry 3074 (class 1259 OID 1033940)
-- Name: im_message_4_idx_relate_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_4_idx_relate_id_status_created ON im_message_4 USING btree (relate_id, status, created);


--
-- TOC entry 3075 (class 1259 OID 1033941)
-- Name: im_message_4_idx_relate_id_status_msg_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_4_idx_relate_id_status_msg_id_created ON im_message_4 USING btree (relate_id, status, msg_id, created);


--
-- TOC entry 3078 (class 1259 OID 1033958)
-- Name: im_message_5_idx_from_id_to_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_5_idx_from_id_to_id_created ON im_message_5 USING btree (user_id, to_id, status);


--
-- TOC entry 3079 (class 1259 OID 1033956)
-- Name: im_message_5_idx_relate_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_5_idx_relate_id_status_created ON im_message_5 USING btree (relate_id, status, created);


--
-- TOC entry 3080 (class 1259 OID 1033957)
-- Name: im_message_5_idx_relate_id_status_msg_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_5_idx_relate_id_status_msg_id_created ON im_message_5 USING btree (relate_id, status, msg_id, created);


--
-- TOC entry 3083 (class 1259 OID 1033974)
-- Name: im_message_6_idx_from_id_to_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_6_idx_from_id_to_id_created ON im_message_6 USING btree (user_id, to_id, status);


--
-- TOC entry 3084 (class 1259 OID 1033972)
-- Name: im_message_6_idx_relate_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_6_idx_relate_id_status_created ON im_message_6 USING btree (relate_id, status, created);


--
-- TOC entry 3085 (class 1259 OID 1033973)
-- Name: im_message_6_idx_relate_id_status_msg_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_6_idx_relate_id_status_msg_id_created ON im_message_6 USING btree (relate_id, status, msg_id, created);


--
-- TOC entry 3088 (class 1259 OID 1033990)
-- Name: im_message_7_idx_from_id_to_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_7_idx_from_id_to_id_created ON im_message_7 USING btree (user_id, to_id, status);


--
-- TOC entry 3089 (class 1259 OID 1033988)
-- Name: im_message_7_idx_relate_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_7_idx_relate_id_status_created ON im_message_7 USING btree (relate_id, status, created);


--
-- TOC entry 3090 (class 1259 OID 1033989)
-- Name: im_message_7_idx_relate_id_status_msg_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_7_idx_relate_id_status_msg_id_created ON im_message_7 USING btree (relate_id, status, msg_id, created);


--
-- TOC entry 3093 (class 1259 OID 1034006)
-- Name: im_message_8_idx_from_id_to_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_8_idx_from_id_to_id_created ON im_message_8 USING btree (user_id, to_id, status);


--
-- TOC entry 3094 (class 1259 OID 1034004)
-- Name: im_message_8_idx_relate_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_8_idx_relate_id_status_created ON im_message_8 USING btree (relate_id, status, created);


--
-- TOC entry 3095 (class 1259 OID 1034005)
-- Name: im_message_8_idx_relate_id_status_msg_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_8_idx_relate_id_status_msg_id_created ON im_message_8 USING btree (relate_id, status, msg_id, created);


--
-- TOC entry 3098 (class 1259 OID 1034022)
-- Name: im_message_9_idx_from_id_to_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_9_idx_from_id_to_id_created ON im_message_9 USING btree (user_id, to_id, status);


--
-- TOC entry 3099 (class 1259 OID 1034020)
-- Name: im_message_9_idx_relate_id_status_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_9_idx_relate_id_status_created ON im_message_9 USING btree (relate_id, status, created);


--
-- TOC entry 3100 (class 1259 OID 1034021)
-- Name: im_message_9_idx_relate_id_status_msg_id_created; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_message_9_idx_relate_id_status_msg_id_created ON im_message_9 USING btree (relate_id, status, msg_id, created);


--
-- TOC entry 3103 (class 1259 OID 1034035)
-- Name: im_recent_session_idx_user_id_peer_id_status_updated; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_recent_session_idx_user_id_peer_id_status_updated ON im_recent_session USING btree (user_id, peer_id, status, updated);


--
-- TOC entry 3104 (class 1259 OID 1034036)
-- Name: im_recent_session_idx_user_id_peer_id_type; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_recent_session_idx_user_id_peer_id_type ON im_recent_session USING btree (user_id, peer_id, type);


--
-- TOC entry 3107 (class 1259 OID 1034048)
-- Name: im_relation_ship_idx_small_id_big_id_status_updated; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_relation_ship_idx_small_id_big_id_status_updated ON im_relation_ship USING btree (small_id, big_id, status, updated);


--
-- TOC entry 3110 (class 1259 OID 1034060)
-- Name: im_user_idx_domain; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_user_idx_domain ON im_user USING btree (domain);


--
-- TOC entry 3111 (class 1259 OID 1034061)
-- Name: im_user_idx_name; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_user_idx_name ON im_user USING btree (name);


--
-- TOC entry 3112 (class 1259 OID 1034062)
-- Name: im_user_idx_phone; Type: INDEX; Schema: teamtalk; Owner: teamtalk; Tablespace:
--

CREATE INDEX im_user_idx_phone ON im_user USING btree (phone);


-- Completed on 2017-06-01 16:45:30

--
-- PostgreSQL database dump complete
--