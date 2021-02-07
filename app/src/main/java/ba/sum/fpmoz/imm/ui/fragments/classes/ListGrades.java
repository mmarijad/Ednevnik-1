package ba.sum.fpmoz.imm.ui.fragments.classes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ba.sum.fpmoz.imm.R;
import ba.sum.fpmoz.imm.model.Grade;
import ba.sum.fpmoz.imm.ui.adapters.OcjeneProfesorAdapter;
import ba.sum.fpmoz.imm.ui.adapters.StudentGradesAdapter;

public class ListGrades extends Fragment {
    FirebaseDatabase db;
    DatabaseReference ref;
    StudentGradesAdapter adapter;
    RecyclerView gradeListView;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View gradeListView = inflater.inflate(R.layout.activity_ocjene_list_for_students, container, false);
        this.gradeListView=gradeListView.findViewById(R.id.classListView);
        this.db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        String key = getActivity().getIntent().getStringExtra("SUBJECT_ID");
        this.ref=this.db.getReference("učenici/").child(userID).child("predmeti").child(key).child("ocjene");
        this.gradeListView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Grade> options =new FirebaseRecyclerOptions.Builder<Grade>().setQuery(this.ref, Grade.class).build();
        this.adapter = new StudentGradesAdapter(options);
        this.gradeListView.setAdapter(this.adapter);
        return gradeListView;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}