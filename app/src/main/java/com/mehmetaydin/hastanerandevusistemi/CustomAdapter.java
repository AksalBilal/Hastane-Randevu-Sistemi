package com.mehmetaydin.hastanerandevusistemi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {

    ArrayList<Kisi> doktorList = new ArrayList<>();
    ArrayList<String> favoriler = new ArrayList<String>();
    Context context;
    CustomAdapter(Context c,ArrayList<String> a,ArrayList<Kisi> k){
        super(c,R.layout.favori_tek_satir,R.id.txtFavorilerDoktorAdi,a);
        context=c;
        favoriler=a;
        doktorList=k;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ınflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tek_satir = ınflater.inflate(R.layout.favori_tek_satir,parent,false);

        TextView doktorAdi = tek_satir.findViewById(R.id.txtFavorilerDoktorAdi);

        doktorAdi.setText(doktorList.get(position).getAd()+ " " +doktorList.get(position).getSoyad());

        return super.getView(position, convertView, parent);
    }
}
