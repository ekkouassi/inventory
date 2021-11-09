package com.digitech.inventories.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AppService {
    public void init();

    public String save(MultipartFile file);

    public Resource load(String filename);
}
