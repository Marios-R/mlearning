package com.example.marios.mathlearn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class Dialekseis extends AppCompatActivity {

    private Menu optionsMenu;
    private MenuItem refreshItem;
    private final static String URL="http://mlearning-projectmr.rhcloud.com/videos.php";
    private Video[] videos;
    private int[] idarray;
    private ListView listView;
    private String ids;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialekseis);
        dbHelper=new DataBaseHelper(this);
        listView=(ListView) findViewById(R.id.listView);
        populateList();
        new SyncWithRemote(new SyncVideos()).execute(URL, ids);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.refresh_menu, menu);
        refreshItem = optionsMenu.findItem(R.id.menuRefresh);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                setRefreshActionButtonState(true);
                new SyncWithRemote(new ManualSyncVideos()).execute(URL,ids);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    private class SyncVideos implements Strategy{
    @Override
    public void syncedDB(String s) {
            try {
                JSONObject topLevel = new JSONObject(s);
                JSONArray vidjson = (JSONArray) topLevel.get("videos");
                String nonexistentvideos = (String) topLevel.get("nonexistentvideos");
                if (vidjson.length() != 0) {
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
                if (!nonexistentvideos.equals("")) {
                    dbHelper.deleteFromVidsIn(nonexistentvideos);
                }
                if (vidjson.length() != 0 || !nonexistentvideos.equals("")) {
                    populateList();
                    Toast.makeText(Dialekseis.this, "Οι διαλέξεις ανανεώθηκαν!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                //No connectivity?
            }
        }
    }

    private void populateList() {
        videos = dbHelper.getVideos();
        VideosAdapter adapter=new VideosAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new VideoSelected());
        StringBuilder sb= new StringBuilder();
        idarray = new int[videos.length];
        for (int i=0; i < videos.length; i++){
            sb.append( "'"+videos[i].videoID+"'," );
            idarray[i]=videos[i].videoID;
        }
        ids=sb.toString();
        ids = ids.substring(0, ids.length() - 1);
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

    private class VideoSelected implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(!videos[position].videoSelected){
                dbHelper.updateSelectedinVids(videos[position].videoID);
            }
            Intent intent = new Intent(Dialekseis.this, ProvolhDialekshs.class);
            intent.putExtra("position",position);
            intent.putExtra("idarray", idarray);
            startActivity(intent);
        }
    }

    private class ManualSyncVideos implements Strategy{
        @Override
        public void syncedDB(String s) {
            if (s.equals("UNDEFINED")) {
                Toast.makeText(Dialekseis.this, "Αδυναμία σύνδεσης...", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject topLevel = new JSONObject(s);
                    JSONArray vidjson = (JSONArray) topLevel.get("videos");
                    String nonexistentvideos = (String) topLevel.get("nonexistentvideos");
                    if (vidjson.length() != 0) {
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
                    if (!nonexistentvideos.equals("")) {
                        dbHelper.deleteFromVidsIn(nonexistentvideos);
                    }
                    if (vidjson.length() == 0 && nonexistentvideos.equals("")) {
                        Toast.makeText(Dialekseis.this, "Τίποτα νεότερο.", Toast.LENGTH_LONG).show();
                    } else {
                        populateList();
                        Toast.makeText(Dialekseis.this, "Οι διαλέξεις ανανεώθηκαν!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    //No connectivity?
                }
            }
            setRefreshActionButtonState(false);
        }
    }

}
