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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class mapNoteFragment extends Fragment {

    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> locations = new ArrayList<>();
    static ArrayList<String> mapNotesTitle = new ArrayList<>();
    static ArrayList<String> mapNotesContent = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.map_notes, container, false) ;
        ListView mapNotesList = root.findViewById(R.id.mapNoteListView) ;
        FloatingActionButton fabMapNotes = root.findViewById(R.id.fabMapNotes) ;

        ArrayList<String> latitudes = new ArrayList<>() ;
        ArrayList<String> longitudes = new ArrayList<>() ;

        places.clear();
        locations.clear();
        latitudes.clear();
        longitudes.clear();
        mapNotesContent.clear();
        mapNotesTitle.clear();

        try {
            places = (ArrayList<String>) ObjectSerializer.deserialize( MainActivity.sharedPreferences.getString(
                    "places", ObjectSerializer.serialize(new ArrayList<String>()))) ;
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize( MainActivity.sharedPreferences.getString(
                    "lats", ObjectSerializer.serialize(new ArrayList<String>()))) ;
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize( MainActivity.sharedPreferences.getString(
                    "lons", ObjectSerializer.serialize(new ArrayList<String>()))) ;
            mapNotesTitle = (ArrayList<String>) ObjectSerializer.deserialize( MainActivity.sharedPreferences.getString(
                    "mnTitle", ObjectSerializer.serialize(new ArrayList<String>()))) ;
            mapNotesContent = (ArrayList<String>) ObjectSerializer.deserialize( MainActivity.sharedPreferences.getString(
                    "mnContent", ObjectSerializer.serialize(new ArrayList<String>()))) ;

        } catch (Exception e) {
            e.printStackTrace();
        }

        if( places.size() > 0 && longitudes.size() > 0 && latitudes.size() > 0 &&
        mapNotesTitle.size() > 0 && mapNotesContent.size() > 0 && latitudes.size() == longitudes.size()
        && longitudes.size() == places.size() && places.size() == mapNotesContent.size() &&
        places.size() == mapNotesTitle.size() ){

            for( int i = 0; i < latitudes.size(); i++ ){
                locations.add( new LatLng( Double.parseDouble(latitudes.get(i)),
                        Double.parseDouble(longitudes.get(i)) ) ) ;
            }

        } else{
                mapNotesTitle.add("Example Note Title") ;
                mapNotesContent.add("") ;
                places.add("") ;
                locations.add( new LatLng(0,0) ) ;
        }

        arrayAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()),
                android.R.layout.simple_list_item_1, mapNotesTitle ) ;
        mapNotesList.setAdapter(arrayAdapter);

        mapNotesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MapNotesEditor.class) ;
                intent.putExtra( "noteId", position ) ;
                startActivity(intent);
            }
        });

        mapNotesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder( getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Delete Map Note !!!")
                        .setMessage("Are you sure you want to delete this map note ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mapNotesTitle.remove(position) ;
                                arrayAdapter.notifyDataSetChanged();
                                mapNotesContent.remove(position) ;
                                locations.remove(position) ;
                                places.remove(position) ;

                                try {
                                    String str = ObjectSerializer.serialize(mapNotesTitle) ;
                                    MainActivity.sharedPreferences.edit().putString( "mnTitle", str ).apply();
                                    str = ObjectSerializer.serialize( mapNotesContent) ;
                                    MainActivity.sharedPreferences.edit().putString("mnContent",str).apply();
                                    str = ObjectSerializer.serialize(places) ;
                                    MainActivity.sharedPreferences.edit().putString("places",str).apply();

                                    ArrayList<String> lat = new ArrayList<>();
                                    ArrayList<String> lon = new ArrayList<>();

                                    for (LatLng coord : locations) {
                                        lat.add(Double.toString(coord.latitude));
                                        lon.add(Double.toString(coord.longitude));
                                    }

                                    str = ObjectSerializer.serialize(lat) ;
                                    MainActivity.sharedPreferences.edit().putString("lats",str).apply();
                                    str = ObjectSerializer.serialize(lon) ;
                                    MainActivity.sharedPreferences.edit().putString("lons",str).apply();

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

        fabMapNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), MapsActivity.class) ;
                startActivity(intent) ;
            }
        });

        return root ;
    }
}







