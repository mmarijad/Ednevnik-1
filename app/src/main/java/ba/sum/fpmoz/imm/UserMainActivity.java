package ba.sum.fpmoz.imm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.imm.model.User;

public class UserMainActivity extends AppCompatActivity{
    private TextView loggedUserTextView;
    private TextView loggedUserRoleTextView;
    private String loggedUser;
    private User user1;
    private String userId;
    private String userEmail;
    String logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        this.loggedUserRoleTextView = findViewById(R.id.loggedUserRoleTxt);

        this.loggedUserTextView = findViewById(R.id.loggedUserTxt);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
        userEmail=user.getEmail();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("korisnici").child(userId);

            loggedUserTextView.setText("Dobrodošli: " + user.getDisplayName());

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                  String logged=snapshot.child("role").getValue(String.class);
                  loggedUserRoleTextView.setText(("prijavljeni ste kao "+logged));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}

            });
        }

        }
