CREATE TABLE `loc_cp_label_ext_info` (
    `LABEL_ID` varchar(32) NOT NULL,
    `sort_desc` varchar(64) DEFAULT NULL COMMENT '排序sql语句，如：order_date asc',
    PRIMARY KEY (`LABEL_ID`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='中国邮政标签扩展信息表' CHECKSUM=0 DELAY_KEY_WRITE=0;