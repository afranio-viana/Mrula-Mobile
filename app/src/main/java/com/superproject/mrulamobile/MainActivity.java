package com.superproject.mrulamobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String imei=getImeiPhone();
        DocumentReference docRef = db.collection("users").document(imei);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>(){
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshot!=null) {
                    DocumentSnapshot document = snapshot;
                    if (document.exists()) {
                        //Toast.makeText(MainActivity.this, "Existe",Toast.LENGTH_SHORT).show();
                        String mac=document.get("mac").toString();
                        Intent intent =new Intent(getApplicationContext(),MenuApplication.class);
                        intent.putExtra("key",mac);
                        startActivity(intent);
                    } else {
                        finish();
                        Intent intent=new Intent(getApplicationContext(),RegisterUser.class);
                        startActivity(intent);
                    }
                }
            }
        });
        /*db.collection("users").get().addOnSuccessListener(snapshot->{
            if(snapshot.isEmpty()){
                Toast.makeText(MainActivity.this, "Não existe",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "Existe",Toast.LENGTH_SHORT).show();
            }
        });*/
        //Toast.makeText(MainActivity.this, String.valueOf(db.collection("55:85:05:33:0F:F3").get()),Toast.LENGTH_SHORT).show();
    }
    public String getImeiPhone(){
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }
}