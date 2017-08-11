package com.example.mertz.simplejournal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mertz.simplejournal.storage.JournalStorageService;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_storageService = new JournalStorageService(this);

        String testValue = m_storageService.GetGratefulnessEntry("2017-08-11", 0);
        EditText editText = (EditText)findViewById(R.id.Gratefulness0Input);
        editText.setText(testValue);
    }

    @Override
    protected void onDestroy() {
        m_storageService.Close();
        super.onDestroy();
    }

    private JournalStorageService m_storageService;
}
