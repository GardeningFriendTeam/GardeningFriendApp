package com.maid.gardeningfriend.panelAdmin;

import java.util.ArrayList;

public class UsuariosGenerador {
    //ATRIBUTOS
    private String ID;
    private String name;
    private String email;
    private ArrayList<String> favoritos;
    boolean isAdmin;

    //constructor
    public UsuariosGenerador(String ID, String name, String email, ArrayList<String> favoritos, boolean isAdmin) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.favoritos = favoritos;
        this.isAdmin = isAdmin;
    }

    // getters y setters
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(ArrayList<String> favoritos) {
        this.favoritos = favoritos;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
