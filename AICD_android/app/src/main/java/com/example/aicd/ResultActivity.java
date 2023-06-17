package com.example.aicd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ResultActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private TextView textView;
    private TextView tx_itemname;
    private TextView tx_partname;
    private TextView fr_drugclass;
    private TextView fr_frontsign;
    private TextView fr_ed;
    private TextView fr_save;
    private TextView fr_shape;
    private TextView fr_companyname;
    private ImageView imageView;
    private ImageView btn_nb;
    private ImageView btn_pdf;
    private ImageView btn_usage;
    String Url;
    private StorageReference storageReference;

    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tx_itemname = findViewById(R.id.tx_itemname);
        tx_partname = findViewById(R.id.tx_partname);
        fr_drugclass = findViewById(R.id.fr_drugclass);
        fr_frontsign = findViewById(R.id.fr_frontsign);
        fr_ed = findViewById(R.id.fr_ed);
        fr_save = findViewById(R.id.fr_save);
        fr_shape = findViewById(R.id.fr_shape);
        fr_companyname = findViewById(R.id.fr_companyname);
        btn_nb = findViewById(R.id.btn_nb);



        // Firebase 실시간 데이터베이스의 "data" 경로에 대한 DatabaseReference 가져오기
        databaseReference = FirebaseDatabase.getInstance().getReference("index");

        // ValueEventListener를 사용하여 데이터 변경을 감지
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // "data" 경로의 데이터가 변경되었을 때 호출되는 메서드
                    // String 값으로 데이터 읽기
                    String value = dataSnapshot.getValue(String.class);
                    if (value != null) {
                        // 읽은 데이터를 텍스트뷰에 설정

                        //textView.setText(value);
                        readData(value);

                        temp = value;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 데이터 읽기가 취소되었을 때 호출되는 메서드
                Log.e("FirebaseData", "Failed to read value.", databaseError.toException());
            }
        });

        btn_pdf = findViewById(R.id.btn_pdf);
        btn_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadBtn(temp);
            }
        });

        btn_nb = findViewById(R.id.btn_nb);
        btn_nb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadBtn(temp);
            }
        });

        btn_usage = findViewById(R.id.btn_usage);
        btn_usage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadBtn(temp);
            }
        });
    }
    private void  readData(String Value){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("pill_data");

        collectionRef.document(Value).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String itemname = documentSnapshot.getString("itemname");
                    String partname = documentSnapshot.getString("partname");
                    String drugclass = documentSnapshot.getString("drugclass");
                    String frontsign = documentSnapshot.getString("frontsign");
                    String ed = documentSnapshot.getString("ed");
                    String save = documentSnapshot.getString("save");
                    String shape = documentSnapshot.getString("shape");
                    String companyname = documentSnapshot.getString("companyname");

                    // 데이터를 원하는 TextView에 표시합니다.
                    tx_itemname.setText(itemname);
                    tx_partname.setText(partname);
                    fr_drugclass.setText(drugclass);
                    fr_frontsign.setText(frontsign);
                    fr_ed.setText(ed);
                    fr_save.setText(save);
                    fr_shape.setText(shape);
                    fr_companyname.setText(companyname);


                } else {
                    // 문서가 존재하지 않는 경우 처리할 작업을 여기에 작성합니다.
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // 데이터를 가져오는 도중 오류가 발생했을 때 처리할 작업을 여기에 작성합니다.
            }
        });



        imageView = findViewById(R.id.imageView);
        storageReference = FirebaseStorage.getInstance().getReference();

        // 해당 경로의 이미지를 다운로드하여 ImageView에 표시
        StorageReference imageRef = storageReference.child("pill").child(Value + ".jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ResultActivity.this)
                        .load(uri)
                        .into(imageView);
            }
        });
    }

    private void LoadBtn(String temp){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("pill_data");

        collectionRef.document(temp).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // String 값으로 데이터 읽기
                    String value = documentSnapshot.getString(temp);
                    if (value != null) {
                        // 읽은 데이터를 텍스트뷰에 설정
                        Url = value;
                    }
                }
            }
        });
        // PDF 링크를 웹 브라우저에서 열기 위한 Intent 생성
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 웹 브라우저에서 PDF 링크 열기
        startActivity(intent);


    }
}
