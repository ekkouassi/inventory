package com.digitech.inventories.controllers;

import com.digitech.inventories.entities.Category;
import com.digitech.inventories.entities.FormWrapper;
import com.digitech.inventories.entities.Product;
import com.digitech.inventories.entities.Response;
import com.digitech.inventories.repositories.CategoryRepository;
import com.digitech.inventories.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/api/rest/products")
    public ResponseEntity<List<Product>> list(){
        List<Product> products = productRepository.findAll();
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }

    @PostMapping("/api/rest/products")
    public ResponseEntity<?> create(@ModelAttribute FormWrapper model){
        Category category = categoryRepository.findById(model.getCategory()).orElse(null);

        if (null == category){
            Response response = new Response();
            response.setMessage("Sorry, category is required to create product.");
            response.setResponseAt(LocalDateTime.now());

            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }

        Product product = new Product();
        this.edit(product, model);
        product.setCategory(category);
        productRepository.save(product);

        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }

    private Product edit (Product product, FormWrapper formWrapper){
        if (null != formWrapper.getName())
            product.setName(formWrapper.getName());
        if (null != formWrapper.getDescription())
            product.setDescription(formWrapper.getDescription());
        if (null != formWrapper.getPrice())
            product.setPrice(formWrapper.getPrice());
        if (formWrapper.getQuantity() > 0)
            product.setQuantity(formWrapper.getQuantity());
        return product;
    }
}
