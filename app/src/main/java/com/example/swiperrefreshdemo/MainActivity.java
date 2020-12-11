package com.example.swiperrefreshdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView mListView;
    private ArrayAdapter mListAdapter;

    @SuppressLint("HandlerLeak")
    private  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage: ");
            switch(msg.what){
                case 1:
                    mListAdapter.clear();
                    mListAdapter.addAll(Cheeses.randomList(20));
                    swipeRefreshLayout.setRefreshing(false);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        mListView = findViewById(android.R.id.list);
        View headView = getLayoutInflater().inflate(R.layout.header, null);

        mListView.addHeaderView(headView);
        /**
         * Create an ArrayAdapter to contain the data for the ListView. Each item in the ListView
         * uses the system-defined simple_list_item_1 layout that contains one TextView.
         */
        mListAdapter = new ArrayAdapter<String>(
                  this,
                  android.R.layout.simple_list_item_1,
                  android.R.id.text1,
                  Cheeses.randomList(20));

        // Set the adapter between the ListView and its backing data.
        mListView.setAdapter(mListAdapter);

        //手势触发下拉刷新
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
//            swipeRefreshLayout.setRefreshing(true);
            myUpdateOperation();

        });
    }



    private void myUpdateOperation() {
     //TODO..
        // Sleep for a small amount of time to simulate a background-task


        // Return a new random list of cheeses


        //1.
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Message msg=new Message();
//                msg.what=1;
//                mHandler.sendMessageDelayed(msg,3000);
//            }
//        }).start();
        //2.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListAdapter.clear();
                            mListAdapter.addAll(Cheeses.randomList(20));
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
        // this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            // Check if user triggered a refresh:
            case R.id.menu_refresh:
                Log.i(TAG, "Refresh menu item selected");

                // Signal SwipeRefreshLayout to start the progress indicator
                swipeRefreshLayout.setRefreshing(true);

                // Start the refresh background task.
                // This method calls setRefreshing(false) when it's finished.
                myUpdateOperation();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);

    }
}