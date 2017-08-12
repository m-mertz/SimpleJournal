package com.example.mertz.simplejournal;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mertz.simplejournal.storage.GratefulnessEntry;
import com.example.mertz.simplejournal.storage.JournalStorageService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_date = GetCurrentDate();
        ((TextView)findViewById(R.id.CurrentDateLabel)).setText(m_date);

        m_gratefulness0Input = (EditText)findViewById(R.id.Gratefulness0Input);
        m_gratefulness1Input = (EditText)findViewById(R.id.Gratefulness1Input);
        m_gratefulness2Input = (EditText)findViewById(R.id.Gratefulness2Input);

        m_storageService = new JournalStorageService(this);

        // TODO remove
        m_storageService.AddOrUpdateGratefulnessEntry(new GratefulnessEntry(m_date, 1, "test"));

        new LoadValuesTask().execute();
    }

    @Override
    protected void onDestroy() {
        m_storageService.Close();
        super.onDestroy();
    }

    private static String GetCurrentDate() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(now);
    }

    private JournalStorageService m_storageService;
    private EditText m_gratefulness0Input;
    private EditText m_gratefulness1Input;
    private EditText m_gratefulness2Input;
    private String m_date;

    // TODO two tasks for read / write, or an intent service? intent service returns values in some
    // async way, but not sure how much boilerplate required to await results and write to UI.
    // if service interface is just about data and synchronous, and we have two background tasks here
    // to call it and link it to UI, that's not too bad.
    private class LoadValuesTask extends AsyncTask<Object, Integer, List<GratefulnessEntry>> {
        @Override
        protected List<GratefulnessEntry> doInBackground(Object[] params) {
            return m_storageService.GetGratefulnessEntries(m_date);
        }

        @Override
        protected void onPostExecute(List<GratefulnessEntry> results) {
            for (GratefulnessEntry entry: results) {
                // TODO this is a bit shit
                switch (entry.Number()) {
                    case 0:
                        m_gratefulness0Input.setText(entry.Value());
                        break;

                    case 1:
                        m_gratefulness1Input.setText(entry.Value());
                        break;

                    case 2:
                        m_gratefulness2Input.setText(entry.Value());
                        break;

                    default:
                        throw new IllegalArgumentException("Unexpected gratefulness number " + entry.Number());
                }
            }
        }
    }
}
