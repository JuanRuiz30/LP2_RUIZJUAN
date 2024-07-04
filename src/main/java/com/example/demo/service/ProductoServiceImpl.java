package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Producto;
import com.example.demo.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private ProductoRepository productoRepository;

	@Override
	public List<Producto> buscarTodosProductos() {

		return productoRepository.findAll();
	}

	@Override
	public Producto buscarProductoPorId(Long id) {
		return productoRepository.findById(id.longValue()).get();
	}

	@Override
	public void save(Producto producto) {
		productoRepository.save(producto);
	}

}
