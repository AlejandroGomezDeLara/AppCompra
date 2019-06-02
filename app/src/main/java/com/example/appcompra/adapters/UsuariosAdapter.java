package com.example.appcompra.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.appcompra.R;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.Usuario;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.ViewHolder> {
    private List<Usuario> usuarios;
    private Activity activity;
    private int layout;
    private Context context;
    private Spinner rolesSpinner;
    private OnItemClickListener listener;

    public UsuariosAdapter(List<Usuario> usuarios, Activity activity, int layout, Context context,OnItemClickListener listener) {
        this.usuarios = usuarios;
        this.listener=listener;
        this.activity = activity;
        this.layout = layout;
        this.context = context;
    }

    public UsuariosAdapter() {
    }

    private Context getContext(){return context;}
    @NonNull
    @Override
    public UsuariosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final UsuariosAdapter.ViewHolder viewHolder,final int i) {
        final Usuario usuario=usuarios.get(i);
        viewHolder.nombre.setText(usuario.getNombre());
        rolesSpinner=viewHolder.roles;
        updateSpinnerRoles(Singleton.getInstance().getRoles());
        switch (usuario.getRol().toLowerCase()){
            case "administrador":
                viewHolder.linearUsuario.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_lista_admin));
                break;
            case "participante":
                viewHolder.linearUsuario.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_lista_participante));

                break;
            case "espectador":
                viewHolder.linearUsuario.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_lista_espectador));
                break;
        }
        viewHolder.borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBorrarUsuario(usuario);
            }
        });
    }


    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        Spinner roles;
        ImageView borrar;
        LinearLayout linearUsuario;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.nombre_usuario);
            roles=itemView.findViewById(R.id.roles);
            borrar=itemView.findViewById(R.id.borrar);
            linearUsuario=itemView.findViewById(R.id.linearUsuario);
        }
    }
    private void updateSpinnerRoles(ArrayList<String> roles) {

        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                valoresSpinner
        );
        */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),R.layout.spinner_item_usuarios,roles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rolesSpinner.setAdapter(adapter);
    }
    public interface OnItemClickListener{
        void onBorrarUsuario(Usuario u);
    }
}
