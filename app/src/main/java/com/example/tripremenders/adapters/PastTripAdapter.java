package com.example.tripremenders.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;

import java.util.ArrayList;

import com.example.tripremenders.R;
import com.example.tripremenders.models.TripModel;

public class PastTripAdapter extends RecyclerView.Adapter<PastTripAdapter.ViewHolder> {

    ArrayList<TripModel> trips;
    Context context;
    MenuInflater menuInflater;
    RecyclerView recyclerView;
    public StartTrip setStartTrip = null;
    LinearLayout lastLinearLayoutClickOn;
    public interface StartTrip {
        void onClick(TripModel tripModel);
    }
    public PastTripAdapter(Context context, ArrayList<TripModel> trips, MenuInflater menuInflater, RecyclerView recyclerView) {
        super();

        this.context = context;
        this.trips = trips;
        this.menuInflater = menuInflater;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public PastTripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.past_trip_item, parent, false);
        PastTripAdapter.ViewHolder viewHolder = new PastTripAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PastTripAdapter.ViewHolder holder, int position) {

        holder.tripNameTextView.setText(trips.get(position).getName());
        holder.dateTextView.setText(trips.get(position).getDate());
        holder.timeTextView.setText(trips.get(position).getTime());
        holder.tripStatus.setText(String.valueOf(trips.get(position).getStatus()));
        holder.startPointTextView.setText(trips.get(position).getStartPoint());
        holder.endPointTextView.setText(trips.get(position).getEndPoint());
        holder.tripTypeTextView.setText(trips.get(position).getTripType());
        if (trips.get(position).getTripType().equals("One Direction")) {
            holder.tripeTypeImageView.setImageResource(R.drawable.ic_ray_start_arrow);
        } else {
            holder.tripeTypeImageView.setImageResource(R.drawable.ic_multiple_stop);
        }

        holder.tripLinearLayout.setOnClickListener(v -> {

            if (lastLinearLayoutClickOn != null
                    && lastLinearLayoutClickOn != holder.buttonsLinearLayout) {
                lastLinearLayoutClickOn.setVisibility(View.GONE);
            }

            lastLinearLayoutClickOn = holder.buttonsLinearLayout;

            final int visibilityOfButtons = holder.buttonsLinearLayout.getVisibility();

            Transition transition = new Slide(Gravity.TOP);
            transition.setDuration(200);
            transition.addTarget(holder.buttonsLinearLayout);
            transition.addListener(new TransitionListenerAdapter() {
                @Override
                public void onTransitionStart(@NonNull Transition transition) {
                    super.onTransitionStart(transition);
                    if (visibilityOfButtons == View.GONE) {
                        recyclerView.smoothScrollToPosition(position);
                    }
                }

            });

            TransitionManager.beginDelayedTransition(holder.tripLinearLayout, transition);
            holder.buttonsLinearLayout
                    .setVisibility(
                            holder.buttonsLinearLayout.getVisibility() == View.GONE ?
                                    View.VISIBLE : View.GONE);
        });


    }

    public void setTrips(ArrayList<TripModel> trips){
        this.trips = trips;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (trips != null)
            return trips.size();
        else return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout tripLinearLayout;

        TextView tripNameTextView;
        TextView dateTextView;
        TextView timeTextView;
        TextView startPointTextView;
        TextView endPointTextView;
        TextView tripTypeTextView;
        TextView tripStatus;
        ImageView tripeTypeImageView;

        LinearLayout buttonsLinearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tripNameTextView = itemView.findViewById(R.id.trip_name);
            dateTextView = itemView.findViewById(R.id.trip_date);
            timeTextView = itemView.findViewById(R.id.trip_time);
            startPointTextView = itemView.findViewById(R.id.trip_start_point);
            endPointTextView = itemView.findViewById(R.id.trip_end_point);
            tripTypeTextView = itemView.findViewById(R.id.trip_type);
            tripStatus = itemView.findViewById(R.id.status);
            tripeTypeImageView = itemView.findViewById(R.id.trip_type_icon);
            tripLinearLayout = itemView.findViewById(R.id.trip_linear_layout);
            buttonsLinearLayout = itemView.findViewById(R.id.buttons_linear_layout);
        }
    }
}
