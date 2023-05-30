package fi.heina.mytasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView tasksRecyclerView;
    private TaskAdapter taskAdapter;
    private List<ToDoModel> taskList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String title = "MY TASKS";
            SpannableString spannableString = new SpannableString(title);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(0.8f), 0, spannableString.length(), 0);
            actionBar.setTitle(spannableString);
        }

        // muutetaan napin iconi mustasta valkoiseksi
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_add_24));
        fab.getDrawable().setTint(ContextCompat.getColor(this, R.color.white));

        databaseHelper = new DatabaseHelper(this);

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();

        try {
            // haetaan tehtävät tietokannasta
            taskList = databaseHelper.getIncompletedTasks();
        } catch (Exception e) {
            // tietokanta on tyhjä
        }

        // luodaan adapteri ja asetetaan se RecyclerView:lle
        taskAdapter = new TaskAdapter(taskList);
        tasksRecyclerView.setAdapter(taskAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // siirrytään NewTaskActivityyn
                startActivity(new Intent(MainActivity.this, NewTaskActivity.class));
            }
        });

        taskAdapter.setOnTaskLongClickListener(new TaskAdapter.OnTaskLongClickListener() {
            @Override
            public void onTaskLongClick(int position) {
                showPopupMenu(position);
            }
        });
    }

    private void showPopupMenu(int position) {
        View anchorView = tasksRecyclerView.findViewHolderForAdapterPosition(position).itemView;
        PopupMenu popupMenu = new PopupMenu(this, anchorView, Gravity.END);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.task_popup_menu, popupMenu.getMenu());

        // lisätään valikkokohtien klikkikuuntelijat
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_complete:
                        completeTask(position);
                        return true;
                    case R.id.menu_delete:
                        deleteTask(position);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }

    private void completeTask(int position) {
        ToDoModel task = taskList.get(position);
        databaseHelper.completeTask(task.getId());
        taskAdapter.notifyItemChanged(position);
        startActivity(new Intent(MainActivity.this,MainActivity.class));
    }

    private void deleteTask(int position) {
        ToDoModel task = taskList.get(position);
        databaseHelper.deleteTask(task.getId());
        taskList.remove(position);
        taskAdapter.notifyItemRemoved(position);
        startActivity(new Intent(MainActivity.this,MainActivity.class));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        // päivitetään tehtävälista
        taskList.clear();
        taskList.addAll(databaseHelper.getIncompletedTasks());
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.home:
                startActivity(new Intent(MainActivity.this,MainActivity.class));
                break;
            case R.id.complete:
                startActivity(new Intent(MainActivity.this, CompletedActivity.class));
                break;
            case R.id.exit:
                finishAffinity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}