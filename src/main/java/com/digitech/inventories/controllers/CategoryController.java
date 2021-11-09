package com.digitech.inventories.controllers;

import com.digitech.inventories.entities.Category;
import com.digitech.inventories.entities.FormWrapper;
import com.digitech.inventories.entities.Response;
import com.digitech.inventories.repositories.CategoryRepository;
import com.digitech.inventories.services.AppService;
import com.digitech.inventories.services.AppServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class CategoryController {

    @Autowired
    CategoryRepository repository;

    @Autowired
    AppServiceImpl service;

    @GetMapping("/api/rest/categories")
    public ResponseEntity<?> list(){
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }
    
    @PostMapping("/api/rest/categories")
    public ResponseEntity<?> create(@Valid @ModelAttribute FormWrapper model) {
        // SAVE
        String fileName =  service.save(model.getImage());
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();
        Category category = new Category();
        category.setName(model.getName());
        category.setDescription(model.getDescription());
        category.setImageUrl(imageUrl);
        repository.save(category);

        return new ResponseEntity<Category>(category, HttpStatus.CREATED);
    }
    
    @GetMapping("/api/rest/categories/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") Long id) {

        Category category = repository.findById(id).orElse(null);
        if (null == category){
            Response response = new Response();
            response.setResponseAt(LocalDateTime.now());
            response.setMessage("Category not found.");

            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Category>(category, HttpStatus.OK);
    }
    
    @PutMapping(value="/api/rest/categories/{id}")
    public ResponseEntity<?>  update(@PathVariable(name = "id") Long id  , @ModelAttribute FormWrapper model  ) {
        //TODSomeEnityDataOentity: process PUT request

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Category edit(Category category, @ModelAttribute FormWrapper model){

        return category;
    }
    
}
