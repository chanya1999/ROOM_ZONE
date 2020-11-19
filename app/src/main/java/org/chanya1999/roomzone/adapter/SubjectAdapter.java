package org.chanya1999.roomzone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.chanya1999.roomzone.MainSubjectActivity;
import org.chanya1999.roomzone.R;
import org.chanya1999.roomzone.model.Subject;
import org.chanya1999.roomzone.util.DateFormatter;


public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {
    private Context mContext;
    private Subject[] mSubjects;

    private String[] daysOfWeek = Subject.longDaysOfWeek;

    public SubjectAdapter(Context context, Subject[] subjects) {
        this.mContext = context;
        this.mSubjects = subjects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject, parent, false);
        return new MyViewHolder(mContext,v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Subject subject = mSubjects[position];
        holder.subjectNameTextView.setText(subject.subjectName);
        holder.dayTextView.setText(daysOfWeek[subject.day]);
        holder.startTimeTextView.setText(DateFormatter.formatTimeForUi(subject.startTime));
        holder.endTimeTextView.setText(DateFormatter.formatTimeForUi(subject.endTime));
        holder.roomTextView.setText(subject.room);
        holder.numberOfStudentTextView.setText(subject.numberOfStudent + "");
        holder.creditTextView.setText(subject.credit + "");
        holder.subject = subject;

    }


    @Override
    public int getItemCount() {
        return mSubjects.length;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView subjectNameTextView;
        TextView dayTextView;
        TextView startTimeTextView;
        TextView endTimeTextView;
        TextView roomTextView;
        TextView numberOfStudentTextView;
        TextView creditTextView;

        View rootView;
        Subject subject;


        public MyViewHolder(final Context context, @NonNull View itemView) {
            super(itemView);

            this.subjectNameTextView = itemView.findViewById(R.id.subject_name_text_view);
            this.dayTextView = itemView.findViewById(R.id.day_text_view);
            this.startTimeTextView = itemView.findViewById(R.id.start_time_text_view);
            this.endTimeTextView = itemView.findViewById(R.id.end_time_text_view);
            this.roomTextView = itemView.findViewById(R.id.room_text_view);
            this.numberOfStudentTextView = itemView.findViewById(R.id.number_of_student_text_view);
            this.creditTextView = itemView.findViewById(R.id.credit_text_view);

            this.rootView = itemView;
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MainSubjectActivity.class);

                    String itemJson = new Gson().toJson(subject);
                    intent.putExtra("subject",itemJson);

                    context.startActivity(intent);
                }
            });
        }
    }
}
