package com.example.toutiao.controller;

import com.example.toutiao.dao.MessageDAO;
import com.example.toutiao.model.*;
import com.example.toutiao.service.MessageService;
import com.example.toutiao.service.UserService;
import com.example.toutiao.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @RequestMapping(path={"/msg/detail"},method = {RequestMethod.GET})
    public String conversationDetail(Model model, @Param("conversationId")String conversationId){
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages=new ArrayList<>();
            for(Message msg:conversationList){
                ViewObject vo=new ViewObject();
                vo.set("message",msg);
                User user=userService.getUser(msg.getFromId());
                if(user==null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch(Exception e){
            logger.error("获取详情消息失败"+e.getMessage());

        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                vo.set("targetId", targetId);
                vo.set("totalCount", msg.getId());
                vo.set("unreadCount", messageService.getUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
            return "letter";
        } catch (Exception e) {
            logger.error("获取站内信list失败" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path={"/msg/addMessage"},method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId")int fromId,
                             @RequestParam("toId")int toId,
                             @RequestParam("content")String content){
        try{
            Message message=new Message();
            message.setContent(content);
            message.setFromId(fromId);
            message.setToId(toId);
            message.setCreatedDate(new Date());
            message.setConversationId(fromId<toId?String.format("%d_%d", fromId, toId) :
                    String.format("%d_%d", toId, fromId));
            messageService.addMessage(message);
            System.out.println(message.getId());
            return ToutiaoUtil.getJSONString(message.getId());
        }catch(Exception e){
            logger.error("添加信息失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"添加信息失败");
        }

    }


}
