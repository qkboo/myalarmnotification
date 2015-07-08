package com.android.example.myalarmnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager mNotiMgr;
    private Context mContext;
    private int NOTI_ID = 0;
    private int count = 0;

    public void onReceiveIntent(Context context, Intent intent) {
        mContext = context;
        mNotiMgr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NOTI_ID = (int) System.currentTimeMillis();
        
        notifyEvent(intent);
        
//        Toast.makeText(context, "알람으로 Intent를 Receiver로 받습니다.", Toast.LENGTH_SHORT).show();
        abortBroadcast();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        onReceiveIntent(context, intent);
    }
    
    
    int counter = 0;


    private void notifyEvent(Intent intent) {
        String title = intent.getStringExtra("title");
        title = (title==null||"".equals(title)?"타이틀이 없습니다":title);
        Notification note=new Notification(R.drawable.s_ball,
                "[" + counter + "] " + title,
                System.currentTimeMillis());
        PendingIntent pIntent = PendingIntent.getActivity(mContext,
                NOTI_ID, new Intent(mContext, IntentMessage.class), 0);
        
        note.setLatestEventInfo( mContext, "제목: " + title,
                "[" + counter + "] " + title, pIntent);
        note.number=++count;
        counter++;
        note.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotiMgr.notify(NOTI_ID, note);
    }

}
