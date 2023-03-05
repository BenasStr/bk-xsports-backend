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

    public String uploadImage(MultipartFile image, String name) {
        try {
            String imageName = addTypeExtension(image, name);

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
        deleteImage(fileName);

        try {
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(IMAGE_BUCKET)
                .object(fileName)
                .stream(image.getInputStream(), image.getSize(), -1)
                .contentType(image.getContentType())
                .build());

            log.info("Image successfully uploaded");
            return fileName;
        } catch (Exception e) {
            throw new ImageUploadException("Couldn't upload image");
        }
    }

    public void deleteImage(String fileName) {
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

    private String addTypeExtension(MultipartFile multipartFile, String name) {
        switch (Objects.requireNonNull(multipartFile.getContentType())) {
            case "image/jpeg" -> {
                return name + ".jpeg";
            }
            case "image/jpg" -> {
                return name + ".jpg";
            }
            case "image/png" -> {
                return name + ".png";
            }
            default -> {
                throw new ImageUploadException("Incorrect image type: use (jpg, jpeg, png)");
            }
        }
    }
}
