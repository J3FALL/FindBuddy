package org.edu.service.impl;

import org.edu.StorageProperties;
import org.edu.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

@Service
public class FileStorageService implements StorageService {

    private final String imagesLocation;

    @Autowired
    public FileStorageService(StorageProperties storageProperties) {
        this.imagesLocation = storageProperties.getImagesLocation();
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
        File imageDestination = new File(imagesLocation + File.separator + userName.hashCode() + ".png");
        ImageIO.write(bufferedProfileImage, "png", imageDestination);
        return imageDestination.getName();
    }

    private BufferedImage cropImage(MultipartFile photo) throws IOException {
        BufferedImage bufferedProfileImage = ImageIO.read(photo.getInputStream());
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
}
