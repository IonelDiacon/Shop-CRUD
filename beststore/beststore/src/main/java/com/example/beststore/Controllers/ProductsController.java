package com.example.beststore.Controllers;


import com.example.beststore.models.Produc;
import com.example.beststore.models.ProductDto;
import com.example.beststore.services.ProductsRepository;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;



@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsRepository repo;

    @GetMapping({"","/"})
    public String showProductList(Model model){
        List<Produc> products = repo.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("products",products);
        return "products/index";

    }

    @GetMapping("/create")
    public String showCreatePage(Model model){
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto",productDto);
        return "products/CreateFile";
    }
    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute ProductDto productDto, BindingResult result){

        if (productDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto", "ImageFile", "The image file is empty"));
        }
        if (result.hasErrors()) {
            return "products/createFile";
        }
        MultipartFile image = productDto.getImageFile();
        Date createAt = new Date();
        String storageFileName = image.getOriginalFilename();
        try{
            String uploadDir = "public/images/";
            Path uploadPath =  Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            try(InputStream inputStream= image.getInputStream()){
                Files.copy(inputStream,Paths.get(uploadDir+storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("exceptie"+ex.getMessage());
        }
        Produc product = new Produc();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product. setCreateAt(createAt);
        product.setImageFileName(storageFileName);

        repo.save(product);


        return "redirect:/products";
    }

    @GetMapping("/edit")
    public  String showEditPage(Model model, @RequestParam int id){

        try{
            Produc product = repo.findById(id).get();
            model.addAttribute("product",product);


            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());

          model.addAttribute("productDto",productDto);
            repo.save(product);





        }catch (Exception  ex){
            System.out.println("error"+ex.getMessage());
            return "redirect:/products";
        }

        return "products/Edit";
    }

    @PostMapping("/edit")
    public String updateProduct(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ) {
        try {
            Produc product = repo.findById(id).get();
            model.addAttribute("product", product);

            if (result.hasErrors()) {
                return "products/Edit";
            }


            if (!productDto.getImageFile().isEmpty()) {
                // delete old image
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());

                try {
                    Files.delete(oldImagePath);
                } catch(Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }

                // save new image file
                MultipartFile image = productDto.getImageFile();
                Date createdAt = new Date();
                String storageFileName =  image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                }

                product.setImageFileName(storageFileName);
            }

            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());



        } catch(Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id) {


        try {
            Produc product = repo.findById(id).get();

            Path imagePath = Paths.get("public/images" + product.getImageFileName());

            try {
                Files.delete(imagePath);
            } catch (IOException e) {
                System.out.println("error" + e);
            }

            repo.delete(product);
        } catch (Exception e) {
            System.out.println(e);
        }
        return"redirect:/products";
    }



}
