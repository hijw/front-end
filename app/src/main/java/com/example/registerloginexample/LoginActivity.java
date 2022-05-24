package com.example.registerloginexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    private Button btn_login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        //회원가입 버튼을 클릭 시 수행
       btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

       btn_login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               // EditText에 현재 입력되어있는 값을 get 해온다.
               String userID = et_id.getText().toString();
               String userPass = et_pass.getText().toString();

               Response.Listener<String> responseListener = new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       try {
                           JSONObject jsonObject = new JSONObject(response);
                           boolean success = jsonObject.getBoolean("success");  // 서버 통신 잘 됐는지 알려주는
                           if (success) { // 로그인에 성공한 경우
                               String userID = jsonObject.getString("userID");
                               String userPass = jsonObject.getString("userPassword");
                               Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                               intent.putExtra("userID", userID);
                               intent.putExtra("userPass", userPass);
                               startActivity(intent);
                           } else { // 로그인에 실패한경우
                               Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                               return;  // 로그인으로 연결되면 안됨, 리턴
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                   }
               };


               LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
               RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
               queue.add(loginRequest);
           }
       });

    }
}