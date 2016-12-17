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
    private final String iconLocation;

    @Autowired
    public FileStorageService(Environment env) {
        imagesLocation = env.getProperty("image.location");
        iconLocation = env.getProperty("icon.location");
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(Paths.get(imagesLocation));
            Files.createDirectory(Paths.get(iconLocation));
        } catch (IOException e) {
            System.err.print("File already exists");
        }
    }

    @Override
    public String store(MultipartFile photo, String userName) throws IOException {
        int imageName = LocalDateTime.now().hashCode();
        File imageDestination = new File(imagesLocation + File.separator + imageName + ".jpg");
        File iconDestination = new File(iconLocation + File.separator + imageName + ".jpg");
        BufferedImage bufferedProfileImage = ImageIO.read(photo.getInputStream());
        if (bufferedProfileImage == null) {
            return null;
        }
        cropAndWriteImage(bufferedProfileImage, imageDestination, iconDestination);
        return imageDestination.getName();
    }

    private void cropAndWriteImage(BufferedImage bufferedProfileImage, File imageDestFile, File iconDestFile) throws IOException {
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
                .size(300, 300)
                .outputFormat("jpg")
                .toFile(imageDestFile);

        Thumbnails.of(out)
                .outputQuality(0.7)
                .size(70, 70)
                .outputFormat("jpg")
                .toFile(iconDestFile);
    }


    @Override
    public boolean delete(User user) {
        if (user.getPhoto() != null) {
            File image = new File(imagesLocation + File.separator + user.getPhoto());
            File icon = new File(iconLocation + File.separator + user.getPhoto());
            return image.delete() && icon.delete();
        }
        return false;
    }
}
