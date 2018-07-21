package com.shivam.probussense.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shivam.probussense.Services.Mynotifyservice;


public class Myreceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())||Intent.ACTION_REBOOT.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, Mynotifyservice.class);
            context.startService(serviceIntent);
        }
    }
}
