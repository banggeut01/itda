package com.ssafy.itda.itda_test.service;

import java.util.List;

import com.ssafy.itda.itda_test.model.Comment;

public interface ICommentService {
	List<Comment> getComments(int stid);
	void createComment(Comment model);
	void updateComment(Comment model);
	void deleteComment(int cmid);
	Comment getComment(int cmid);
}	
