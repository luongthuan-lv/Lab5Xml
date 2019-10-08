package vn.edu.lab5xml;

import android.content.Context;
import android.print.PrinterId;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class DataAdapter extends BaseAdapter {
    private Context context;
    private List<TinTuc> tinTucList;

    public DataAdapter(Context context, List<TinTuc> tinTucList) {
        this.context = context;
        this.tinTucList = tinTucList;
    }

    @Override
    public int getCount() {
        return tinTucList.size();
    }

    @Override
    public Object getItem(int position) {
        return tinTucList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view=LayoutInflater.from(context).inflate(R.layout.tintuc,parent,false);
        TextView tvData=view.findViewById(R.id.tvData);
        TextView tvDescription=view.findViewById(R.id.tvDescription);
        TinTuc tinTuc=tinTucList.get(position);
        tvData.setText(tinTuc.title);
        tvDescription.setText(tinTuc.description);

        return view;
    }
}
