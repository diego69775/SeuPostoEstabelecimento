package com.example.seupostoestabelecimento.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.model.Produto;

import java.util.List;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder> {

    final private List<Produto> listaProdutos;

    public AdapterProduto(List<Produto> lista) {
        this.listaProdutos = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Produto produto = listaProdutos.get(position);
        holder.descricao.setText(produto.getDescricao());
        String textoValor = "R$ "+produto.getValor();
        holder.valor.setText(textoValor);
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        final TextView descricao;
        final TextView valor;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.txtDescricao);
            valor = itemView.findViewById(R.id.txtValor);

        }
    }

}
