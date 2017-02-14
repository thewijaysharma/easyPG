package utility.classified.wijaysharma.easypg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Wijay Sharma on 8/8/2016.
 */
public class Demo extends Fragment {
    public Demo()
    {}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.demo,container,false);
        return view;
    }
}
