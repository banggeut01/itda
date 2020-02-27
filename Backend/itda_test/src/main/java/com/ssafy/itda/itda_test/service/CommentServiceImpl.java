package com.ssafy.itda.itda_test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.itda.itda_test.dao.CommentDao;
import com.ssafy.itda.itda_test.model.Comment;

@Service
public class CommentServiceImpl implements ICommentService {

	@Autowired
	private CommentDao commentDao;

	@Override
	public List<Comment> getComments(int stid) {
		return commentDao.getComments(stid);
	}

	@Override
	public void createComment(Comment model) {
		commentDao.createComment(model);
	}

	@Override
	public void updateComment(Comment model) {
		commentDao.updateComment(model);
	}

	@Override
	public void deleteComment(int cmid) {
		commentDao.deleteComment(cmid);
	}

	@Override
	public Comment getComment(int cmid) {
		return commentDao.getComment(cmid);
	}
}
