package ua.in.mitya.advertising.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ua.in.mitya.advertising.ContentActivity;
import ua.in.mitya.advertising.R;
import ua.in.mitya.advertising.model.DataModel;

/**
 * Created by mitya on 05.12.2016.
 */

public class ConnectAsyncTask extends AsyncTask<Void, Void, Void> {

    private ProgressDialog dialog;
    private Context context;
    private String video, clickUrl;
    private List<String> models;
    private String linc;

    public ConnectAsyncTask(Context context, String url, List<String> models) {
        this.context = context;
        dialog = new ProgressDialog(context);
        this.models = models;
        this.linc = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage(context.getString(R.string.download_message));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL(linc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String data = "";

            while ((data = reader.readLine()) != null) {
                Gson gson = new Gson();
                DataModel model = gson.fromJson(data, DataModel.class);
                video = model.getVideoSource();
                clickUrl = model.getClickUrl();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra("video", video);
        intent.putExtra("click", clickUrl);
        context.startActivity(intent);
    }
}
