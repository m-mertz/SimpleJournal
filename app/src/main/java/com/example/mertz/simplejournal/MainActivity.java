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
        m_goals0Input = (EditText)findViewById(R.id.Goals0Input);
        m_goals1Input = (EditText)findViewById(R.id.Goals1Input);
        m_goals2Input = (EditText)findViewById(R.id.Goals2Input);
        m_affirmationsInput = (EditText)findViewById(R.id.AffirmationsInput);
        m_wins0Input = (EditText)findViewById(R.id.Wins0Input);
        m_wins1Input = (EditText)findViewById(R.id.Wins1Input);
        m_wins2Input = (EditText)findViewById(R.id.Wins2Input);
        m_improvementInput = (EditText)findViewById(R.id.ImprovementInput);

        m_storageService = new JournalStorageService(this);

        // TODO add load and save operations for other inputs
        // reuse as much code as possible in the load/save tasks and service implementation, avoid
        // too much duplicate code.
        // maybe use a common base class for the data types, and generic save/load methods in the service
        // that just get called with the correct table names.
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
        GratefulnessEntry[] entries = {
            new GratefulnessEntry(m_date, 0, m_gratefulness0Input.getText().toString()),
            new GratefulnessEntry(m_date, 1, m_gratefulness1Input.getText().toString()),
            new GratefulnessEntry(m_date, 2, m_gratefulness2Input.getText().toString())
        };

        new SaveValuesTask(view).execute(entries);
    }

    private IJournalStorageService m_storageService;
    private TextView m_dateLabel;
    private EditText m_gratefulness0Input;
    private EditText m_gratefulness1Input;
    private EditText m_gratefulness2Input;
    private EditText m_goals0Input;
    private EditText m_goals1Input;
    private EditText m_goals2Input;
    private EditText m_affirmationsInput;
    private EditText m_wins0Input;
    private EditText m_wins1Input;
    private EditText m_wins2Input;
    private EditText m_improvementInput;
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
