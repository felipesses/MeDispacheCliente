package com.example.megam.medispachecliente.model;

import java.util.List;

public class Request {
    //private String nome;
    private String endereco;
    private String total;
    private List<Pedidos> OrderPedidos;// lista com os produtos pedidos
    private String status;
    private String valor_entrega;
    private String taxa_entrega;

    public Request() {

    }

    public Request(String endereco, String total, String valor_entrega, String taxa_entrega, List<Pedidos> orderPedidos) {
        //this.nome = nome;
        this.endereco = endereco;
        this.total = total;
        this.valor_entrega = valor_entrega;
        this.taxa_entrega = taxa_entrega;
        OrderPedidos = orderPedidos;
        this.status ="0"; //0: pedido, 1:entregando, 2:entregue

    }

    public String getValor_entrega() {
        return valor_entrega;
    }

    public void setValor_entrega(String valor_entrega) {
        this.valor_entrega = valor_entrega;
    }

    public String getTaxa_entrega() {
        return taxa_entrega;
    }

    public void setTaxa_entrega(String taxa_entrega) {
        this.taxa_entrega = taxa_entrega;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /*    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }*/

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Pedidos> getOrderPedidos() {
        return OrderPedidos;
    }

    public void setOrderPedidos(List<Pedidos> orderPedidos) {
        OrderPedidos = orderPedidos;
    }
}
