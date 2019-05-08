package edu.utep.cs.cs4330.dailyreminder;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import edu.utep.cs.cs4330.dailyreminder.Models.DatabaseHelper;
import edu.utep.cs.cs4330.dailyreminder.Models.Task;



public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> tasks;
    public TaskAdapter taskAdapter;
    private EditText filter;
    private Toolbar toolbar;
    DatabaseHelper db;
    ListView taskView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Toolbar and Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        //DB
        db = new DatabaseHelper(this);

        // Content View
        setContentView(R.layout.activity_main);

        // Fill List
        this.tasks = dummyFill(3);

        // Variables
        ArrayList<Task> tmp   = new ArrayList<Task>();
        this.filter           = findViewById(R.id.searchFilter);

        // Set visibility of search bar off from the beginning
        this.filter.setVisibility(View.VISIBLE);

        // Set Tasks Adapter (ListView)
        this.taskView = findViewById(R.id.items_list);
        this.taskAdapter = new TaskAdapter(this, tasks);
        this.taskView.setAdapter(taskAdapter);
        this.taskView.setTextFilterEnabled(true);

        // Create search filter
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        String action = getIntent().getAction();
        String type   = getIntent().getType();
        if (Intent.ACTION_SEND.equalsIgnoreCase(action) && type != null && ("text/plain".equals(type))){
            String url = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            String priority = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            FragmentManager fm = getSupportFragmentManager();
            AddDialog addDialogFragment = new AddDialog();
            Bundle args = new Bundle();
            args.putString("url", url);
            args.putString("priority", priority);
            addDialogFragment.setArguments(args);
            addDialogFragment.show(fm, "add_item");
        }



        taskView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            itemClicked(position);
        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTask:
//                Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
                showAddDialog();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newmenu, menu);
        return true;
    }

    public ArrayList<Task> dummyFill(int n){
        ArrayList<Task> tmp = new ArrayList<Task>();
        Date currentTime = Calendar.getInstance().getTime();
        Random rand = new Random();

        int counter = 0;
        int priority = 0;
        int cut = n / 3;

        for (int i = 0; i < n ; i++) {
            if (counter > cut){
                priority++;
                counter = 0;
            }


            tmp.add(new Task("Task "+i, "Description "+i, currentTime, currentTime, 1, "3"));
            counter++;
        }

        return tmp;
    }

    // =========================
    // CRUD
    // =========================

    public void addItem(String name, String priority) {
        Log.i("numba", priority);

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();


        //     public boolean insertData(String name, String date_created, String date_end, String priority, String file1, String file2, String description) {
        db.insertData(name, today, today, Integer.parseInt(priority), "file1", "file2", "Do homework");

        //public Task(String title, String description,Date startDate, Date deadLine, int priority, int id) {

        Task t = new Task(name, "Do Homework", today, today, Integer.parseInt(priority), db.lastId());


        this.tasks.add(t);
        this.taskAdapter.addItem(t); // Add to adapter (To be able to filter it)
        this.taskAdapter.notifyDataSetChanged();
    }


    // =========================
    // Dialogs
    // =========================

    public void showAddDialog(){
        FragmentManager fm = getSupportFragmentManager();
        AddDialog addDialogFragment = new AddDialog();
        addDialogFragment.show(fm, "add_item");
    }

    public void itemClicked(int position){
        Intent taskIntent = new Intent(this, ViewTask.class);
//        String itemDataAsString = gson.toJson(itm.getList()); // Serialize Object to pass it
//        itemIntent.putExtra("position", position);
//        itemIntent.putExtra("itemDataAsString", itemDataAsString);
        taskIntent.putExtra("Title", tasks.get(position).getTitle());
        taskIntent.putExtra("Date", tasks.get(position).getDeadLine().toString());
        startActivity(taskIntent);
    }

}
