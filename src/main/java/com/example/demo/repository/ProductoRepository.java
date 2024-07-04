package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Producto;
import com.example.demo.entity.Usuario;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
