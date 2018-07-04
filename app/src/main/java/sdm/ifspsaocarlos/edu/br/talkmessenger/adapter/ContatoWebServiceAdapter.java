package sdm.ifspsaocarlos.edu.br.talkmessenger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sdm.ifspsaocarlos.edu.br.talkmessenger.R;
import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Contato;

public class ContatoWebServiceAdapter extends RecyclerView.Adapter<ContatoWebServiceAdapter.ContatoViewHolder> {

    private List<Contato> contatos;
    private Context context;

    private static ItemClickListener clickListener;

    public ContatoWebServiceAdapter(List<Contato> contatos, Context context) {
        this.contatos = contatos;
        this.context = context;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    @Override
    public ContatoWebServiceAdapter.ContatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contato_celula, parent, false);
        return new ContatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContatoWebServiceAdapter.ContatoViewHolder holder, int position) {
        Contato contato = contatos.get(position);
        holder.nome.setText(contato.getNomeCompleto());
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class ContatoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nome;

        public ContatoViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tvNome);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tvNome) {
                if (clickListener != null)
                    clickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
