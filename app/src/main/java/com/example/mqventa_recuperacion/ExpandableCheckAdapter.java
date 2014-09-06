package com.example.mqventa_recuperacion;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rolandoantonio on 09-03-14.
 */
public class ExpandableCheckAdapter  extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableCheckAdapter(Context contexto , List<String> dataHeader ,HashMap<String, List<String>> listData )
    {
            this._context = contexto;
            this._listDataHeader = dataHeader;
            this._listDataChild = listData;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this._listDataChild.get(this._listDataHeader.get(i))
                .size();
    }

    @Override
    public Object getGroup(int i) {
        return this._listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i2) {
        return this._listDataChild.get(this._listDataHeader.get(i))
                .get(i2);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup)  {

            String headerTitle = (String) getGroup(i);
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) view
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return view;
    }

    @Override
    public View getChildView(int i, final int i2, boolean b, View view, ViewGroup viewGroup) {

        final String childText = (String) getChild(i,i2);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.cheklist_group, null);
        }

        CheckBox txtListChild = (CheckBox) view
                .findViewById(R.id.checked_box_recuperacion);


        txtListChild.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }
}
