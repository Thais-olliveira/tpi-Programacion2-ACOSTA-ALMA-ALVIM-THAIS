/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entities.Usuario;
import enums.Rol;
import exception.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alma
/**
 * Servicio que gestiona el CRUD de Usuarios en memoria.
 *
 * Reglas de negocio:
 *  - El mail debe ser único entre usuarios activos.
 *  - Soft-delete: eliminado = true.
 *  - Los pedidos existentes de un usuario eliminado siguen consultables.
 */
public class UsuarioService {

    private final List<Usuario> usuarios = new ArrayList<>();

    // ── Listar ────────────────────────────────────────────────────────────
    public List<Usuario> listarActivos() {
        return usuarios.stream().filter(u -> !u.isEliminado()).toList();
    }

    // ── Buscar por ID ─────────────────────────────────────────────────────
    public Usuario buscarPorId(long id) throws EntidadNoEncontradaException {
        return usuarios.stream()
            .filter(u -> u.getId() == id && !u.isEliminado())
            .findFirst()
            .orElseThrow(() -> new EntidadNoEncontradaException(
                "No se encontró usuario activo con ID: " + id));
    }

    // ── Crear ─────────────────────────────────────────────────────────────
    public Usuario crear(String nombre, String apellido, String mail, String celular, String contrasena, Rol rol) throws MailDuplicadoException {
        validarMail(mail, -1);
        Usuario nuevo = new Usuario(nombre, apellido, mail, celular, contrasena, rol);
        usuarios.add(nuevo);
        return nuevo;
    }

    // ── Editar ────────────────────────────────────────────────────────────
    public void editar(long id, String nombre, String apellido, String mail, String celular, String contrasena, Rol rol) throws EntidadNoEncontradaException, MailDuplicadoException {
        Usuario u = buscarPorId(id);

        if (nombre     != null && !nombre.isBlank())     u.setNombre(nombre);
        if (apellido   != null && !apellido.isBlank())   u.setApellido(apellido);
        if (mail       != null && !mail.isBlank()) {
            validarMail(mail, id);   // excluye al propio usuario
            u.setMail(mail);
        }
        if (celular    != null && !celular.isBlank())    u.setCelular(celular);
        if (contrasena != null && !contrasena.isBlank()) u.setContrasena(contrasena);
        if (rol        != null)                          u.setRol(rol);
    }

    // ── Eliminar ──────────────────────────────────────────────────────────
    public void eliminar(long id) throws EntidadNoEncontradaException {
        Usuario u = buscarPorId(id);
        u.setEliminado(true);
    }

    // ── Validaciones internas ─────────────────────────────────────────────
    /**
     * Valida unicidad de mail excluyendo al usuario con el id dado
     * (usar id = -1 cuando es un alta nueva).
     */
    private void validarMail(String mail, long excludeId) throws MailDuplicadoException {
        if (mail == null || mail.isBlank()) {
            throw new IllegalArgumentException("El mail no puede estar vacío.");
        }
        boolean dup = usuarios.stream()
            .filter(u -> !u.isEliminado() && u.getId() != excludeId)
            .anyMatch(u -> u.getMail().equalsIgnoreCase(mail));
        if (dup) throw new MailDuplicadoException(mail);
    }

    private static class MailDuplicadoException extends Exception {
        public MailDuplicadoException(String mail) {
        }
    }
}
