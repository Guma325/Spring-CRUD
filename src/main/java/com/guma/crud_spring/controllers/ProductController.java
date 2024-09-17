package com.guma.crud_spring.controllers;

import com.guma.crud_spring.domain.product.Product;
import com.guma.crud_spring.domain.product.ProductRepository;
import com.guma.crud_spring.domain.product.RequestProduct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.beans.Transient;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity getAllProducts() {
        var allProducts = productRepository.findAllByActiveTrue();
        return ResponseEntity.ok(allProducts);
    }
    @PostMapping
    public ResponseEntity registerProduct(@RequestBody @Valid RequestProduct data) {
        System.out.println(data);
        Product produto = new Product(data);
        productRepository.save(produto);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity updateProduct(@RequestBody @Valid RequestProduct data) {
        Optional<Product> optionalProduto = productRepository.findById(data.id());
        if (optionalProduto.isPresent()) {
            Product produto = optionalProduto.get();
            produto.setName(data.name());
            produto.setPrice_in_cents(data.price_in_cents());
            return ResponseEntity.ok().build();
        }
        throw new EntityNotFoundException();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteProduct(@PathVariable String id) {
        Optional<Product> optionalProduto = productRepository.findById(id);
        if (optionalProduto.isPresent()) {
            Product produto = optionalProduto.get();
            System.out.println(produto.getName());
            produto.setActive(false);
            return ResponseEntity.noContent().build();
        }
        throw new EntityNotFoundException();
    }

}
