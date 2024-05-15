package com.maid.gardeningfriend.notas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.maid.gardeningfriend.R;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;
    // Constructor que toma las opciones del adaptador y el contexto
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);

        this.context = context;
    }

    // Método llamado para enlazar datos de una nota a una vista en el RecyclerView
    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        // Establecer los valores del título, contenido y fecha en las vistas
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timeTextView.setText(Utility.timestampToString(note.timestamp));

        // Configurar un clic en la vista del elemento para abrir los detalles de la nota
        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, NoteDetailsActivity.class);
            intent.putExtra("title", note.title);
            intent.putExtra("content", note.content);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    // Método llamado para crear una vista de elemento de nota
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar la vista de elemento de nota desde un diseño XML
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    // Clase interna que define una vista de elemento de nota
    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView, contentTextView, timeTextView;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            // Obtener referencias a las vistas de título, contenido y fecha
            titleTextView = itemView.findViewById(R.id.text_view_note_title);
            contentTextView = itemView.findViewById(R.id.text_view_note_content);
            timeTextView = itemView.findViewById(R.id.text_view_note_time);
        }
    }
}
