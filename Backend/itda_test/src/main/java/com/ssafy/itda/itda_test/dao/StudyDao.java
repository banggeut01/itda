package com.ssafy.itda.itda_test.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssafy.itda.itda_test.model.Meeting;
import com.ssafy.itda.itda_test.model.Study;

@Repository
public class StudyDao {

	String ns = "ssafy.study.";

	@Autowired
	private SqlSession sqlSession;

	public List<Study> getAllStudy() {
		return sqlSession.selectList(ns + "getAllStudy");
	}

	public Study getStudy(int stid) {
		return sqlSession.selectOne(ns + "getStudy", stid);

	}

	public int createStudy(Study model) {
		sqlSession.insert(ns + "createStudy", model);
		return sqlSession.selectOne(ns + "getStid", model);
	}

	public void deleteStudy(int stid) {
		sqlSession.delete(ns + "deleteStudy", stid);
	}

	public List<Study> getStudyByWanted(String wid) {
		return sqlSession.selectList(ns + "getStudyByWanted", wid);
	}

	public List<Study> getStudyByCompany(String cid) {
		return sqlSession.selectList(ns + "getStudyByCompany", cid);
	}

	public void createMeeting(Meeting model) {
		sqlSession.insert(ns + "createMeeting", model);
	}

	public List<Meeting> getStudyMeeting(int stid) {
		return sqlSession.selectList(ns + "getStudyMeeting", stid);
	}

	public Meeting getMeeting(int mid) {
		return sqlSession.selectOne(ns + "getMeeting", mid);
	}
	
	public void deleteMeeting(int mid) {
		sqlSession.delete(ns + "deleteMeeting", mid);
	}
}
