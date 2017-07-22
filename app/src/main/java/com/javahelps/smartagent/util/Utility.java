package com.javahelps.smartagent.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.javahelps.smartagent.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gobinath on 10/29/15.
 */
public class Utility {


    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Check LocationService
    public static boolean isLocationServiceAvailable(Context ctx) {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

//    // Show alert dialog to confirm and enable the network
//    public static boolean askToEnableNetwork(final Context ctx) {
//        final boolean[] yes = new boolean[1];
//        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
//        builder.setMessage(R.string.internet_request_msg)
//                .setTitle("Unable to connect")
//                .setCancelable(false)
//                .setPositiveButton("Settings",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                Intent i = new Intent(
//                                        Settings.ACTION_WIRELESS_SETTINGS);
//                                ctx.startActivity(i);
//                                yes[0] = true;
//                            }
//                        })
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
////                                finish();
//                                yes[0] = false;
//                            }
//                        });
//        AlertDialog alert = builder.create();
//        alert.show();
//        return yes[0];
//    }

//    // Show alert dialog to confirm and enable the LocationService
//    public static boolean askToEnableLocationService(final Context ctx) {
//        final boolean[] yes = new boolean[1];
//        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
//        builder.setMessage(R.string.location_service_request_msg)
//                .setTitle("Unable to detect location")
//                .setCancelable(false)
//                .setPositiveButton("Settings",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                Intent i = new Intent(
//                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                ctx.startActivity(i);
//                                yes[0] = true;
//                            }
//                        })
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
////                                ctx.finish();
//                                yes[0] = false;
//                            }
//                        });
//        AlertDialog alert = builder.create();
//        alert.show();
//        return yes[0];
//    }
//
//    public static boolean askToEnableGPSservice(final Context ctx) {
//        final boolean[] yes = new boolean[1];
//        // Show alert dialog to confirm and enable the GPS service
//        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
//            builder.setMessage(R.string.gps_request_msg)
//                    .setTitle("Unable to detect location")
//                    .setCancelable(false)
//                    .setPositiveButton("Settings",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int id) {
//                                    Intent i = new Intent(
//                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                    ctx.startActivity(i);
//                                    yes[0] = true;
//                                }
//                            })
//                    .setNegativeButton("Cancel",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int id) {
//                                    yes[0] = false;
////                                    finish();
//                                }
//                            });
//            AlertDialog alert = builder.create();
//            alert.show();
//        }
//        return yes[0];
//    }

    public static Feature[] getAllFeatures(Context ctx) {
        Resources resources = ctx.getResources();
        TypedArray featureNames = resources.obtainTypedArray(R.array.feature_names);
        TypedArray featureDescriptions = resources.obtainTypedArray(R.array.feature_descriptions);
        TypedArray featureIcons = resources.obtainTypedArray(R.array.feature_icons);
        TypedArray featurePermission = resources.obtainTypedArray(R.array.feature_permissions);

        int noOfFeatures = featureNames.length();
        Feature[] features = new Feature[noOfFeatures];

        for (int i = 0; i < noOfFeatures; i++) {
            Feature feature = new Feature();
            feature.setName(featureNames.getString(i));
            feature.setDescription(featureDescriptions.getString(i));
            feature.setIcon(featureIcons.getDrawable(i));
            feature.setPermissions(featurePermission.getString(i).split(","));
            features[i] = feature;
        }

        featureNames.recycle();
        featureDescriptions.recycle();
        featureIcons.recycle();
        featurePermission.recycle();

        return features;
    }

    public static String[] nonGrantedPermissions(Context ctx) {
        List<String> nonGrantedPermissions = new ArrayList<>();

        // Android 6.0 or latest
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Resources resources = ctx.getResources();
            TypedArray featurePermission = resources.obtainTypedArray(R.array.feature_permissions);
            int size = featurePermission.length();

            for (int i = 0; i < size; i++) {
                String str = featurePermission.getString(i);
                // There can be more than one permissions associated with the feature
                String[] permissions = str.split(",");
                for (String permission : permissions) {
                    if (ctx.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        nonGrantedPermissions.add(permission);
                    }
                }
            }

            featurePermission.recycle();
        }

        return nonGrantedPermissions.toArray(new String[0]);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    public static byte[] serialize(Serializable obj) {
        try (ByteArrayOutputStream serialObj = new ByteArrayOutputStream();
             ObjectOutputStream objStream = new ObjectOutputStream(serialObj)) {
            objStream.writeObject(obj);
            return serialObj.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object deserialize(byte[] data) {
        try (ByteArrayInputStream serialObj = new ByteArrayInputStream(data);
             ObjectInputStream objStream = new ObjectInputStream(serialObj)) {
            return objStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
