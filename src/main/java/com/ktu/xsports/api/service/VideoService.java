package com.ktu.xsports.api.service;

import com.ktu.xsports.api.exceptions.ImageUploadException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class VideoService {
    private static final String VIDEO_BUCKET = "videos";

    @Autowired
    private MinioClient minioClient;

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
            InputStream inputStream = video.getInputStream();

            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(VIDEO_BUCKET)
                    .object("")
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(video.getContentType())
                    .build()
            );

            return "";
        } catch (Exception e) {
            //TODO add exception here
            throw new ImageUploadException("");
        }
    }

    public String updateVideo(MultipartFile video, String fileName) {
        deleteVideo(fileName);
        return uploadVideo(video, fileName);
    }

    public void deleteVideo(String fileName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(VIDEO_BUCKET)
                    .object(fileName)
                    .build()
            );
        } catch (Exception e) {
            //TODO create normal stuff
            throw new ImageUploadException("This failed");
        }
    }
}
