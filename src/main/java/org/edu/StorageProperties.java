package org.edu;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("storage")
public class StorageProperties {

    private String imagesLocation = "D:/user_images";

    private String iconsLocation = "D:/user_icons";

    public String getImagesLocation() {
        return imagesLocation;
    }

    public void setImagesLocation(String imagesLocation) {
        this.imagesLocation = imagesLocation;
    }

    public String getIconsLocation() {
        return iconsLocation;
    }

    public void setIconsLocation(String iconsLocation) {
        this.iconsLocation = iconsLocation;
    }
}
