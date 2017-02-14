package utility.classified.wijaysharma.easypg;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostAddNew extends Fragment {

    EditText titleet,cityet,descnet,addet,contactet,rentet;
    RadioButton pgboys,pggirls;
    RadioGroup radioGroup;
    TextView headingtv,pgfor,addtv,desctv,selectimgtv;
    Button browsebtn,postbtn;
    private static final int SELECTEDPIC = 1;
    private static final int RESULT_OK = -1;
    ProgressDialog pd;
    String u;
    String username;
    Context context;
    private static int RESULT_LOAD_IMG = 1;
    private String imgDecodableString,imagePath ;
    ProgressDialog progressDialog ;
    String title, city, contact, desc, add,rent;
    String catagory="";

    public PostAddNew() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View cview=inflater.inflate(R.layout.newpostad_layout, container, false);
        context=getContext();

        username=getArguments().getString("Userid");
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

        browsebtn=(Button)cview.findViewById(R.id.button4);
        postbtn=(Button)cview.findViewById(R.id.button5);

        browsebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECTEDPIC);
            }
        });



        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = titleet.getText().toString();
                city = cityet.getText().toString();
                contact = contactet.getText().toString();
                desc = descnet.getText().toString();
                add = addet.getText().toString();
                rent=rentet.getText().toString();

                if (pgboys.isChecked()) {
                    catagory = "Boys";
                }
                else {
                    catagory = "Girls";
                }

/*
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                pgimage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                 */


                //u = "http://www.givebloodsavelife.org/pgtracker/ad1.php?name="+username+"&rent="+rent+"&image1="+pgimage+"&mobile="+contact+"&address="+add+"&desc="+desc+"&title="+title+"&catagory="+catagory+"&city="+city;
                // new jsonParser().execute();

                new UploadAsyntask().execute();

            }});

        return cview;

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                imagePath = getRealPathFromURI(selectedImage);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getActivity().getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    private String uploadFile() {
        String url = "http://www.givebloodsavelife.org/pgtracker/ad1.php" ;
        HttpClient httpclient;
        HttpPost httppost;
        String responseString = null;
        httpclient = new DefaultHttpClient();
        httppost = new HttpPost(url);


        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                @Override
                public void transferred(long num) {
                    //publishProgress((int) ((num / (float) totalSize) * 100));
                }
            });
            // Adding file data to http body
            File source = new File(imagePath);
            entity.addPart("fileToUpload", new FileBody(source));
            entity.addPart("city",new StringBody(city) );
            entity.addPart("address", new StringBody(add));
            entity.addPart("name", new StringBody(username));
            entity.addPart("mobile", new StringBody(contact));
            entity.addPart("rent", new StringBody(rent));
            entity.addPart("desc", new StringBody(desc));
            entity.addPart("title", new StringBody(title));
            entity.addPart("category", new StringBody(catagory));

            long totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
                JSONObject jsonObject = new JSONObject(responseString);


            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    private class UploadAsyntask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Uploading");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            uploadFile();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
        }
    }

}
