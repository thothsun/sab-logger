package com.thothsun.sablogger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.i(1, 65, 3);
        Logger.tag("benkonghaha").d("hello jj");
        test();
    }

    private void test(){
        Logger.stacks(2).d("stack test");
        Logger.i(100,"分");
    }
}