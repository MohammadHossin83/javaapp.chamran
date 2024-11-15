package com.example.app2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity{
    private static  final int REQUSEST_CODE =1;
    private TextView responseTextView;
@Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button requestbutton=findViewById(R.id.request_button);
    responseTextView =findViewById(R.id.response_Text_View);
    requestbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            requestContactPermissin();

        }
    });
}
private void requestContactPermissin() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        ==
    PackageManager.PERMISSION_GRANTED) {
    updateResponseText("این مجوز قبلا صادر شده است");
    }else {
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.READ_CONTACTS},REQUSEST_CODE);
    }
}
@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission,
                                           @NonNull int[] granResults ) {
    super.onRequestPermissionsResult(requestCode,permission,granResults);
    if (requestCode == REQUSEST_CODE) {
        if (granResults.length > 0 && granResults[0] ==
                PackageManager.PERMISSION_GRANTED){
            updateResponseText("شما برای دسترسی به مخاطبان دسترسی دادید");
        }
        else {
            updateResponseText("شما برای دسترسی به مخاطبان مجوز ندادید");
        }
    }
}

private void updateResponseText(String message)
{
    responseTextView.setText(message);
}


}