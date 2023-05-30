package fi.heina.mytasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompletedActivity extends AppCompatActivity {

    private RecyclerView completedTasksRecyclerView;
    private TaskAdapter taskAdapter;
    private List<ToDoModel> taskList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String title = "completed tasks";
            SpannableString spannableString = new SpannableString(title);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(0.8f), 0, spannableString.length(), 0);
            actionBar.setTitle(spannableString);
        }

        databaseHelper = new DatabaseHelper(this);

        completedTasksRecyclerView = findViewById(R.id.completedTasksRecyclerView);
        completedTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();

        try {
            // haetaan tehtävät tietokannasta, jotka ovat merkitty valmiiksi
            taskList = databaseHelper.getCompletedTasks();
        } catch (Exception e) {
            // tietokanta on tyhjä
        }

        // luodaan adapteri ja asetetaan se RecyclerView:lle
        taskAdapter = new TaskAdapter(taskList);
        completedTasksRecyclerView.setAdapter(taskAdapter);

        taskAdapter.setOnTaskLongClickListener(new TaskAdapter.OnTaskLongClickListener() {
            @Override
            public void onTaskLongClick(int position) {
                // näytetään valikkonäkymä
                showPopupMenu(position);
            }
        });

    }

    private void showPopupMenu(int position) {
        View anchorView = Objects.requireNonNull(completedTasksRecyclerView.findViewHolderForAdapterPosition(position)).itemView;
        PopupMenu popupMenu = new PopupMenu(this, anchorView, Gravity.END);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.task_popup_menu, popupMenu.getMenu());

        // piilotetaan "Complete" -vaihtoehto
        popupMenu.getMenu().findItem(R.id.menu_complete).setVisible(false);

        // lisätään valikkokohtien klikkikuuntelijat
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_complete:

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

    private void deleteTask(int position) {
        ToDoModel task = taskList.get(position);
        databaseHelper.deleteTask(task.getId());
        taskList.remove(position);
        taskAdapter.notifyItemRemoved(position);
        startActivity(new Intent(CompletedActivity.this,CompletedActivity.class));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        // päivitetään tehtävälista
        taskList.clear();
        taskList.addAll(databaseHelper.getCompletedTasks());
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
                startActivity(new Intent(CompletedActivity.this,MainActivity.class));
                break;
            case R.id.complete:
                startActivity(new Intent(CompletedActivity.this, CompletedActivity.class));
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