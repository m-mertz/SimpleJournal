package com.example.mertz.simplejournal;

import android.os.AsyncTask;
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

        new LoadValuesTask().execute();
    }

    @Override
    protected void onDestroy() {
        m_storageService.Close();
        super.onDestroy();
    }

    private JournalStorageService m_storageService;

    // TODO two tasks for read / write, or an intent service? intent service returns values in some
    // async way, but not sure how much boilerplate required to await results and write to UI.
    // if service interface is just about data and synchronous, and we have two background tasks here
    // to call it and link it to UI, that's not too bad.
    private class LoadValuesTask extends AsyncTask<Object, Integer, String> {

        @Override
        protected String doInBackground(Object[] params) {
            String gratefulness0 = m_storageService.GetGratefulnessEntry("2017-08-11", 0);
            return gratefulness0;
        }

        @Override
        protected void onPostExecute(String s) {
            EditText editText = (EditText)findViewById(R.id.Gratefulness0Input);
            editText.setText(s);
        }
    }
}
