package com.example.megam.medispachecliente.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.megam.medispachecliente.MainActivity;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.model.Produtos;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class Cadastrar_Produto extends AppCompatActivity {
ImageButton botao_cadastro;

EditText valor_cadastro, quantidade_cadastro, nome_cadastro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar__produto);
        inicializarcomponentes();
        eventobotao();
    }

    private void eventobotao() {
        botao_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nome_cadastro.getText().toString().trim();
                String valor = valor_cadastro.getText().toString().trim();

                String quantidade = quantidade_cadastro.getText().toString().trim();
                String id = UUID.randomUUID().toString();
                adicionarprodutos(nome, valor, quantidade, id);


            }
        });
    }

    private void adicionarprodutos(String nome, String valor, String quantidade, String id) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Produtos p = new Produtos();
        p.setNome(nome);
        p.setValor(valor);
        p.setQuantidade(quantidade);
        p.setId(id);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProdutosEmpresa").child(user.getUid()).child(id);
        reference.setValue(p);
        Toast.makeText(getApplicationContext(), "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private void inicializarcomponentes() {
        botao_cadastro = findViewById(R.id.botao_cadastro);
        valor_cadastro = findViewById(R.id.valor_cadastro);
        quantidade_cadastro = findViewById(R.id.quantidade_cadastro);
        nome_cadastro = findViewById(R.id.nome_cadastro);
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN");
        SimpleMaskFormatter smf2 = new SimpleMaskFormatter("NN.NN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(quantidade_cadastro, smf);
        MaskTextWatcher maskTextWatcher2 = new MaskTextWatcher(valor_cadastro, smf2);
        quantidade_cadastro.addTextChangedListener(maskTextWatcher);
        valor_cadastro.addTextChangedListener(maskTextWatcher2);




    }
}
