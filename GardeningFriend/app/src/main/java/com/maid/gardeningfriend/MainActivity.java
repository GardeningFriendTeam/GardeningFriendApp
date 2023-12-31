package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maid.gardeningfriend.favoritos.FavoritosSeccion;

/**
 * Contiene la logica / funcion para implementar el menu
 * todas las activities deben extenderse de esta clase
 * @return menu navegacion
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Verificar si el usuario ha iniciado sesión
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // El usuario ha iniciado sesión, ocultar elementos de registro y login
            menu.findItem(R.id.registro).setVisible(false);
            menu.findItem(R.id.login_logout).setVisible(false);

            // Obtén una referencia a la colección de usuarios en Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = auth.getCurrentUser().getUid(); // ID del usuario actual

            // Obtén el documento del usuario actual
            DocumentReference userRef = db.collection("usuarios").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Documento del usuario encontrado
                        Boolean isAdmin = document.getBoolean("isAdmin");

                        // Dependiendo si el usuario es un administrador, muestra u oculta el elemento "Panel Admin"
                        menu.findItem(R.id.panel_admin).setVisible(isAdmin != null && isAdmin);
                    }
                } else {
                    // Manejo de errores
                    Log.d(TAG, "Error obteniendo documento:", task.getException());
                }
            });

        } else {
            // El usuario no ha iniciado sesión, ocultar elementos de perfil y panel admin
            menu.findItem(R.id.perfil).setVisible(false);
            menu.findItem(R.id.panel_admin).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        }

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