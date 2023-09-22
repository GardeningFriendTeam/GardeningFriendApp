package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // inicializa el menu en la actividad
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // valida que seccion fue seleccionada
        if(item.getItemId() == R.id.inicio){
            Toast.makeText(this, "inicio", Toast.LENGTH_SHORT).show();
            return true;

        } else if(item.getItemId() == R.id.enciclopedia) {
            Toast.makeText(this, "enciclopedia", Toast.LENGTH_SHORT).show();
            return true;

        } else if(item.getItemId() == R.id.recomendaciones){
            Toast.makeText(this, "recomendaciones", Toast.LENGTH_SHORT).show();
            return true;

        } else if(item.getItemId() == R.id.registro){
            Toast.makeText(this, "registro", Toast.LENGTH_SHORT).show();
            return true;

        } else if(item.getItemId() == R.id.login_logout){
            Toast.makeText(this, "login / logout", Toast.LENGTH_SHORT).show();
            return true;

        } else if(item.getItemId() == R.id.perfil){
            Toast.makeText(this, "login / logout", Toast.LENGTH_SHORT).show();
            return true;

        } else if(item.getItemId() == R.id.panel_admin){
            Toast.makeText(this, "login / logout", Toast.LENGTH_SHORT).show();
            return true;

        } else{
            Toast.makeText(this, "opcion invalida", Toast.LENGTH_SHORT).show();
            return true;
        }

    }
}