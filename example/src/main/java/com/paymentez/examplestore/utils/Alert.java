package com.paymentez.examplestore.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.paymentez.android.rest.PaymentDialogCallback;

/**
 * Created by mmucito on 22/09/17.
 */

public class Alert {
    public static void show(Context mContext, String title, String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void showPayDialog(Context mContext, final PaymentDialogCallback callback){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setTitle("Place Order With Authentication 3DS");
        builder1.setMessage("Do you want to debit with 3D Secure 2 authentication?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        callback.onAccept();
                    }
                });
        builder1.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        callback.onAvoid();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void showAuthResponseDialog(Context mContext,  String title, String message, final PaymentDialogCallback callback){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        callback.onAccept();
                    }
                });
        builder1.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        callback.onAvoid();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
