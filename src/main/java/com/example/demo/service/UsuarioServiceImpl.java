package com.example.demo.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.utils.Utilitarios;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public void crearUsuario(Usuario usuario, Model model, MultipartFile foto) {
		String nombreFoto = Utilitarios.guardarImagen(foto);
		usuario.setUrlImagen(nombreFoto);

		String passwordHash = Utilitarios.extraerHash(usuario.getPassword());
		usuario.setPassword(passwordHash);

		usuarioRepository.save(usuario);

		model.addAttribute("registroCorrecto", "Registro Correcto");
		model.addAttribute("usuario", new Usuario());

	}

	@Override
	public boolean validarUsuario(Usuario usuario, HttpSession session) {
		Usuario usuarioEncontradoPorcCorreo = usuarioRepository.findByCorreo(usuario.getCorreo());

		if (usuarioEncontradoPorcCorreo == null) {
			return false;
		}

		if (!Utilitarios.checkPassword(usuario.getPassword(), usuarioEncontradoPorcCorreo.getPassword())) {
			return false;
		}

		session.setAttribute("usuario", usuarioEncontradoPorcCorreo.getCorreo());

		return true;
	}

	@Override
	public Usuario buscarUsuarioPorCorreo(String correo) {
		return usuarioRepository.findByCorreo(correo);
	}

}