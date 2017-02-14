package utility.classified.wijaysharma.easypg;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class ShowAdstoUser extends Fragment {
    TextView showmyads;
    ListView listView;

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

    Context context;
    String username;
    ProgressDialog pd;
    String u;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

     View cview=inflater.inflate(R.layout.showmyads, container, false);

        username=getArguments().getString("Userid");

        showmyads=(TextView)cview.findViewById(R.id.textView6);
        listView=(ListView)cview.findViewById(R.id.listView2);
        context=getContext();

        pgtitle=new ArrayList();
        pgrent=new ArrayList();
        pgaddress=new ArrayList();
        pgcatagory=new ArrayList();
        pgowner=new ArrayList();
        pgdesc=new ArrayList();
        pgcity=new ArrayList();
        pgid=new ArrayList();
        pgmobile=new ArrayList();
        pgimage=new ArrayList();

        u="http://www.givebloodsavelife.org/pgtracker/getuserads.php?User="+username;
        new jsonParser().execute();

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Bundle bd=new Bundle();
//                fragmentTransaction=fragmentManager.beginTransaction();
//                bd.putString("Pgid", pgid.get(position).toString());
//                EditPost editPost=new EditPost();
//                editPost.setArguments(bd);
//                fragmentTransaction.replace(R.id.contain,editPost);
//                fragmentTransaction.commit();
//            }
//        });


        return cview;

    }

    class jsonParser extends AsyncTask<Void,Void,Void>
    {
        String incommingjson;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(context);
            pd.setMessage("Searching...");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            incommingjson=getJson();
            return null;
        }

        public String getJson()
        {
            StringBuilder sb=new StringBuilder();
            try {
                URL url = new URL(u);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();
                int code=con.getResponseCode();
                if(code==200)
                {

                    InputStream in=con.getInputStream();
                    Scanner obj=new Scanner(in);
                    while(obj.hasNext())
                    {
                        sb.append(obj.nextLine());
                    }

                }
                else
                {
                    Toast.makeText(context, "Response Code:" + code, Toast.LENGTH_SHORT).show();

                }

            }
            catch(Exception ex){}
            return(sb.toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
            try {
                JSONArray mainarray=new JSONArray(incommingjson);

                for(int i=0;i<mainarray.length();i++) {
                    JSONObject object = mainarray.getJSONObject(i);

                    pgid.add(object.getString("PG_ID"));
                    pgowner.add(object.getString("Owner_Name"));
                    pgmobile.add(object.getString("Mobile_No"));
                    pgrent.add(object.getString("Rent"));
                    pgdesc.add(object.getString("Description"));
                    pgtitle.add(object.getString("Title"));
                    pgcity.add(object.getString("City"));
                    pgcatagory.add(object.getString("Category"));
                    pgaddress.add(object.getString("Address"));

                    //-----------------------------------------------------------
                    String img = "http://www.givebloodsavelife.org/pgtracker/pics/"+object.getString("image");
                    pgimage.add(img);
                    //---------------------------------------------------------------------
                }
                CustomAdapterCity customAdapterCity=new CustomAdapterCity(context,pgid,pgowner,pgmobile,pgrent,pgdesc,pgtitle,pgcity,pgcatagory,pgaddress,pgimage);
                listView.setAdapter(customAdapterCity);

            }catch(Exception ex){
                Toast.makeText(context,""+ex,Toast.LENGTH_LONG).show();
            }
        }
    }



}
