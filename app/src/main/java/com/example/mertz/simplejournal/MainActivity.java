package com.example.mertz.simplejournal;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.support.design.widget.Snackbar;

import com.example.mertz.simplejournal.storage.IJournalStorageService;
import com.example.mertz.simplejournal.storage.JournalEntry;
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

        // Default focus should be on the layout, which will remove focus from inputs.
        m_defaultFocusView = findViewById(R.id.ConstraintLayout);

        m_storageService = new JournalStorageService(this);

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

        HideSoftKeyboard(MainActivity.this, view);
        RequestDefaultFocus();

        m_date = AddDays(m_date, 1);
        new LoadValuesTask().execute();
    }

    public void OnPreviousDate(View view) {

        HideSoftKeyboard(MainActivity.this, view);
        RequestDefaultFocus();

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

        HideSoftKeyboard(MainActivity.this, view);
        RequestDefaultFocus();

        JournalValuesContainer data = new JournalValuesContainer();

        data.GratefulnessEntries = GetJournalEntries(m_date, new EditText[] { m_gratefulness0Input, m_gratefulness1Input, m_gratefulness2Input });
        data.GoalEntries = GetJournalEntries(m_date, new EditText[] { m_goals0Input, m_goals1Input, m_goals2Input });
        data.AffirmationEntries = GetJournalEntries(m_date, new EditText[] { m_affirmationsInput });
        data.WinEntries = GetJournalEntries(m_date, new EditText[] { m_wins0Input, m_wins1Input, m_wins2Input });
        data.ImprovementEntries = GetJournalEntries(m_date, new EditText[] { m_improvementInput });

        new SaveValuesTask(view).execute(data);
    }

    private static List<JournalEntry> GetJournalEntries(Date date, EditText[] inputs) {

        List<JournalEntry> results = new ArrayList<>();
        int number = 0;

        for (EditText input: inputs) {
            results.add(new JournalEntry(date, number++, input.getText().toString()));
        }

        return results;
    }

    private void RequestDefaultFocus() {
        m_defaultFocusView.requestFocus();
        m_defaultFocusView.requestFocusFromTouch();
    }

    private static void HideSoftKeyboard(Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
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
    private View m_defaultFocusView;
    // Date for the data in this activity, used for loading and saving data.
    private Date m_date;

    private class LoadValuesTask extends AsyncTask<Object, Integer, JournalValuesContainer> {
        @Override
        protected JournalValuesContainer doInBackground(Object[] params) {

            JournalValuesContainer result = new JournalValuesContainer();

            result.GratefulnessEntries = m_storageService.GetGratefulnessEntries(m_date);
            result.GoalEntries = m_storageService.GetGoalEntries(m_date);
            result.AffirmationEntries = m_storageService.GetAffirmationEntries(m_date);
            result.WinEntries = m_storageService.GetWinEntries(m_date);
            result.ImprovementEntries = m_storageService.GetImprovementEntries(m_date);

            return result;
        }

        @Override
        protected void onPostExecute(JournalValuesContainer result) {

            String uiDateString = DateFormat.getDateInstance().format(m_date);
            m_dateLabel.setText(uiDateString);

            SetUiValues(result.GratefulnessEntries, new EditText[] { m_gratefulness0Input, m_gratefulness1Input, m_gratefulness2Input });
            SetUiValues(result.GoalEntries, new EditText[] { m_goals0Input, m_goals1Input, m_goals2Input });
            SetUiValues(result.AffirmationEntries, new EditText[] { m_affirmationsInput });
            SetUiValues(result.WinEntries, new EditText[] { m_wins0Input, m_wins1Input, m_wins2Input });
            SetUiValues(result.ImprovementEntries, new EditText[] { m_improvementInput });
        }

        private void SetUiValues(List<JournalEntry> values, EditText[] inputs) {

            // Clear inputs, so we don't keep previous data if a field doesn't have a stored value.
            for (EditText input : inputs) {
                input.setText("");
            }

            int numInputs = inputs.length;

            // Set given values. We use the Number property of a value to determine the correct input
            // field, so it's ok if there isn't a value for every given input. But all inputs must be
            // given, and in correct order.
            for (JournalEntry value : values) {
                if (value.Number() < numInputs) {
                    inputs[value.Number()].setText(value.Value());
                } else {
                    Log.e("LoadValuesTask", "Cannot handle entry with number " + value.Number() +
                        ", given number of inputs is " + numInputs);
                }
            }
        }
    }

    private class SaveValuesTask extends AsyncTask<JournalValuesContainer, Integer, Boolean> {
        SaveValuesTask(View view) {
            m_view = view;
        }

        @Override
        protected Boolean doInBackground(JournalValuesContainer[] params) {

            if (params == null || params.length != 1 || params[0] == null) {
                Log.e("SaveError", "Expected a single JournalValuesContainer parameter");
                return false;
            }

            JournalValuesContainer data = params[0];
            boolean success = true;

            try {
                if (data.GratefulnessEntries != null) {
                    for (JournalEntry entry : data.GratefulnessEntries) {
                        m_storageService.AddOrUpdateGratefulnessEntry(entry);
                    }
                }

                if (data.GoalEntries != null) {
                    for (JournalEntry entry : data.GoalEntries) {
                        m_storageService.AddOrUpdateGoalEntry(entry);
                    }
                }

                if (data.AffirmationEntries != null) {
                    for (JournalEntry entry : data.AffirmationEntries) {
                        m_storageService.AddOrUpdateAffirmationEntry(entry);
                    }
                }

                if (data.WinEntries != null) {
                    for (JournalEntry entry : data.WinEntries) {
                        m_storageService.AddOrUpdateWinEntry(entry);
                    }
                }

                if (data.ImprovementEntries != null) {
                    for (JournalEntry entry : data.ImprovementEntries) {
                        m_storageService.AddOrUpdateImprovementEntry(entry);
                    }
                }
            } catch (Throwable t) {
                Log.e("SaveError", "Failed to save entries", t);
                success = false;
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

    private class JournalValuesContainer {

        public List<JournalEntry> GratefulnessEntries;

        public List<JournalEntry> GoalEntries;

        public List<JournalEntry> AffirmationEntries;

        public List<JournalEntry> WinEntries;

        public List<JournalEntry> ImprovementEntries;
    }
}
