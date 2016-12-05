package org.edu.service.impl;

import org.edu.model.User;
import org.edu.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;

@Service
@PropertySource("classpath:storage.properties")
public class FileStorageService implements StorageService {

    private final String imagesLocation;

    @Autowired
    public FileStorageService(Environment env) {
        imagesLocation = env.getProperty("image.location");
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(Paths.get(imagesLocation));
        } catch (IOException e) {
            System.err.print("File already exists");
        }
    }

    @Override
    public String store(MultipartFile photo, String userName) throws IOException {
        BufferedImage bufferedProfileImage = cropImage(photo);
        if (bufferedProfileImage == null) {
            return null;
        }
        File imageDestination = new File(imagesLocation + File.separator + LocalDateTime.now().hashCode() + ".png");
        ImageIO.write(bufferedProfileImage, "png", imageDestination);
        return imageDestination.getName();
    }

    private BufferedImage cropImage(MultipartFile photo) throws IOException {
        BufferedImage bufferedProfileImage = ImageIO.read(photo.getInputStream());
        if (bufferedProfileImage == null) {
            return null;
        }
        int height = bufferedProfileImage.getHeight();
        int width = bufferedProfileImage.getWidth();
        BufferedImage out;
        if (height > width) {
            out = bufferedProfileImage.getSubimage(0, 0, width, width);
        } else if (height < width) {
            int diff = width - height;
            out = bufferedProfileImage.getSubimage(diff / 2, 0, height, height);
        } else {
            out = bufferedProfileImage;
        }
        return out;
    }

    @Override
    public boolean delete(User user) {
        if (user.getPhoto() != null) {
            File file = new File(imagesLocation + File.separator + user.getPhoto());
            return file.delete();
        }
        return false;
    }
}
