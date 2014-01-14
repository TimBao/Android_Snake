package com.snake.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.snake.com.db.ClassSnakeDB;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.app.ListActivity;

public class UserInfoActivity extends ListActivity {

    private List<HashMap<String,String>> listItem;
    private Button curDelete;
    private int pointX, pointY, endX, endY;
    private int position;
    private ClassSnakeDB snakeDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.userinfo_layout);

        snakeDB = new ClassSnakeDB(this);
        updateData();

        final SimpleAdapter adapter = new SimpleAdapter(this, 
                                                listItem,
                                                R.layout.userinfo_layout,
                                                new String[] { "query", "name", "score" },
                                                new int[] {R.id.query, R.id.name, R.id.score}
                                                );

        setListAdapter(adapter);

        final ListView lv = getListView();
        lv.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pointX = (int)event.getX();
                    pointY = (int)event.getY();
                    position = lv.pointToPosition(pointX, pointY);
                    if (curDelete != null) {
                        curDelete.setVisibility(View.GONE);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    endX = (int)event.getX();
                    endY = (int)event.getY();
                    if (Math.abs(pointX - endX) > 30) {
                        int firstVisiblePosition = lv.getFirstVisiblePosition();
                        final View view = lv.getChildAt(position - firstVisiblePosition);
                        Button delBtn = (Button)view.findViewById(R.id.del);
                        if (delBtn != null) {
                            delBtn.setVisibility(View.VISIBLE);
                            curDelete = delBtn;
                            delBtn.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    listItem.remove(position);
                                    adapter.notifyDataSetChanged();
                                    if (snakeDB != null) {
                                        TextView text = (TextView)view.findViewById(R.id.name);
                                        String name = (String)text.getText();
                                        text = (TextView)view.findViewById(R.id.score);
                                        String score = (String)text.getText();
                                        snakeDB.deleteUserInfo(name, score);
                                    }
                                }
                            });
                        }
                    }
                    break;
                default:
                    break;
                }
                return false;
            }
        });
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
