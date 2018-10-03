package com.example.toutiao.service;

import com.example.toutiao.dao.CommentDAO;
import com.example.toutiao.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

    @Autowired
    private CommentDAO commentDAO;

    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentDAO.selectByEntity(entityId,entityType);
    }

    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }

}
