package com.snake.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.snake.com.db.ClassSnakeDB;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.app.ListActivity;

public class UserInfoActivity extends ListActivity {

    private List<HashMap<String,String>> listItem;
    private ClassSnakeDB snakeDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.userinfo_layout);

        snakeDB = new ClassSnakeDB(this);
        updateData();

        ListAdapter adapter = new SimpleAdapter(this, 
                                                listItem,
                                                R.layout.userinfo_layout,
                                                new String[] { "query", "name", "score" },
                                                new int[] {R.id.query, R.id.name, R.id.score}
                                                );

        setListAdapter(adapter);
    }

    private void updateData() {
        listItem = new ArrayList<HashMap<String, String> >();
        if (snakeDB != null && snakeDB.getUserInfo() != null) {
            int size = snakeDB.getUserInfo().length;
            if (size <= 0) {
                return;
            }
            String[][] userInfo = new String[size][2];
            userInfo = snakeDB.getUserInfo();
            for (int i = 0; i < size; ++i) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("query", String.valueOf(i+1));
                map.put("name", userInfo[i][0]);
                map.put("score", userInfo[i][1]);
                listItem.add(map);
            }
        }
    }

}
