package com.ecom.ecom_proj.controller;

import com.ecom.ecom_proj.model.Product;
import com.ecom.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.GenericArrayType;
import java.sql.SQLOutput;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> allProducts = productService.getAllProducts();
        if(allProducts.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<> (allProducts,HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        Product requestedProduct =productService.getProductById(id);
        if(requestedProduct != null) {
            return new ResponseEntity<>(requestedProduct, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/product")  //used by - AddProduct comp ,
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
        try{
            Product product1 = productService.addProduct(product,imageFile);
            return new ResponseEntity<>(product1,HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{productId}/image")  // Home Comp ,
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
        Product product1 = productService.getProductById(productId);
        byte[] imageFile = product1.getImageData();
        if (imageFile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product1.getImageType()))
                .body(imageFile);
    }


    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,
                                                @RequestPart Product product,
                                                @RequestPart MultipartFile imageFile){
         Product product1 =  null;
         try{
             product1 =  productService.updateProduct(id,product,imageFile);
         }catch (Exception e){
             return  new ResponseEntity<>("failesd",HttpStatus.BAD_REQUEST);
         }
         if(product1 != null){
             return new ResponseEntity<>("Updated",HttpStatus.OK);
         }
         else {
             return new ResponseEntity<>("Fail to up", HttpStatus.BAD_REQUEST);
         }
    }


    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product1 = productService.getProductById(id);
        if (product1 != null){
            productService.deleteProduct(id);
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Product notfound",HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        List<Product> products = productService.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

}
