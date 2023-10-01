package com.maid.gardeningfriend;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Crea un objeto parceable con los parametros selec por el usuario en recomendaciones
 * para lugar enviarlo a la segunda pantalla de la seccion
 * @return objeto parceable con param selecionados
 */
public class CultivosRecoParceable implements Parcelable{
    private String temperaturaSelec;
    private String estacionSelec;
    private String regSelec;

    //constructor
    public CultivosRecoParceable(String temperaturaSelec, String estacionSelec, String regSelec) {
        this.temperaturaSelec = temperaturaSelec;
        this.estacionSelec = estacionSelec;
        this.regSelec = regSelec;
    }

    //getters
    public String getTemperaturaSelec() {
        return temperaturaSelec;
    }

    public String getEstacionSelec() {
        return estacionSelec;
    }

    public String getRegSelec() {
        return regSelec;
    }


    //parceable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(temperaturaSelec);
        dest.writeString(estacionSelec);
        dest.writeString(regSelec);
    }

    public static final Creator<CultivosRecoParceable> CREATOR = new Creator<CultivosRecoParceable>() {
        @Override
        public CultivosRecoParceable createFromParcel(Parcel parcel) {
            // se recrea el objeto
            String temperaturaSelec = parcel.readString();
            String estacionSelec = parcel.readString();
            String regSelec = parcel.readString();
            return new CultivosRecoParceable(temperaturaSelec, estacionSelec, regSelec);
        }

        @Override
        public CultivosRecoParceable[] newArray(int i) {
            return new CultivosRecoParceable[i];
        }
    };

    private CultivosRecoParceable(Parcel in){
        temperaturaSelec = in.readString();
        estacionSelec = in.readString();
        regSelec = in.readString();
    }

}
