package utility.classified.wijaysharma.easypg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

public class SearchWithCity extends AppCompatActivity {

    String cityname;
    ProgressDialog pd;
    Context context;

    TextView tvcity;
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


    String u;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_with_city);


        Intent cityintent=getIntent();
        final Bundle bundle=cityintent.getBundleExtra("CityName");
        cityname=bundle.getString("City");
        u="http://www.givebloodsavelife.org/pgtracker/Search2.php?city="+cityname;
        context=this;
        lv=(ListView)findViewById(R.id.listView);

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


        tvcity=(TextView)findViewById(R.id.textView16);
        tvcity.setText(cityname);
        new jsonParser().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle pgindex=new Bundle();
                pgindex.putInt("index",position);
                pgindex.putString("cityname",cityname);
//                bundle.putString("pgid",pgid.get(position).toString());
//                bundle.putString("pgtitle",pgtitle.get(position).toString());
//                bundle.putString("pgrent",pgrent.get(position).toString());
//                bundle.putString("pgaddress",pgaddress.get(position).toString());
//                bundle.putString("pgcatagory",pgcatagory.get(position).toString());
//                bundle.putString("pgdesc",pgdesc.get(position).toString());
//                bundle.putString("pgcity",pgcity.get(position).toString());
//                bundle.putString("pgmobile",pgmobile.get(position).toString());
//                bundle.putString("pgowner",pgowner.get(position).toString());
//                Intent intentdetails=new Intent(context,DetailsofPg.class);
//                intentdetails.putExtra("pginfo",bundle);
//                startActivity(intentdetails);

                Intent intentindex=new Intent(context,DetailsofPg.class);
                intentindex.putExtra("indexbundle",pgindex);
                startActivity(intentindex);
            }
        });

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
                    lv.setAdapter(customAdapterCity);

            }catch(Exception ex){
                Toast.makeText(context,""+ex,Toast.LENGTH_LONG).show();
            }
        }
    }

}
