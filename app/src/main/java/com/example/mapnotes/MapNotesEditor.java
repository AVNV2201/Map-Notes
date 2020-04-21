package com.example.mapnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

public class MapNotesEditor extends AppCompatActivity {

    int noteId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_notes_editor);

        EditText mapNoteTitleEditText = findViewById(R.id.mapNotesTitle) ;
        EditText mapNotePlaceEditText = findViewById(R.id.mapNotesLocation) ;
        EditText mapNoteContentEditText = findViewById(R.id.mapNotesContent) ;
        Button goToMaps = findViewById(R.id.goToMaps) ;

        final Intent intent = getIntent() ;
        noteId = intent.getIntExtra("noteId", -1 ) ;

        if( noteId != -1 ){
            mapNoteContentEditText.setText(mapNoteFragment.mapNotesContent.get(noteId));
            mapNotePlaceEditText.setText(mapNoteFragment.places.get(noteId));
            mapNoteTitleEditText.setText(mapNoteFragment.mapNotesTitle.get(noteId));
        } else {
            mapNoteFragment.mapNotesContent.add("") ;
            mapNoteFragment.places.add("") ;
            mapNoteFragment.mapNotesTitle.add("") ;
            mapNoteFragment.locations.add(new LatLng(0,0)) ;
            noteId = mapNoteFragment.locations.size()-1 ;
        }

        goToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MapsActivity.class) ;
                intent.putExtra("id",noteId) ;
                startActivity(intent1);
            }
        });

        mapNoteTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mapNoteFragment.mapNotesTitle.set( noteId, String.valueOf( s ) ) ;
                mapNoteFragment.arrayAdapter.notifyDataSetChanged();

                String str = null;
                try {
                    str = ObjectSerializer.serialize(mapNoteFragment.mapNotesTitle );
                    MainActivity.sharedPreferences.edit().putString( "mnTitle", str ).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mapNotePlaceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mapNoteFragment.places.set(noteId,String.valueOf(s)) ;

                try {
                    String str = ObjectSerializer.serialize(mapNoteFragment.places) ;
                    MainActivity.sharedPreferences.edit().putString("places",str).apply() ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mapNoteContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mapNoteFragment.mapNotesContent.set(noteId,String.valueOf(s)) ;

                try {
                    String str = ObjectSerializer.serialize(mapNoteFragment.mapNotesContent) ;
                    MainActivity.sharedPreferences.edit().putString("mnContent",str).apply() ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
