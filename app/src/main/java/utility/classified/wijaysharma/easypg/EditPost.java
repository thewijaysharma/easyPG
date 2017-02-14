package utility.classified.wijaysharma.easypg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class EditPost extends Fragment {

    EditText titleet,cityet,descnet,addet,contactet,rentet;
    String uptitle,upcity,upcontact,uprent,updesc,upadd,upcategory;
    RadioButton pgboys,pggirls;
    RadioGroup radioGroup;
    TextView headingtv,pgfor,addtv,desctv,selectimgtv;
    Button browsebtn,updatebtn,deletebtn;
    private static final int SELECTEDPIC = 1;
    private static final int RESULT_OK = -1;
    Bitmap pgimage;
    ProgressDialog pd;
    String u,uupdate,userpgid,udelete;
    String city,address,ownername,mobileno,rent,desc,title,category,image;

    Context context;


    public EditPost() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cview=inflater.inflate(R.layout.newpostad_layout, container, false);
        context=getContext();

        userpgid=getArguments().getString("Pgid");

        titleet=(EditText)cview.findViewById(R.id.editText9);
        cityet=(EditText)cview.findViewById(R.id.editText10);
        contactet=(EditText)cview.findViewById(R.id.editText11);
        addet=(EditText)cview.findViewById(R.id.editText12);
        descnet=(EditText)cview.findViewById(R.id.editText13);
        rentet=(EditText)cview.findViewById(R.id.editText15);

        pgboys=(RadioButton)cview.findViewById(R.id.radioButton);
        pggirls=(RadioButton)cview.findViewById(R.id.radioButton2);
        radioGroup=(RadioGroup)cview.findViewById(R.id.radiogroup);
        radioGroup.clearCheck();

        headingtv=(TextView)cview.findViewById(R.id.textView2);
        pgfor=(TextView)cview.findViewById(R.id.textView3);
        desctv=(TextView)cview.findViewById(R.id.textView4);
        addtv=(TextView)cview.findViewById(R.id.textView5);
        selectimgtv=(TextView)cview.findViewById(R.id.textViewselectimg);

        u="http://www.givebloodsavelife.org/pgtracker/getpginfo.php?pgid="+userpgid;
        new jsonParser().execute();

        browsebtn=(Button)cview.findViewById(R.id.button4);
        updatebtn=(Button)cview.findViewById(R.id.button5);
        deletebtn=(Button)cview.findViewById(R.id.button6);

        browsebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECTEDPIC);
            }
        });


        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uptitle = titleet.getText().toString();
                upcity = cityet.getText().toString();
                upcontact = contactet.getText().toString();
                updesc = descnet.getText().toString();
                upadd = addet.getText().toString();
                uprent = rentet.getText().toString();

                if (pgboys.isChecked()) {
                    upcategory = "Boys";
                } else {
                    upcategory = "Girls";
                }
                uupdate = "http://www.givebloodsavelife.org/pgtracker/updatepginfo.php?city=" + upcity + "&address=" + upadd + "&mobile=" + upcontact + "&rent=" + uprent + "&desc=" + updesc + "&title=" + uptitle + "&catagory=" + upcategory + "&image1=" + pgimage + "";
                new UpdatejsonParser().execute();
            }




    });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                udelete="http://www.givebloodsavelife.org/pgtracker/Delete1.php?id="+userpgid;
                new DeletejsonParser().execute();
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
            pd.setMessage(" loading...");
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
                JSONArray mainArray=new JSONArray(incommingjson);
                JSONObject jsonObject=mainArray.getJSONObject(0);
                userpgid=jsonObject.getString("PG_ID");
                city=jsonObject.getString("City");
                address=jsonObject.getString("Address");
                ownername=jsonObject.getString("Owner_Name");
                mobileno=jsonObject.getString("Mobile_No");
                rent=jsonObject.getString("Rent");
                desc=jsonObject.getString("Description");
                title=jsonObject.getString("Title");
                category=jsonObject.getString("Category");
                image=jsonObject.getString("image");




            }catch(Exception ex){
                Toast.makeText(getContext(),""+ex,Toast.LENGTH_LONG).show();
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
                URL url = new URL(uupdate);
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
                    Toast.makeText(context,"PG information updated successfully",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();


            }catch(Exception ex){
                Toast.makeText(context,""+ex,Toast.LENGTH_LONG).show();
            }
        }
    }

    class DeletejsonParser extends AsyncTask<Void,Void,Void>
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
                URL url = new URL(udelete);
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
                    Toast.makeText(context,"PG information deleted successfully",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();


            }catch(Exception ex){
                Toast.makeText(context,""+ex,Toast.LENGTH_LONG).show();
            }
        }
    }




}
