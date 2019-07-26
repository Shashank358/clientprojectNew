package com.iiysoftware.instituteapp.StudentsReq;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iiysoftware.instituteapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentsSendReq extends Fragment {

    private static EditText reason, from, to;
    private TextView nameR;
    private static Button send;
    private ProgressDialog dialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    public StudentsSendReq() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_students_send_req, container, false);

        nameR=(TextView) view.findViewById(R.id.nameR);
        reason=(EditText)view.findViewById(R.id.reason);
        from = (EditText) view.findViewById(R.id.from_edittext);
        to = (EditText) view.findViewById(R.id.to_edittext);
        send=(Button)view.findViewById(R.id.send);

        dialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();

        //--fetch students data-----------
        db.collection("Students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){

                        String uid = doc.getData().get("Uid").toString();
                        if (uid.equals(mAuth.getCurrentUser().getUid())){
                            String name = doc.getData().get("Name").toString();
                            nameR.setText(name);
                        }
                    }
                }
            }
        });

        //--------choose date from-to------------------

        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("sending request");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                final String name,rea, from_date, to_date;
                name=nameR.getText().toString();
                rea=reason.getText().toString();
                from_date = from.getText().toString();
                to_date = to.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(rea) && !TextUtils.isEmpty(from_date) && !TextUtils.isEmpty(to_date))
                {
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);
                    SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss a - dd-MMM");
                    String formattedDate = df.format(c);

                    Map<String, Object> obj = new HashMap<>();
                    obj.put("name", name);
                    obj.put("reason", rea);
                    obj.put("from", from_date);
                    obj.put("to", to_date);
                    obj.put("Uid", mAuth.getCurrentUser().getUid());
                    obj.put("date", formattedDate);

                    db.collection("requests").document(formattedDate)
                            .set(obj)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("request", "DocumentSnapshot successfully written!");
                                    Toast.makeText(getContext(),"Request Send",Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();
                                    nameR.setText("");
                                    reason.setText("");
                                    from.setText("");
                                    to.setText("");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Request", "Error writing document", e);
                                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    Toast.makeText(getContext(),"Fill all the details",Toast.LENGTH_LONG).show();
                }


            }
        });

        return view;
    }

}
