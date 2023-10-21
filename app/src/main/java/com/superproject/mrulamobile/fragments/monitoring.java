package com.superproject.mrulamobile.fragments;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.superproject.mrulamobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link monitoring#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class monitoring extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView text_upper_arm;
    private TextView text_lower_arm;
    private TextView text_neck;
    private TextView text_trunk;
    private TextView text_wrist;
    private View rootview;
    private TextView text_score_mrula;
    public ConnectivityManager connectivityManager;
    public NetworkInfo networkInfo;
    public FirebaseFirestore db;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment monitoring.
     */
    // TODO: Rename and change types and number of parameters
    public static monitoring newInstance(String param1, String param2) {
        monitoring fragment = new monitoring();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public monitoring() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String mac=getArguments().getString("key");
        //Toast.makeText(this.getContext(), mac,Toast.LENGTH_SHORT).show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query last_query= db.collection(mac).orderBy("date", Query.Direction.DESCENDING).limit(1);
        last_query.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()) {
                    Toast.makeText(getContext(), mac,Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(), value.getDocuments().get(0).get("upper_arm").toString(),Toast.LENGTH_SHORT).show();
                    text_upper_arm=rootview.findViewById(R.id.upper_arm);
                    text_upper_arm.setText(value.getDocuments().get(0).get("upper_arm").toString());

                    text_lower_arm=rootview.findViewById(R.id.lower_arm);
                    text_lower_arm.setText(value.getDocuments().get(0).get("lower_arm").toString());

                    text_neck=rootview.findViewById(R.id.neck);
                    text_neck.setText(value.getDocuments().get(0).get("neck").toString());

                    text_trunk=rootview.findViewById(R.id.trunk);
                    text_trunk.setText(value.getDocuments().get(0).get("trunk").toString());

                    text_wrist=rootview.findViewById(R.id.wrist);
                    text_wrist.setText(value.getDocuments().get(0).get("wrists").toString());

                    text_score_mrula=rootview.findViewById(R.id.score_mrula);
                    String score_mrula=value.getDocuments().get(0).get("score_mrula").toString();
                    Integer score_mrula2=Integer.valueOf(value.getDocuments().get(0).get("score_mrula").toString());
                    text_score_mrula.setText(score_mrula);
                    int duration = 1000; // duração do som em milissegundos
                    int delay = 0; // atraso entre cada ciclo em milissegundos
                    if (score_mrula2 >= 5) {
                        int numCycles = 4; // número de ciclos a serem repetidos
                        if(score_mrula2<7){
                            numCycles=2;
                        }else{
                            numCycles=4;
                        }
                        int finalNumCycles = numCycles;
                        new Thread(new Runnable() {
                            public void run() {
                                ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                                for (int i = 0; i < finalNumCycles; i++) {
                                    toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000);
                                    try {
                                        Thread.sleep(duration); // aguarda a duração do som
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    toneGenerator.stopTone(); // para o som
                                    try {
                                        Thread.sleep(delay); // adiciona um pequeno atraso
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                toneGenerator.release(); // libera o objeto ToneGenerator
                            }
                        }).start();
                    }


                }
            }
        });

        // Inflate the layout for this fragment

        rootview=inflater.inflate(R.layout.fragment_monitoring, container, false);
        return rootview;
    }
}