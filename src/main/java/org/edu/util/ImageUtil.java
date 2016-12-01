package org.edu.util;

import net.coobird.thumbnailator.Thumbnails;

import org.edu.model.dto.ProfileImageDto;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

@Component
public class ImageUtil implements ServletContextAware {


    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void processProfileImage(ProfileImageDto profileImageDTO, String userKey)
            throws IOException {

        // Reduce original image size. Thumbnailator will not modify
        // image if less than 600x600

        BufferedImage bufferedProfileImage =
                Thumbnails.of(profileImageDTO.getFile().getInputStream())
                        .forceSize(600, 600)
                        .allowOverwrite(true)
                        .outputFormat("png")
                        .asBufferedImage();

        saveProfileImage(bufferedProfileImage, userKey, false);

        // Create profile image icon. Saved to separate directory

        BufferedImage bufferedIconImage =
                Thumbnails.of(profileImageDTO.getFile().getInputStream())
                        .forceSize(32, 32)
                        .allowOverwrite(true)
                        .outputFormat("png")
                        .asBufferedImage();

        saveProfileImage(bufferedIconImage, userKey, true);
    }

    private void saveProfileImage(BufferedImage bufferedImage, String userKey, boolean isIcon) throws IOException {
        File imageDestination  = new File("src\\main\\resources\\resources\\avatars\\icons");
        if (imageDestination == null) {
            System.out.println("BLYAD");
        }
        if (bufferedImage == null) {
            System.out.println("SUKA BLYAD");
        }
        ImageIO.write(bufferedImage, "png", imageDestination);
//        String destination = isIcon ? applicationSettings.getProfileIconPath() : applicationSettings.getProfileImagePath();
//        File imageDestination = new File(destination + userKey);
//        ImageIO.write(bufferedImage, "png", imageDestination);

    }

}
