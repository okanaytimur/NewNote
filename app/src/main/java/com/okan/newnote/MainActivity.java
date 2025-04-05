package com.okan.newnote;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Floating Action Button
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddEditNoteActivity.class));
        });

        loadNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes(); // Sayfa yenilendiğinde notları tekrar yükle
    }

    // Notları veritabanından yükle
    private void loadNotes() {
        List<Note> notes = db.getAllNotes();
        adapter = new NotesAdapter(notes, this);
        recyclerView.setAdapter(adapter);
    }
}