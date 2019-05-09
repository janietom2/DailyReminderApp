package edu.utep.cs.cs4330.dailyreminder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;

import edu.utep.cs.cs4330.dailyreminder.Models.DatabaseHelper;

public class ViewTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView dueDate;
    private TextView startDate;
    private EditText description;
    private Spinner pr;
    private Boolean deadClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_view_task);
        DatabaseHelper db = new DatabaseHelper(this);

        dueDate = (TextView) findViewById(R.id.dueDate);
        startDate = (TextView) findViewById(R.id.startDate);
        description = (EditText) findViewById(R.id.description);
        pr = (Spinner) findViewById(R.id.pr);

        String taskTitle = getIntent().getStringExtra("Title");
        String taskDate = getIntent().getStringExtra("end_date");
        String taskStartDate = getIntent().getStringExtra("start_date");
        String taskdescription = getIntent().getStringExtra("description");
        String taskpriority = getIntent().getStringExtra("priority");
        String taskfinished = getIntent().getStringExtra("finished");
        String taskId = getIntent().getStringExtra("id");

        dueDate.setText(taskDate);
        startDate.setText(taskStartDate);
        description.setText(taskdescription);

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
            dueDate = (TextView) findViewById(R.id.dueDate);
            dueDate.setText(currentTime);
            this.deadClick = false;
        }else{
            startDate = (TextView) findViewById(R.id.startDate);
            startDate.setText(currentTime);
        }

    }

    // edit(String id, String name, String description, String start_date, String end_date, String priority, int finished)
    private void editItem(){

    }


}
