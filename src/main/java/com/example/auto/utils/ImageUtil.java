package com.example.auto.utils;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

public interface ImageUtil {
    Resource loadImage(String fileName) throws MalformedURLException;
    String saveImage(MultipartFile file);

}
