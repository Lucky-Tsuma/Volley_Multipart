import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    private Uri filePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        upload();
    }

    private void upload() {
        InputStream inputStream;
        try {
            inputStream = getContentResolver().openInputStream(filePath);
            byte[] inputData = getBytesFromInputStream(inputStream);

            VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "url_comes_here", response -> {
                try {
                    /* below is  */
                    String results = new String(response.data);
                    
                    /* Youcan convert it to json as below */
                    JSONObject jsonObject = new JSONObject(results);
                    
                    /* continue from here */

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Log.e("BUG_HERE", error.getMessage(), error);
            }) {
                @Override
                protected Map<String, String> getParams() {
                    /* the normal post data comes here e.g */
                    pms.put("key", "value");
                    return pms;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    //long ctm = System.currentTimeMillis();
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("food", new DataPart(fudname, inputData));
                    return params;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, 1.0f));
            Volley.newRequestQueue(getApplicationContext()).add(request);

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File Not Found! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public byte[] getBytesFromInputStream(InputStream isd) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = isd.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
