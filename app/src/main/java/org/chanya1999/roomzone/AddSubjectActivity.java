package org.chanya1999.roomzone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.chanya1999.roomzone.database.AppDatabase;
import org.chanya1999.roomzone.model.Subject;
import org.chanya1999.roomzone.util.AppExecutors;
import org.chanya1999.roomzone.util.DateFormatter;

import java.util.Calendar;
import java.util.Date;

//คลาสเพิ่มข้อมูล
public class AddSubjectActivity extends AppCompatActivity {

    private Button saveButton;
    private Button clearButton;
    private EditText subjectNameEditText;
    private TextView selectDayEditText;
    private TextView selectStartTimeEditText;
    private TextView selectEndTimeEditText;
    private EditText roomEditText;
    private EditText numberOfStudentEditText;
    private EditText creditEditText;
    private EditText noteEditText;

    private int mSelectDayEditText = 0;
    private Calendar mSelectStartTimeEditText = Calendar.getInstance();
    private Calendar mSelectEndTimeEditText = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //กำหนดตัวแปรต่าง ๆ สำหรับเชื่อมต่อกับการแสดงผล
        saveButton = findViewById(R.id.save_button);
        clearButton = findViewById(R.id.clear_button);

        subjectNameEditText = findViewById(R.id.subject_name_edit_text);
        selectDayEditText = findViewById(R.id.select_day_edit_text);
        selectStartTimeEditText = findViewById(R.id.select_start_time_edit_text);
        selectEndTimeEditText = findViewById(R.id.select_end_time_edit_text);
        roomEditText = findViewById(R.id.room_edit_text);
        numberOfStudentEditText = findViewById(R.id.number_of_student_edit_text);
        creditEditText = findViewById(R.id.credit_edit_text);
        noteEditText = findViewById(R.id.note_edit_text);

        //กำหนดเหตุการณ์เมื่อกดช่องใส่วันในสัปดาห์
        selectDayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //สร้าง picker สำหรับเลือกวันในสัปดาห์
                final NumberPicker picker = new NumberPicker(AddSubjectActivity.this);
                final String[] daysOfWeek = Subject.longDaysOfWeek;
                picker.setMinValue(0);
                picker.setMaxValue(6);
                picker.setDisplayedValues(daysOfWeek);
                picker.setWrapSelectorWheel(false);
                picker.setValue(mSelectDayEditText);

                FrameLayout layout = new FrameLayout(AddSubjectActivity.this);
                layout.addView(picker, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER));

                AlertDialog alertDialog = new AlertDialog.Builder(AddSubjectActivity.this)
                        .setView(layout)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mSelectDayEditText = picker.getValue();
                                selectDayEditText.setText(daysOfWeek[mSelectDayEditText]);

                            }
                        })
                        .setNegativeButton("CANCEL",null)
                        .show();

                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
            }
        });

        //กำหนดเหตุการณ์เมื่อกดช่องใส่เวลาเริ่มต้น
        selectStartTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //สร้าง picker สำหรับเลือกเวลาเริ่มต้น
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddSubjectActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                mSelectStartTimeEditText.set(Calendar.HOUR_OF_DAY, hour);
                                mSelectStartTimeEditText.set(Calendar.MINUTE, minute);
                                String formatDate = DateFormatter.formatTimeForUi(mSelectStartTimeEditText.getTime());
                                selectStartTimeEditText.setText(formatDate);
                            }
                        },
                        mSelectStartTimeEditText.get(Calendar.HOUR_OF_DAY),
                        mSelectStartTimeEditText.get(Calendar.MINUTE),
                        true
                );
                timePickerDialog.show();
            }
        });

        //กำหนดเหตุการณ์เมื่อกดช่องใส่เวลาเริ่มต้น
        selectEndTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //สร้าง picker สำหรับเลือกเวลาเลิกเรียน
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddSubjectActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                mSelectEndTimeEditText.set(Calendar.HOUR_OF_DAY, hour);
                                mSelectEndTimeEditText.set(Calendar.MINUTE, minute);
                                String formatDate = DateFormatter.formatTimeForUi(mSelectEndTimeEditText.getTime());
                                selectEndTimeEditText.setText(formatDate);
                            }
                        },
                        mSelectEndTimeEditText.get(Calendar.HOUR_OF_DAY),
                        mSelectEndTimeEditText.get(Calendar.MINUTE),
                        true
                );
                timePickerDialog.show();
            }
        });

        //กำหนดเหตุการณ์เมื่อกดปุ่มบันทึก
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String subjectName = subjectNameEditText.getText().toString();
                final int day = mSelectDayEditText;
                final Date startTime = mSelectStartTimeEditText.getTime();
                final Date endTime = mSelectEndTimeEditText.getTime();
                final String room = roomEditText.getText().toString();
                final String note = noteEditText.getText().toString();
                final int numberOfStudent;
                final int credit;

                //ตรวจสอบการป้อนข้อมูลชื่อวิชา หากเว้นว่างให้แสดง toast แจ้งเตือน
                if(subjectName.length()==0){
                    Toast.makeText(AddSubjectActivity.this, "Subject name is null",Toast.LENGTH_SHORT).show();
                }

                //ตรวจสอบการป้อนข้อมูลวันในสัปดาห์ หากเว้นว่างให้แสดง toast แจ้งเตือน
                else if(selectDayEditText.getText().toString().length()==0){
                    Toast.makeText(AddSubjectActivity.this, "Day is null",Toast.LENGTH_SHORT).show();
                }

                //ตรวจสอบการป้อนข้อมูลเวลาเริ่มต้น หากเว้นว่างให้แสดง toast แจ้งเตือน
                else if(selectStartTimeEditText.getText().toString().length()==0){
                    Toast.makeText(AddSubjectActivity.this, "Start time is null",Toast.LENGTH_SHORT).show();
                }

                //ตรวจสอบการป้อนข้อมูลเวลาเลิกเรียน หากเว้นว่างให้แสดง toast แจ้งเตือน
                else if(selectEndTimeEditText.getText().toString().length()==0){
                    Toast.makeText(AddSubjectActivity.this, "End time is null",Toast.LENGTH_SHORT).show();
                }

                //ตรวจสอบการป้อนข้อมูลสถานที่เรียน หากเว้นว่างให้แสดง toast แจ้งเตือน
                else if(roomEditText.getText().toString().length()==0){
                    Toast.makeText(AddSubjectActivity.this, "Room is null",Toast.LENGTH_SHORT).show();
                }

                //ตรวจสอบการป้อนข้อมูลจำนวนผู้เรียน หากเว้นว่างให้แสดง toast แจ้งเตือน
                else if(numberOfStudentEditText.getText().toString().length()==0){
                    Toast.makeText(AddSubjectActivity.this, "Number of student is null",Toast.LENGTH_SHORT).show();
                }

                //ตรวจสอบการป้อนข้อมูลจำนวนหน่วยกิต หากเว้นว่างให้แสดง toast แจ้งเตือน
                else if(creditEditText.getText().toString().length()==0){
                    Toast.makeText(AddSubjectActivity.this, "Credit is null",Toast.LENGTH_SHORT).show();
                }

                //กรณีผ่านทุกเงื่อนไขตรวจสอบ
                else
                {
                    numberOfStudent = Integer.parseInt(numberOfStudentEditText.getText().toString());
                    credit = Integer.parseInt(creditEditText.getText().toString());

                    //สร้างวัตถุรายวิชาที่ต้องการจัดเก็บโดยกำหนดพารามิเตอร์ตามที่ผู้ใช้กรอก
                    final Subject subject = new Subject(0,subjectName,day,startTime,endTime,room,numberOfStudent, credit, note, Calendar.getInstance().getTime());

                    //แยกเธรดในการเชื่อมต่อฐานข้อมูล เพื่อเพิ่มข้อมูลลงฐานข้อมูล
                    AppExecutors executors = new AppExecutors();
                    executors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase db = AppDatabase.getInstance(AddSubjectActivity.this);
                            db.subjectDao().addSubject(subject);
                            Log.i("Add User: ",subjectName+"\n"+day+" start:"+mSelectStartTimeEditText+"-"+mSelectEndTimeEditText+"\n room:"+room+"\n numOfStu:"+numberOfStudent);
                            finish();
                        }
                    });
                    Toast.makeText(AddSubjectActivity.this, "ADD SUBJECT "+ subjectName.toUpperCase() + " ALREADY",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //กำหนดเหตุการณ์เมื่อกดปุ่มล้าง ให้ช่องป้อนข้อมูลทุกช่องเว้นว่าง
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectNameEditText.setText("");
                selectDayEditText.setText("");
                selectStartTimeEditText.setText("");
                selectEndTimeEditText.setText("");
                roomEditText.setText("");
                numberOfStudentEditText.setText("");
                noteEditText.setText("");
            }
        });
    }
}