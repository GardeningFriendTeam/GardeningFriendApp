package com.maid.gardeningfriend;

/**
 * Genera los cultivos que van a poblar las secciones
 * de "recomendaciones" y "enciclopedia"
 * @return objetos que contienen la info de cultivos
 */
public class CultivosGenerador {
    //atributos
    String ID;
    String nombre;
    String tipo;
    String duracionCrecimiento;
    String caracteristicas;
    String temperatura;
    String estacionSiembra;
    String region;
    String imagen;

    //constructor
    public CultivosGenerador(String ID, String nombre, String tipo, String duracionCrecimiento, String caracteristicas, String temperatura, String estacionSiembra, String region, String imagen) {
        this.ID = ID;
        this.nombre = nombre;
        this.tipo = tipo;
        this.duracionCrecimiento = duracionCrecimiento;
        this.caracteristicas = caracteristicas;
        this.temperatura = temperatura;
        this.estacionSiembra = estacionSiembra;
        this.region = region;
        this.imagen = imagen;
    }

    //getters
    public String getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDuracionCrecimiento() {
        return duracionCrecimiento;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public String getEstacionSiembra() {
        return estacionSiembra;
    }

    public String getRegion() {
        return region;
    }

    public String getImagen() {
        return imagen;
    }
}
