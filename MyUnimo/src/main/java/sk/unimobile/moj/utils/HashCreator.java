package sk.unimobile.moj.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by denis.valdman on 12. 12. 2016.
 */

public class HashCreator {

    public static void createHash(Activity activity){
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = activity.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = activity.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Logger.i("Package Name: "+ activity.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Logger.i("Key Hash: "+ key);

                byte[] cert = signature.toByteArray();
                InputStream input = new ByteArrayInputStream(cert);
                CertificateFactory cf = null;
                try {
                    cf = CertificateFactory.getInstance("X509");
                } catch (CertificateException e) {
                    e.printStackTrace();
                }
                X509Certificate c = null;
                try {
                    c = (X509Certificate) cf.generateCertificate(input);
                } catch (CertificateException e) {
                    e.printStackTrace();
                }
                String hexString = null;
                md = MessageDigest.getInstance("SHA1");
                byte[] publicKey = md.digest(c.getEncoded());
                hexString = byte2HexFormatted(publicKey);

                Logger.i("fingerprint: "+ hexString);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Logger.i("Name not found "+ e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Logger.i("No such an algorithm "+ e.toString());
        } catch (Exception e) {
            Logger.i("Exception", e.toString());
        }
    }


    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }

}
