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
import org.chanya1999.roomzone.util.DateConverter;
import org.chanya1999.roomzone.util.DateFormatter;

import java.util.Calendar;
import java.util.Date;

public class MainSubjectActivity extends AppCompatActivity {
    private TextView subjectNameTextView;
    private TextView dayTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView roomTextView;
    private TextView numberOfStudentTextView;
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
        subjectNameTextView = findViewById(R.id.m_s_subject_name_text_view);
        dayTextView = findViewById(R.id.m_s_day_text_view);
        startTimeTextView = findViewById(R.id.m_s_start_time_text_view);
        endTimeTextView = findViewById(R.id.m_s_end_time_text_view);
        roomTextView = findViewById(R.id.m_s_room_text_view);
        numberOfStudentTextView = findViewById(R.id.m_s_number_of_student_text_view);
        lastUpdateTextView = findViewById(R.id.last_update_text_view);
        lastUpdateTitleTextView = findViewById(R.id.last_update_title_text_view);
        editOrSaveButton = findViewById(R.id.m_s_edit_or_save_note_button);
        noteEditText = findViewById(R.id.m_s_note_edit_text);

        deleteButton = findViewById(R.id.m_s_delete_button);
        closeButton = findViewById(R.id.m_s_close_button);

        Intent intent = getIntent();
        String subjectJson = intent.getStringExtra("subject");
        final Subject subject = new Gson().fromJson(subjectJson,Subject.class);
        subjectNameTextView.setText(subject.subjectName);
        dayTextView.setText(Subject.longDaysOfWeek[subject.day]);
        startTimeTextView.setText(DateFormatter.formatTimeForUi(subject.startTime));
        endTimeTextView.setText(DateFormatter.formatTimeForUi(subject.endTime));
        roomTextView.setText(subject.room);
        numberOfStudentTextView.setText(subject.numberOfStudent+"");
        noteEditText.setText(subject.note);
        noteEditText.setEnabled(editNoteStage);
        lastUpdateTextView.setText(DateFormatter.formatDateForUi(subject.lastUpdate)+" "+DateFormatter.formatTimeForUi(subject.lastUpdate));
        lastUpdateTitleTextView.setText("LAST UPDATE: ");

        editOrSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNoteStage = !editNoteStage;
                editOrSaveButton.setImageResource(editNoteStage ? R.drawable.ic_baseline_save_24 : R.drawable.ic_baseline_edit_24);
                noteEditText.setEnabled(editNoteStage);
                lastUpdateTextView.setText("");
                lastUpdateTitleTextView.setText("");
                Date tempLastUpdate = subject.lastUpdate;
                noteEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(noteEditText, InputMethodManager.SHOW_IMPLICIT);

                if(!editNoteStage){
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    final Subject newSubject = new Subject(subject.id,subject.subjectName,subject.day,subject.startTime,subject.endTime,subject.room,subject.numberOfStudent,noteEditText.getText().toString(), Calendar.getInstance().getTime());
                    AppExecutors executors = new AppExecutors();
                    executors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase db = AppDatabase.getInstance(MainSubjectActivity.this);
                            db.subjectDao().updateSubject(newSubject);
                        }
                    });
                    lastUpdateTitleTextView.setText("LAST UPDATE: ");
                    if(!subject.note.equals(newSubject.note)) {
                        lastUpdateTextView.setText(DateFormatter.formatDateForUi(newSubject.lastUpdate)+" "+DateFormatter.formatTimeForUi(newSubject.lastUpdate));
                    }
                    else {
                        lastUpdateTextView.setText(DateFormatter.formatDateForUi(tempLastUpdate)+" "+DateFormatter.formatTimeForUi(tempLastUpdate));
                    }

                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainSubjectActivity.this);
                builder.setTitle("WARNING");
                builder.setMessage("Do you want to delete "+subject.subjectName + "?");builder.setNegativeButton("NO", null);
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}