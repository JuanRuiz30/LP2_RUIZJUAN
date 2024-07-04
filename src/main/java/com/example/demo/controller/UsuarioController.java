package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Producto;
import com.example.demo.entity.Usuario;

import com.example.demo.service.UsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/registrar")
	public String showRegistrarUsuario(Model model) {

		model.addAttribute("usuario", new Usuario());
		return "registrar_usuario";
	}

	@PostMapping("/registrar")
	public String registrarUsuario(Usuario usuario, Model model, @RequestParam("foto") MultipartFile foto) {

		usuarioService.crearUsuario(usuario, model, foto);
		return "registrar_usuario";
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "login";
	}

	@PostMapping("/login")
	public String login(Usuario usuario, Model model, HttpSession session) {
		boolean usuarioValido = usuarioService.validarUsuario(usuario, session);
		if (usuarioValido) {
			return "redirect:/menu";
		}
		model.addAttribute("loginInvalido", "No existe el usuario");
		model.addAttribute("usuario", new Usuario());
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

}