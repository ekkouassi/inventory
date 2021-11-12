package com.digitech.inventories.services;

import com.digitech.inventories.entities.FormWrapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

@Service
public class AppServiceImpl implements AppService {

    @Override
    public void init() {
        try {
            Path root = Paths.get("uploads");
            Files.createDirectory(root);
        }catch (IOException e){
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file) {
       try {
         String filename =  this.GenerateFileName(10);
         String extension = file.getOriginalFilename().split("\\.")[1];
         byte[] bytes = file.getBytes();
         String uploadedFile = filename +"." + extension;
         String insPath =  "uploads/" + uploadedFile;
         Files.write(Paths.get(insPath), bytes);

         return ServletUriComponentsBuilder.fromCurrentContextPath()
                   .path("/uploads/")
                   .path(uploadedFile)
                   .toUriString();
       } catch (IOException e) {
           throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
       }
    }

    private String GenerateFileName (int length) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        Random rnd = new Random();
        StringBuilder sb =new StringBuilder(length);

        for (int i = 0; i < length; ++i)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString().toLowerCase();

    }
}
