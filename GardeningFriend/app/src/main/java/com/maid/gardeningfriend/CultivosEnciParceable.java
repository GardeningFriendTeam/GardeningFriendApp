package com.maid.gardeningfriend;

import android.os.Parcel;
import android.os.Parcelable;

public class CultivosEnciParceable implements Parcelable {
    /**
     * Crea un objeto parceable con texto ingresado por el usuario en
     * enciclopedia
     * para enviarlo a la segunda pantalla de la seccion
     * @return objeto parceable con param selecionados
     */

    private String nombreIngresado;

    //constructor
    public CultivosEnciParceable(String nombreIngresado) {
        this.nombreIngresado = nombreIngresado;
    }

    //getters
    public String getnombreIngresado() {
        return nombreIngresado;
    }


    //parceable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(nombreIngresado);
    }

    public static final Parcelable.Creator<CultivosEnciParceable> CREATOR = new Parcelable.Creator<CultivosEnciParceable>() {
        @Override
        public CultivosEnciParceable createFromParcel(Parcel parcel) {
            // se recrea el objeto
            String nombreIngresado = parcel.readString();
            return new CultivosEnciParceable(nombreIngresado);
        }

        @Override
        public CultivosEnciParceable[] newArray(int i) {
            return new CultivosEnciParceable[i];
        }
    };

    private CultivosEnciParceable(Parcel in){
        nombreIngresado = in.readString();
    }
}
