package utility.classified.wijaysharma.easypg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity {

    Context context;
    String userid,userpw;
    ImageButton loginbtn;
    ImageButton gobtn;
    String u;
    EditText usernameet,pwet;
    EditText searchcityet;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=this;

        sharedPreferences=getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);
        if(sharedPreferences.getString("id",null)!=null)
        {
            Bundle bundle=new Bundle();
            bundle.putString("Userid",sharedPreferences.getString("id",null));
            Intent intent=new Intent(context,UserLoggedIn.class);
            intent.putExtra("LoginInfo",bundle);
            startActivity(intent);
            finish();


        }


        usernameet =(EditText)findViewById(R.id.editText);
        pwet =(EditText)findViewById(R.id.editText2);

        ImageView loginimg=(ImageView)findViewById(R.id.imageView);
        loginbtn=(ImageButton)findViewById(R.id.button);
        gobtn=(ImageButton)findViewById(R.id.imgbutton);
        TextView newusertv=(TextView)findViewById(R.id.textView);
        searchcityet=(EditText)findViewById(R.id.searchcityedittext);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userid = usernameet.getText().toString();
                userpw = pwet.getText().toString();

                if(userid.equals("")){
                    Toast.makeText(getApplicationContext(),"Username cannot be empty",Toast.LENGTH_SHORT).show();

                }
                else if(userpw.equals("")){
                    Toast.makeText(getApplicationContext(),"Password field cannot be blank",Toast.LENGTH_LONG).show();
                }
                else{

                    u = "http://www.givebloodsavelife.org/pgtracker/Login1.php?User=" + userid + "&password=" + userpw + "";
                    new jsonParser().execute();
//                    new jsonParser().execute();
                }}

        });

        newusertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,RegisterNow.class);
                startActivity(intent);

            }
        });

        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchcity=searchcityet.getText().toString();
                Intent intentcity=new Intent(context,SearchWithCity.class);
                Bundle bundle=new Bundle();
                bundle.putString("City",searchcity);
                intentcity.putExtra("CityName", bundle);
                startActivity(intentcity);

            }
        });

    }

    class jsonParser extends AsyncTask<Void,Void,Void>
    {
        String incommingjson;
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(context);
            pd.setMessage("Signing in... Please wait");
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
            pd.hide();

            try {
                JSONObject mainobj = new JSONObject(incommingjson);
                String result=mainobj.getString("code");

                if(result.equals("1")) {
                    Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_LONG).show();

                    editor=sharedPreferences.edit();
                    editor.putString("id",userid);
                    editor.commit();

                    Bundle bundle=new Bundle();
                    bundle.putString("Userid",userid);
                    bundle.putString("Password", userpw);
                    Intent intent=new Intent(context,UserLoggedIn.class);
                    intent.putExtra("LoginInfo",bundle);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid User",Toast.LENGTH_LONG).show();
                    pd.hide();

                }

            }catch(Exception ex){}
        }
    }

}
