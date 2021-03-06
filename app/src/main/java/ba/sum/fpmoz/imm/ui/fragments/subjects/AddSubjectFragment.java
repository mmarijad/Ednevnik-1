package ba.sum.fpmoz.imm.ui.fragments.subjects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ba.sum.fpmoz.imm.R;
import ba.sum.fpmoz.imm.model.Subject;


public class AddSubjectFragment extends Fragment {
    FirebaseDatabase db;
    DatabaseReference ref;
    EditText subjectNameInp;
    Button addSubjectBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View subjectAdminView = inflater.inflate(R.layout.activity_subject_admin,container,false);

        this.db = FirebaseDatabase.getInstance();
        this.ref = this.db.getReference("predmeti");

        this.subjectNameInp = subjectAdminView.findViewById(R.id.subjectNameInp);
        this.addSubjectBtn = subjectAdminView.findViewById(R.id.AddSubjectBtn);

        addSubjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_nastavnika = " ";
                String id = "";
                String newSubjectKey = ref.push().getKey();
                String subjectName = subjectNameInp.getText().toString();
                ref.child(newSubjectKey).setValue(new Subject(newSubjectKey, subjectName, id_nastavnika, id ));
                subjectNameInp.setText("");
                Toast.makeText(subjectAdminView.getContext(),
                        "Uspješno ste dodali predmet",Toast.LENGTH_LONG).show();

            }
        });
        return subjectAdminView;

    }

}
