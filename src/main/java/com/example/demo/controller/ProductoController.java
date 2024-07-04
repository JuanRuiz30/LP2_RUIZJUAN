package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Categoria;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.service.PdfService;
import com.example.demo.service.ProductoService;
import com.example.demo.service.UsuarioService;

@Controller
public class ProductoController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private PdfService pdfService;

	@GetMapping("/menu")
	public String showMenu(HttpSession session, Model model) {
		if (session.getAttribute("usuario") == null) {
			return "redirect:/";
		}

		String correo = session.getAttribute("usuario").toString();
		Usuario usuario = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto", usuario.getUrlImagen());
		model.addAttribute("nombre", usuario.getNombre());
		model.addAttribute("apellidos", usuario.getApellidos());

		List<Producto> productos = productoService.buscarTodosProductos();
		model.addAttribute("productos", productos);

		return "menu";
	}

	@GetMapping("/nuevo_producto")
	public String showRegistrarProducto(HttpSession session, Model model) {

		String correo = session.getAttribute("usuario").toString();
		Usuario usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto", usuarioEntity.getUrlImagen());
		model.addAttribute("nombre", usuarioEntity.getNombre());
		model.addAttribute("apellidos", usuarioEntity.getApellidos());

		model.addAttribute("producto", new Producto());
		List<Categoria> categorias = categoriaRepository.findAll();
		if (categorias.isEmpty()) {
			Categoria categoriaAlimentos = new Categoria(null, "Alimentos");
			Categoria categoriaJuguetes = new Categoria(null, "Juguetes");
			Categoria categoriaRopa = new Categoria(null, "Ropa");
			categoriaRepository.saveAll(List.of(categoriaAlimentos, categoriaJuguetes, categoriaRopa));
			categorias = List.of(categoriaAlimentos, categoriaJuguetes, categoriaRopa);
		}
		model.addAttribute("categorias", categorias);
		return "registrar_producto";
	}

	@PostMapping("/nuevo_producto")
	public String registrarProducto(@ModelAttribute Producto producto, Model model) {
		Categoria categoria = categoriaRepository.findById(producto.getCategoria().getCategoria_id())
				.orElseThrow(() -> new IllegalArgumentException("Categoría no válida"));
		producto.setCategoria(categoria);
		productoService.save(producto);
		return "redirect:/menu";
	}

	@GetMapping("/detalle_producto/{id}")
	public String verProducto(HttpSession session, Model model, @PathVariable("id") Long id) {

		String correo = session.getAttribute("usuario").toString();
		Usuario usuario = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto", usuario.getUrlImagen());
		model.addAttribute("nombre", usuario.getNombre());
		model.addAttribute("apellidos", usuario.getApellidos());

		Producto productoEncontrado = productoService.buscarProductoPorId(id);
		model.addAttribute("producto", productoEncontrado);
		return "detalle_producto";
	}

	@GetMapping("/eliminar_producto/{id}")
	public String eliminarProducto(@PathVariable("id") Long id) {
		productoRepository.deleteById(id);
		return "redirect:/menu";
	}

	@GetMapping("/editar_producto/{id}")
	public String mostrarFormularioEditar(HttpSession session, @PathVariable("id") Long id, Model model) {

		String correo = session.getAttribute("usuario").toString();
		Usuario usuario = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto", usuario.getUrlImagen());
		model.addAttribute("nombre", usuario.getNombre());
		model.addAttribute("apellidos", usuario.getApellidos());

		Producto producto = productoService.buscarProductoPorId(id);
		List<Categoria> categorias = categoriaRepository.findAll();
		model.addAttribute("producto", producto);
		model.addAttribute("categorias", categorias);
		return "editar_producto";
	}

	@PostMapping("/actualizar_producto")
	public String actualizarProducto(@ModelAttribute("producto") Producto producto, Model model) {
		try {
			productoService.save(producto);
			return "redirect:/menu";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "Error al actualizar el producto: " + e.getMessage());
			model.addAttribute("producto", producto);
			return "editar_producto";
		}
	}

	@GetMapping("/productos/generar_pdf")
	public ResponseEntity<InputStreamResource> generarPdf(HttpSession session, Model model) throws IOException {
		if (session.getAttribute("usuario") == null) {
			return ResponseEntity.status(401).build();
		}

		String correo = session.getAttribute("usuario").toString();
		Usuario usuario = usuarioService.buscarUsuarioPorCorreo(correo);
		if (usuario == null) {
			return ResponseEntity.status(401).build();
		}

		List<Producto> productos = productoService.buscarTodosProductos();
		Map<String, Object> datosPdf = Map.of("productos", productos, "nombreCompletoUsuario",
				usuario.getNombre() + " " + usuario.getApellidos());

		ByteArrayInputStream pdfBytes = pdfService.generarPdfDeHtml("template_pdf_productos", datosPdf);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Disposition", "inline; filename=productos.pdf");

		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(pdfBytes));
	}
}