package org.chanya1999.roomzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.chanya1999.roomzone.adapter.SubjectAdapter;
import org.chanya1999.roomzone.database.AppDatabase;
import org.chanya1999.roomzone.model.Subject;
import org.chanya1999.roomzone.util.AppExecutors;

//คลาสหน้าจอแสดงผลหลัก
public class MainActivity extends AppCompatActivity {


    private Button addClassroomButton;
    private RecyclerView mRecyclerView;
    private TextView isEmptyTextView;


    //เมธอดกำหนดการทำงานเมื่อระบบใช้งานหน้าจอหลัก
    @Override
    protected void onResume() {
        super.onResume();

        //กำหนดการแสดงผลบนหน้าจอหลักให้มีการปรับปรุงให้ตรงกับฐานข้อมูลในปัจจุบันทุกครั้งเมื่อมีการเข้าถึง
        final AppExecutors executors = new AppExecutors();
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(MainActivity.this);
                final Subject[] subjects = db.subjectDao().getAllSubjects();

                //แบ่งเธรดในการเชื่อมต่อฐานข้อมูล และการแสดงผลสถานะว่าง/มีข้อมูล
                executors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        SubjectAdapter adapter = new SubjectAdapter(MainActivity.this, subjects);
                        mRecyclerView.setAdapter(adapter);
                        isEmptyTextView.setText("Empty Subject");
                        isEmptyTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_folder_open_24,0,0,0);

                        //ตรวจสอบหากมีข้อมูลไม่ต้องแสดงผลสถานะว่าง
                        if(subjects.length>0){
                            isEmptyTextView.setText("");
                            isEmptyTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //กำหนดค่าเริ่มต้นของตัวแปรต่าง ๆ เพื่อเชื่อมต่อกับส่วนแสดงผล
        isEmptyTextView = findViewById(R.id.is_empty_text_view);
        addClassroomButton = findViewById(R.id.add_classroom_button);
        mRecyclerView = findViewById(R.id.subject_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        addClassroomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddSubjectActivity.class);
                startActivity(intent);
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}