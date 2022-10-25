package com.example.demo.repository;

import com.example.demo.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    //    @Select( "select * from comment where commentid=#{id}")
    public Comment findAllByCommentid(Integer id);

    //    @Query(value = "select * from comment",nativeQuery = true)
    public List<Comment> findAll();


//    @Insert("insert into comment(score,commenttime) values (#{score},#{commenttime})")
//    @Options(useGeneratedKeys = true,keyProperty = "commentid")
//    int insert(Integer score,String commenttime);
}
