package com.spil.dev.tms.Activity.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.spil.dev.tms.Activity.Model.DetailKontainer;
import com.spil.dev.tms.R;

import java.util.List;

public class ContainerAdapter extends ArrayAdapter<DetailKontainer> {
    public boolean enableViewDepo = false;

    public ContainerAdapter(@NonNull Context context, int resource, @NonNull List<DetailKontainer> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_container, parent, false);
        TextView tvNoKontainer = v.findViewById(R.id.tvNoKontainer);
        TextView tvNamaKontainer = v.findViewById(R.id.tvNamaKontainer);
        TextView tvDepo1 = v.findViewById(R.id.tvDepo1);
        TextView tvDepo2 = v.findViewById(R.id.tvDepo2);
        View vDepo = v.findViewById(R.id.vInfoDepo);

        vDepo.setVisibility(enableViewDepo ? View.VISIBLE : View.GONE);

        DetailKontainer kontainer = getItem(position);
        tvNoKontainer.setText(kontainer.getContainerNo() );
        tvNamaKontainer.setText(kontainer.getContainerName());
        tvDepo1.setText(kontainer.getPickupLocationName());
        tvDepo2.setText(kontainer.getDestinationName());
        return v;
    }
}
