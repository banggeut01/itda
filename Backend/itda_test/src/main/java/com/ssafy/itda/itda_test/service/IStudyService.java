package com.ssafy.itda.itda_test.service;

import java.util.List;

import com.ssafy.itda.itda_test.model.Meeting;
import com.ssafy.itda.itda_test.model.Study;

public interface IStudyService {
	List<Study> getAllStudy();
	Study getStudy(int stid);
	int createStudy(Study model);
	void deleteStudy(int stid);
	List<Study> getStudyByWanted(String wid);
	List<Study> getStudyByCompany(String cid);
	void createMeeting(Meeting model);
	List<Meeting> getStudyMeeting(int stid);
	Meeting getMeeting(int mid);
	void deleteMeeting(int mid);
}
