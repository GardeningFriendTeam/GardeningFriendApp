package com.maid.gardeningfriend;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * genera un objeto parceable del usuario seleccionado
 * para pasar sus datos a la siguiente pantalla
 */
public class UsuarioParceable implements Parcelable {
    //Atributos
    String ID;
    String name;
    String email;
    String rol;

    //constructor
    public UsuarioParceable(String ID, String name, String email, String rol) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.rol = rol;

    }

    //getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    public String getID() {
        return ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(rol);
    }

    public static final Creator<UsuarioParceable> CREATOR = new Creator<UsuarioParceable>() {
        @Override
        public UsuarioParceable createFromParcel(Parcel parcel) {
            String ID = parcel.readString();
            String name = parcel.readString();
            String email = parcel.readString();
            String rol = parcel.readString();
            return new UsuarioParceable(ID,name,email,rol);
        }

        @Override
        public UsuarioParceable[] newArray(int i) {
            return new UsuarioParceable[i];
        }
    };
}
