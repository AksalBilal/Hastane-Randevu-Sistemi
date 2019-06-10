package com.mehmetaydin.hastanerandevusistemi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;
import com.mehmetaydin.hastanerandevusistemi.Model.Randevu;

import java.util.ArrayList;

public class CustomAdapterRandevu extends ArrayAdapter<String> {
    ArrayList<Randevu> randevuList = new ArrayList<>();
    ArrayList<String> randevular = new ArrayList<String>();
    Context context;
    CustomAdapterRandevu(Context c, ArrayList<String> a, ArrayList<Randevu> k){
        super(c,R.layout.randevu_tek_satir,R.id.txtRandevuBilgi,a);
        context=c;
        randevular=a;
        randevuList=k;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ınflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tek_satir = ınflater.inflate(R.layout.randevu_tek_satir,parent,false);

        TextView randevuBilgi = tek_satir.findViewById(R.id.txtRandevuBilgi);

        randevuBilgi.setText(randevular.get(position));

        return super.getView(position, convertView, parent);
    }
}
