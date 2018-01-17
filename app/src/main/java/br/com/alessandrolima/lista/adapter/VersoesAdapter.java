package br.com.alessandrolima.lista.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.alessandrolima.lista.R;
import br.com.alessandrolima.lista.model.Android;

/**
 * Created by ALUNO on 17/01/2018.
 */

public class VersoesAdapter extends RecyclerView.Adapter<VersoesAdapter.AndroidViewHolder>{

    private List<Android> androids;
    private Context context;

    public VersoesAdapter(Context context, List<Android> androids){
        this.androids = androids;
        this.context = context;
    }

    @Override
    public AndroidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.android_item, parent, false);

        return new AndroidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AndroidViewHolder holder, int position) {
        Android android = androids.get(position);
        holder.tvVersao.setText(android.getVersao());
        holder.tvNome.setText(android.getNome());
        holder.tvAPI.setText(android.getApi());

        Picasso.with(context)
                .load(android.getUrlImagem())
                .into(holder.ivLogo);
    }

    @Override
    public int getItemCount() {
        return androids.size();
    }


    protected class AndroidViewHolder extends RecyclerView.ViewHolder{

        ImageView ivLogo;
        TextView tvNome;
        TextView tvAPI;
        TextView tvVersao;

        public AndroidViewHolder(View itemView) {
            super(itemView);

            ivLogo = (ImageView) itemView.findViewById(R.id.ivLogo);
            tvNome = (TextView) itemView.findViewById(R.id.tvNome);
            tvAPI = (TextView) itemView.findViewById(R.id.tvAPI);
            tvVersao = (TextView) itemView.findViewById(R.id.tvVersao);
        }
    }


}
