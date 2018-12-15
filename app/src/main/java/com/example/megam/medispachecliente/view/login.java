package com.example.megam.medispachecliente.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.megam.medispachecliente.MainActivity;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.control.Conexao;
import com.example.megam.medispachecliente.model.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText loginadress, loginpass;
    Button loginbotao, novocad;
    TextView resetsenha;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        inicializarcomponentes();
        auth = Conexao.getFirebaseAuth();
        EventoClicks();


    }

    private void EventoClicks() {
        novocad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new  Intent(getApplicationContext(), usercadastro.class);
                startActivity(i);
                finish();
            }
        });

        loginbotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginadress.getText().toString().trim();
                String pass = loginpass.getText().toString().trim();

                if(!email.equals("")&&!pass.equals("")){
                    logar(email, pass);

                }else{
                    alert("preencha todos os dados");
                }

            }
        });

        resetsenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, ResetSenha.class);
                startActivity(i);
            }
        });
    }

    private void logar(final String email, final String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    alert("Conectando ao Servidor, aguarde");
                    Intent i = new  Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }else{
                    alert("Email ou senha incorretos");
                }
            }
        });


    }


    private void inicializarcomponentes() {

        novocad = findViewById(R.id.novocad);
        loginbotao = findViewById(R.id.loginbotao);
        loginadress = findViewById(R.id.loginadress);
        loginpass = findViewById(R.id.loginpass);
        resetsenha = findViewById(R.id.resetarsenha);
    }
    private void alert(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

    }
}
