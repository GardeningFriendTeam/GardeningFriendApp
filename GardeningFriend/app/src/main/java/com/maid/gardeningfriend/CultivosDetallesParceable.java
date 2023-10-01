package com.maid.gardeningfriend;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * esta clase crea un objeto parceable
 * con toda la info asociada a la tarjeta seleccionada
 * en 'recomendaciones' luego de filtrar
 */
public class CultivosDetallesParceable implements Parcelable {
    //atributos
    String nombreCultivo;
    String tempCultivo;
    String estCultivo;
    String regCultivo;
    String infoCultivo;
    String imgCultivo;

    //constructor
    public CultivosDetallesParceable(String nombreCultivo, String tempCultivo, String estCultivo, String regCultivo, String infoCultivo, String imgCultivo) {
        this.nombreCultivo = nombreCultivo;
        this.tempCultivo = tempCultivo;
        this.estCultivo = estCultivo;
        this.regCultivo = regCultivo;
        this.infoCultivo = infoCultivo;
        this.imgCultivo = imgCultivo;
    }

    //getters
    public String getNombreCultivo() {
        return nombreCultivo;
    }

    public String getTempCultivo() {
        return tempCultivo;
    }

    public String getEstCultivo() {
        return estCultivo;
    }

    public String getRegCultivo() {
        return regCultivo;
    }

    public String getInfoCultivo() {
        return infoCultivo;
    }

    public String getImgCultivo() {
        return imgCultivo;
    }

    //metodos especiales

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreCultivo);
        dest.writeString(tempCultivo);
        dest.writeString(estCultivo);
        dest.writeString(regCultivo);
        dest.writeString(infoCultivo);
        dest.writeString(imgCultivo);
    }

    public static final Creator<CultivosDetallesParceable> CREATOR = new Creator<CultivosDetallesParceable>() {
        @Override
        public CultivosDetallesParceable createFromParcel(Parcel parcel) {
            // se recrea el objeto
            String nombreCultivo = parcel.readString();
            String tempCultivo = parcel.readString();
            String estCultivo = parcel.readString();
            String regCultivo = parcel.readString();
            String infoCultivo = parcel.readString();
            String imgCultivo = parcel.readString();
            return new CultivosDetallesParceable(nombreCultivo,tempCultivo,estCultivo,regCultivo,infoCultivo,imgCultivo);
        }

        @Override
        public CultivosDetallesParceable[] newArray(int i) {
            return new CultivosDetallesParceable[i];
        }
    };

    private CultivosDetallesParceable(Parcel in){
        nombreCultivo = in.readString();
        tempCultivo = in.readString();
        estCultivo = in.readString();
        regCultivo = in.readString();
        infoCultivo = in.readString();
        imgCultivo = in.readString();
    }
}
