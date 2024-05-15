package com.maid.gardeningfriend.notas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;

public class NoteActivity extends MainActivity {
    FloatingActionButton addNoteBtn;
    RecyclerView recyclerViewNotes;
    NoteAdapter noteAdapter;
    FirebaseAuth mAuth;
    TextView textViewCommonUsers;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mAuth = FirebaseAuth.getInstance();

        // Obtener referencias a elementos de la interfaz de usuario
        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerViewNotes = findViewById(R.id.recycler_notas);
        textViewCommonUsers = findViewById(R.id.textViewCommonUser);


        // Configurar un clic en el botón para iniciar la actividad de detalles de nota
        addNoteBtn.setOnClickListener((v) -> {
            if (mAuth.getCurrentUser() != null) {
                if (mAuth.getCurrentUser().isEmailVerified()) {
                    startActivity(new Intent(NoteActivity.this, NoteDetailsActivity.class));
                } else {
                    Toast.makeText(NoteActivity.this, "Por favor valida tu email para acceder a esta función", Toast.LENGTH_SHORT).show();
                }
            } else {
                // El usuario no está autenticado, puedes mostrar un mensaje o redirigirlo a la página de inicio de sesión.
                Toast.makeText(NoteActivity.this, "Inicia sesión o regístrate para acceder a esta función", Toast.LENGTH_SHORT).show();
            }
        });
        // Configurar y mostrar la lista de notas en un RecyclerView
        setupRecyclerView();


        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();

                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    String noteId = noteAdapter.getSnapshots().getSnapshot(position).getId();

                    // Guarda una copia de la nota que se eliminará
                    Note deletedNote = noteAdapter.getSnapshots().get(position);

                    // Elimina la nota de la base de datos
                    Utility.getCollectionReferenceForNotes().document(noteId).delete();

                    // Muestra un mensaje Snackbar con opción de deshacer
                    Snackbar snackbar = Snackbar.make(recyclerViewNotes, "Nota eliminada", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Deshacer", v -> {
                        // Reinserta la nota en la base de datos (puedes utilizar el mismo ID)
                        Utility.getCollectionReferenceForNotes().document(noteId).set(deletedNote);
                    });
                    snackbar.show();
                }
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);


    }

    // Configura el RecyclerView para mostrar las notas
    void setupRecyclerView() {
        if (mAuth.getCurrentUser() != null) {
            // El usuario está autenticado, muestra el RecyclerView
            recyclerViewNotes.setVisibility(View.VISIBLE);
            textViewCommonUsers.setVisibility(View.GONE);

            // Obtener una consulta de Firestore para las notas ordenadas por fecha descendente
            Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
            // Configurar las opciones del adaptador para Firebase Firestore
            FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                    .setQuery(query, Note.class).build();
            // Configurar el administrador de diseño y el adaptador del RecyclerView
            recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
            noteAdapter = new NoteAdapter(options, this);
            recyclerViewNotes.setAdapter(noteAdapter);
        } else {
            // El usuario no está autenticado, muestra solo el mensaje para usuarios no registrados.
            recyclerViewNotes.setVisibility(View.GONE);
            textViewCommonUsers.setVisibility(View.VISIBLE);
        }
    }

    // Inicia la escucha de cambios en el adaptador cuando la actividad se reanuda
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            noteAdapter.startListening();
            recyclerViewNotes.setVisibility(View.VISIBLE);
        }else {
            textViewCommonUsers.setVisibility(View.VISIBLE);
            recyclerViewNotes.setVisibility(View.GONE);
        }
    }

    // Detiene la escucha de cambios en el adaptador cuando la actividad se detiene
    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }

    // Notifica al adaptador de cualquier cambio cuando la actividad se reanuda
    @Override
    protected void onResume() {
        super.onResume();
        if (noteAdapter != null) {
            noteAdapter.notifyDataSetChanged();
        }
    }

}