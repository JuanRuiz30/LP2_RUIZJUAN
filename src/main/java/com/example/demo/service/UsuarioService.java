package com.example.demo.service;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Usuario;

public interface UsuarioService {

	void crearUsuario(Usuario usuario, Model model, MultipartFile foto);

	boolean validarUsuario(Usuario usuario, HttpSession session);

	Usuario buscarUsuarioPorCorreo(String correo);
}
