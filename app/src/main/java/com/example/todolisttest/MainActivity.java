package com.example.todolisttest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST_CODE = 1;
    public static final int EDIT_NOTE_REQUEST_CODE = 2;


    NotesViewModel notesViewModel;
    RecyclerView recyclerView;
    NotesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton buttonAddNote = findViewById(R.id.add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditAddNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST_CODE);


            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new NotesAdapter();
        recyclerView.setAdapter(adapter);


        notesViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(NotesViewModel.class);
        notesViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                notesViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, EditAddNoteActivity.class);
                intent.putExtra(EditAddNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(EditAddNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(EditAddNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(EditAddNoteActivity.EXTRA_DATE, note.getDate());
                startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(EditAddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(EditAddNoteActivity.EXTRA_DESCRIPTION);
            String date = data.getStringExtra(EditAddNoteActivity.EXTRA_DATE);

            Note note = new Note(title, description, date);
            notesViewModel.insert(note);

            Toast.makeText(this, "Note Saved ", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {

            int id = data.getIntExtra(EditAddNoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(EditAddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(EditAddNoteActivity.EXTRA_DESCRIPTION);
            String date = data.getStringExtra(EditAddNoteActivity.EXTRA_DATE);

            Note note = new Note(title, description, date);
            note.setId(id);
            notesViewModel.update(note);
            Log.d("update note", "onActivityResult: " + note.getTitle());
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                notesViewModel.deleteAllNotes();
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }

    }
}