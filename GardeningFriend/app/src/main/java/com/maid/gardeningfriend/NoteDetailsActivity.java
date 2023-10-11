package com.maid.gardeningfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView, deleteTextViewBtn;
    String title, content, docId;
    boolean isEditMode = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        // Obtener referencias a elementos de la interfaz de usuario
        titleEditText = findViewById(R.id.note_title_text);
        contentEditText = findViewById(R.id.note_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteTextViewBtn = findViewById(R.id.delete_note_text_view);

        // Recibir los datos enviados desde la actividad anterior
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        // Configurar los campos de título y contenido con los datos recibidos
        titleEditText.setText(title);
        contentEditText.setText(content);

        // Si estamos en modo de edición, actualizar el título y mostrar el botón de eliminación
        if (isEditMode){
            pageTitleTextView.setText("Editar nota");
            deleteTextViewBtn.setVisibility(View.VISIBLE);
        }

        // Configurar un clic en el botón de guardar para guardar la nota
        saveNoteBtn.setOnClickListener( (v)-> saveNote());

        // Configurar un clic en el botón de eliminación para eliminar la nota de Firebase
        deleteTextViewBtn.setOnClickListener((v -> deleteNoteFromFirebase()));
    }

    // Método para guardar una nueva nota o actualizar una existente
    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if (noteTitle==null || noteTitle.isEmpty()){
            titleEditText.setError("Ingrese un titulo");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        // Guardar la nota en Firebase
        saveNoteToFirebase(note);
    }

    // Método para guardar la nota en Firebase Firestore
    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if (isEditMode) {
            // Modificar la nota existente
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else {
            // Crear una nueva nota
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // Nota guardada exitosamente
                    Utility.showToast(NoteDetailsActivity.this, "Nota Agregada exitosamente");
                    finish(); // Cerrar la actividad actual
                }else {
                    Utility.showToast(NoteDetailsActivity.this, "Fallo al agregar la nota");
                }
            }
        });
    }

    // Método para eliminar la nota de Firebase Firestore
    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // Nota eliminada exitosamente
                    Utility.showToast(NoteDetailsActivity.this, "Nota eliminada exitosamente");
                    finish(); // Cerrar la actividad actual
                }else {
                    Utility.showToast(NoteDetailsActivity.this, "Fallo al eliminar la nota");
                }
            }
        });
    }
}