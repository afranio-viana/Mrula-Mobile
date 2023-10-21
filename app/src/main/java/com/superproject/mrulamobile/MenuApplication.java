package com.superproject.mrulamobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.superproject.mrulamobile.fragments.monitoring;
import com.superproject.mrulamobile.fragments.reports;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuApplication extends AppCompatActivity {
    BottomNavigationView bottomNav;
    Fragment monitoring=new monitoring();
    Fragment reports=new reports();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_application);
        Bundle extras=getIntent().getExtras();
        String mac=extras.getString("key");
        bottomNav=findViewById(R.id.bottom_navigation);
        replacefragment(monitoring,mac);
        bottomNav.setOnItemSelectedListener(item -> {
            int id=item.getItemId();
            switch (id){
                case R.id.page_1:
                    replacefragment(monitoring,mac);
                    return true;
                case R.id.page_2:
                    replacefragment(reports,mac);
                    return true;
            }
            return true;
        });

    }
    public void replacefragment(Fragment newFragment,String mac){
        Bundle bundle=new Bundle();
        bundle.putString("key", mac);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        newFragment.setArguments(bundle);
        transaction.replace(R.id.frame_layout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public  void registerDevice(View view) {
        String imei=getImeiPhone();
        DocumentReference docRef = db.collection("users").document(imei);
        docRef.delete();
        goBack();
    }
    public String getImeiPhone(){
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }
    public void goBack() {
        Intent intent = new Intent(getApplicationContext(), RegisterUser.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}