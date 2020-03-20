package com.app.test.mvvm.databinding.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by able on 2019/9/9.
 * description:
 */
public class ReplyDto implements Serializable, MultiItemEntity {
    public static final int NO_EMPTY_TYPE = 0;
    public static final int EMPTY_TYPE = 1;
    public int type;

    public String replyId;//	string 	回复id
    public String userId;//string 	用户uuid
    public String uuid;//string 	用户uuid
    public boolean isFamousTeacher;//bool 	是否是名师
    public String userName;//string 	用户名
    public String userAvatar;//	string 	用户头像
    public long time;//string 	时间戳
    public String content;//	string 	留言内容
    public int praiseCount;//	string 	点赞数

    public boolean isFirstReply;

    @Override
    public int getItemType() {
        return type;
    }
}
