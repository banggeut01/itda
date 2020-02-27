package com.ssafy.itda.itda_test.help;

import java.io.Serializable;
import java.util.List;

import com.ssafy.itda.itda_test.model.Comment;
import com.ssafy.itda.itda_test.model.Files;
import com.ssafy.itda.itda_test.model.Meeting;
import com.ssafy.itda.itda_test.model.Study;

public class StudyResult implements Serializable {
	private Study study;
	private List<Comment> comments;
	private List<Files> files;
	private List<Meeting> meetings;
	private String msg;
	private String state;

	public StudyResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StudyResult(Study study, String msg, String state) {
		super();
		this.study = study;
		this.msg = msg;
		this.state = state;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Files> getFiles() {
		return files;
	}

	public void setFiles(List<Files> files) {
		this.files = files;
	}

	public List<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(List<Meeting> meetings) {
		this.meetings = meetings;
	}

	@Override
	public String toString() {
		return "StudyResult [study=" + study + ", comments=" + comments + ", files=" + files + ", meetings=" + meetings
				+ ", msg=" + msg + ", state=" + state + "]";
	}

}
