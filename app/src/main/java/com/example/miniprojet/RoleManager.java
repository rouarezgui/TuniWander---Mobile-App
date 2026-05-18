package com.example.miniprojet;

import android.content.Context;
import android.content.Intent;

public class RoleManager {

    public static void go(Context context, String role) {

        switch (role) {

            case "ADMIN":
                context.startActivity(new Intent(context, AdminDashboardActivity.class));
                break;

            case "GUIDE":
                context.startActivity(new Intent(context, GuideProfilActivity.class));
                break;

            case "AGENCY":
                context.startActivity(new Intent(context, AgenceProfilActivity.class));
                break;

            default:
                context.startActivity(new Intent(context, ListeLieuxActivity.class));
                break;
        }
    }
}