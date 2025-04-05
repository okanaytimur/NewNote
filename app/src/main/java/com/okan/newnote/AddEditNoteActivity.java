package com.okan.newnote;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {
    private EditText etTitle, etContent;
    private Button btnSave;
    private DatabaseHelper db;
    private int noteId = -1; // -1 ise yeni not, değilse düzenleme

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        db = new DatabaseHelper(this);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        btnSave = findViewById(R.id.btnSave);

        // Intent'ten not ID'sini al
        noteId = getIntent().getIntExtra("note_id", -1);

        // Düzenleme modunda ise notu yükle
        if(noteId != -1) {
            Note note = db.getNoteById(noteId); // Bu metodu DatabaseHelper'a eklemeniz gerekir
            etTitle.setText(note.getTitle());
            etContent.setText(note.getContent());
        }

        btnSave.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();

        Note note = new Note(noteId, title, content);

        if(noteId == -1) {
            db.insertNote(note);
        } else {
            db.updateNote(note);
        }

        finish(); // Ana ekrana dön
    }
}