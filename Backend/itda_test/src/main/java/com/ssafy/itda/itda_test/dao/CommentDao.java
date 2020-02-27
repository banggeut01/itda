package com.ssafy.itda.itda_test.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssafy.itda.itda_test.model.Comment;

@Repository
public class CommentDao {
	String ns = "ssafy.comment.";

	@Autowired
	private SqlSession sqlSession;

	public List<Comment> getComments(int stid) {
		return sqlSession.selectList(ns + "getComments", stid);
	}

	public void createComment(Comment model) {
		sqlSession.insert(ns + "createComment", model);
	}

	public void updateComment(Comment model) {
		sqlSession.update(ns + "updateComment", model);
	}

	public void deleteComment(int cmid) {
		sqlSession.delete(ns + "deleteComment", cmid);
	}
	
	public Comment getComment(int cmid) {
		return sqlSession.selectOne(ns + "getComment", cmid);
	}
}
