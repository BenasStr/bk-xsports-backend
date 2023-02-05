package com.ktu.xsports.api.service;

import com.ktu.xsports.api.exceptions.ImageUploadException;
import com.ktu.xsports.api.exceptions.NotAnImageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@Service
public class ImageService {

    public void uploadImage(MultipartFile image) {
        try {
            ImageIO.read(image.getInputStream());
        } catch (IOException e) {
            throw new NotAnImageException("uga buga"); //TODO create error.
        }

        try {
            //TODO make this shorter
            image.transferTo(new File("/Users/benas/Coding/Learning/University/Bakalauras/bk-xsports-backend/src/main/resources/images/" + image.getOriginalFilename()));
        } catch (IOException e) {
            throw new ImageUploadException("ugen bugen 2"); //TODO create error.
        }
    }
}
