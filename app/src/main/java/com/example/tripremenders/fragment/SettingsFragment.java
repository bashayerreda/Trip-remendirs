package com.example.tripremenders.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.tripremenders.MainActivity;
import com.example.tripremenders.R;
import com.example.tripremenders.RegistrationActivity;
import com.example.tripremenders.models.NoteModel;
import com.example.tripremenders.models.NoteViewModel;
import com.example.tripremenders.models.TripModel;
import com.example.tripremenders.models.TripViewModel;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment  extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Button syncButton = view.findViewById(R.id.sync_button);
        syncButton.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReferenceTrips =
                    database.getReference("trips").child(currentUser.getUid());
            DatabaseReference databaseReferenceNotes =
                    database.getReference("notes").child(currentUser.getUid());

            TripViewModel tripViewModel =
                    ViewModelProviders.of(this).get(TripViewModel.class);

            NoteViewModel noteViewModel =
                    ViewModelProviders.of(this).get(NoteViewModel.class);

            LiveData<List<TripModel>> allTrips = tripViewModel.getAllTrips();
            LiveData<List<NoteModel>> allNotes = noteViewModel.getAllNotes();

            allTrips.observe(getActivity(), new Observer<List<TripModel>>() {

                @Override
                public void onChanged(@Nullable final List<TripModel> tripModels) {
                    // Update the cached copy of the words in the adapter.
                    databaseReferenceTrips.setValue(tripModels);
                    allTrips.removeObserver(this);
                }
            });

            allNotes.observe(getActivity(), new Observer<List<NoteModel>>() {

                @Override
                public void onChanged(@Nullable final List<NoteModel> notesModels) {
                    // Update the cached copy of the words in the adapter.
                    databaseReferenceNotes.setValue(notesModels);
                    allNotes.removeObserver(this);
                }
            });
        });

        Button logoutButton = view.findViewById(R.id.Logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.alert_dialog_signout_item);
                dialog.setCancelable(false);
                dialog.show();
                TextView textViewYesLogout = dialog.findViewById(R.id.text_yes_logout);
                TextView textViewNoLogout = dialog.findViewById(R.id.text_no_logout);
                textViewYesLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TripViewModel tripViewModel =
                                ViewModelProviders.of(SettingsFragment.this)
                                        .get(TripViewModel.class);

                        NoteViewModel noteViewModel =
                                ViewModelProviders.of(SettingsFragment.this)
                                        .get(NoteViewModel.class);

                        tripViewModel.deleteAll();
                        noteViewModel.deleteAll();

                        dialog.dismiss();
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                        startActivity(intent);
                        getActivity().finishAffinity();

                    }
                });
                textViewNoLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }
}