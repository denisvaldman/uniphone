package sk.unimobile.moj;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PersistableBundle;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

public class SimDataChecker {

    static String NEWLINE = "\n";


    public SIM checkItOut(Context context) {

        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            imei = mTelephonyMgr.getImei();
        } else
            imei = mTelephonyMgr.getDeviceId();


        SIM simObj = new SIM();
        simObj.setHasPriv(mTelephonyMgr.hasCarrierPrivileges());
        simObj.setImei(imei);
        simObj.setIccid(mTelephonyMgr.getSimSerialNumber());
        simObj.setImsi(mTelephonyMgr.getSubscriberId());

        //PersistableBundle bndl = mTelephonyMgr.getCarrierConfig();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                simObj.setAuthentication(mTelephonyMgr.getIccAuthentication(TelephonyManager.APPTYPE_SIM, TelephonyManager.AUTHTYPE_EAP_SIM, ""));
            }
        }catch(Exception e){
            simObj.setAuthentication("No Carrier Privilege: No UICC");
        }

        return simObj;
    }

    public class SIM{
        private String imei;
        private String iccid;
        private boolean hasPriv;
        private String imsi;
        private String authentication;


        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getIccid() {
            return iccid;
        }

        public String composeData() {
            String result = "";
            StringBuilder sb = new StringBuilder();

            sb.append("has Carrier Privileges: ").append(getHasPriv()).append(NEWLINE);
            sb.append("ICCID: ").append(getIccid()).append(NEWLINE);
            sb.append("subscriber ID: ").append(getImsi()).append(NEWLINE);
            sb.append("imei: ").append(getImei()).append(NEWLINE);
            sb.append("IccAuth: ").append(getAuthentication()).append(NEWLINE);

            result = sb.toString();
            Logger.i("DATA: "+result);
            return result;
        }

        public void setIccid(String iccid) {
            this.iccid = iccid;
        }

        public boolean getHasPriv() {
            return hasPriv;
        }

        public void setHasPriv(boolean hasPriv) {
            this.hasPriv = hasPriv;
        }

        public String getImsi() {
            return imsi;
        }

        public void setImsi(String imsi) {
            this.imsi = imsi;
        }

        public String getAuthentication() {
            return authentication;
        }

        public void setAuthentication(String authentication) {
            this.authentication = authentication;
        }
    }

}
