package api;

import ro.polak.webserver.controller.MainController;
import ro.polak.webserver.servlet.HTTPRequest;
import ro.polak.webserver.servlet.HTTPResponse;
import ro.polak.webserver.servlet.Servlet;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;

public class SmsInbox extends Servlet {

    public void main(HTTPRequest request, HTTPResponse response) {

        Cursor cursor = ((Activity) MainController.getInstance().getContext())
                .getContentResolver().query(Uri.parse("content://sms/inbox"),
                        null, null, null, null);
        cursor.moveToFirst();

        int max = 50;
        int i = 0;

        String json = "";

        json += "{\n";
        json += "\t\"messages\" : [\n";
        do {
            json += "\t\t{\n";
            int cc = cursor.getColumnCount();
            int ccm1 = cc - 1;
            for (int idx = 0; idx < cc; idx++) {
                json += "\t\t\t\"" + cursor.getColumnName(idx) + "\":\"";
                if (cursor.getString(idx) != null)
                    json += cursor.getString(idx).toString()
                            .replace("\"", "\\\"");

                json += "\"";

                if (idx != ccm1) {
                    json += ",\n";
                } else {
                    json += "\n";
                }
            }
            json += "\t\t}";

            if (i++ == max) {
                json += "\n";
                break;
            }

            if (!cursor.isLast()) {
                json += ",\n";
            } else {
                json += "\n";
            }

        } while (cursor.moveToNext());

        json += "\t]\n";
        json += "}";

        response.getPrintWriter().print(json);
    }
}