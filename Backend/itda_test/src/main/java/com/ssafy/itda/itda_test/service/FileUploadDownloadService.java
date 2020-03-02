package com.ssafy.itda.itda_test.service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.itda.itda_test.exception.FileDownloadException;
import com.ssafy.itda.itda_test.exception.FileUploadException;
import com.ssafy.itda.itda_test.property.FileUploadProperties;

@Service
public class FileUploadDownloadService {
	private final Path fileLocation;

	// 저장할 디렉토리 설정, 디렉토리 생성
	@Autowired
	public FileUploadDownloadService(FileUploadProperties prop) {
		this.fileLocation = Paths.get(prop.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileLocation);
		} catch (Exception e) {
			throw new FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
		}
	}

	// 파일 저장
	public String storeImg(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String store_filename = null;

		try {
			// 파일명에 부적합 문자가 있는지 확인한다.
			if (fileName.contains(".."))
				throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);
			String lower_filename = fileName.toLowerCase();
			if (!(lower_filename.contains(".jpg") || lower_filename.contains(".png")
					|| lower_filename.contains(".jpeg"))) {
				throw new FileUploadException("저장할 수 없는 확장자 입니다. " + fileName);
			} else {
				String uuid = UUID.randomUUID().toString();
				int chk_extension = 0;
				if (lower_filename.contains(".jpg")) {
					chk_extension = 1;
				} else if (lower_filename.contains(".png")) {
					chk_extension = 2;
				} else if (lower_filename.contains(".jpeg")) {
					chk_extension = 3;
				}
				switch (chk_extension) {
				case 1:
					store_filename = uuid + ".jpg";
					break;
				case 2:
					store_filename = uuid + ".png";
					break;
				case 3:
					store_filename = uuid + ".jpeg";
					break;
				}
			}
			Path targetLocation = this.fileLocation.resolve(store_filename);

			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return store_filename;
		} catch (Exception e) {
			throw new FileUploadException("[" + fileName + "] 파일 업로드에 실패하였습니다. 다시 시도하십시오.", e);
		}
	}

	// 파일 저장
	public String storeFile(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String store_filename = null;

		try {
			// 파일명에 부적합 문자가 있는지 확인한다.
			if (fileName.contains(".."))
				throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);
			String lower_filename = fileName.toLowerCase();
			int validFile = validFile(lower_filename);
			if (validFile == -1) {
				throw new FileUploadException("저장할 수 없는 확장자 입니다. " + fileName);
			}
			Path targetLocation = this.fileLocation.resolve(fileName);

			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (Exception e) {
			throw new FileUploadException("[" + fileName + "] 파일 업로드에 실패하였습니다. 다시 시도하십시오.", e);
		}
	}

	private int validFile(String fileName) {
		String[] extension = { ".hwp", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf", ".jpg", ".jpeg", ".png", ".gif",
				".bmp", ".tiff",".java", ".py", ".txt", ".md" };
		for (int i = 0; i < extension.length; i++) {
			if (fileName.contains(extension[i])) {
				return i;
			}
		}
		return -1;
	}

	// 파일 다운로드
	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists()) {
				return resource;
			} else {
				throw new FileDownloadException(fileName + " 파일을 찾을 수 없습니다.");
			}
		} catch (MalformedURLException e) {
			throw new FileDownloadException(fileName + " 파일을 찾을 수 없습니다.", e);
		}
	}

}
