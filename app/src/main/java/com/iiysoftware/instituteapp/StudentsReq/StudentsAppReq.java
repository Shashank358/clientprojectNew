package com.iiysoftware.instituteapp.StudentsReq;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iiysoftware.instituteapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentsAppReq extends Fragment {

    private TextView name, notif;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUser;

    public StudentsAppReq() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_students_app_req, container, false);

        name = view.findViewById(R.id.req_stu_name);
        notif = view.findViewById(R.id.req_stu_notif);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();

        db.collection("accepted_req").document("MainData")
                .collection("Names").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String uid = document.getData().get("Uid").toString();
                        String requester = document.getData().get("name").toString();

                        if (currentUser.equals(uid)){
                            name.setText("Hello " + requester);
                            notif.setText("Your leave request has been approved");
                        }else {
                            name.setText("Hello");
                            notif.setText("No Request");
                        }
                    }
                }
            }
        });

        return view;
    }

}
