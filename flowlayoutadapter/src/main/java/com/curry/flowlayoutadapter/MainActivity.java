package com.curry.flowlayoutadapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.curry.flowlayoutadapter.v2.TestTagAdapter;
import com.curry.flowlayoutadapter.v2.XCFlowLayout2;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private XCFlowLayout2 flowLayout;
    private ArrayList<String> mStrList;
    private LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flowLayout = findViewById(R.id.flowLayout);
        initData();
        initView();
    }

    private void initData() {
        mStrList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mStrList.add("" + i + "   qqqq");
        }
    }

    private void initView() {

        mLayoutInflater = LayoutInflater.from(this);

        flowLayout.setAdapter(new TestTagAdapter(this,mStrList));
        flowLayout.setOnTagClickListener(new XCFlowLayout2.OnTagClickListener() {
            @Override
            public boolean onTagClick(int position, View view, XCFlowLayout2 parent) {
//                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
                Log.e("mainactivity", "onTagClick: " + position);
                parent.remove(position);
                return false;
            }
        });
        flowLayout.setmSelectedMax(1);
    }




}
