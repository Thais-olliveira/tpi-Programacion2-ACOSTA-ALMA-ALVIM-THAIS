/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;
import enums.Rol;

/**
 *
 * @author Alma
 */
/**
 */
public class Usuario extends Base {

    private String nombre;
    private String apellido;
    private String mail;       // único
    private String celular;
    private String contrasena;
    private Rol rol;

    public Usuario(String nombre, String apellido, String mail, String celular, String contrasena, Rol rol) {
        super();
        this.nombre     = nombre;
        this.apellido   = apellido;
        this.mail       = mail;
        this.celular    = celular;
        this.contrasena = contrasena;
        this.rol        = rol;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────
    public String getNombre(){ 
        return nombre; }
    
    public void   setNombre(String nombre){ 
        this.nombre = nombre; }
    
    public String getApellido() {
        return apellido; }
    
    public void   setApellido(String apellido) {
        this.apellido = apellido; }
    
    public String getMail() {
        return mail; }
    
    public void   setMail(String mail) {
        this.mail = mail; }
    
    public String getCelular() {
        return celular; }
    
    public void   setCelular(String celular){ 
        this.celular = celular; }
    
    public String getContrasena() { 
        return contrasena; }
    
    public void   setContrasena(String c) { 
        this.contrasena = c; }
    
    public Rol    getRol()  { 
        return rol; }
    
    public void   setRol(Rol rol) { 
        this.rol = rol; }
    

    @Override
    public String toString() {
        return "[ID: " + getId() + "] " + nombre + " " + apellido + 
                " | Mail: " + mail +
                " | Celular: " + celular +
                " | Rol: " + rol;
               //String.format(
//            "[ID: %d] %-15s %-15s | Mail: %-30s | Cel: %-15s | Rol: %s",
//            getId(), nombre, apellido, mail, celular, rol
//        );
    }
}

