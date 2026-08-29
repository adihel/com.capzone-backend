package com.capzone;

public class MensajeContacto {
    private int idContacto;

    public int getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(int idContacto) {
        this.idContacto = idContacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public MensajeContacto(int idContacto, String nombre, String correo, String mensaje, LocalDateTime fechaEnvio) {
        this.idContacto = idContacto;
        this.nombre = nombre;
        this.correo = correo;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
    }

    private String nombre;
    private String correo;
    private String mensaje;
    private LocalDateTime fechaEnvio;

    public MensajeContacto() {
    }
}
