package com.ktu.xsports.api.service;

import com.ktu.xsports.api.exceptions.ImageUploadException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@Slf4j
public class ImageService {
    private static final String IMAGE_BUCKET = "images";
    private static final String USER_PREFIX = "users-";
    private static final String SPORT_PREFIX = "sports-";
    private static final String CATEGORIES_PREFIX = "categories-";

    @Autowired
    private MinioClient minioClient;

    public byte[] getImage(String imageName) {
        try {
            return IOUtils.toByteArray(
                minioClient.getObject(
                    GetObjectArgs.builder()
                        .bucket(IMAGE_BUCKET)
                        .object(imageName)
                        .build())
            );
        } catch (Exception e) {
            throw new ImageUploadException("Unable to get image");
        }
    }

    public String uploadUserImage(MultipartFile image, String name) {
        try {
            String imageName = getCustomPictureName(image, name);

            minioClient.putObject(PutObjectArgs.builder()
                .bucket(IMAGE_BUCKET)
                .object(imageName)
                .stream(image.getInputStream(), image.getSize(), -1)
                .contentType(image.getContentType())
                .build());

            log.info("Image successfully uploaded");
            return imageName;
        } catch (Exception e) {
            throw new ImageUploadException("Couldn't upload image");
        }
    }

    public String updateProfileImage(MultipartFile image, String fileName) {
        deleteUserProfileImage(fileName);
        return uploadUserImage(image, fileName);
    }

    public void deleteUserProfileImage(String fileName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(IMAGE_BUCKET)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new ImageUploadException("Couldn't remove image");
        }
    }

    private String getCustomPictureName(MultipartFile multipartFile, String name) {
        switch (Objects.requireNonNull(multipartFile.getContentType())) {
            case "image/jpeg" -> {
                return USER_PREFIX + name + ".jpeg";
            }
            case "image/jpg" -> {
                return USER_PREFIX + name + ".jpg";
            }
            case "image/png" -> {
                return USER_PREFIX + name + ".png";
            }
            default -> {
                throw new ImageUploadException("Incorrect image type: use (jpg, jpeg, png)");
            }
        }
    }
}
