package org.chanya1999.roomzone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import org.chanya1999.roomzone.database.AppDatabase;
import org.chanya1999.roomzone.model.Subject;
import org.chanya1999.roomzone.util.AppExecutors;
import org.chanya1999.roomzone.util.DateFormatter;

import java.util.Calendar;
import java.util.Date;

//คลาสแสดงหน้ารายละเอียดรายวิชา
public class MainSubjectActivity extends AppCompatActivity {
    private TextView subjectNameTextView;
    private TextView dayTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView roomTextView;
    private TextView numberOfStudentTextView;
    private TextView creditTextView;
    private EditText noteEditText;
    private TextView lastUpdateTextView;
    private TextView lastUpdateTitleTextView;
    private ImageButton editOrSaveButton;
    private boolean editNoteStage;

    private Button deleteButton;
    private Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_subject);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //กำหนดค่าเริ่มต้นให้กับตัวแปรต่าง ๆ เพื่อเชื่อมต่อกับส่วนของการแสดงผล
        subjectNameTextView = findViewById(R.id.m_s_subject_name_text_view);
        dayTextView = findViewById(R.id.m_s_day_text_view);
        startTimeTextView = findViewById(R.id.m_s_start_time_text_view);
        endTimeTextView = findViewById(R.id.m_s_end_time_text_view);
        roomTextView = findViewById(R.id.m_s_room_text_view);
        numberOfStudentTextView = findViewById(R.id.m_s_number_of_student_text_view);
        creditTextView = findViewById(R.id.m_s_credit_text_view);
        lastUpdateTextView = findViewById(R.id.last_update_text_view);
        lastUpdateTitleTextView = findViewById(R.id.last_update_title_text_view);
        editOrSaveButton = findViewById(R.id.m_s_edit_or_save_note_button);
        noteEditText = findViewById(R.id.m_s_note_edit_text);

        deleteButton = findViewById(R.id.m_s_delete_button);
        closeButton = findViewById(R.id.m_s_close_button);

        //รับค่าที่ถูกบีบอัดมาพร้อมกับการเรียกการแสดงผล
        Intent intent = getIntent();
        String subjectJson = intent.getStringExtra("subject");
        final Subject subject = new Gson().fromJson(subjectJson,Subject.class);
        //กำหนดการแสดงผลต่าง ๆ ให้เป็นค่าที่รับมา
        subjectNameTextView.setText(subject.subjectName);
        dayTextView.setText(Subject.longDaysOfWeek[subject.day]);
        startTimeTextView.setText(DateFormatter.formatTimeForUi(subject.startTime));
        endTimeTextView.setText(DateFormatter.formatTimeForUi(subject.endTime));
        roomTextView.setText(subject.room);
        numberOfStudentTextView.setText(subject.numberOfStudent+"");
        creditTextView.setText(subject.credit+"");
        noteEditText.setText(subject.note);
        noteEditText.setEnabled(editNoteStage);
        lastUpdateTextView.setText(DateFormatter.formatDateForUi(subject.lastUpdate)+" "+DateFormatter.formatTimeForUi(subject.lastUpdate));
        lastUpdateTitleTextView.setText("LAST UPDATE: ");

        //กำหนดเหตการณ์เมื่อกดปุ่มแก้ไข หรือบันทึกข้อความ
        editOrSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //เมื่อกดปุ่มจะสลับไปมาระหว่างแก้ไข-บันทึก
                editNoteStage = !editNoteStage;
                //ปรับการแสดงตามสถานะปุ่มแก้ไข-บันทึก
                editOrSaveButton.setImageResource(editNoteStage ? R.drawable.ic_baseline_save_24 : R.drawable.ic_baseline_edit_24);
                //กำหนดความสามรถในการแก้ไขตามสถานะ
                noteEditText.setEnabled(editNoteStage);
                //ซ่อนการแสดงวันที่ปรับปรุงล่าสุด
                lastUpdateTextView.setText("");
                lastUpdateTitleTextView.setText("");
                Date tempLastUpdate = subject.lastUpdate;
                //กำหนดให้แสดงแป้นพิมพ์และเคอเซอร์เมื่อมีการกดปุ่มแก้ไข
                noteEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(noteEditText, InputMethodManager.SHOW_IMPLICIT);

                //กรณีที่กดปุ่มเมื่ออยู่ในสถานะบันทึก(ไม่ใช่สถานะแก้ไข)
                if(!editNoteStage){
                    //ซ่อนแป้นพิมพ์และเคอเซอร์
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    //สร้างวัตถุใหม่เพื่อส่งไปปรับปรุงในฐานข้อมูล
                    final Subject newSubject = new Subject(subject.id,subject.subjectName,subject.day,subject.startTime,subject.endTime,subject.room,subject.numberOfStudent, subject.credit, noteEditText.getText().toString(), Calendar.getInstance().getTime());
                    //แยกเธรดในการปรับปรุงข้อมูล
                    AppExecutors executors = new AppExecutors();
                    executors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase db = AppDatabase.getInstance(MainSubjectActivity.this);
                            db.subjectDao().updateSubject(newSubject);
                        }
                    });
                    //แสดงข้อมูลวันที่ปรับปรุงข้อมูลล่าสุด
                    lastUpdateTitleTextView.setText("LAST UPDATE: ");
                    //ตรวจสอบว่าข้อมูลมีความเปลี่ยนแปลงหรือไม่ ถ้ามีจะปรับปรุงวันที่แก้ไข
                    if(!subject.note.equals(newSubject.note)) {
                        lastUpdateTextView.setText(DateFormatter.formatDateForUi(newSubject.lastUpdate)+" "+DateFormatter.formatTimeForUi(newSubject.lastUpdate));
                    }
                    else {
                        lastUpdateTextView.setText(DateFormatter.formatDateForUi(tempLastUpdate)+" "+DateFormatter.formatTimeForUi(tempLastUpdate));
                    }

                }
            }
        });
        //กำหนดเหตุการณ์เมื่อกดปุ่มลบ
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //สร้าง alert dialog เพื่อยืนยันการลบข้อมูล
                AlertDialog.Builder builder = new AlertDialog.Builder(MainSubjectActivity.this);
                builder.setTitle("WARNING");
                builder.setMessage("Do you want to delete "+subject.subjectName.toUpperCase() + "?");builder.setNegativeButton("NO", null);
                //กำหนดเหต์การณ์เมื่อกดยืนยันที่จะลบ
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //แยกเธรดในการลบข้อมูลในฐานข้อมูล
                        AppExecutors executors = new AppExecutors();
                        executors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase db = AppDatabase.getInstance(MainSubjectActivity.this);
                                db.subjectDao().deleteSubject(subject);
                            }
                        });
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
        //กำหนดเหตุการณ์เมื่อกดปิด
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}