
package com.android.example.myalarmnotification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;

public class SetAlarm extends Activity {

    Toast mToast;
    EditText sendMsg;
    
    Calendar cal = Calendar.getInstance();
    int counter = 0;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent recvIntent = new Intent(getBaseContext(), AlarmReceiver.class);
            Intent actIntent = new Intent(getBaseContext(), IntentMessage.class);
            switch(msg.what) {
                case 0:
                    recvIntent.putExtra("title", "[" + counter++ + "] 테스트 합니다...");
                    PendingIntent appInte0nt = PendingIntent.getBroadcast(getBaseContext(), counter++, recvIntent, 0);

                    Log.d("androidbee", "[" + counter++ + "]Alarm time:" + cal.getTime().toString());

                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), appInte0nt);
                    break;
                case 1:
                    String title = sendMsg.getText().toString();
                    title = (title==null?"타이틀이 없습니다":title);
                    actIntent.putExtra("title", title);
                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getBaseContext(), counter++, recvIntent, 0);
//                    PendingIntent pendingIntent2 = PendingIntent.getActivity(getBaseContext(), counter++, actIntent, 0);

                    Log.d("androidbee", "Title=" + title + " | Alarm time:" + cal.getTime().toString());

                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent2);
                    break;
            }

        }

    };

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.alarm_main);

        Button btn_send = (Button) findViewById(R.id.btn_send);
        sendMsg = (EditText) findViewById(R.id.editText1);
        btn_send.setOnClickListener( new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                showDateTimeDialog(1);
            }
        });
        
        Button button = (Button) findViewById(R.id.set_alarm_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(0);
            }
        });

    }

    private void showDateTimeDialog(int type) {
        final int what = type;
        // Create the dialog
        final Dialog mDateTimeDialog = new Dialog(this);
        // Inflate the root layout
        final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(
                R.layout.datetime_dialog, null);
        // Grab widget instance
        final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
                .findViewById(R.id.DateTimePicker);
        // Check is system is set to use 24h time (this doesn't seem to work as
        // expected though)
        final String timeS = android.provider.Settings.System.getString(getContentResolver(),
                android.provider.Settings.System.TIME_12_24);
        final boolean is24h = !(timeS == null || timeS.equals("12"));

        // Update demo TextViews when the "OK" button is clicked
        ((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime))
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        mDateTimePicker.clearFocus();

                        cal.set(Calendar.YEAR, mDateTimePicker.get(Calendar.YEAR));
                        cal.set(Calendar.MONTH, (mDateTimePicker.get(Calendar.MONTH)));
                        cal.set(Calendar.DAY_OF_MONTH, mDateTimePicker.get(Calendar.DAY_OF_MONTH));
                        if (mDateTimePicker.is24HourView()) {
                            cal.set(Calendar.HOUR_OF_DAY, mDateTimePicker.get(Calendar.HOUR_OF_DAY));
                            cal.set(Calendar.MINUTE, mDateTimePicker.get(Calendar.MINUTE));
                        } else {
                            cal.set(Calendar.HOUR, mDateTimePicker.get(Calendar.HOUR));
                            cal.set(Calendar.MINUTE, mDateTimePicker.get(Calendar.MINUTE));
                        }
                        
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        handler.sendMessage(msg);
                        
                        mDateTimeDialog.dismiss();
                    }
                });

        // Cancel the dialog when the "Cancel" button is clicked
        ((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog))
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mDateTimeDialog.cancel();
                    }
                });

        // Reset Date and Time pickers when the "Reset" button is clicked
        ((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime))
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        mDateTimePicker.reset();
                    }
                });

        // Setup TimePicker
        mDateTimePicker.setIs24HourView(is24h);
        // No title on the dialog window
        mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set the dialog content view
        mDateTimeDialog.setContentView(mDateTimeDialogView);
        // Display the dialog
        mDateTimeDialog.show();
    }

}
