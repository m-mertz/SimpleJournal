package com.example.mertz.simplejournal;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.design.widget.Snackbar;

import com.example.mertz.simplejournal.storage.GratefulnessEntry;
import com.example.mertz.simplejournal.storage.IJournalStorageService;
import com.example.mertz.simplejournal.storage.JournalStorageService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_dateLabel = (TextView)findViewById(R.id.CurrentDateLabel);
        m_gratefulness0Input = (EditText)findViewById(R.id.Gratefulness0Input);
        m_gratefulness1Input = (EditText)findViewById(R.id.Gratefulness1Input);
        m_gratefulness2Input = (EditText)findViewById(R.id.Gratefulness2Input);

        m_storageService = new JournalStorageService(this);

        // TODO add missing input fields, load and save operations.
        // reuse as much code as possible in the load/save tasks and service implementation, avoid
        // too much duplicate code.
        m_date = new Date();
        new LoadValuesTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void OnNextDate(View view) {

        // TODO would be nice if we could persist unsaved changes in some temporary way, or prompt
        // before discarding them by switching the date
        m_date = AddDays(m_date, 1);
        new LoadValuesTask().execute();
    }

    public void OnPreviousDate(View view) {
        m_date = AddDays(m_date, -1);
        new LoadValuesTask().execute();
    }

    private static Date AddDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public void OnSave(View view) {
        List<GratefulnessEntry> entries = new ArrayList<>();

        AddGratefulnessEntryIfNotEmpty(m_gratefulness0Input, m_date, 0, entries);
        AddGratefulnessEntryIfNotEmpty(m_gratefulness1Input, m_date, 1, entries);
        AddGratefulnessEntryIfNotEmpty(m_gratefulness2Input, m_date, 2, entries);

        new SaveValuesTask(view).execute(entries.toArray(new GratefulnessEntry[entries.size()]));
    }

    private static void AddGratefulnessEntryIfNotEmpty(EditText input, Date date, int number, List<GratefulnessEntry> list) {
        String value = input.getText().toString();
        list.add(new GratefulnessEntry(date, number, value));
    }

    private IJournalStorageService m_storageService;
    private TextView m_dateLabel;
    private EditText m_gratefulness0Input;
    private EditText m_gratefulness1Input;
    private EditText m_gratefulness2Input;
    // Date for the data in this activity, used for loading and saving data.
    private Date m_date;

    private class LoadValuesTask extends AsyncTask<Object, Integer, List<GratefulnessEntry>> {
        @Override
        protected List<GratefulnessEntry> doInBackground(Object[] params) {
            return m_storageService.GetGratefulnessEntries(m_date);
        }

        @Override
        protected void onPostExecute(List<GratefulnessEntry> results) {
            String uiDateString = DateFormat.getDateInstance().format(m_date);
            m_dateLabel.setText(uiDateString);

            m_gratefulness0Input.setText("");
            m_gratefulness1Input.setText("");
            m_gratefulness2Input.setText("");

            for (GratefulnessEntry entry: results) {
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
                        Log.e("LoadValuesTask", "Cannot handle gratefulness entry with number " + entry.Number());
                }
            }
        }
    }

    private class SaveValuesTask extends AsyncTask<GratefulnessEntry, Integer, Boolean> {
        SaveValuesTask(View view) {
            m_view = view;
        }

        @Override
        protected Boolean doInBackground(GratefulnessEntry[] params) {
            boolean success = true;

            for (GratefulnessEntry entry: params) {
                try {
                    m_storageService.AddOrUpdateGratefulnessEntry(entry);
                } catch (Throwable t) {
                    Log.e("SaveError", "Failed to save entries", t);
                    success = false;
                }
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            String message = result ? "Save successful" : "Error on save!";
            Snackbar.make(m_view, message, Snackbar.LENGTH_SHORT).show();
        }

        private View m_view;
    }
}
