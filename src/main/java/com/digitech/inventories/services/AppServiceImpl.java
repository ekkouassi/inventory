package com.digitech.inventories.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class AppServiceImpl implements AppService {
    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        }catch (IOException e){
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file) {
       try {
           Files.copy(file.getInputStream(), this.root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
           return StringUtils.cleanPath(file.getOriginalFilename());
       } catch (Exception e) {
           throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
       }
    }

    @Override
    public Resource load(String filename) {
        return null;
    }
}
