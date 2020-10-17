package com.example.seupostoestabelecimento.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.model.Promocao;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterPromocao extends RecyclerView.Adapter<AdapterPromocao.MyViewHolder> {

    final private List<Promocao> listaPromocao;

    public AdapterPromocao(List<Promocao> lista) {
        this.listaPromocao = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_promocao, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Locale ptBr = new Locale("pt", "BR");
        Promocao promocao = listaPromocao.get(position);
        holder.nomePromocao.setText(promocao.getNomeProduto());
        String textoValor = NumberFormat.getCurrencyInstance(ptBr).format(promocao.getValorNormal());
        holder.valor.setText(textoValor);
        String textoPromocao = NumberFormat.getCurrencyInstance(ptBr).format(promocao.getValorPromocao());
        holder.valorPromocao.setText(textoPromocao);
        String textoPromocaoData = promocao.getDataInicial()+" ~ "+promocao.getDataFinal();
        holder.dataPromocao.setText(textoPromocaoData);

    }

    @Override
    public int getItemCount() {
        return listaPromocao.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        final TextView nomePromocao;
        final TextView dataPromocao;
        final TextView valor;
        final TextView valorPromocao;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomePromocao = itemView.findViewById(R.id.txtNomePromocao);
            valor = itemView.findViewById(R.id.txtValor);
            valorPromocao = itemView.findViewById(R.id.txtValorPromocao);
            dataPromocao = itemView.findViewById(R.id.txtDataPromocao);

            valor.setPaintFlags(valor.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

}
