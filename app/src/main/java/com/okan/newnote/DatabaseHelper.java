package com.okan.newnote;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Veritabanı sabitleri
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";

    // Tablo oluşturma SQL sorgusu
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NOTES + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_CONTENT + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE); // Tabloyu oluştur
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    // Yeni not ekleme
    public long insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        long id = db.insert(TABLE_NOTES, null, values);
        db.close();
        return id;
    }


    // Tüm notları getirme
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
                );
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    // Not güncelleme
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        return db.update(TABLE_NOTES, values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    // Not silme
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    /**
     * ID'ye göre tek bir not getirir
     * @param id Aranan notun ID'si
     * @ Note nesnesi veya bulunamazsa null
     */
    public Note getNoteById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Sorgu için WHERE koşulu
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        // Sorguyu çalıştır
        Cursor cursor = db.query(
                TABLE_NOTES,    // Tablo adı
                null,           // Tüm sütunları getir
                selection,      // WHERE koşulu
                selectionArgs,  // WHERE argümanları
                null, null, null
        );

        Note note = null;

        // Cursor'da veri varsa işle
        if (cursor != null && cursor.moveToFirst()) {
            // Yeni Note nesnesi oluştur
            note = new Note(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
            );

            cursor.close(); // Cursor'ı kapatmayı unutma!
        }

        db.close();
        return note;
    }
}