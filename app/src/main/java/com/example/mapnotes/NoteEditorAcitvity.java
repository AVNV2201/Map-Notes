package com.example.mapnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.io.IOException;

public class NoteEditorAcitvity extends AppCompatActivity {

    int noteId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor_acitvity);

        EditText noteTitleEditText = findViewById(R.id.noteTitleEditText) ;
        EditText noteContentEditText = findViewById( R.id.noteEditorEditText2 ) ;

        Intent intent = getIntent() ;
        noteId = intent.getIntExtra("noteId",-1) ;

        if( noteId != -1 ){
            noteTitleEditText.setText( simpleNoteFragment.simpleNotesTitle.get(noteId) ) ;
            noteContentEditText.setText( simpleNoteFragment.simpleNotesContent.get(noteId) );
        } else{
            simpleNoteFragment.simpleNotesContent.add("") ;
            simpleNoteFragment.simpleNotesTitle.add("") ;
            noteId = simpleNoteFragment.simpleNotesContent.size()-1 ;
        }

        noteTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                simpleNoteFragment.simpleNotesTitle.set( noteId, String.valueOf( s ) ) ;
                simpleNoteFragment.arrayAdapter.notifyDataSetChanged();

                String str = null;
                try {
                    str = ObjectSerializer.serialize(simpleNoteFragment.simpleNotesTitle );
                    MainActivity.sharedPreferences.edit().putString( "snTitle", str ).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        noteContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                simpleNoteFragment.simpleNotesContent.set( noteId, String.valueOf(s) ) ;

                try {
                    String str = ObjectSerializer.serialize( simpleNoteFragment.simpleNotesContent ) ;
                    MainActivity.sharedPreferences.edit().putString("snContent", str).apply();
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
