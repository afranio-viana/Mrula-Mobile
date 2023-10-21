package com.superproject.mrulamobile;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class RegisterUser extends AppCompatActivity {
    User user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        String imei=getImeiPhone();
        DocumentReference docRef = db.collection("users").document(imei);
        user= new User();
    }

    private void addUser(String imeiUser, String mac){
        user.setUserImei(imeiUser);
        user.setMac(mac);
        db.collection("users").document(imeiUser).set(user);
        //Toast.makeText(RegisterUser.this, user.getUserImei(),Toast.LENGTH_SHORT).show();
        finish();
    }

    public String getImeiPhone(){
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }
    public void scanner(View view){
        scanner = findViewById(R.id.button_qr_code);
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Digitalize o QR Code");
        intentIntegrator.setOrientationLocked(false);
        List<String> acceptedTypes = new ArrayList<String>();
        acceptedTypes.add("QR_CODE");
        intentIntegrator.initiateScan(acceptedTypes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String deviceId = getImeiPhone();
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Operação cancelada", Toast.LENGTH_SHORT).show();
            } else {
                String mac = intentResult.getContents();

                String patternMac = "^([0-9a-fA-F]{2}[:-]){5}[0-9a-fA-F]{2}$";
                boolean isMac = mac.matches(patternMac);

                if (isMac) {
                    addUser(deviceId, mac);
                    //Toast.makeText(RegisterUser.this, deviceId,Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(RegisterUser.this, "Formato inválido de MAC",Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode = Integer.parseInt(null), resultCode = Integer.parseInt(null), data);
        }
    }

    @Override
    public void onBackPressed() {
        // não chame o super desse método
    }

}