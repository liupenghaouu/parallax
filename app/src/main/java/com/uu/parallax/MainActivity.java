package com.uu.parallax;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ParallaxListView lvMain;

    private ArrayList<String> names = new ArrayList<String>();
    private ImageView ivGirl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNames();
        lvMain = (ParallaxListView) findViewById(R.id.lv_main);


        //在设置数据适配器之前 设置  头布局
        View view = View.inflate(getApplicationContext(), R.layout.layout_head, null);
        ivGirl = (ImageView) view.findViewById(R.id.iv_gril);
        lvMain.setImageView(ivGirl);
        lvMain.addHeaderView(view);
        lvMain.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                names) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        });

    }

    /**
     * 初始化数据集合
     */
    private void initNames() {
        for (int i = 0; i < 20; i++) {
            names.add("数据" + i);

        }
    }
}
