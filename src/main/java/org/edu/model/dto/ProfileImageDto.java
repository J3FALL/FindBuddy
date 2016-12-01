package org.edu.model.dto;

import org.springframework.web.multipart.MultipartFile;

public class ProfileImageDto {

    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
