package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
}
