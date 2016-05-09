package com.example.marios.mathlearn;

/**
 * Created by Marios on 4/9/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class CustomAdapter extends BaseAdapter{
    Video[] vids;
    Context context;
    int image = R.drawable.small_success;
    //int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomAdapter(Dialekseis dialekseis, Video[] v) {
        // TODO Auto-generated constructor stub
        vids=v;
        context=dialekseis;
        //imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return vids.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.listview, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        //holder.img=(TextView) rowView.findViewById(R.id.textView1);
        if (vids[position].videoSelected) {
            holder.tv.setBackgroundColor(1426063360);
            if (vids[position].videoViewed){
                holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
                holder.img.setImageResource(image);
            }
        }

        //holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.tv.setText(vids[position].videoTitle);
        //holder.img.setImageResource(imageId[position]);
        /*rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });*/
        return rowView;
    }

}
