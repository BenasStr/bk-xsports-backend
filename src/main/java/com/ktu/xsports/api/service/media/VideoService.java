package com.ktu.xsports.api.service.media;

import com.ktu.xsports.api.advice.exceptions.ImageUploadException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VideoService {
    private static final String VIDEO_BUCKET = "videos";
    private final MinioClient minioClient;
    private final int fileSize = 25000000;

    public byte[] getVideo(String fileName) {
       try {
           return IOUtils.toByteArray(
               minioClient.getObject(
                   GetObjectArgs.builder()
                       .bucket(VIDEO_BUCKET)
                       .object(fileName)
                       .build()
               )
           );
       } catch (Exception e) {
           //TODO create normal errors.
           throw new ImageUploadException("This failed");
       }
    }

    public String uploadVideo(MultipartFile video, String fileName) {
        try {
            String videoName = addTypeExtension(video, fileName);
            InputStream inputStream = video.getInputStream();

            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(VIDEO_BUCKET)
                    .object(videoName)
                    .stream(inputStream, inputStream.available(), fileSize)
                    .contentType(video.getContentType())
                    .build()
            );

            return videoName;
        } catch (Exception e) {
            //TODO add exception here
            throw new ImageUploadException(e.getMessage());
        }
    }

    public String updateVideo(MultipartFile video, String fileName) {
        deleteVideo(fileName);
        try {
            InputStream inputStream = video.getInputStream();
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(VIDEO_BUCKET)
                    .object(getLastPart(fileName))
                    .stream(inputStream, inputStream.available(), fileSize)
                    .contentType(video.getContentType())
                    .build()
            );

            return fileName;
        } catch (Exception e) {
            //TODO add exception here
            throw new ImageUploadException("");
        }
    }

    public void deleteVideo(String fileName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(VIDEO_BUCKET)
                    .object(getLastPart(fileName))
                    .build()
            );
        } catch (Exception e) {
            //TODO create normal stuff
            throw new ImageUploadException("This failed");
        }
    }

    private String getLastPart(String url) {
        return url.split("/")[5];
    }

    private String addTypeExtension(MultipartFile file, String fileName) {
        System.out.println(Objects.requireNonNull(file.getContentType()));
        if (Objects.requireNonNull(file.getContentType()).equals("video/mp4")) {
            return fileName + ".mp4";
        }
        throw new ImageUploadException("Couldn't upload video");
    }
}
