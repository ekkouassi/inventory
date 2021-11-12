package com.digitech.inventories.controllers;

import com.digitech.inventories.entities.Category;
import com.digitech.inventories.entities.FormWrapper;
import com.digitech.inventories.entities.Product;
import com.digitech.inventories.entities.Response;
import com.digitech.inventories.repositories.CategoryRepository;
import com.digitech.inventories.repositories.ProductRepository;
import com.digitech.inventories.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;


@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AppService service;

    @GetMapping("/api/rest/products")
    public ResponseEntity<List<Product>> list(){
        List<Product> products = productRepository.findAll();
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }

    @PostMapping("/api/rest/products")
    public ResponseEntity<?> create(@Valid @ModelAttribute FormWrapper model){
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
    
    @GetMapping("/api/rest/products/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") String id){
        Product product = productRepository.findById(id).orElse(null);

        if (null == product){
            Response response = new Response();
            response.setResponseAt(LocalDateTime.now());
            response.setMessage("Sorry, the given isn't exists. product not found.");

            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @PutMapping("/api/rest/products/{id}")
    public ResponseEntity<?> update(@ModelAttribute FormWrapper formWrapper, @PathVariable(name = "id") String id) {
        Product product = productRepository.findById(id).orElse(null);

        if (null == product){
            Response response = new Response();
            response.setResponseAt(LocalDateTime.now());
            response.setMessage("Sorry, the given isn't exists. product not found.");

            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }
        productRepository.save(this.edit(product, formWrapper));

        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @DeleteMapping("/api/rest/products/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") String id){
        Product product = productRepository.findById(id).orElse(null);
        Response response = new Response();
        if (null == product){
            response.setResponseAt(LocalDateTime.now());
            response.setMessage("Product not found.");
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }
        productRepository.delete(product);

        response.setResponseAt(LocalDateTime.now());
        response.setMessage("Product response is deleted successful");
        return new ResponseEntity<Response>(response, HttpStatus.NO_CONTENT);
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
        if (null != formWrapper.getImage()){
            String imageUrl = service.save(formWrapper.getImage());
            product.setImageUrl(imageUrl);
        }
        return product;
    }
}
