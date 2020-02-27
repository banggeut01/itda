package com.ssafy.itda.itda_test.controller;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.cj.protocol.a.NativeConstants.StringLengthDataType;
import com.ssafy.itda.itda_test.help.CommentResult;
import com.ssafy.itda.itda_test.help.Result;
import com.ssafy.itda.itda_test.help.UserResult;
import com.ssafy.itda.itda_test.help.WantedResult;
import com.ssafy.itda.itda_test.model.Comment;
import com.ssafy.itda.itda_test.model.Company;
import com.ssafy.itda.itda_test.model.WantedStack;
import com.ssafy.itda.itda_test.model.Scrap;
import com.ssafy.itda.itda_test.model.Stack;
import com.ssafy.itda.itda_test.model.User;
import com.ssafy.itda.itda_test.model.Wanted;
import com.ssafy.itda.itda_test.service.ICommentService;
import com.ssafy.itda.itda_test.service.IStackService;
import com.ssafy.itda.itda_test.service.IUserService;
import com.ssafy.itda.itda_test.service.IWantedService;
import com.ssafy.itda.itda_test.service.JwtServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

//http://localhost:8197/humans/swagger-ui.html
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/api")
@Api(value = "SSAFY", description = "SSAFY Resouces Management 2019")
public class CommentController {
	public static final Logger logger = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private ICommentService commentService;

	@Autowired
	private IUserService userService;

	@Autowired
	private JwtServiceImpl jwtService;

	@ApiOperation(value = "스터디에 속한 댓글 목록을 조회한다.", response = List.class)
	@RequestMapping(value = "/getCommentById/{stid}", method = RequestMethod.GET)
	public ResponseEntity<List<CommentResult>> getCommentById(@PathVariable int stid) throws Exception {
		logger.info("1-------------getCommentById-----------------------------" + new Date());
		List<CommentResult> crList = new ArrayList<>();
		List<Comment> cList = commentService.getComments(stid); // stid인 댓글 리스트(uname, uimg 없음)
		for (Comment c : cList) {
			CommentResult cr = new CommentResult();
			cr.setComment(c);
			User user = userService.getUser(c.getUid());
			cr.setUname(user.getUname());
			cr.setUimg(user.getUimg());
			crList.add(cr);
		}

		return new ResponseEntity<List<CommentResult>>(crList, HttpStatus.OK);
	}

	@ApiOperation(value = "스터디 상세 페이지에서 댓글을 작성한다.", response = Result.class)
	@RequestMapping(value = "/createComment", method = RequestMethod.POST)
	public ResponseEntity<Result> createComment(HttpServletRequest req, @RequestBody Comment model) throws Exception {
		logger.info("2-------------createComment -----------------------------" + new Date());
		Map<String, Object> resultMap = new HashMap<>();
		String token = req.getHeader("jwt-auth-token");
		Result r = new Result();
		if (token != null && !token.equals("")) {
			resultMap.putAll(jwtService.get(req.getHeader("jwt-auth-token")));
			int uid = (int) resultMap.get("uid");
			if (model.getStid() == 0 || model.getContent() == null || model.getContent().equals("")) {
				r.setMsg("필수값이 누락되었습니다.");
				r.setState("fail");
			} else {
				model.setUid(uid);
				commentService.createComment(model);
				r.setMsg("댓글 작성이 완료되었습니다.");
				r.setState("success");
			}
		} else {
			r.setMsg("uid 값이 없습니다.");
			r.setState("fail");
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}

	@ApiOperation(value = "스터디 상세 페이지에서 댓글을 수정한다.", response = Result.class)
	@RequestMapping(value = "/updateComment", method = RequestMethod.PUT)
	public ResponseEntity<Result> updateComment(HttpServletRequest req, @RequestBody Comment model) throws Exception {
		logger.info("3-------------updateComment -----------------------------" + new Date());
		Map<String, Object> resultMap = new HashMap<>();
		String token = req.getHeader("jwt-auth-token");
		Result r = new Result();
		if (token != null && !token.equals("")) {
			resultMap.putAll(jwtService.get(req.getHeader("jwt-auth-token")));
			int uid = (int) resultMap.get("uid");
			if (model.getCmid() == 0 || model.getContent() == null || model.getContent().equals("")) {
				r.setMsg("필수값이 누락되었습니다.");
				r.setState("fail");
			} else {
				commentService.updateComment(model);
				r.setMsg("댓글 수정이 완료되었습니다.");
				r.setState("success");
			}
		} else {
			r.setMsg("uid 값이 없습니다.");
			r.setState("fail");
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}

	@ApiOperation(value = "스터디 상세 페이지에서 댓글을 삭제한다.", response = Result.class)
	@RequestMapping(value = "/deleteComment/{cmid}", method = RequestMethod.DELETE)
	public ResponseEntity<Result> deleteComment(HttpServletRequest req, @PathVariable int cmid) throws Exception {
		logger.info("4-------------deleteComment -----------------------------" + new Date());
		Map<String, Object> resultMap = new HashMap<>();
		String token = req.getHeader("jwt-auth-token");
		Result r = new Result();
		if (token != null && !token.equals("")) {
			resultMap.putAll(jwtService.get(req.getHeader("jwt-auth-token")));
			int uid = (int) resultMap.get("uid");
			if (cmid == 0) {
				r.setMsg("키값이 누락되었습니다.");
				r.setState("fail");
			} else {
				Comment comment = commentService.getComment(cmid);
				if (comment.getUid() == uid || uid == 1) {
					commentService.deleteComment(cmid);
					r.setMsg("댓글 삭제가 완료되었습니다.");
					r.setState("success");
				} else {
					r.setMsg("권한이 없습니다.");
					r.setState("fail");
				}
			}
		} else {
			r.setMsg("uid 값이 없습니다.");
			r.setState("fail");
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
}
