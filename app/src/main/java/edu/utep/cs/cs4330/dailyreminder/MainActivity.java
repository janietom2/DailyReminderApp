package edu.utep.cs.cs4330.dailyreminder;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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



public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ArrayList<Task> tasks;
    public TaskAdapter taskAdapter;
    private EditText filter;
    private Toolbar toolbar;
    private Boolean shaked = false;
    DatabaseHelper db;
    ListView taskView;
    Sensor accel;
    SensorManager sm;

    private float acelVal;
    private float acelLast;
    private float shake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Toolbar and Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Sensor
        sm = (SensorManager)  getSystemService(SENSOR_SERVICE);
        accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        //DB
        db = new DatabaseHelper(this);

        // Content View
        setContentView(R.layout.activity_main);

        this.tasks = loadFromDB();

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

        registerForContextMenu(taskView);
        taskView.setOnCreateContextMenuListener(this);

        taskView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            itemClicked(position);
        });
        taskAdapter.notifyDataSetChanged();
        taskAdapter.setNotifyOnChange(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.tasks = loadFromDB();

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

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTask:
                showAddDialog();
                return true;
        }
        return false;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position;
        switch (item.getItemId()) {
            case R.id.delete_item:
                showDeleteDialog(itemPosition);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newmenu, menu);
        return true;
    }

    // =========================
    // Dialogs
    // =========================

    public void showAddDialog(){
        FragmentManager fm = getSupportFragmentManager();
        AddDialog addDialogFragment = new AddDialog();
        addDialogFragment.show(fm, "add_item");
    }

    public void showDeleteDialog(int position){
        FragmentManager fm = getSupportFragmentManager();
        DeleteDialog deleteDialogFragment = new DeleteDialog();
        Bundle args = new Bundle();
        args.putInt("position", position);
        deleteDialogFragment.setArguments(args);
        deleteDialogFragment.show(fm, "delete_item");
    }

    public void showDeleteDialogShake(){
        FragmentManager fm = getSupportFragmentManager();
        DeleteDialogShake deleteDialogFragment = new DeleteDialogShake();
        Bundle args = new Bundle();
        deleteDialogFragment.setArguments(args);
        deleteDialogFragment.show(fm, "delete_item");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }


    public void itemClicked(int position){
        Intent taskIntent = new Intent(this, ViewTask.class);
//        String itemDataAsString = gson.toJson(itm.getList()); // Serialize Object to pass it
//        itemIntent.putExtra("position", position);
//        itemIntent.putExtra("itemDataAsString", itemDataAsString);
        taskIntent.putExtra("Title", tasks.get(position).getTitle());
        taskIntent.putExtra("end_date", tasks.get(position).getDeadLine().toString());
        taskIntent.putExtra("description", tasks.get(position).getDescription().toString());
        taskIntent.putExtra("start_date", tasks.get(position).getStartDate().toString());
        taskIntent.putExtra("priority", tasks.get(position).getPriority());
        taskIntent.putExtra("finished", tasks.get(position).getFinished());
        taskIntent.putExtra("id", tasks.get(position).getId());
        startActivity(taskIntent);
    }

    // =========================
    // CRUD
    // =========================

    public void addItem(String name, String priority) {
        Log.i("numba", priority);
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        db.insertData(name, today, today, Integer.parseInt(priority), "file1", "file2", "Do homework");
        Task t = new Task(name, Integer.parseInt(priority), db.lastId());
        this.tasks.add(t);
        this.taskAdapter.addItem(t); // Add to adapter (To be able to filter it)
        this.taskAdapter.notifyDataSetChanged();
        reSort();
    }

    private ArrayList<Task> loadFromDB(){

        // Lists
        ArrayList<Task> red = new ArrayList<Task>(); // high
        ArrayList<Task> purple = new ArrayList<Task>(); // medium
        ArrayList<Task> blue = new ArrayList<Task>(); // low
        ArrayList<Task> done = new ArrayList<Task>(); // done


        ArrayList<Task> tks = new ArrayList<Task>();
        Cursor response = db.fetchAllData();

        if(response.getCount() == 0) {
            Toast.makeText(getBaseContext(), "Seems you have nothing to do then! Or add a new task!", Toast.LENGTH_LONG).show();
        }

        while(response.moveToNext()) {
            Task tmp = new Task(response.getString(1),
                    response.getString(7),
                    response.getString(2),
                    response.getString(3),
                    Integer.valueOf(response.getString(4)),
                    response.getString(0),
                    Integer.valueOf(response.getString(8)));

            if(response.getString(8).equals("1")){
                done.add(tmp);
            } else if (response.getString(4).equals("1")){
                purple.add(tmp);
            } else if(response.getString(4).equals("0")){
                red.add(tmp);
            }else{
                blue.add(tmp);
            }
        }

        tks.addAll(red);
        tks.addAll(purple);
        tks.addAll(blue);
        tks.addAll(done);

        return tks;
    }

    public void DeleteItemDialog(int position){
        Task t;
        t = this.tasks.get(position);
        db.delete(t.getId());
        this.tasks.remove(t);
        this.taskAdapter.removeItem(position);
        taskAdapter.notifyDataSetChanged();
    }

    private void reSort(){
        this.tasks = loadFromDB();

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
    }


    public void deleteDone(){
        ArrayList<Task> temp = this.tasks;
        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).getFinished() == 1) {
                db.delete(temp.get(i).getId());
                this.taskAdapter.removeItem(i);
                this.tasks.remove(temp.get(i));
                taskAdapter.notifyDataSetChanged();
            }
        }
        reSort();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
//         Toast.makeText(getBaseContext(), "Shake it!! Shake it!!", Toast.LENGTH_LONG).show();

        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        acelLast = acelVal;
        acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z));
        float delta = acelVal - acelLast;

        shake = shake * 0.9f + delta;

        if(shake > 12 && !shaked){
//            this.shaked = true;
            Toast.makeText(getBaseContext(), "Poof! Completed tasks are gone!", Toast.LENGTH_LONG).show();
//            showDeleteDialogShake();
            deleteDone();
        }
//        this.shaked = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
