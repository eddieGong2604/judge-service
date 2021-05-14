package com.csit314.testservice.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    public Resource loadFileAsResource(String fileName) throws MalformedURLException {
        Path filePath = Paths.get("src/main/resources").resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("File not found " + fileName);
        }
    }
}