package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.maid.gardeningfriend.favoritos.FavoritosSeccion;

/**
 * Contiene la logica / funcion para implementar el menu
 * todas las activities deben extenderse de esta clase
 * @return menu navegacion
 */
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
            startActivity(new Intent(this, Inicio.class));
            return true;

        } else if(item.getItemId() == R.id.enciclopedia) {
            startActivity(new Intent(this, Enciclopedia.class));
            return true;

        } else if(item.getItemId() == R.id.recomendaciones){
            startActivity(new Intent(this, Recomendaciones.class));
            return true;

        } else if (item.getItemId() == R.id.favoritos) {
            startActivity(new Intent(this, FavoritosSeccion.class));
            return true;

        } else if(item.getItemId() == R.id.registro){
            startActivity(new Intent(this, RegistroActivity.class));
            return true;

        } else if(item.getItemId() == R.id.login_logout){
            startActivity(new Intent(this, Login.class));
            return true;

        } else if(item.getItemId() == R.id.perfil){
            startActivity(new Intent(this, Perfil.class));
            return true;

        } else if(item.getItemId() == R.id.panel_admin){
            startActivity(new Intent(this, PanelAdmin.class ));
            return true;

        } else if(item.getItemId() == R.id.contacto){
            startActivity(new Intent(this, ContactoSeccion.class));
            return true;

        } else if(item.getItemId() == R.id.mis_notas){
            startActivity(new Intent(this, NoteActivity.class));
            return true;

        } else if(item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Login.class));
            finish();
            return true;

        } else{
            Toast.makeText(this, "opcion invalida", Toast.LENGTH_SHORT).show();
            return true;
        }

    }
}