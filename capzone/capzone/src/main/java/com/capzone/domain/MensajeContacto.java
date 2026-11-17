package com.capzone.domain;

import java.time.LocalDateTime;

public class MensajeContacto {

    private int id;
    private String nombre;
    private String correo;
    private String asunto;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private boolean leido;

    public MensajeContacto() {
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
    }

    public MensajeContacto(String nombre, String correo, String asunto, String mensaje) {
        this();
        this.nombre = nombre;
        this.correo = correo;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public boolean isLeido() { return leido; }
    public void setLeido(boolean leido) { this.leido = leido; }

    @Override
    public String toString() {
        return "MensajeContacto{id=" + id + ", nombre='" + nombre + "', correo='" + correo
                + "', asunto='" + asunto + "', fechaEnvio=" + fechaEnvio + ", leido=" + leido + "}";
    }
}
