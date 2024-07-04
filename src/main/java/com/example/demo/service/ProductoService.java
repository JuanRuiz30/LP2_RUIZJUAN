package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Producto;

public interface ProductoService {

	List<Producto> buscarTodosProductos();

	Producto buscarProductoPorId(Long id);

	void save(Producto producto);
}
