package fi.heina.mytasks;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<ToDoModel> taskList;
    public TaskAdapter(List<ToDoModel> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ToDoModel task = taskList.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());
        holder.deadlineTextView.setText(task.getDeadline());
        holder.completedTextView.setText(task.getCompleted());

        String imageIconString = task.getNoteicon();
        if (imageIconString.equals("noteundone")) {
            holder.noteIcon.setImageResource(R.drawable.noteundone);
            String currentDay = getCurrentDate();
            if (task.getDeadline().compareTo(currentDay) < 0) {
                holder.deadlineTitle.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.myRed));
                holder.deadlineDay.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.myRed));
            } else {
                holder.deadlineTitle.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
                holder.deadlineDay.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
            }
        } else if (imageIconString.equals("notedone")) {
            holder.noteIcon.setImageResource(R.drawable.notedone);
        }

        // lisätään valikkonäkymän toiminnallisuus pitkälle painallukselle
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onTaskLongClickListener != null) {
                    onTaskLongClickListener.onTaskLongClick(position);
                    return true;
                }
                return false;
            }
        });
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView deadlineTextView;
        TextView completedTextView;
        ImageView noteIcon;
        TextView deadlineTitle;
        TextView deadlineDay;


        public TaskViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titletext);
            descriptionTextView = itemView.findViewById(R.id.descriptiontext);
            deadlineTextView = itemView.findViewById(R.id.deadlineDay);
            completedTextView = itemView.findViewById(R.id.completedDay);
            noteIcon = itemView.findViewById(R.id.imageicon);
            deadlineTitle = itemView.findViewById(R.id.deadlineTitle);
            deadlineDay = itemView.findViewById(R.id.deadlineDay);

            // lisätään pitkän painalluksen kuuntelija
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onTaskLongClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onTaskLongClickListener.onTaskLongClick(position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    public interface OnTaskLongClickListener {
        void onTaskLongClick(int position);
    }

    private static OnTaskLongClickListener onTaskLongClickListener;

    public void setOnTaskLongClickListener(OnTaskLongClickListener listener) {
        onTaskLongClickListener = listener;
    }
}
