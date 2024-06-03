package com.maid.gardeningfriend.panelAdmin;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;

public class RecetasGenerador {

    // atributos
    String nombre;
    String categoria;
    String cookTime;
    String imageUrl;
    ArrayList<String> Ingredientes = new ArrayList<String>();
    String instrucciones;
    @DocumentId
    String id;

    // constructor
    public RecetasGenerador(String nombre, String categoria, String cookTime, String imageUrl, ArrayList<String> ingredientes, String instrucciones, String id) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.cookTime = cookTime;
        this.imageUrl = imageUrl;
        Ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.id = id;
    }

    public RecetasGenerador() {
        // Constructor vacío requerido por Firestore
    }

    // getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<String> getIngredientes() {
        return Ingredientes;
    }

    public void setIngredientes(ArrayList<String> ingredientes) {
        Ingredientes = ingredientes;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
