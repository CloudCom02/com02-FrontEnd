package com.example.com02;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DeviceListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);

        ImageButton showPopupButton = findViewById(R.id.bluetoothButton);
        showPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 팝업창 띄우기
                showPopup();
            }
        });
    }

    private void showPopup() {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_connect_check, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Popup Title")
                .setMessage("This is a simple popup message.")
                .setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
