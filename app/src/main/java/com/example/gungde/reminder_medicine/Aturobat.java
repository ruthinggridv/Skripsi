package com.example.gungde.reminder_medicine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.example.gungde.reminder_medicine.utils.AlarmReceiver;
import com.example.gungde.reminder_medicine.utils.BaseActivity;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Aturobat extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edtNama)
    EditText edtNama;
    @BindView(R.id.edtNotes)
    EditText edtNotes;
    @BindView(R.id.edtRefill)
    EditText edtRefill;
    @BindView(R.id.edtMaks)
    EditText edtMaks;
    @BindView(R.id.edtTime1)
    EditText edtTime1;
    @BindView(R.id.btnTime1)
    Button btnTime1;
    @BindView(R.id.bg1)
    LinearLayout bg1;
    @BindView(R.id.bg2)
    LinearLayout bg2;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.btnOut)
    Button btnOut;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static Aturobat inst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aturobat);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public static Aturobat instance() {
        return inst;
    }

    /*public void onSuccess(String time) {
        presenter.sendNotif(time, edtNama.getText().toString());
    }*/

    private void setTimePicker(final EditText et) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        et.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void setAlarm(EditText et) {
        Calendar calendar = Calendar.getInstance();
        String time = et.getText().toString();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(3)));
        calendar.set(Calendar.SECOND, 0);
        Intent myIntent = new Intent(Aturobat.this, AlarmReceiver.class);
        myIntent.putExtra("TIME", time);
        pendingIntent = PendingIntent.getBroadcast(Aturobat.this, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }


    @OnClick(R.id.btnTime1)
    public void onChangeTime1() {
        setTimePicker(edtTime1);
    }

    @OnClick(R.id.btnAdd)
    public void onViewClicked() {
        if (edtNama.getText().toString().trim().equals("")) {
            showSnackBar("Mohon input nama terlebih dahulu");
        } else {
            setAlarm(edtTime1);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @OnClick(R.id.btnOut)
    public void onViewClicked1() {
        finish();
    }
}
