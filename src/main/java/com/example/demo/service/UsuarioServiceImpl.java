package com.example.demo.service;

import javax.servlet.http.HttpSession; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.UsuarioService;
import com.example.demo.utils.Utilitarios;


@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	
	@Override
	public void crearUsuario(Usuario usuario, Model model, MultipartFile foto) {
		// guardar foto
		String nombreFoto = Utilitarios.guardarImagen(foto);
		usuario.setUrlImagen(nombreFoto);
		
		//Hash Password
		String passwordHash = Utilitarios.extraerHash(usuario.getPassword());
		usuario.setPassword(passwordHash);
		
		// guardar usuario
		usuarioRepository.save(usuario);
		
		// responder a la vista
		model.addAttribute("registroCorrecto", "Registro Correcto");
		model.addAttribute("usuario", new Usuario());
		
	}

	@Override
	public boolean validarUsuario(Usuario usuario, HttpSession session) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Usuario buscarUsuarioPorCorreo(String correo) {
		// TODO Auto-generated method stub
		return null;
	}

}

