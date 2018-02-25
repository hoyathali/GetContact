package eswar.getcontact;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

/**
 * Created by Eswar
 */

public class SmsReceiver extends BroadcastReceiver {
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    String message,phone;
    SmsManager smsManager=SmsManager.getDefault();
    StringBuilder stringBuilder;

    @Override
    public void onReceive(Context context, Intent intent) {


        sp = context.getSharedPreferences("pref", 0);
        spe = sp.edit();
        stringBuilder = new StringBuilder();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] obj = (Object[]) bundle.get("pdus");
            if (obj != null) {
                for (int i = 0; i < obj.length; i++) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj[i]);
                    message = smsMessage.getMessageBody().toString();
                    phone = smsMessage.getOriginatingAddress().toString();
                }
            }
        }
        try {
            ContentResolver content = context.getContentResolver();

            Cursor cursor = content.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cursor.moveToNext()) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    int hasphone = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                    if (hasphone > 0) {
                        Cursor cursor2 = content.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                        while (cursor2.moveToNext()) {
                            String number = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            try {
                                if (name.equals(message) || name.contains(message)) {
                                    stringBuilder.append("").append(name + "\n").append("").append(number + "\n");
                                }
                            } catch (Exception e) {
                            }
                        }
                        cursor2.close();
                    }
                }
                cursor.close();
                Toast.makeText(context, stringBuilder, Toast.LENGTH_SHORT).show();
                smsManager.sendTextMessage(phone, null, "" + stringBuilder, null, null);
            }
        }catch (Exception e){}
    }

}