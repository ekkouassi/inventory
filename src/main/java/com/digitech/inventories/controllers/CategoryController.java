package com.digitech.inventories.controllers;

import com.digitech.inventories.entities.Category;
import com.digitech.inventories.entities.FormWrapper;
import com.digitech.inventories.entities.Response;
import com.digitech.inventories.repositories.CategoryRepository;
import com.digitech.inventories.services.AppServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.PutMapping;
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
        Category category = new Category();

        if (null != model.getCategory()){
            Category cat = repository.findById(model.getCategory()).orElse(null);
            assert cat != null;
            category.setCategory(cat);
        }

        repository.save(this.edit(category, model));

        return new ResponseEntity<Category>(category, HttpStatus.CREATED);
    }
    
    @GetMapping("/api/rest/categories/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") String id) {

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
    public ResponseEntity<?>  update(@PathVariable(name = "id") String id  , @ModelAttribute FormWrapper model  ) {
        //TODSomeEnityDataOentity: process PUT request
        Category category = repository.findById(id).orElse(null);

        if (null == category){
            Response response = new Response();
            response.setResponseAt(LocalDateTime.now());
            response.setMessage("Category not found.");

            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }

        this.edit(category, model);
        repository.save(category);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/api/rest/categories/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") String id){
        Category category = repository.findById(id).orElse(null);
        Response response = new Response();

        if (null == category){
            response.setResponseAt(LocalDateTime.now());
            response.setMessage("Category not found.");

            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }

        repository.delete(category);

        response.setMessage("Category " + category.getName() + "was deleted successful.");
        response.setResponseAt(LocalDateTime.now());

        return new ResponseEntity<Response>(response, HttpStatus.NO_CONTENT);
    }

    private Category edit(Category category, @ModelAttribute FormWrapper model){
        if(null != model.getName()) {
            category.setName(model.getName());
        }
        if (null != model.getDescription()){
            category.setDescription(model.getDescription());
        }
        if (null != model.getImage()){
            String fileName =  service.save(model.getImage());
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(fileName)
                    .toUriString();
            category.setImageUrl(imageUrl);
        }
        return category;
    }
    
}
