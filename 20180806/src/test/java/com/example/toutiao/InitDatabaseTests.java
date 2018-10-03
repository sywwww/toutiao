//package com.example.toutiao;
//
//import com.example.toutiao.dao.LoginTicketDAO;
//import com.example.toutiao.dao.NewsDAO;
//import com.example.toutiao.dao.UserDAO;
//import com.example.toutiao.model.LoginTicket;
//import com.example.toutiao.model.News;
//import com.example.toutiao.model.User;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import java.util.Date;
//import java.util.Random;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = ToutiaoApplication.class)
//@Sql("/init-schema.sql")
//public class InitDatabaseTests {
//    @Autowired
//    UserDAO userDAO;
//
//    @Autowired
//    NewsDAO newsDAO;
//
//    @Autowired
//    LoginTicketDAO loginTicketDAO;
//
//    @Test
//    public void initData() {
//        Random random = new Random();
//        for (int i = 0; i < 11; ++i) {
//            User user = new User();
//            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
//            user.setName(String.format("USER%d", i));
//            user.setPassword("");
//            user.setSalt("");
//            userDAO.addUser(user);
//
//            News news = new News();
//            news.setCommentCount(i);
//            Date date = new Date();
//            date.setTime(date.getTime() + 1000*3600*5*i);
//            news.setCreatedDate(date);
//            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
//            news.setLikeCount(i+1);
//            news.setUserId(i+1);
//            news.setTitle(String.format("TITLE{%d}", i));
//            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
//            newsDAO.addNews(news);
//
//            user.setPassword("newpassword");
//            userDAO.updatePassword(user);
//
//            LoginTicket ticket = new LoginTicket();
//            ticket.setStatus(0);
//            ticket.setUserId(i+1);
//            ticket.setExpired(date);
//            ticket.setTicket(String.format("TICKET%d", i+1));
//            loginTicketDAO.addTicket(ticket);
//
//            loginTicketDAO.updateStatus(ticket.getTicket(), 2);
//
//        }
//
//        Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
//        userDAO.deleteById(1);
//        Assert.assertNull(userDAO.selectById(1));
//
//        Assert.assertEquals(1, loginTicketDAO.selectByTicket("TICKET1").getUserId());
//        Assert.assertEquals(2, loginTicketDAO.selectByTicket("TICKET1").getStatus());
//    }
//
//}
package com.example.toutiao;

import com.example.toutiao.dao.CommentDAO;
import com.example.toutiao.dao.LoginTicketDAO;

import com.example.toutiao.dao.NewsDAO;
import com.example.toutiao.dao.UserDAO;
import com.example.toutiao.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
@Sql("/init-schema.sql")

public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    CommentDAO commentDAO;

    @Test
    public void initData() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);

//            user.setPassword("newpassword");
//            user.setId(i);
//            userDAO.updatePassword(user);
            //MySql运行在safe-updates模式下，该模式会导致非主键条件下无法执行update或者delete命令，
            //加了一行setId就可以了
//想要使用主键，可以用 useGeneratedKeys="true" keyProperty="id">
            // 每次改代码后记得重新生成表，要不然内容不会更改，不会覆盖原有的内容，显示的还是之前旧的数据！！！！！
            for(int j = 0; j < 3; ++j) {
                Comment comment = new Comment();
                comment.setUserId(i+1);
                comment.setCreatedDate(new Date());
                comment.setStatus(0);
                comment.setContent("这里是一个评论啊！" + String.valueOf(j));
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                commentDAO.addComment(comment);
            }
            LoginTicket ticket = new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i+1);
            ticket.setExpired(date);
            ticket.setTicket(String.format("TICKET%d", i+1));
            loginTicketDAO.addTicket(ticket);

            loginTicketDAO.updateStatus(ticket.getTicket(), 2);

        }

     //  Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
     //   userDAO.deleteById(1);
    //    Assert.assertNull(userDAO.selectById(1));

     //   Assert.assertEquals(1, loginTicketDAO.selectByTicket("TICKET1").getUserId());
    //    Assert.assertEquals(2, loginTicketDAO.selectByTicket("TICKET1").getStatus());
       Assert.assertNotNull(commentDAO.selectByEntity(1,EntityType.ENTITY_NEWS).get(0));
    }

}
