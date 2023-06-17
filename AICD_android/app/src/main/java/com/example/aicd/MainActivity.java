package com.example.aicd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.aicd.databinding.ActivityMainBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private StorageReference imageReference = FirebaseStorage.getInstance().getReference();
    private Uri currentFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCapture.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            imageLauncher.launch(intent);
        });

        binding.btnSend.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            uploadImageToStorage("image");
            startActivity(intent);
        });
    }

    private final ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        currentFile = uri;
                        Glide.with(this)
                                .load(uri)
                                .transform(new CenterCrop(), new RoundedCorners(16))  // 이미지를 원하는 형태로 변형 (여기서는 CenterCrop과 RoundedCorners 사용)
                                .into(binding.btnCapture);
                        binding.btnCapture.setImageURI(uri);
                    }
                } else {
                    Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void uploadImageToStorage(String filename) {
        try {
            if (currentFile != null) {
                imageReference.child("images/" + filename).putFile(currentFile)
                        .addOnCompleteListener(task -> Toast.makeText(this, "Success upload", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Error on upload", Toast.LENGTH_SHORT).show());
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
