package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * segunda pantalla de "recomendaciones"
 * se muestran los cultivos filtrados
 */
public class RecomendacionesCultivos extends MainActivity {

    //variables que contienen los parametros selec
    String tempSelec;
    String estSelec;
    String regSelec;
    // array que contiene los cultivos que coinciden con los parametros
    ArrayList<CultivosGenerador> cultivosFiltrados = new ArrayList<>();
    int[] imagenesCultivos = {R.mipmap.ic_aceituna, R.mipmap.ic_calabaza, R.mipmap.ic_cebolla, R.mipmap.ic_lechuga};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendaciones_cultivos);

        //se recibe el objeto parceable con los datos
        CultivosReco opcSeleccionadas = getIntent().getParcelableExtra("datosReco");

        // se reciben los datos selec en la pantalla anterior
        tempSelec = opcSeleccionadas.getTemperaturaSelec();
        estSelec = opcSeleccionadas.getEstacionSelec();
        regSelec = opcSeleccionadas.getRegSelec();

        //se identifica el recyclerview de la activity
        RecyclerView recycler = findViewById(R.id.recycler_cultivos);

        //se inicializa funcion para agregar las tarjetas
        addModelsCultivos();

        // se activa el "adapter" para que pase las tarjetas al recycler
        RecomendacionesRecyclerView adapter = new RecomendacionesRecyclerView(this,cultivosFiltrados);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * agrega los cultivos que coinciden con los param selecionados por el user
     * @return cultivos que se añaden a un array
     */
    private void addModelsCultivos(){
        // flag que se activa si hay resultados que coinciden con la busqueda
        boolean resultados = false;

        // 1 - se extraen todos los recursos para instanciar los cultivos
        String[] cultivosNombre = getResources().getStringArray(R.array.cultivos_nombres);
        String[] cultivosTemp = getResources().getStringArray(R.array.culvtivos_temp);
        String[] cultivosEst = getResources().getStringArray(R.array.cultivos_estacion);
        String[] cultivosReg = getResources().getStringArray(R.array.cultivos_region);
        String[] cultivosInfo = getResources().getStringArray(R.array.cultivos_info);
        String[] cultivosID = getResources().getStringArray(R.array.cultivos_id);
        String[] cultivosCrecimiento = getResources().getStringArray(R.array.cultivos_crecimiento);
        String[] cultivosTipos = getResources().getStringArray(R.array.cultivos_tipos);

        // 2 - se iteran los arrays con la informacion y se generan nuevas instancias
        for (int i = 0; i < cultivosNombre.length; i++) {
            // se valida que coincida con los parametros del usuario
            if(cultivosTemp[i].toString().equals(tempSelec) && cultivosEst[i].toString().equals(estSelec) && cultivosReg[i].toString().equals(regSelec)){
                // si es correcto se crean las instancias correspondientes
                cultivosFiltrados.add(new CultivosGenerador(
                        cultivosID[i],
                        cultivosNombre[i],
                        cultivosTipos[i],
                        cultivosCrecimiento[i],
                        cultivosInfo[i],
                        cultivosTemp[i],
                        cultivosEst[i],
                        cultivosReg[i],
                        imagenesCultivos[i]));
                // se activa flag
                resultados = true;
            }
        }

        // si ningun resultado coincide con la busqueda
        if(!resultados){
            //mensaje de error
            Toast.makeText(this, "ningun cultivo coincide con los parametros seleccionados, lo sentimos", Toast.LENGTH_SHORT).show();
        }

    }


}