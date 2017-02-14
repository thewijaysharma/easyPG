package utility.classified.wijaysharma.easypg;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class EditProfileNew extends Fragment {
    EditText nameet,emailet,cityet,passwordet;
    String nameup,emailup,passwordup,cityup;
    ImageView logoimg;
    Button updatebtn;
    TextView heading,nametv,emailtv,citytv,passtv,instrustiontv;
    String u,urlup;
    String name,password,city,email;
    Context context;
    ProgressDialog pd;

    String username;
    public EditProfileNew(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        username=getArguments().getString("Userid");

         View cview=inflater.inflate(R.layout.activity_edit_profile, container, false);
        nameet=(EditText)cview.findViewById(R.id.editText14);
        emailet=(EditText)cview.findViewById(R.id.editText11);
        cityet=(EditText)cview.findViewById(R.id.editText13);
        passwordet=(EditText)cview.findViewById(R.id.editText10);

         context=getContext();
        heading=(TextView)cview.findViewById(R.id.textView10);
        nametv=(TextView)cview.findViewById(R.id.textView11);
        emailtv=(TextView)cview.findViewById(R.id.textView12);
        citytv=(TextView)cview.findViewById(R.id.textView13);
        passtv=(TextView)cview.findViewById(R.id.textView14);
        instrustiontv=(TextView)cview.findViewById(R.id.textView15);

        logoimg=(ImageView)cview.findViewById(R.id.imageView4);
        updatebtn=(Button)cview.findViewById(R.id.button3);

        u="http://www.givebloodsavelife.org/pgtracker/getuserinfo.php?uid="+username;
        new jsonParser().execute();


        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameup= nameet.getText().toString();
               emailup=  emailet.getText().toString();
                passwordup= passwordet.getText().toString();
                cityup= cityet.getText().toString();

                urlup="http://www.givebloodsavelife.org/pgtracker/updateuserinfo.php?uid="+username+"&name="+nameup+"&email="+emailup+"&password="+passwordup+"&city="+cityup+"";
                new UpdatejsonParser().execute();

            }
        });

        return cview;
    }

    class jsonParser extends AsyncTask<Void,Void,Void>
    {
        String incommingjson;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
       pd=new ProgressDialog(context);
            pd.setMessage("Loading....");
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
                con.setRequestMethod("GET");
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
                JSONObject mainobj = mainarray.getJSONObject(0);
                 name=mainobj.getString("Name");
                 email=mainobj.getString("Email");
                 password=mainobj.getString("Password");
                city=mainobj.getString("City");

                nameet.setText(name);
                emailet.setText(email);
                passwordet.setText(password);
                cityet.setText(city);


            }catch(Exception ex){
                Toast.makeText(context,""+ex,Toast.LENGTH_LONG).show();
            }
        }
    }


    class UpdatejsonParser extends AsyncTask<Void,Void,Void>
    {
        String incommingjson;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
pd=new ProgressDialog(context);
            pd.setMessage("loading....");
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
                URL url = new URL(urlup);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
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
                    Toast.makeText(getContext(), "Response Code:" + code, Toast.LENGTH_SHORT).show();

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

                JSONObject mainobj=new JSONObject(incommingjson);
                String result=mainobj.getString("code");
                if(result.equals("1")){
                    Toast.makeText(context,"Profile updated successfully",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();


            }catch(Exception ex){
                Toast.makeText(context,""+ex,Toast.LENGTH_LONG).show();
            }
        }
    }


}
