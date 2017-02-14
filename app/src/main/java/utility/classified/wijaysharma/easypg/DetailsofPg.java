package utility.classified.wijaysharma.easypg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class DetailsofPg extends AppCompatActivity {

    TextView desctv,catagorytv,titletv,renttv,addresstv,contacttv;
    ImageView img;
    int index;
    Context context;
    ProgressDialog pd;
    String u;
    String cityname;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsof_pg);

        Intent intentindex=getIntent();
        Bundle bd=intentindex.getBundleExtra("indexbundle");
        cityname=bd.getString("cityname");
        index=bd.getInt("index");

        u="http://www.givebloodsavelife.org/pgtracker/Search2.php?city="+cityname;
        context=this;

        desctv=(TextView)findViewById(R.id.textView30);
        catagorytv=(TextView)findViewById(R.id.textView31);
        titletv=(TextView)findViewById(R.id.textView22);
        renttv=(TextView)findViewById(R.id.textView28);
        addresstv=(TextView)findViewById(R.id.textView24);
        contacttv=(TextView)findViewById(R.id.textView26);
        img=(ImageView)findViewById(R.id.imageView6);

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

        new jsonParser().execute();

//        String desc=intentdetails.getStringExtra("pgdesc");
//        String catagory=intentdetails.getStringExtra("pgcatagory");
//        String title=intentdetails.getStringExtra("pgtitle");
//        String rent=intentdetails.getStringExtra("pgrent");
//        String address=intentdetails.getStringExtra("pgaddress");
//        String contact=intentdetails.getStringExtra("pgcontact");
//
//        desctv.setText(desc);
//        catagorytv.setText(catagory);
//        titletv.setText(title);
//        renttv.setText(rent);
//        addresstv.setText(address);
//        contacttv.setText(contact);
//        img.setImageResource();


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

                for(int i=0;i<=mainarray.length();i++) {
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

                    String pic = "http://www.givebloodsavelife.org/pgtracker/pics/"+object.getString("image");
                    pgimage.add(pic);

                    desctv.setText(pgdesc.get(index).toString());
                    catagorytv.setText(pgcatagory.get(index).toString());
                    titletv.setText(pgtitle.get(index).toString());
                    renttv.setText(pgrent.get(index).toString());
                    addresstv.setText(pgaddress.get(index).toString());
                    contacttv.setText(pgmobile.get(index).toString());

                    Picasso.with(context).load(pgimage.get(index).toString()).placeholder(R.drawable.ic_action_name).into(img);


//                    String img = object.getString("image");
//                    byte [] encodeByte=Base64.decode(img, Base64.DEFAULT);
//                    Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//                    pgimage.add((Bitmap)bitmap);
                }


            }catch(Exception ex){
                Toast.makeText(context,""+ex,Toast.LENGTH_LONG).show();
            }
        }
    }

}
