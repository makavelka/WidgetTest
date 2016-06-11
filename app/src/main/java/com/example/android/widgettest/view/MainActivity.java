package com.example.android.widgettest.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.widgettest.PrefsUtils;
import com.example.android.widgettest.R;
import com.example.android.widgettest.service.GetRssDataService;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        final EditText editText = (EditText) findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GetRssDataService.class);
                PrefsUtils.saveUrl(editText.getText().toString(), MainActivity.this);
                startService(intent);
                finish();
            }
        });
    }
}
