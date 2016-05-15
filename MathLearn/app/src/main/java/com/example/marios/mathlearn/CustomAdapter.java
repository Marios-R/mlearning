package com.example.marios.mathlearn;

/**
 * Created by Marios on 4/9/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class CustomAdapter extends BaseAdapter{

    private Video[] vids;
    private Assignment[] assignmts;
    private Announcement[] announcmnts;

    Context context;
    DataBaseHelper dbHelper;
    int image = R.drawable.small_success;
    private static LayoutInflater inflater=null;
    public CustomAdapter(Context c) {
        // TODO Auto-generated constructor stub
        //vids=v;
        context=c;
        dbHelper = new DataBaseHelper(c);
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setVideos(Video[] v){
        vids=v;
    }

    public void setAssignmts(Assignment[] a){
        assignmts=a;
    }
    public void setAnnouncements(Announcement[] a){
        announcmnts=a;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        int len=0;
        if (context instanceof Dialekseis) {
            len= vids.length;
        }
        else if (context instanceof Askhseis) {
            len= assignmts.length;
        }
        else if (context instanceof Anakoinwseis){
            len= announcmnts.length;
        }
        return len;
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
        // TODO Auto-generated constructor stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.listview, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        if (context instanceof Dialekseis) {
            holder.tv.setText(vids[position].videoTitle);
            if (vids[position].videoSelected) {
                rowView.setBackgroundColor(1426063360);
                if (vids[position].videoViewed){
                    holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
                    holder.img.setImageResource(image);
                }
            }
        }
        else if (context instanceof Askhseis){
            holder.tv.setText(assignmts[position].assignTitle);
            if (assignmts[position].assignSelected){
                rowView.setBackgroundColor(1426063360);
            }
        }
        else if (context instanceof Anakoinwseis){
            holder.tv.setText(announcmnts[position].annDate);
            if (announcmnts[position].annSelected){
                rowView.setBackgroundColor(1426063360);
            }
        }

        return rowView;
    }

}
