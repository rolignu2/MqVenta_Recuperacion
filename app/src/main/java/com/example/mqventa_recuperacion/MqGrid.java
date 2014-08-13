package com.example.mqventa_recuperacion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rolandoantonio on 08-12-14.
 */
public class MqGrid extends BaseAdapter {

    private Context context;
    private List<String> Cadenas;
    private int[] Images;


    public MqGrid(Context contexto , List<String> cadenas , int[] images )
    {
        this.context = contexto;
        this.Cadenas = cadenas;
        this.Images = images;
    }

    @Override
    public int getCount() {
        return this.Cadenas.size();
    }

    @Override
    public Object getItem(int i) {
        return this.Cadenas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            grid = new View(this.context);
            grid = inflater.inflate(R.layout.grid_image, null);
            TextView textView = (TextView) grid.findViewById(R.id.txt_data_grid);
            ImageView imageView = (ImageView)grid.findViewById(R.id.image_view_grid);
            textView.setText(this.Cadenas.get(i));
            imageView.setImageResource(this.Images[i]);
        } else {
            grid = (View)view;
        }
        return grid;
    }
}
