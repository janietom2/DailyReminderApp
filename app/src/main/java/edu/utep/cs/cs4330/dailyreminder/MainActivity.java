package edu.utep.cs.cs4330.dailyreminder;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import edu.utep.cs.cs4330.dailyreminder.Models.Task;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> tasks;
    public TaskAdapter taskAdapter;
    private EditText filter;
    private Toolbar toolbar;
    ListView taskView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Windows Operations
        windowOperations();

        // Content View
        setContentView(R.layout.activity_main);


        // Fill List
        this.tasks = dummyFill(15);

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

        taskView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            itemClicked(position);
        });
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
            tmp.add(new Task("Task "+i, "Description "+i, currentTime, priority));
            counter++;
        }

        return tmp;
    }

    // Create Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.newmenu, menu);
        return true;
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

    public void windowOperations(){
        // Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // New ToolBar
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
    }
}
