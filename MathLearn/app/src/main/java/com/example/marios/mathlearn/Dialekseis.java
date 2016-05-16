package com.example.marios.mathlearn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Dialekseis extends MainBase implements Strategy{

    private final static String URL="http://mlearning-projectmr.rhcloud.com/videos.php";
    private Video[] videos;
    private int[] idarray;
    private ListView listView;
    private DataBaseHelper dbHelper;
    private String ids;
    //private String

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //url = String.format("http://mlearning-projectmr.rhcloud.com/videos.php");
        strategy=this;
        listView=(ListView) findViewById(R.id.listView);
        dbHelper = new DataBaseHelper(this);
        populateList();
    }

    @Override
    public void syncedDB(String s) {
        try{
            JSONObject topLevel = new JSONObject(s);
            JSONArray vidjson = (JSONArray) topLevel.get("videos");
            String nonexistentvideos = (String) topLevel.get("nonexistentvideos");
            if (vidjson.length()!=0) {
                for (int i = 0; i < vidjson.length(); i++) {
                    try {
                        JSONObject oneObject = vidjson.getJSONObject(i);
                        // Pulling items from the array
                        int id = oneObject.getInt("ID");
                        String code = oneObject.getString("Code");
                        String title = oneObject.getString("Title");
                        int sequence = oneObject.getInt("Sequence");
                        dbHelper.insertInVids(new Video(title, code, false, false, sequence, id));
                    } catch (JSONException e) {
                        // Oops
                    }
                }
            }
            if (!nonexistentvideos.equals("")){
                dbHelper.deleteFromVidsIn(nonexistentvideos);
            }
            if (vidjson.length()==0 && nonexistentvideos.equals("") ){
                Toast.makeText(Dialekseis.this, "Τίποτα νεότερο.", Toast.LENGTH_LONG).show();
            }else{
                populateList();
                Toast.makeText(Dialekseis.this, "Οι διαλέξεις ανανεώθηκαν!", Toast.LENGTH_LONG).show();
            }
        }catch(JSONException e){
            //No connectivity?
        }
    }

    @Override
    public void populateList() {
        videos = dbHelper.getVideos();
        VideosAdapter adapter=new VideosAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!videos[position].videoSelected){
                    dbHelper.updateSelectedinVids(videos[position].videoID);
                }
                Intent intent = new Intent(Dialekseis.this, ProvolhDialekshs.class);
                intent.putExtra("video", videos[position].videoLink);
                intent.putExtra("position",position);
                intent.putExtra("idarray",idarray);
                intent.putExtra("title",videos[position].videoTitle);
                startActivity(intent);

            }
        });
        StringBuilder sb= new StringBuilder();
        idarray = new int[videos.length];
        for (int i=0; i < videos.length; i++){
            sb.append( "'"+videos[i].videoID+"'," );
            idarray[i]=videos[i].videoID;
        }
        ids=sb.toString();
        ids = ids.substring(0, ids.length() - 1);
    }

    @Override
    public String provideIds() {
        return ids;
    }

    @Override
    public String provideURL() {
        return URL;
    }


    private class VideosAdapter extends BaseAdapter{

        private int image = R.drawable.small_success;
        private LayoutInflater inflater=null;

        @Override
        public int getCount() {
            return videos.length;
        }

        @Override
        public Object getItem(int position) {
                    return position;
                }

        @Override
        public long getItemId(int position) {
                    return position;
                }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = ( LayoutInflater )Dialekseis.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.listview, null);
            TextView tv=(TextView) rowView.findViewById(R.id.textView1);
            ImageView img = (ImageView) rowView.findViewById(R.id.imageView1);
            tv.setText(videos[position].videoTitle);
            if (videos[position].videoSelected) {
                rowView.setBackgroundColor(1426063360);
                if (videos[position].videoViewed){
                    img.setImageResource(image);
                }
            }
            return rowView;
        }

    }

}
