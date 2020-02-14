package com.ssafy.itda.itda_test.property;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="file")
public class FileUploadProperties {
    private String uploadDir;
 
    public String getUploadDir() {
        return uploadDir;
    }
 
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
