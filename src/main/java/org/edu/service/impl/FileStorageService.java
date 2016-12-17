package org.edu.service.impl;

import net.coobird.thumbnailator.Thumbnails;

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
        File imageDestination = new File(imagesLocation + File.separator + LocalDateTime.now().hashCode() + ".jpg");
        BufferedImage bufferedProfileImage = ImageIO.read(photo.getInputStream());
        if (bufferedProfileImage == null) {
            return null;
        }
        cropAndWriteImage(bufferedProfileImage, imageDestination);
        return imageDestination.getName();
    }

    private void cropAndWriteImage(BufferedImage bufferedProfileImage, File destFile) throws IOException {
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
        Thumbnails.of(out)
                .outputQuality(0.7)
                .size(out.getWidth(), out.getHeight())
                .outputFormat("jpg")
                .toFile(destFile);
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
