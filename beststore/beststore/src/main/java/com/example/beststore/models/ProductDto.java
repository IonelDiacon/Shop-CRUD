package com.example.beststore.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class ProductDto {

    public Date createAt;
    @NotEmpty(message = "the name is required!")
    private String name;
    @NotEmpty(message = "the brand is required!")
    private String brand;
    @NotEmpty(message = "the category is required!")
    private String category;
@Min(value = 0 , message = "min 0")
    private double price;

@Size(min = 10,max = 2000,message = "the description length from 10 to 2000 letters")
private String description;

private MultipartFile  ImageFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImageFile() {
        return ImageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        ImageFile = imageFile;
    }
}
