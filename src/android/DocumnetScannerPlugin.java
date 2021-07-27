package cordova.plugin.documentscannerplugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import com.example.mylibrary.MainActivity;
import com.example.mylibrary.StorageControler;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class DocumnetScannerPlugin extends CordovaPlugin {

  CallbackContext callbackContext;
  boolean base64 = false;
  int targetHeight = 0;
  int targetWidth = 0;
  int sourceType = 0;
  int quality=0;
  boolean CROP_ENABLE = false;

  @Override
  public boolean execute(
    String action,
    JSONArray args,
    CallbackContext callbackContext
  )
    throws JSONException {
    JSONObject jsonObject = args.getJSONObject(0);
    this.callbackContext = callbackContext;
    base64 = false;
    cordova.setActivityResultCallback(this);

    base64 = jsonObject.getBoolean("base64");
    Intent intent = new Intent(
      cordova.getActivity().getApplicationContext(),
      MainActivity.class
    );
    CROP_ENABLE = jsonObject.getBoolean("CROP_ENABLE");
    targetHeight = jsonObject.getInt("targetHeight");
    targetWidth = jsonObject.getInt("targetWidth");
    sourceType = jsonObject.getInt("sourceType");
    quality=jsonObject.getInt("quality");
    intent.putExtra("sourceType", sourceType);
    intent.putExtra("CROP_ENABLE", CROP_ENABLE);
    intent.putExtra("targetHeight", targetHeight);
    intent.putExtra("targetWidth", targetWidth);
    intent.putExtra("edgeDetection", jsonObject.getBoolean("edgeDetection"));
    

    cordova.getActivity().startActivityForResult(intent, 400);
    PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
    r.setKeepCallback(true);
    callbackContext.sendPluginResult(r);
    return true;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.e("cordova", "onActivityResult: " + resultCode);
    if (resultCode == 401 && requestCode == 400) {
      Bundle extras = data.getExtras(); // Get data sent by the Intent
      if (extras != null) {
        String information = extras.getString("filepath");
        if (information != null) {
          if (base64) {
            try {
              information =
                StorageControler.getFileToByte(
                  cordova.getActivity().getApplicationContext(),
                  Uri.parse(information),targetWidth,targetHeight,qua
                );
            } catch (Exception e) {}
          }
          PluginResult resultado = new PluginResult(
            PluginResult.Status.OK,
            information
          );
          resultado.setKeepCallback(true);
          this.callbackContext.sendPluginResult(resultado);
          //this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, information));

          return;
        } else {
          PluginResult resultado = new PluginResult(
            PluginResult.Status.NO_RESULT
          );
          resultado.setKeepCallback(true);
          this.callbackContext.sendPluginResult(resultado);
        }
      } else {
        PluginResult resultado = new PluginResult(
          PluginResult.Status.NO_RESULT
        );
        resultado.setKeepCallback(true);
        this.callbackContext.sendPluginResult(resultado);
      }
    }
  }
}
