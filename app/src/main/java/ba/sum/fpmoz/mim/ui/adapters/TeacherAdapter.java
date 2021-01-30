package ba.sum.fpmoz.mim.ui.adapters;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import ba.sum.fpmoz.mim.R;
import ba.sum.fpmoz.mim.TeacherAdminEditActivity;
import ba.sum.fpmoz.mim.UserAdminEditActivity;
import ba.sum.fpmoz.mim.model.Teacher;


public class TeacherAdapter extends FirebaseRecyclerAdapter<Teacher, TeacherAdapter.TeacherViewHolder> {

    public TeacherAdapter(@NonNull FirebaseRecyclerOptions<Teacher> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TeacherViewHolder holder, int position, @NonNull Teacher model) {
        holder.teacherName.setText(model.getName());
        holder.teacherSurname.setText(model.getEmail());
        holder.teacherUid.setText(model.getCourse());
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_teacher_admin, parent, false);
        TeacherViewHolder viewHolder = new TeacherViewHolder(view);

        viewHolder.setOnClickListener(new Adapter.ClickListener() {
            @Override
            public void OnClickListener(View v, int position) {

            }

            @Override
            public void OnLongClickListener(View v, int position) {

            }
        });
        return viewHolder;
    }

    public class TeacherViewHolder extends RecyclerView.ViewHolder{
        TextView teacherName;
        TextView teacherSurname;
        TextView teacherUid;
        Button teacherEditBtn;
        Button teacherDeleteBtn;

        Adapter.ClickListener clickListener;

        public void setOnClickListener(Adapter.ClickListener clickListener){
            this.clickListener = clickListener;
        }

        public TeacherViewHolder(@NonNull final View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacherNameTxt);
            teacherSurname = itemView.findViewById(R.id.teacherSurnameTxt);
            teacherUid = itemView.findViewById(R.id.teacherUidTxt);
            teacherEditBtn = itemView.findViewById(R.id.teacherEditBtn);
            teacherDeleteBtn = itemView.findViewById(R.id.teacherDeleteBtn);

            teacherDeleteBtn.setOnClickListener(v -> getRef(getAdapterPosition()).removeValue());

            teacherEditBtn.setOnClickListener((v) -> {
                String key = getRef(getAdapterPosition()).getKey();
                Intent i= new Intent(itemView.getContext(), TeacherAdminEditActivity.class);
                i.putExtra("TEACHER_ID", key);
                itemView.getContext().startActivity(i);
            } );
            itemView.setOnClickListener((v) -> clickListener.OnClickListener(v, getAdapterPosition()));
            itemView.setOnLongClickListener((v) -> {
                clickListener.OnClickListener(v, getAdapterPosition());
                return true;
            } );
        }
    }
}
