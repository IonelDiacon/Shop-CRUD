package com.example.beststore.services;

import com.example.beststore.models.Produc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository  extends JpaRepository<Produc, Integer> {
}
