package ba.sum.fpmoz.mim.ui.fragments.users;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ba.sum.fpmoz.mim.R;
import ba.sum.fpmoz.mim.model.Admin;
import ba.sum.fpmoz.mim.model.Student;
import ba.sum.fpmoz.mim.model.Teacher;
import ba.sum.fpmoz.mim.model.User;


public class AddUsersFragment extends Fragment{

    private FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref, stu, nas, pred;
    EditText studentNameInp;
    EditText studentEmailInp;
    EditText studentPasswordInp, teacherCourseInp, studentGradeInp;
    CheckBox teacherChck;
    Button addStudentBtn;
    TextView messageTxt1;
    Spinner spinnerNasPred;
    TextView spinnerTxt;
    String item;
    List<String> predmetno;
    String selectedItem;
    HashMap<String,String> predmetiUid=new HashMap<String,String>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        final View userAdminView = inflater.inflate(R.layout.activity_user_admin, container, false);
        this.spinnerTxt=userAdminView.findViewById(R.id.spinnerTxt);
        this.db = FirebaseDatabase.getInstance();
        this.ref = this.db.getReference("korisnici");
        this.nas = this.db.getReference("nastavnici");
        this.stu = this.db.getReference("učenici");
        this.pred=this.db.getReference("predmeti");
        predmetno=new ArrayList<>();

        pred.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String >predmeti=new ArrayList<String>();
                for(DataSnapshot ds:snapshot.getChildren()){
                    String prvi=ds.child("name").getValue().toString();
                    String drugi=ds.child("uid").getValue().toString();
                    predmetno.add(prvi);
                    predmeti.add(prvi);
                    predmetiUid.put(prvi,drugi);
                }
                spinnerNasPred=userAdminView.findViewById(R.id.spinnerNasPred);
                final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,predmeti);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerNasPred.setAdapter(arrayAdapter);
                spinnerNasPred.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedItem=arrayAdapter.getItem(position);
                        spinnerTxt.setText(selectedItem);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinnerTxt.setText(selectedItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        this.studentNameInp = userAdminView.findViewById(R.id.studentNameInp);
        this.studentEmailInp = userAdminView.findViewById(R.id.studentEmailInp);
        this.studentPasswordInp = userAdminView.findViewById(R.id.studentPasswordInp);
        this.addStudentBtn = userAdminView.findViewById(R.id.addStudentBtn);
        this.studentGradeInp = userAdminView.findViewById(R.id.studentGradeInp);
        this.teacherChck = userAdminView.findViewById(R.id.teacherChck);
        this.messageTxt1 = userAdminView.findViewById(R.id.messageTxt1);
        this.spinnerTxt=userAdminView.findViewById(R.id.spinnerTxt);


        teacherChck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((CheckBox)v).isChecked())
                {
                    spinnerTxt.setVisibility(View.VISIBLE);
                    spinnerNasPred.setVisibility(View.VISIBLE);
                    studentGradeInp.setVisibility(View.GONE);
                }
                else
                {
                    spinnerTxt.setVisibility(View.GONE);
                    spinnerNasPred.setVisibility(View.GONE);
                    studentGradeInp.setVisibility(View.VISIBLE);
                }
            }
        });


        this.addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String displayName = studentNameInp.getText().toString();
                String email = studentEmailInp.getText().toString();
                String password = studentPasswordInp.getText().toString();
                String course = selectedItem;
                String grade = studentGradeInp.getText().toString();
                String uidpred=predmetiUid.get(selectedItem);

                if(teacherChck.isChecked()){
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest
                                    .Builder()
                                    .setDisplayName(displayName)
                                    .build();
                            user.updateProfile(changeRequest).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()){
                                    studentEmailInp.setText("");
                                    studentPasswordInp.setText("");
                                    studentNameInp.setText("");
                                    messageTxt1.setText("Korisnički račun je uspješno napravljen.");
                                    String role="nastavnik";
                                    User newUser = new User(user.getUid(), user.getEmail(), user.getDisplayName(), role);
                                    ref.child(user.getUid()).setValue(newUser);
                                    String id = ref.child(user.getUid()).setValue(newUser).toString();
                                    Teacher t = new Teacher(user.getUid(), user.getEmail(), user.getDisplayName(), course,uidpred);
                                    nas.child(user.getUid()).setValue(t);
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Nastala je greška pri dodoavanju korisnika: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest
                                    .Builder()
                                    .setDisplayName(displayName)
                                    .build();
                            user.updateProfile(changeRequest).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()){
                                    studentEmailInp.setText("");
                                    studentPasswordInp.setText("");
                                    studentNameInp.setText("");
                                    messageTxt1.setText("Korisnički račun je uspješno napravljen.");
                                    String role = "učenik";
                                    User newUser = new User(user.getUid(), user.getEmail(), user.getDisplayName(), role);
                                    ref.child(user.getUid()).setValue(newUser);
                                    String id = ref.child(user.getUid()).setValue(newUser).toString();
                                    Student s = new Student(user.getUid(), user.getEmail(), user.getDisplayName(), grade);
                                    stu.child(user.getUid()).setValue(s);
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Nastala je greška pri dodavanju korisnika: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        return userAdminView;
    }
}
