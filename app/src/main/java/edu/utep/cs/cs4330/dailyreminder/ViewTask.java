package edu.utep.cs.cs4330.dailyreminder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import edu.utep.cs.cs4330.dailyreminder.Models.DatabaseHelper;
import edu.utep.cs.cs4330.dailyreminder.Models.Utilities;

public class ViewTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private EditText dueDate;
    private EditText startDate;
    private EditText description;
    private Spinner pr;
    private Boolean deadClick = false;
    private DatabaseHelper db;
    private String taskTitle;
    private String taskId;
    private Button doneButton;
    private ImageButton mic;
    private int gPr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_view_task);
        this.db = new DatabaseHelper(this);

        this.dueDate = (EditText) findViewById(R.id.dueDate);
        this.startDate = (EditText) findViewById(R.id.startDate);
        this.description = (EditText) findViewById(R.id.description);
        this.pr = (Spinner) findViewById(R.id.pr);
        this.doneButton = (Button) findViewById(R.id.thisIsDone);

        TextView theTitle = (TextView) findViewById(R.id.taskName);

        this.mic = (ImageButton) findViewById(R.id.speakNow);
        this.taskTitle = getIntent().getStringExtra("Title");
        String taskDate = getIntent().getStringExtra("end_date");
        String taskStartDate = getIntent().getStringExtra("start_date");
        String taskdescription = getIntent().getStringExtra("description");
        int taskpriority = getIntent().getIntExtra("priority", 0);
        int taskfinished = getIntent().getIntExtra("finished", 0);
        this.gPr = taskfinished;


        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        if(taskfinished == 1) {
            this.doneButton.setEnabled(false);
            this.doneButton.setText("Task is completed");
        }else{
            this.doneButton.setText("I have completed this task");
            this.doneButton.setEnabled(true);
        }


        this.taskId = getIntent().getStringExtra("id");

        theTitle.setText(this.taskTitle);
        this.dueDate.setText(taskDate);
        this.startDate.setText(taskStartDate);
        this.description.setText(taskdescription);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.prioritynums));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        pr.setAdapter(adapter);

        dueDate.setOnClickListener(view -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "Date Picker");
            this.deadClick = true;
        });

        startDate.setOnClickListener(view -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "Date Picker");
        });

        this.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markDone();
            }
        });

    }

    private void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say your description");


        try{
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    this.description.setText(text.get(0));
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.viewmenu, menu);
            return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveInfo:
                editItem();
                return true;
        }
        return false;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        String currentTime = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        if(deadClick) {
            dueDate = (EditText) findViewById(R.id.dueDate);
            dueDate.setText(currentTime);
            this.deadClick = false;
        }else{
            startDate = (EditText) findViewById(R.id.startDate);
            startDate.setText(currentTime);
        }

    }

    // edit(String id, String name, String description, String start_date, String end_date, String priority, int finished)
    private void editItem(){
        if(this.gPr == 0){
            String npr = String.valueOf(Utilities.priorityConverter(pr.getSelectedItem().toString()));
            this.db.edit(this.taskId, this.taskTitle, description.getText().toString(), startDate.getText().toString(), dueDate.getText().toString(), npr, 0);
            Toast.makeText(getBaseContext(), "Saved!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getBaseContext(), "Cannot save once you are done with the task!", Toast.LENGTH_LONG).show();
        }

    }

    private void markDone(){
        String npr = String.valueOf(Utilities.priorityConverter(pr.getSelectedItem().toString()));
        this.db.edit(this.taskId, this.taskTitle, description.getText().toString(), startDate.getText().toString(), dueDate.getText().toString(), npr, 1);
        Toast.makeText(getBaseContext(), "Task Done! Good Job!", Toast.LENGTH_LONG).show();

    }

}
