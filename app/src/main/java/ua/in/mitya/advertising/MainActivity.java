package ua.in.mitya.advertising;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ua.in.mitya.advertising.model.DataModel;
import ua.in.mitya.advertising.tasks.ConnectAsyncTask;

public class MainActivity extends AppCompatActivity {

    private Button btnContentScreen;
    private CoordinatorLayout coordinatorLayout;
    private static long back_pressed;
    private static final String LINC = "http://mitya.in.ua:8081/LoopMe/json.txt";
    private List<String> modelArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getUI();
        modelArray = new ArrayList<>();
    }

    public void getUI() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main);
        btnContentScreen = (Button) findViewById(R.id.button);
        btnContentScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasConnection(MainActivity.this)) {
                    new ConnectAsyncTask(MainActivity.this, LINC, modelArray)
                            .execute();
                } else {
                    Snackbar.make(coordinatorLayout, R.string.errore_connection, Snackbar.LENGTH_SHORT)
                            .setActionTextColor(Color.RED).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else {
            Snackbar snackbarExitMessage = Snackbar.make(coordinatorLayout,
                    R.string.press_to_exit_message, Snackbar.LENGTH_SHORT);
            snackbarExitMessage.setActionTextColor(Color.RED);
            snackbarExitMessage.show();
        }
        back_pressed = System.currentTimeMillis();
    }

    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
