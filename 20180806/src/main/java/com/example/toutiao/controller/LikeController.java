package com.example.toutiao.controller;

import com.example.toutiao.model.Comment;
import com.example.toutiao.model.EntityType;
import com.example.toutiao.model.HostHolder;
import com.example.toutiao.model.News;
import com.example.toutiao.service.LikeService;
import com.example.toutiao.service.NewsService;
import com.example.toutiao.util.ToutiaoUtil;
import org.apache.catalina.Host;
import org.apache.ibatis.annotations.Param;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.TooManyListenersException;

@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    NewsService newsService;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newsId")int newsId) {
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.like(userId,EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newsId")int newsId) {
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.dislike(userId,EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
