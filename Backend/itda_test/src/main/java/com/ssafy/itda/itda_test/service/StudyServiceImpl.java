package com.ssafy.itda.itda_test.service;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ssafy.itda.itda_test.dao.StudyDao;
import com.ssafy.itda.itda_test.model.Study;

@Service
public class StudyServiceImpl implements IStudyService {

	@Autowired
	private StudyDao studyDao;
	
	@Override
	public List<Study> getAllStudy() {
		// TODO Auto-generated method stub
		return studyDao.getAllStudy();
	}

	@Override
	public Study getStudy(int stid) {
		// TODO Auto-generated method stub
		return studyDao.getStudy(stid);
	}

	@Override
	public int createStudy(Study model) {
		return studyDao.createStudy(model);
	}

	@Override
	public void deleteStudy(int stid) {
		// TODO Auto-generated method stub
		studyDao.deleteStudy(stid);
	}

	@Override
	public List<Study> getStudyByWanted(String wid) {
		return studyDao.getStudyByWanted(wid);
	}

	@Override
	public List<Study> getStudyByCompany(String cid) {
		return studyDao.getStudyByCompany(cid);
	}

}
