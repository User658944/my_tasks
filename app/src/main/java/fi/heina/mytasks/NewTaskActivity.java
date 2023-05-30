package fi.heina.mytasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewTaskActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText deadlineInput;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        // piilotetaan ylävalikko (action bar)
        getSupportActionBar().hide();

        databaseHelper = new DatabaseHelper(this);
        deadlineInput = findViewById(R.id.newTaskDeadline);
        calendar = Calendar.getInstance();

        Button selectDateButton = findViewById(R.id.selectDate);
        selectDateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                showDatePickerDialog();
                selectDateButton.setText("");
            }
        });

        Button cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewTaskActivity.this,MainActivity.class));
            }
        });

        Button addTaskButton = findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // haetaan tekstikenttien tiedot
                EditText titleInput = findViewById(R.id.newTaskTitle);
                EditText descriptionInput = findViewById(R.id.newTaskDescription);
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                String deadlineString = deadlineInput.getText().toString();

                // tarkistetaan etteivät tekstikentät ole tyhjiä
                if (title.isEmpty() || description.isEmpty() || deadlineString.isEmpty()) {
                    Snackbar.make(view, "Fill all fields", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // luodaan uusi ToDoModel-instanssi
                ToDoModel task = new ToDoModel();
                task.setTitle(title);
                task.setDescription(description);
                task.setDeadline(deadlineString);
                task.setNoteicon("noteundone");

                // viedään tehtävä tietokantaan
                databaseHelper.insertTask(task);

                // palataan MainActivityyn
                startActivity(new Intent(NewTaskActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void showDatePickerDialog() {
        // asetetaan kalenteri nykyiseen päivämäärään
        calendar = Calendar.getInstance();

        // luodaan DatePickerDialog kuuntelijalla, joka asettaa valitun päivämäärän EditText-kenttään
        DatePickerDialog datePickerDialog = new DatePickerDialog(NewTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String selectedDate = dateFormat.format(calendar.getTime());

                deadlineInput.setText(selectedDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}