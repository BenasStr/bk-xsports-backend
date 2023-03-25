package com.ktu.xsports.api.service;

import com.ktu.xsports.api.exceptions.ImageUploadException;
import com.ktu.xsports.config.HostConfiguration;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private static final String IMAGE_BUCKET = "images";
    private final HostConfiguration hostConfiguration;
    private final MinioClient minioClient;

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
        String imageName = addTypeExtension(image, name);
        try {

            minioClient.putObject(PutObjectArgs.builder()
                .bucket(IMAGE_BUCKET)
                .object(imageName)
                .stream(image.getInputStream(), image.getSize(), -1)
                .contentType(image.getContentType())
                .build());

            log.info("Image successfully uploaded");
            return hostConfiguration.getUrl() + "/images/" + imageName;
        } catch (Exception e) {
            throw new ImageUploadException("Couldn't upload image");
        }
    }

    public String updateImage(MultipartFile image, String fileName) {
        deleteImage(fileName);

        try {
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(IMAGE_BUCKET)
                .object(getLastPart(fileName))
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
        getLastPart(fileName);
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

    private String getLastPart(String url) {
        return url.split("/")[5];
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
