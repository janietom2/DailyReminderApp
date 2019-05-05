package edu.utep.cs.cs4330.dailyreminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewTask extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);


        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        dueDate = (TextView) findViewById(R.id.dueDate);

        String taskTitle = getIntent().getStringExtra("Title");
        String taskDate = getIntent().getStringExtra("Date");
        toolbar_title.setText(taskTitle);
        dueDate.setText(taskDate);

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
