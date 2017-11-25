package com.example.android.quakereport.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.quakereport.R;
import com.example.android.quakereport.model.Earthquake;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ASPIRE on 013, 13, Nov, 2017.
 */

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.ViewHolder> {
    private Context context;
    private List<Earthquake> earthquakeList;
    private static OnItemClickListener listener;


    public EarthquakeAdapter(Context context, List<Earthquake> earthquakeList) {
        this.context = context;
        this.earthquakeList = earthquakeList;
    }

    public static void setOnItemClickListener(OnItemClickListener listener){
        EarthquakeAdapter.listener = listener;

    }


    @Override
    public EarthquakeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.earthquake_list_item,parent,false);
        return new EarthquakeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EarthquakeAdapter.ViewHolder holder, int position) {
        holder.bind(earthquakeList.get(position), listener);
        Earthquake earthquake = earthquakeList.get(position);

        //Format the Decimal Value for the magnitude
        double earthquakeMagnitude = earthquake.getMagnitude();

        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String formattedMagnitude = decimalFormat.format(earthquakeMagnitude);

        holder.magnitude.setText(formattedMagnitude);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        holder.magnitudeCircle = (GradientDrawable) holder.magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());

        // Set the color on the magnitude circle
        holder.magnitudeCircle.setColor(magnitudeColor);



        //Split the location into two different Strings
        String originalLocation = earthquake.getLocation();

        if (originalLocation.contains("of")){
            String[] splitString = originalLocation.split("(?<=of)");
            holder.direction.setText(splitString[0]);
            holder.location.setText(splitString[1].trim());
        }else{
            holder.direction.setText("Near the");
            holder.location.setText(earthquake.getLocation());
        }


        //Convert UNIX Epoch Time to much readable form.
        long unixEpochTime = earthquake.getDate();
        Date timeAndDate = new Date(unixEpochTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:ss a");

        String formattedDate = dateFormat.format(timeAndDate);
        String formattedTime = timeFormat.format(timeAndDate);

        holder.date.setText(formattedDate);
        holder.time.setText(formattedTime);
    }

    @Override
    public int getItemCount() {
        return earthquakeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date, direction, time, location, magnitude;
        private GradientDrawable magnitudeCircle;

        public ViewHolder(View itemView) {
            super(itemView);
            magnitude = itemView.findViewById(R.id.magnitude);
            direction = itemView.findViewById(R.id.location_offset);
            location = itemView.findViewById(R.id.primary_location);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }

        public void bind(final Earthquake earthquake, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(earthquake, getAdapterPosition());
                }
            });
        }
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(context, magnitudeColorResourceId);
    }

    public interface OnItemClickListener {
        void onItemClick(Earthquake item, int position);

    }

    public void clear(){
        earthquakeList.clear();
    }

}
