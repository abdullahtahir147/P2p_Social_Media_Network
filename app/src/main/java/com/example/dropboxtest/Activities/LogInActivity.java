package com.example.dropboxtest.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dropboxtest.Constants;
import com.example.dropboxtest.R;

public class LogInActivity extends AppCompatActivity {

    public Context context=this;
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences=this.getSharedPreferences("DropBox",MODE_PRIVATE);
        String token=sharedPreferences.getString("DropBox_token","");
        if(!token.equals("")){
            Constants.ACCESS_TOKEN=token;
            Log.v("access token",token);
        } else{

            Toast.makeText(getApplicationContext(),"You must sign up",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button LogIn=findViewById(R.id.LoginbtnHome);
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.ACCESS_TOKEN!=""){

                    Intent intent=new Intent(LogInActivity.this,ButtomNavigation_Activity.class);
                    startActivity(intent);

                }

            }
        });

        Button SignIn=(Button)findViewById(R.id.SingUpbtnHome);
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogInActivity.this,Cloud.class);
                startActivity(intent);
            }
        });
    }

    
    
    
//        final StitchAppClient client = Stitch.initializeDefaultAppClient("pspn_1-ykutk");
//
//        final RemoteMongoClient mongoClient =
//                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
//
//        final RemoteMongoCollection<Document> coll =
//                mongoClient.getDatabase("Data").getCollection("Coll");
//
//        client.getAuth().loginWithCredential(new AnonymousCredential()).continueWithTask(
//                new Continuation<StitchUser, Task<RemoteUpdateResult>>() {
//
//                    @Override
//                    public Task<RemoteUpdateResult> then(@NonNull Task<StitchUser> task) throws Exception {
//                        if (!task.isSuccessful()) {
//                            Log.e("STITCH", "Login failed!");
//                            throw task.getException();
//                        }
//
//                        final Document updateDoc = new Document(
//                                "owner_id",
//                                task.getResult().getId()
//                        );
//
//                        updateDoc.put("number", 42);
//                        return coll.updateOne(
//                                null, updateDoc, new RemoteUpdateOptions().upsert(true)
//                        );
//                    }
//                }
//        ).continueWithTask(new Continuation<RemoteUpdateResult, Task<List<Document>>>() {
//            @Override
//            public Task<List<Document>> then(@NonNull Task<RemoteUpdateResult> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    Log.e("STITCH", "Update failed!");
//                    throw task.getException();
//                }
//                List<Document> docs = new ArrayList<>();
//                return coll
//                        .find(new Document("owner_id", client.getAuth().getUser().getId()))
//                        .limit(100)
//                        .into(docs);
//            }
//        }).addOnCompleteListener(new OnCompleteListener<List<Document>>() {
//            @Override
//            public void onComplete(@NonNull Task<List<Document>> task) {
//                if (task.isSuccessful()) {
//                    Log.d("STITCH", "Found docs: " + task.getResult().toString());
//                    return;
//                }
//                Log.e("STITCH", "Error: " + task.getException().toString());
//                task.getException().printStackTrace();
//            }
//        });
    
}
