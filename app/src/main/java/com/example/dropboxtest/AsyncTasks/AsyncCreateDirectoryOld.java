package com.example.dropboxtest.AsyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class AsyncCreateDirectoryOld extends AsyncTask<Void,Void,Void> {
    public String path;
    public Boolean autoRename;
    public Context context;

    public AsyncCreateDirectoryOld(String path, boolean autoRename, Context context) {
        this.path=path;
        this.autoRename=autoRename;
        this.context=context;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        SharedPreferences sharedPreferences=context.getSharedPreferences("DropBox",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("DropBox_Directory_Created",true);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {

            //String urlParameters = "{" + "\"path\": \""+ path +"\"," + "\"autorename\": \""+autoRename.toString() + "\"}";
            String urlParameters = "{\r\n" + "    \"path\": \""+path+"\",\r\n" + "    \"autorename\": "+autoRename.toString()+"\r\n" + "}";
            URL url = new URL("https://api.dropboxapi.com/2/files/create_folder_v2");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer tcZu55if91AAAAAAAAAAfhY4akCxpD_0zWultgUY0W0-a9OzO8cH5Km95jzjil8x");
            connection.setRequestProperty("Content-Type", "application/json");

            DataOutputStream dataOutputStreamutput = new DataOutputStream(connection.getOutputStream());
            dataOutputStreamutput.write(urlParameters.getBytes("UTF-8"));
            dataOutputStreamutput.flush();

            OutputStream out = connection.getOutputStream();


            int response_conde = connection.getResponseCode();
            Log.v("ResponseCode",response_conde+"");
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




        return null;
    }
}
