package com.example.marios.mathlearn;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Askhseis extends AppCompatActivity{

    private Menu optionsMenu;
    private MenuItem refreshItem;
    private static final String URL = String.format("http://mlearning-projectmr.rhcloud.com/assignments.php");
    private String ids;
    private Assignment[] assignments;
    private ListView listView;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anakoinwseis);
        dbHelper=new DataBaseHelper(this);
        listView=(ListView) findViewById(R.id.listView);
        populateList();
        setRefreshActionButtonState(true);
        new SyncWithRemote(new SyncAssign()).execute(URL, ids);
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
                new SyncWithRemote(new ManualSyncAssign()).execute(URL,ids);
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

    private class SyncAssign implements Strategy{
    @Override
    public void syncedDB(String s) {
            try {
                JSONObject topLevel = new JSONObject(s);
                JSONArray assignjson = (JSONArray) topLevel.get("assignments");
                String nonexistentassignments = (String) topLevel.get("nonexistentassignments");
                if (assignjson.length() != 0) {
                    for (int i = 0; i < assignjson.length(); i++) {
                        try {
                            JSONObject oneObject = assignjson.getJSONObject(i);
                            // Pulling items from the array
                            int id = oneObject.getInt("ID");
                            String title = oneObject.getString("Title");
                            String link = oneObject.getString("Link");
                            int sequence = oneObject.getInt("Sequence");
                            dbHelper.insertInAssignments(new Assignment(title, link, false, id, sequence));
                        } catch (JSONException e) {
                            // Oops
                        }
                    }
                }
                if (!nonexistentassignments.equals("")) {
                    dbHelper.deleteFromAssignments(nonexistentassignments);
                }
                if(assignjson.length() != 0 || !nonexistentassignments.equals("")) {
                    populateList();
                    Toast.makeText(Askhseis.this, "Οι ασκήσεις ανανεώθηκαν!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                //No connectivity?
            }
        }
    }

    private class ManualSyncAssign implements Strategy{
        @Override
        public void syncedDB(String s) {
            if (s.equals("UNDEFINED")) {
                Toast.makeText(Askhseis.this, "Αδυναμία σύνδεσης...", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject topLevel = new JSONObject(s);
                    JSONArray assignjson = (JSONArray) topLevel.get("assignments");
                    String nonexistentassignments = (String) topLevel.get("nonexistentassignments");
                    if (assignjson.length() != 0) {
                        for (int i = 0; i < assignjson.length(); i++) {
                            try {
                                JSONObject oneObject = assignjson.getJSONObject(i);
                                // Pulling items from the array
                                int id = oneObject.getInt("ID");
                                String title = oneObject.getString("Title");
                                String link = oneObject.getString("Link");
                                int sequence = oneObject.getInt("Sequence");
                                dbHelper.insertInAssignments(new Assignment(title, link, false, id, sequence));
                            } catch (JSONException e) {
                                // Oops
                            }
                        }
                    }
                    if (!nonexistentassignments.equals("")) {
                        dbHelper.deleteFromAssignments(nonexistentassignments);
                    }
                    if (assignjson.length() == 0 && nonexistentassignments.equals("")) {
                        Toast.makeText(Askhseis.this, "Τίποτα νεότερο.", Toast.LENGTH_LONG).show();
                    } else {
                        populateList();
                        Toast.makeText(Askhseis.this, "Οι ασκήσεις ανανεώθηκαν!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    //No connectivity?
                }
            }
            setRefreshActionButtonState(false);
        }
    }

    //@Override
    private void populateList() {
        assignments=dbHelper.getAssignments();
        AssignmentsAdapter adapter = new AssignmentsAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AssignSelected());
        StringBuilder sb= new StringBuilder();
        for (int i=0; i < assignments.length; i++){
            sb.append( "'"+assignments[i].assignID+"'," );
        }
        ids=sb.toString();
        ids = ids.substring(0, ids.length() - 1);
    }


    private class AssignmentsAdapter extends BaseAdapter{

        private LayoutInflater inflater=null;

        @Override
        public int getCount() {
            return assignments.length;
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
            inflater = ( LayoutInflater )Askhseis.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.listview, null);
            TextView tv=(TextView) rowView.findViewById(R.id.textView1);
            tv.setText(assignments[position].assignTitle);
            if (assignments[position].assignSelected){
                rowView.setBackgroundColor(1426063360);
            }
            return rowView;
        }
    }

    private class AssignSelected implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(!assignments[position].assignSelected){
                dbHelper.updateSelectedinAssignments(assignments[position].assignID);
            }
            String url = assignments[position].assignLink;
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

}
