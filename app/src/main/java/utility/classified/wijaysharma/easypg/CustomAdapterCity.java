package utility.classified.wijaysharma.easypg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Wijay Sharma on 8/11/2016.
 */
public class CustomAdapterCity extends ArrayAdapter {
    ArrayList pgtitle;
    ArrayList pgrent;
    ArrayList pgowner;
    ArrayList pgaddress;
    ArrayList pgdesc;
    ArrayList pgmobile;
    ArrayList pgcity;
    ArrayList pgcatagory;
    ArrayList pgimage;
    ArrayList pgid;
    ArrayList imageUrlList;
    Context context;

    public CustomAdapterCity(Context context,ArrayList pgid,
    ArrayList pgowner,ArrayList pgmobile,ArrayList pgrent,ArrayList pgdesc,ArrayList pgtitle,ArrayList pgcity,ArrayList pgcatagory,ArrayList pgaddress,ArrayList url) {
        super(context, R.layout.customlayout_city,pgid);

        this.pgmobile=pgmobile;
        this.pgrent=pgrent;
        this.pgdesc=pgdesc;
        this.pgtitle=pgtitle;
        this.pgcity=pgcity;
        this.pgcatagory=pgcatagory;
        this.pgaddress=pgaddress;
        this.pgowner=pgowner;
        this.pgid=pgid;
        this.imageUrlList=url;


//      this.imagelist=imagelist;

        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customview=inflater.inflate(R.layout.customlayout_city,null);

        ImageView previewpg=(ImageView)customview.findViewById(R.id.imageView5);

        TextView title=(TextView)customview.findViewById(R.id.textView17);
        title.setText(pgtitle.get(position).toString());

        TextView rent=(TextView)customview.findViewById(R.id.textView18);

        TextView rentamount=(TextView)customview.findViewById(R.id.textView19);
        rentamount.setText(pgrent.get(position).toString());

        TextView address=(TextView)customview.findViewById(R.id.textView20);

        TextView actualadd=(TextView)customview.findViewById(R.id.textView21);
        actualadd.setText(pgaddress.get(position).toString());

		//------------------------------------display image------------------------------------------
        Picasso.with(context).load(imageUrlList.get(position).toString()).placeholder(R.drawable.ic_action_name).into(previewpg);
//---------------------------------------------------------------------------------------------------

        return customview;
    }

}
