package com.superproject.mrulamobile.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.superproject.mrulamobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link reports#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reports extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootview;
    private GraphView graphView;
    private GraphView graphViewLowerArm;
    private GraphView graphViewNeck;
    private GraphView graphViewTrunk;
    private GraphView graphViewWrist;
    private GraphView graphViewScore;

    public reports() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment reports.
     */
    // TODO: Rename and change types and number of parameters
    public static reports newInstance(String param1, String param2) {
        reports fragment = new reports();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        rootview=inflater.inflate(R.layout.fragment_reports, container, false);
        graphView=rootview.findViewById(R.id.idGraphViewUpperArm);
        graphViewLowerArm=rootview.findViewById(R.id.idGraphViewLowerArm);
        graphViewNeck=rootview.findViewById(R.id.idGraphViewNeck);
        graphViewTrunk=rootview.findViewById(R.id.idGraphViewTrunk);
        graphViewWrist=rootview.findViewById(R.id.idGraphViewWrist);
        graphViewScore=rootview.findViewById(R.id.idGraphViewScore);
        String mac=getArguments().getString("key");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection(mac);
        Query query = collectionRef.limit(100);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int i = 0;
                //upper_arm
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                double maxY=-1000;
                double minY=1000;


                //lower_arm
                LineGraphSeries<DataPoint> seriesLoweArm= new LineGraphSeries<>();
                double maxyLowerArm=-1000;
                double minYLowerArm=1000;

                //Neck
                LineGraphSeries<DataPoint> seriesNeck= new LineGraphSeries<>();
                double maxyNeck=-1000;
                double minYNeck=1000;

                //Trunk
                LineGraphSeries<DataPoint> seriesTrunk= new LineGraphSeries<>();
                double maxyTrunk=-1000;
                double minYTrunk=1000;

                //Wrist
                LineGraphSeries<DataPoint> seriesWrist= new LineGraphSeries<>();
                double maxyWrist=-1000;
                double minYWrist=1000;

                //Score
                LineGraphSeries<DataPoint> seriesScore= new LineGraphSeries<>();
                double maxyScore=-1000;
                double minYScore=1000;

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Obtenha os valores necessários dos documentos
                    double y = documentSnapshot.getDouble("upper_arm");
                    double y_lower_arm=documentSnapshot.getDouble("lower_arm");
                    double y_neck=documentSnapshot.getDouble("neck");
                    double y_trunk=documentSnapshot.getDouble("trunk");
                    double y_wrist=documentSnapshot.getDouble("wrists");
                    double y_score=documentSnapshot.getDouble("score_mrula");
                    // Use o contador como o eixo x
                    double x = i;

                    // Incrementa o contador
                    i++;

                    // Crie um novo LineGraphView para este conjunto de dados

                    //Upper_arm
                    series.appendData(new DataPoint(x, y), true, 100);
                    if(maxY<y){
                        maxY=y;
                    }
                    if(minY>y){
                        minY=y;
                    }

                    //lower_arm
                    seriesLoweArm.appendData(new DataPoint(x, y_lower_arm), true, 100);
                    if(maxyLowerArm<y_lower_arm){
                        maxyLowerArm=y_lower_arm;
                    }
                    if(minYLowerArm>y_lower_arm){
                        minYLowerArm=y_lower_arm;
                    }

                    //Neck
                    seriesNeck.appendData(new DataPoint(x, y_neck), true, 100);
                    if(maxyNeck<y_neck){
                        maxyNeck=y_neck;
                    }
                    if(minYNeck>y_neck){
                        minYNeck=y_neck;
                    }

                    //Trunk
                    seriesTrunk.appendData(new DataPoint(x, y_trunk), true, 100);
                    if(maxyTrunk<y_trunk){
                        maxyTrunk=y_trunk;
                    }
                    if(minYTrunk>y_trunk){
                        minYTrunk=y_trunk;
                    }

                    //Wrist
                    seriesWrist.appendData(new DataPoint(x, y_wrist), true, 100);
                    if(maxyWrist<y_wrist){
                        maxyWrist=y_wrist;
                    }
                    if(minYWrist>y_wrist){
                        minYWrist=y_wrist;
                    }

                    //Score
                    seriesScore.appendData(new DataPoint(x, y_score), true, 100);
                    if(maxyScore<y_score){
                        maxyScore=y_score;
                    }
                    if(minYScore>y_score){
                        minYScore=y_score;
                    }

                }

                //Upper_arm
                graphView.addSeries(series);
                graphView.getViewport().setXAxisBoundsManual(true);
                graphView.getViewport().setMinX(0);
                graphView.getViewport().setMaxX(i);
                graphView.getViewport().setYAxisBoundsManual(true);
                graphView.getViewport().setMinY(minY);
                graphView.getViewport().setMaxY(maxY);
                graphView.animate();

                //Lower_arm
                graphViewLowerArm.addSeries(seriesLoweArm);
                graphViewLowerArm.getViewport().setXAxisBoundsManual(true);
                graphViewLowerArm.getViewport().setMinX(0);
                graphViewLowerArm.getViewport().setMaxX(i);
                graphViewLowerArm.getViewport().setYAxisBoundsManual(true);
                graphViewLowerArm.getViewport().setMinY(minYLowerArm);
                graphViewLowerArm.getViewport().setMaxY(maxyLowerArm);
                graphViewLowerArm.animate();

                //Lower_arm
                graphViewNeck.addSeries(seriesNeck);
                graphViewNeck.getViewport().setXAxisBoundsManual(true);
                graphViewNeck.getViewport().setMinX(0);
                graphViewNeck.getViewport().setMaxX(i);
                graphViewNeck.getViewport().setYAxisBoundsManual(true);
                graphViewNeck.getViewport().setMinY(minYNeck);
                graphViewNeck.getViewport().setMaxY(maxyNeck);
                graphViewNeck.animate();

                //Trunk
                graphViewTrunk.addSeries(seriesTrunk);
                graphViewTrunk.getViewport().setXAxisBoundsManual(true);
                graphViewTrunk.getViewport().setMinX(0);
                graphViewTrunk.getViewport().setMaxX(i);
                graphViewTrunk.getViewport().setYAxisBoundsManual(true);
                graphViewTrunk.getViewport().setMinY(minYTrunk);
                graphViewTrunk.getViewport().setMaxY(maxyTrunk);
                graphViewTrunk.animate();

                //Wrist
                graphViewWrist.addSeries(seriesWrist);
                graphViewWrist.getViewport().setXAxisBoundsManual(true);
                graphViewWrist.getViewport().setMinX(0);
                graphViewWrist.getViewport().setMaxX(i);
                graphViewWrist.getViewport().setYAxisBoundsManual(true);
                graphViewWrist.getViewport().setMinY(minYWrist);
                graphViewWrist.getViewport().setMaxY(maxyWrist);
                graphViewWrist.animate();

                //Score
                graphViewScore.addSeries(seriesScore);
                graphViewScore.getViewport().setXAxisBoundsManual(true);
                graphViewScore.getViewport().setMinX(0);
                graphViewScore.getViewport().setMaxX(i);
                graphViewScore.getViewport().setYAxisBoundsManual(true);
                graphViewScore.getViewport().setMinY(minYScore);
                graphViewScore.getViewport().setMaxY(maxyScore);
                graphViewScore.animate();
            }
        });
        // Inflate the layout for this fragment

        return rootview;
    }
}