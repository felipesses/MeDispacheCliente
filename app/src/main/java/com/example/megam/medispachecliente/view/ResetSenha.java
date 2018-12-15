package com.example.megam.medispachecliente.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.control.Conexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetSenha extends AppCompatActivity {
EditText email;
Button reset;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_senha);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        inicializarcomponentes();
        auth = Conexao.getFirebaseAuth();
        eventoClick();
    }

    private void eventoClick() {
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kemail = email.getText().toString().trim();

                if(!kemail.equals("")){
                    resetarsenha(kemail);
                }else{
                    alert("preencha todos os dados");
                }

            }
        });

    }

    private void resetarsenha(String kemail) {
        auth.sendPasswordResetEmail(kemail).addOnCompleteListener(ResetSenha.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                alert("um link de redefinição foi enviado para seu e-mail");
                finish();
            }else{
                alert(("E-mail não encontrado"));
            }
            }
        });

    }

    private void alert(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

    }

    private void inicializarcomponentes() {
        email = findViewById(R.id.cemail);
        reset = findViewById(R.id.creset);
    }
}
