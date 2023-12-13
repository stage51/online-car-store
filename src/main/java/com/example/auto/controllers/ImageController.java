package com.example.auto.controllers;

import com.example.auto.utils.ImageUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.http.HttpResponse;

@RestController
public class ImageController {
    private ImageUtil imageUtil;
    @Autowired
    public void setImageUtil(ImageUtil imageUtil){this.imageUtil = imageUtil;}
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> loadImage(
            @PathVariable String filename, HttpServletRequest request)
            throws IOException {
        Resource resource = imageUtil.loadImage(filename);
        String contentType;
        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
