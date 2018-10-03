package com.example.toutiao.dao;

import com.example.toutiao.model.Comment;
import com.example.toutiao.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageDAO {
    String TABLE_NAME="message";
    String INSERT_FIELDS="from_id,to_id,content,created_date,has_read,conversation_id";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;

//    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
//            ") values (#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);


    //@Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id order by id desc limit #{offset},#{limit}"})
    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} group by conversation_id order by conversation_id desc limit #{offset},#{limit}"})
                                //SELECT *,count(id) AS cnt FROM message where from_id=12 or to_id=12 GROUP BY conversation_id ORDER BY conversation_id desc;
    List<Message> getConversationList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnReadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where conversation_id=#{conversationId} order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId")String conversationId,
                                        @Param("offset")int offset,
                                        @Param("limit")int limit);
}
