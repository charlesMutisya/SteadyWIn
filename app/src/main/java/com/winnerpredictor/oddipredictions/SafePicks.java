package com.winnerpredictor.oddipredictions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SafePicks extends Fragment {
    View view;
    RecyclerView mrecycler;
    LinearLayoutManager mlinearlayout;
    TextView loading;
    DatabaseReference mdatabasereference;
    FirebaseRecyclerAdapter<Model, DailyPicks.ItemViewHolder> firebaseRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view= inflater.inflate(R.layout.twoplus, container, false);

        mdatabasereference = FirebaseDatabase.getInstance().getReference().child("jackpot").child("2+odds");
        loading = view.findViewById(R.id.loaddaily);


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        mrecycler = view.findViewById(R.id.recycler);
        mrecycler.setHasFixedSize(false);
        mlinearlayout = new LinearLayoutManager(getContext());
        mrecycler.setLayoutManager(mlinearlayout);
        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Model, DailyPicks.ItemViewHolder>(
                Model.class,
                R.layout.listviewcard,
                DailyPicks.ItemViewHolder.class,
                mdatabasereference
        ) {
            @Override
            protected void populateViewHolder(DailyPicks.ItemViewHolder itemViewHolder, Model model, int position) {

                final String item_key = getRef(position).getKey();
                itemViewHolder.setTitle(model.getTitle());
                itemViewHolder.setDetails(model.getBody());
                itemViewHolder.setTime(model.getTime());
                loading.setVisibility(View.GONE);
                itemViewHolder.mnview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent adDetails = new Intent(v.getContext(), PostDetails.class);
                        adDetails.putExtra("postkey", item_key);
                        adDetails.putExtra("selection", "2+odds");
                        startActivity(adDetails);
                    }

                });
            }
        };
        mrecycler.setAdapter(firebaseRecyclerAdapter);

    }
}
