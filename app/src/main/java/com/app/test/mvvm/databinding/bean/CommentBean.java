package com.app.test.mvvm.databinding.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by able on 2019/9/9.
 * description:
 */
public class CommentBean implements Serializable, MultiItemEntity {
    public static final int COMMENT_TYPE = 0;
    public static final int INFO_TYPE = 1;

    public int commentType;
    //	string 	留言id
    public String commentId;
    //string 	用户uuid
    public String userId;
    //string 	用户uuid
    public String uuid;
    //	bool 	是否是名师
    public boolean isFamousTeacher;
    //string 	用户名
    public String userName;
    //	string 	用户头像
    public String userAvatar;
    //string 	时间戳
    public long time;
    //	string 	留言内容
    public String content;
    //	string 	点赞数
    public int praiseCount;
    // 	string 	回复数
    public int replyCount;
    //	list < Reply > 	回复列表
    public List<ReplyDto> replyList;

    /**
     * 是否是显示全部
     */
    public boolean isShowAll;
    /**
     * 文字是否超出范围
     */
    public Boolean hasEllipsis;

    /**
     * 是否是显示全部
     */
    public boolean detailIsShowAll;
    /**
     * 文字是否超出范围
     */
    public Boolean detailHasEllipsis;


    public int allCount;

    @Override
    public int getItemType() {
        return commentType;
    }
}
