package com.example.mapnotes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class simpleNoteFragment extends Fragment {

    static ArrayList<String> simpleNotesTitle ;
    static ArrayList<String> simpleNotesContent ;
    static ArrayAdapter<String> arrayAdapter ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.simple_notes, container, false) ;

        ListView simpleNotesList = root.findViewById(R.id.simpleNoteList) ;
        simpleNotesContent = new ArrayList<>() ;
        simpleNotesTitle = new ArrayList<>() ;

        simpleNotesTitle.clear();
        simpleNotesContent.clear();

        try {
            simpleNotesTitle = (ArrayList<String>) ObjectSerializer.deserialize( MainActivity.sharedPreferences.getString(
                    "snTitle", ObjectSerializer.serialize( new ArrayList<String>() ) ) ) ;
            simpleNotesContent = (ArrayList<String>) ObjectSerializer.deserialize( MainActivity.sharedPreferences.getString(
                    "snContent", ObjectSerializer.serialize( new ArrayList<String>() ) ) ) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if( simpleNotesTitle.size() == 0 || simpleNotesContent.size() == 0 ||
        simpleNotesContent.size() != simpleNotesTitle.size()) {
                simpleNotesContent.clear();
                simpleNotesTitle.clear();
                simpleNotesTitle.add("Example Note") ;
                simpleNotesContent.add("") ;
        }

        arrayAdapter  = new ArrayAdapter<String>(Objects.requireNonNull(getContext()),
                android.R.layout.simple_list_item_1, simpleNotesTitle ) ;
        simpleNotesList.setAdapter(arrayAdapter);

        simpleNotesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent( getContext(), NoteEditorAcitvity.class ) ;
                intent.putExtra( "noteId", position ) ;
                startActivity(intent);
            }
        });

        FloatingActionButton fab = root.findViewById(R.id.fabSimpleNotes) ;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), NoteEditorAcitvity.class ) ;
                startActivity( intent );
            }
        });

        simpleNotesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder( getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Delete Item !!!")
                        .setMessage("Are you sure you want to delete this note ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                simpleNotesTitle.remove(position) ;
                                arrayAdapter.notifyDataSetChanged();
                                simpleNotesContent.remove(position) ;

                                try {
                                    String str = ObjectSerializer.serialize(simpleNotesTitle) ;
                                    MainActivity.sharedPreferences.edit().putString( "snTitle", str ).apply();
                                    str = ObjectSerializer.serialize( simpleNotesContent) ;
                                    MainActivity.sharedPreferences.edit().putString("snContent",str).apply();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton( "NO", null )
                        .show() ;

                return true;
            }
        });

        return root ;
    }
}







