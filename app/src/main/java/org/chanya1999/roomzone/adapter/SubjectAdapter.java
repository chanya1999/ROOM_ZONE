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

//คลาสสำหรับเชื่อมต่อระว่างข้อมูลกับการแสดงใน recycler view
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {
    private Context mContext;
    private Subject[] mSubjects;

    //อาเรย์เก็บชื่อวันในสัปดาห์
    private String[] daysOfWeek = Subject.longDaysOfWeek;

    //เมธอดเริ่มต้นสำหรับการรับข้อมูลเพื่อนำมาแปลงและจัดระเบียบเพื่อการแสดงผง
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

    //เมธอดสำหรับกำหนดค่าต่าง ๆ สำหนชรับการแสดงข้อมูลของแต่ละรายวิชาในหน้าจอหลัก
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

    //เมธอดสำหรับส่งคืนขนาดของวัตถุที่จะนำไปแสดงผล
    @Override
    public int getItemCount() {
        return mSubjects.length;
    }

    //คลาสสำหรับการเชื่อมต่อกับ recycler view
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

        //เมธอดสำหรับกำหนดตัวแปรเริ่มต้นในการเชื่อมต่อระหว่าง item กับ recycler view
        public MyViewHolder(final Context context, @NonNull View itemView) {
            super(itemView);
            //กำหนดตัวแปรต่าง ๆ ให้อ้างอิงถึงส่วนของการแสดงผล
            this.subjectNameTextView = itemView.findViewById(R.id.subject_name_text_view);
            this.dayTextView = itemView.findViewById(R.id.day_text_view);
            this.startTimeTextView = itemView.findViewById(R.id.start_time_text_view);
            this.endTimeTextView = itemView.findViewById(R.id.end_time_text_view);
            this.roomTextView = itemView.findViewById(R.id.room_text_view);
            this.numberOfStudentTextView = itemView.findViewById(R.id.number_of_student_text_view);
            this.creditTextView = itemView.findViewById(R.id.credit_text_view);

            //กำหนดเหตุการณ์เมื่อ item ถูกคลิ๊ก
            this.rootView = itemView;
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //สร้าง intent ใหม่
                    Intent intent = new Intent(context, MainSubjectActivity.class);
                    //บีบอัดวัตถุและส่งไปยัง intent ใหม่
                    String itemJson = new Gson().toJson(subject);
                    intent.putExtra("subject",itemJson);
                    //เริ่มต้น intent ใหม่
                    context.startActivity(intent);
                }
            });
        }
    }
}
