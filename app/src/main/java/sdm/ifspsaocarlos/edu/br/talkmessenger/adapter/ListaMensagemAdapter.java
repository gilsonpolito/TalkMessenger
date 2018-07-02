package sdm.ifspsaocarlos.edu.br.talkmessenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import sdm.ifspsaocarlos.edu.br.talkmessenger.R;
import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Mensagem;

public class ListaMensagemAdapter extends ArrayAdapter<Mensagem> {

    private final int layoutResourceId;
    private final Context context;
    private final List<Mensagem> mensagems;
    private String id;

    public ListaMensagemAdapter(@NonNull Context context, int resource, @NonNull List<Mensagem> mensagens) {
        super(context, resource, mensagens);
        this.layoutResourceId = resource;
        this.context = context;
        this.mensagems = mensagens;
        if (this.mensagems.size() > 0) {
            id = mensagems.get(0).getOrigemId();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LerOrigemDestinoHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new LerOrigemDestinoHolder();
            holder.mensagemTV = (TextView) row.findViewById(R.id.tv_mensagem);

            row.setTag(holder);
        } else {
            holder = (LerOrigemDestinoHolder) row.getTag();
        }

        Mensagem msg = mensagems.get(position);
        if (msg.getOrigemId().equals(id)) {
            holder.mensagemTV.setGravity(Gravity.LEFT);
            holder.mensagemTV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        } else {
            holder.mensagemTV.setGravity(Gravity.RIGHT);
            holder.mensagemTV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        holder.mensagemTV.setText(msg.getCorpo());

        return row;
    }

    static class LerOrigemDestinoHolder {
        TextView mensagemTV;
    }

}