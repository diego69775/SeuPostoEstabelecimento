package com.example.seupostoestabelecimento.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.model.Estabelecimento;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterPosto extends RecyclerView.Adapter<AdapterPosto.MyViewHolder>{

    final private List<Estabelecimento> listaPosto;

    public AdapterPosto(List<Estabelecimento> lista) {
        this.listaPosto = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_posto, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Estabelecimento estabelecimento = listaPosto.get(position);
        double nota=0;

        if(estabelecimento.getAvaliacao() != null){
            for (int i=0; i<estabelecimento.getAvaliacao().size();i++){
                nota = nota + estabelecimento.getAvaliacao().get(i).getNota();
            }
            nota = nota / estabelecimento.getAvaliacao().size();
        }

        if(estabelecimento.getProduto() != null){
            holder.textEstabelecimento.setText(estabelecimento.getNomeFantasia());
            holder.textTelefone.setText(estabelecimento.getTelefone());
            String textoEndereco = estabelecimento.getRua()+" "+estabelecimento.getNumero()+" "+estabelecimento.getBairro();
            holder.textEndereco.setText(textoEndereco);
            String textoAvaliacao = "Nota: "+nota;
            holder.textAvaliacao.setText(textoAvaliacao);
            Locale ptBr = new Locale("pt", "BR");

            for (int i=0; i<estabelecimento.getProduto().size();i++){
                if(i==0){
                    holder.textProd1.setVisibility(View.VISIBLE);
                    String textoProd1 = estabelecimento.getProduto().get(i).getDescricao()+" ";
                    textoProd1 = textoProd1 + NumberFormat.getCurrencyInstance(ptBr).format(estabelecimento.getProduto().get(i).getValor());
                    holder.textProd1.setText(textoProd1);
                }
                if(i==1){
                    holder.textProd2.setVisibility(View.VISIBLE);
                    String textoProd2 = estabelecimento.getProduto().get(i).getDescricao()+" ";
                    textoProd2 = textoProd2 + NumberFormat.getCurrencyInstance(ptBr).format(estabelecimento.getProduto().get(i).getValor());
                    holder.textProd2.setText(textoProd2);
                }
                if(i==2){
                    holder.textProd3.setVisibility(View.VISIBLE);
                    String textoProd3 = estabelecimento.getProduto().get(i).getDescricao()+" ";
                    textoProd3 = textoProd3 + NumberFormat.getCurrencyInstance(ptBr).format(estabelecimento.getProduto().get(i).getValor());
                    holder.textProd3.setText(textoProd3);
                }
                if(i==3){
                    holder.textProd4.setVisibility(View.VISIBLE);
                    String textoProd4 = estabelecimento.getProduto().get(i).getDescricao()+" ";
                    textoProd4 = textoProd4 + NumberFormat.getCurrencyInstance(ptBr).format(estabelecimento.getProduto().get(i).getValor());
                    holder.textProd4.setText(textoProd4);
                }
                if(i==4){
                    holder.textProd5.setVisibility(View.VISIBLE);
                    String textoProd5 = estabelecimento.getProduto().get(i).getDescricao()+" ";
                    textoProd5 = textoProd5 + NumberFormat.getCurrencyInstance(ptBr).format(estabelecimento.getProduto().get(i).getValor());
                    holder.textProd5.setText(textoProd5);
                }
                if(i==5){
                    holder.textProd6.setVisibility(View.VISIBLE);
                    String textoProd6 = estabelecimento.getProduto().get(i).getDescricao()+" ";
                    textoProd6 = textoProd6 + NumberFormat.getCurrencyInstance(ptBr).format(estabelecimento.getProduto().get(i).getValor());
                    holder.textProd6.setText(textoProd6);
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return listaPosto.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        final TextView textEstabelecimento;
        final TextView textTelefone;
        final TextView textEndereco;
        final TextView textAvaliacao;
        final TextView textProd1;
        final TextView textProd2;
        final TextView textProd3;
        final TextView textProd4;
        final TextView textProd5;
        final TextView textProd6;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textEstabelecimento = itemView.findViewById(R.id.txtEstabelecimento);
            textTelefone = itemView.findViewById(R.id.txtTelefone);
            textEndereco = itemView.findViewById(R.id.txtEndereco);
            textAvaliacao = itemView.findViewById(R.id.txtAvaliacao);
            textProd1 = itemView.findViewById(R.id.txtProd1);
            textProd2 = itemView.findViewById(R.id.txtProd2);
            textProd3 = itemView.findViewById(R.id.txtProd3);
            textProd4 = itemView.findViewById(R.id.txtProd4);
            textProd5 = itemView.findViewById(R.id.txtProd5);
            textProd6 = itemView.findViewById(R.id.txtProd6);

            textProd1.setVisibility(View.GONE);
            textProd2.setVisibility(View.GONE);
            textProd3.setVisibility(View.GONE);
            textProd4.setVisibility(View.GONE);
            textProd5.setVisibility(View.GONE);
            textProd6.setVisibility(View.GONE);

        }
    }

}
