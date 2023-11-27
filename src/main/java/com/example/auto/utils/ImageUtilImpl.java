package com.example.auto.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Component
public class ImageUtilImpl implements ImageUtil {
    String directory = "src/main/resources/static/images/";
    public ImageUtilImpl(){}
    public String saveImage(MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String name = encrypt(file.getOriginalFilename()) + file.getOriginalFilename();
                Path path = Paths.get(directory + name);
                Files.write(path, bytes);
                return name;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public Resource loadImage(String fileName)
            throws MalformedURLException {
        Path fileStorageLocation =
                Paths.get("src/main/resources/static/images/").toAbsolutePath().normalize();
        Path filePath = fileStorageLocation.resolve(fileName).normalize();
        return new UrlResource(filePath.toUri());
    }
    private String encrypt(String text){
        StringBuilder result = new StringBuilder(text);
        result.append(new Date());
        return String.valueOf(result.toString().hashCode());
    }
}
