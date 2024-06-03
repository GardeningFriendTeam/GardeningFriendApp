package com.maid.gardeningfriend.panelAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;

import java.util.ArrayList;
import java.util.Map;

public class PanelAdminEditarRecetas extends MainActivity {
    // atributos
    RecetasGenerador receta;
    EditText nombreEdit;
    EditText categoriaEdit;
    EditText instruccionesEdit;
    EditText cookTimeEdit;
    EditText imageUrlEdit;
    EditText ingrediente1Edit;
    EditText ingrediente2Edit;
    EditText ingrediente3Edit;
    EditText ingrediente4Edit;
    EditText ingrediente5Edit;
    ImageView loaderEdit;
    Button btnEditar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // default instructions
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_admin_editar_recetas);
        // geting intent
        receta = (RecetasGenerador) getIntent().getSerializableExtra("RECETA_SELEC");
        // identifying elements
        nombreEdit = findViewById(R.id.editText_nombre_receta_edit);
        categoriaEdit = findViewById(R.id.editText_categoria_receta_edit);
        instruccionesEdit = findViewById(R.id.editText_instrucciones_receta_edit);
        cookTimeEdit = findViewById(R.id.editText_cookTime_receta_edit);
        imageUrlEdit = findViewById(R.id.editText_url_receta_edit);
        ingrediente1Edit = findViewById(R.id.editText_ingrediente1_receta_edit);
        ingrediente2Edit = findViewById(R.id.editText_ingrediente2_receta_edit);
        ingrediente3Edit = findViewById(R.id.editText_ingrediente3_receta_edit);
        ingrediente4Edit = findViewById(R.id.editText_ingrediente4_receta_edit);
        ingrediente5Edit = findViewById(R.id.editText_ingrediente5_receta_edit);
        loaderEdit = findViewById(R.id.loader_add_receta_edit);
        btnEditar = findViewById(R.id.btn_receta_edit);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReceta();
            }
        });
        // setting its current information to editTexts
        nombreEdit.setText(receta.nombre);
        categoriaEdit.setText(receta.categoria);
        instruccionesEdit.setText(receta.instrucciones);
        cookTimeEdit.setText(receta.cookTime);
        imageUrlEdit.setText(receta.imageUrl);
        ingrediente1Edit.setText(receta.getIngredientes().get(0));
        ingrediente2Edit.setText(receta.getIngredientes().get(1));
        ingrediente3Edit.setText(receta.getIngredientes().get(2));
        ingrediente4Edit.setText(receta.getIngredientes().get(3));
        ingrediente5Edit.setText(receta.getIngredientes().get(4));
    }

    public void updateReceta(){
        // displaying loader
        loaderEdit.setVisibility(View.VISIBLE);

        // ingredientes arraylist
        ArrayList<String> ingredientes = new ArrayList<>();
        ingredientes.add(ingrediente1Edit.getText().toString());
        ingredientes.add(ingrediente2Edit.getText().toString());
        ingredientes.add(ingrediente3Edit.getText().toString());
        ingredientes.add(ingrediente4Edit.getText().toString());
        ingredientes.add(ingrediente5Edit.getText().toString());

        // modified receta objetct
        RecetasGenerador recetaEdit = new RecetasGenerador(
                nombreEdit.getText().toString(),
                categoriaEdit.getText().toString(),
                cookTimeEdit.getText().toString(),
                imageUrlEdit.getText().toString(),
                ingredientes,
                instruccionesEdit.getText().toString(),
                ""
        );
        // document reference
        DocumentReference docRef = db.collection("recetas").document(receta.id);
        Map<String,Object> recetasMap = recetaEdit.toMap();

        // executing request
        docRef.update(recetasMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            // success message
                            Toast.makeText(
                                    PanelAdminEditarRecetas.this,
                                    "la receta ha sido editada correctamente!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // error message
                            Toast.makeText(
                                    PanelAdminEditarRecetas.this,
                                    "la operacion no se pudo completar",
                                    Toast.LENGTH_SHORT).show();
                            // console log
                            Log.e("EDIT_RECETA", "OPERATION FAILED", task.getException());
                        }
                    }
                });

        // hiding loader
        loaderEdit.setVisibility(View.GONE);
    }
}