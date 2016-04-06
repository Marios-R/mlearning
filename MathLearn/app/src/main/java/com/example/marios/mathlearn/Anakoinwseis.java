package com.example.marios.mathlearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Anakoinwseis extends AppCompatActivity {

    String[] titloi;
    String[] anakoinwseis;
    ListView listViewAn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anakoinwseis);

        titloi = new String[15];
        titloi[0]="27 Μαρ 2016";
        titloi[1]="1 Μαρ 2016";
        titloi[2]="27 Φεβ 2016";
        titloi[3]="23 Φεβ 2016";
        titloi[4]="22 Φεβ 2016";
        titloi[5]="17 Φεβ 2016";
        titloi[6]="16 Φεβ 2016";
        titloi[7]="5 Φεβ 2016";
        titloi[8]="4 Φεβ 2016";
        titloi[9]="4 Φεβ 2016";
        titloi[10]="2 Φεβ 2016";
        titloi[11]="28 Ιαν 2016";
        titloi[12]="17 Ιαν 2016";
        titloi[13]="17 Ιαν 2016";
        titloi[14]="ΕΠΙΣΤΡΟΦΗ";

        anakoinwseis = new String[14];
        anakoinwseis[0]="Επαναληπτικό τεστ ανέβηκε στην ενότητα των ασκήσεων. Πατήστε στο δεύτερο φυλλάδιο. Να το λύσετε για να μπορέσω να δω που έχετε ελλείψεις. Πρέπει να κάνετε ότι καλύτερο μπορείτε.";
        anakoinwseis[1]="Ελα να διαβάζουμε!";
        anakoinwseis[2]="Με το 10ο μάθημα στην ουσία τελειώνουμε και την παράγραφο 1.7 του βιβλίου";
        anakoinwseis[3]="Τελος και η παράγραφος 1.6 με το 9ο μάθημα!";
        anakoinwseis[4]="Το 8ο μάθημα περιλαμβάνει και το πρώτο κομμάτι από την παράγραφο 1.6.";
        anakoinwseis[5]="Με το μάθημα 7 τελειώνει η θεωρία του 1.5. Βγήκε λίγο θολό σε μερικά σημεία αλλά προσπαθήστε να το δείτε...";
        anakoinwseis[6]="Βγήκε λίγο μεγάλο το 6ο μάθημα. Να το δείτε όμως! Αύριο θα ανέβει και το υπόλοιπο για να τελειώσουμε το 1.5. Αυτό θα είναι 17 λεπτά μη τρελαίνεστε!!";
        anakoinwseis[7]="Με το 5ο μάθημα τελειώνουμε την παράγραφο 1.4!";
        anakoinwseis[8]="Με το 4ο μάθημα τελειώνουμε την παράγραφο 1.3!";
        anakoinwseis[9]="Με το 3ο μάθημα τελειώνει και η θεωρία του 1.2!";
        anakoinwseis[10]="Ανέβηκε το 2ο μάθημα! Βασισμένο πάνω στη παράγραφο 1.2 είναι και περιλαμβάνει τον ορισμό πραγματικής συνάρτησης, πεδίο ορισμού και γραφικές παραστάσεις. Έχει παραδείγματα για το πως βρίσκεις το πεδίο ορισμού με βάση τον τύπο μιας συνάρτησης (αν δεν σου δίνουν το πεδίο ορισμού της) και μερικά βασικά παραδείγματα γραφικών παραστάσεων για να καταλάβετε την λογική με την οποία σχεδιάζονται. Στο τέλος έχει μερικές υπενθυμίσεις σε ιδιότητες των λογαρίθμων. Το επόμενο βίντεο θα ξεκινήσει με τη γραφική παράσταση της lnx και θα ολοκληρώνει τη θεωρία από το 1.2.";
        anakoinwseis[11]="Ανέβηκε το 1ο μάθημα! Δείτε το και προσπαθήστε να κάνετε την άσκηση 3 και την άσκηση 2 από το βιβλίο και τις ασκήσεις σε διαστήματα από το pdf το δικό μου που έχω ανεβάσει στην ενότητα των ασκήσεων (πρώτο φυλλάδιο). Άντε να διαβάζουμε!";
        anakoinwseis[12]="Ανέβηκε η πρώτη φυλλάδα ασκήσεων! Πηγαίντε στην ενότητα των ασκήσεων και πατήστε στο πρώτο φυλλάδιο.";
        anakoinwseis[13]="Καλώς ήλθατε στην υπέροχη σούπερ γουάου τάξη του κύριου Ράπτη! Παρακαλούμε να τον σέβεστε και να τον ακούτε με προσοχή! lol";

        listViewAn=(ListView) findViewById(R.id.anakoinwseis);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titloi );
        listViewAn.setAdapter(adapter);

        listViewAn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (((TextView)view).getText().toString().equals("ΕΠΙΣΤΡΟΦΗ")){
                    Intent intent = new Intent(Anakoinwseis.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(Anakoinwseis.this, ProvolhAnakoinwshs.class);
                    intent.putExtra("str2", anakoinwseis[position]);
                    startActivity(intent);
                }
            }
        });
    }
}
