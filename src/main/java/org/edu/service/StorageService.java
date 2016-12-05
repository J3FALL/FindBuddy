package org.edu.service;

import org.edu.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface StorageService {

    void init();

    String store(MultipartFile file, String userName) throws IOException;

    boolean delete(User user);
}
