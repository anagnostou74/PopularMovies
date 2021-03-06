package gr.mobap.popularmovies.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gr.mobap.popularmovies.R;


public class OverviewFragment extends Fragment {
    private static final String TAG = OverviewFragment.class.getSimpleName();
    private CharSequence mLabel;

    public OverviewFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_movie_overview, container, false);
        TextView textView = view.findViewById(R.id.activity_text_overview);
        textView.setText(mLabel);
        Log.v(TAG, "mLabel onCreateView: " + mLabel);
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (data != null) {
            mLabel = data.getString("overview");
        }

    }
}
