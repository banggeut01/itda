package com.ssafy.itda.itda_test.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ssafy.itda.itda_test.help.Result;
import com.ssafy.itda.itda_test.model.Files;
import com.ssafy.itda.itda_test.model.User;
import com.ssafy.itda.itda_test.payload.FileUploadResponse;
import com.ssafy.itda.itda_test.service.FileUploadDownloadService;
import com.ssafy.itda.itda_test.service.IFileService;
import com.ssafy.itda.itda_test.service.IUserService;
import com.ssafy.itda.itda_test.service.JwtServiceImpl;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RequestMapping("/api")
@RestController
public class FileUploadController {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private FileUploadDownloadService service;

	@Autowired
	private IUserService userService;

	@Autowired
	private IFileService fileService;

	@Autowired
	private JwtServiceImpl jwtService;

	@PostMapping("/uploadImg")
	public FileUploadResponse uploadImg(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
		String fileName = service.storeImg(file);
		String fileDownloadUri = null;
		Map<String, Object> resultMap = new HashMap<>();
		String token = req.getHeader("jwt-auth-token");
		if (token != null && !token.equals("")) {
			resultMap.putAll(jwtService.get(token));
			int uid = (int) resultMap.get("uid");
			User user = userService.getUser(uid);
			if (user.getUimg() != null) {
				File uimg_file = new File(user.getUimg());
				if (uimg_file.exists()) {
					uimg_file.delete();
				}
			}
			fileDownloadUri = ServletUriComponentsBuilder.fromPath("/files/").path(fileName).toUriString();
			user.setUimg(fileDownloadUri);
			userService.imgUpload(user);
			return new FileUploadResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
		} else {
			return new FileUploadResponse(null, null, null, 0L);
		}
	}

	@PostMapping("/uploadFile")
	public FileUploadResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("stid") int stid,
			HttpServletRequest req) {
		String fileName = service.storeFile(file);
		String fileDownloadUri = null;
		Map<String, Object> resultMap = new HashMap<>();
		String token = req.getHeader("jwt-auth-token");
		if (token != null && !token.equals("")) {
			resultMap.putAll(jwtService.get(token));
			int uid = (int) resultMap.get("uid");
			Files newfile = new Files();
			newfile.setUid(uid);
			newfile.setStid(stid);
			fileDownloadUri = ServletUriComponentsBuilder.fromPath("/files/").path(fileName).toUriString();
			newfile.setUrl(fileDownloadUri);
			newfile.setFileName(fileName);
			fileService.insertFile(newfile);
			return new FileUploadResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
		} else {
			return new FileUploadResponse(null, null, null, 0L);
		}
	}

//	@RequestMapping(value = "/downloadFile/{fileName:.+}", method = RequestMethod.GET)
	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = service.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@ApiOperation(value = "업로드 파일을 삭제한다.", response = Result.class)
	@RequestMapping(value = "/deleteFile/{fid}", method = RequestMethod.DELETE)
	public ResponseEntity<Result> deleteFile(HttpServletRequest req, @RequestBody int fid) throws Exception {
		logger.info("4-------------deleteFile -----------------------------" + new Date());
		Map<String, Object> resultMap = new HashMap<>();
		String token = req.getHeader("jwt-auth-token");
		Result r = new Result();
		if (token != null && !token.equals("")) {
			resultMap.putAll(jwtService.get(req.getHeader("jwt-auth-token")));
			int uid = (int) resultMap.get("uid");
			if (fid == 0) {
				r.setMsg("키값이 누락되었습니다.");
				r.setState("fail");
			} else {
				Files file = fileService.getFile(fid);
				if (file == null) {
					r.setMsg("파일을 찾을 수 없습니다.");
					r.setState("fail");
				} else {
					if (file.getUid() != uid) {
						r.setMsg("작성자가 아니라 삭제할 수 없습니다.");
						r.setState("fail");
					} else {
						fileService.deleteFile(fid);
						r.setMsg("파일 삭제가 완료되었습니다.");
						r.setState("success");
					}
				}
			}
		} else {
			r.setMsg("uid 값이 없습니다.");
			r.setState("fail");
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
}
