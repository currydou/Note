package com.curry.flowlayoutadapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.flowlayoutadapter.v2.BaseAdapter;
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
        mStrList.add("JUSTH HELLO!");
        mStrList.add("JUSTH HEJUSTH HELLO!LLO!");
        mStrList.add("JUSTH LLO!");
        mStrList.add("JUSTH HEJUSTH HELLO!LLO!");
        mStrList.add("JUSTH HELLO!");
        mStrList.add("JUSTLO!");
        mStrList.add("JUSTH HELLO!");
        mStrList.add("JUSTH HEJUSTH HELLO!LLO!");
        mStrList.add("JUSTH HELO!");
        mStrList.add("JUSTH HELLO!");
    }

    private void initView() {

        mLayoutInflater = LayoutInflater.from(this);

        flowLayout.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mStrList.size();
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                View view = mLayoutInflater.inflate(R.layout.layout_textview, parent, false);
                ((TextView) view.findViewById(R.id.item_textview)).setText(mStrList.get(position));
                return view;
            }
        });
    }

}
