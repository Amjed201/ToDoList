package com.example.todolisttest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolisttest.R;

import java.text.DateFormat;
import java.util.Calendar;

public class EditAddNoteActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_ID =
            "com.example.todolisttest.ui.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.todolisttest.ui.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.example.todolisttest.ui.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE =
            "com.example.todolisttest.ui.EXTRA_DATE";

    private EditText editTextTitle;
    private EditText editTextDescription;
    //    private EditText editTextDate;
    private TextView editDate;

    private Button datePickerButton;

    public static String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
//        editTextDate = findViewById(R.id.edit_text_date);
        editDate = findViewById(R.id.editDate);

        datePickerButton = findViewById(R.id.datePicker_button);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });


        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
//            editTextDate.setText(intent.getStringExtra(EXTRA_DATE));
            editDate.setText(intent.getStringExtra(EXTRA_DATE));


        } else {
            setTitle("Add note");
        }
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
//        String date=editTextDate.getText().toString();

        String date = currentDate;

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter a Title / Description ", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_DATE, date);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        editDate.setText(currentDate);

    }
}