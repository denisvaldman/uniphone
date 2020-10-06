package sk.unimobile.moj;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PersistableBundle;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.TelephonyManager;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;

import java.io.UnsupportedEncodingException;

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

        public ResponseObject composeData() {
            String result = "";
            StringBuilder sb = new StringBuilder();

            sb.append("has Carrier Privileges: ").append(getHasPriv()).append(NEWLINE);
            sb.append("ICCID: ").append(getIccid()).append(NEWLINE);
            sb.append("subscriber ID: ").append(getImsi()).append(NEWLINE);
            sb.append("imei: ").append(getImei()).append(NEWLINE);
            sb.append("IccAuth: ").append(getAuthentication()).append(NEWLINE);

            result = sb.toString();
            Logger.i("DATA: "+result);
            return new ResponseObject(result,getHasPriv());
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

    public String impl1apdu1(Context context) throws Exception{
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = telephonyManager.iccOpenLogicalChannel("A000000151000000",0x00);
            int channelNo = iccOpenLogicalChannelResponse.getChannel();
            //# You may get something like this 6F1F8410A0000005591010FFFFFFFF8900000100A5049F6501FFE00582030202009000, & you can ignore it.
            telephonyManager.iccTransmitApduLogicalChannel(channelNo,0x00,0xA4,0x04,0x00,0x10,"A0000001249921F2300100014D4F5E00");// applet selection command

            String returnVal = telephonyManager.iccTransmitApduLogicalChannel(channelNo,0x00,0x42,0x00,0x00,0x00,"");

            boolean isIccLogicalChannelClosed = telephonyManager.iccCloseLogicalChannel(channelNo);
            return returnVal;

        }catch (NoSuchMethodError e){
            throw new Exception();
        }
    }

//    public String impl1apdu2(Context context)throws Exception{
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//        try{
//            IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = telephonyManager.iccOpenLogicalChannel("A000000151000000",0x00);
//            int channelNo = iccOpenLogicalChannelResponse.getChannel();
//            //# You may get something like this 6F1F8410A0000005591010FFFFFFFF8900000100A5049F6501FFE00582030202009000, & you can ignore it.
//            telephonyManager.iccTransmitApduLogicalChannel(channelNo,0x00,0xA4,0x04,0x00,0x10,"A0000001249921F2300100014D4F5E00");
//
//            int data = 0x01;
//            String returnVal = telephonyManager.iccTransmitApduLogicalChannel(channelNo,0x00,0x43,0x00,0x00,0x01, Integer.toString(data));
//
//            boolean isIccLogicalChannelClosed = telephonyManager.iccCloseLogicalChannel(channelNo);
//            return returnVal;
//
//        } catch (NoSuchMethodError e){
//            throw new Exception();
//        }
//
//    }

    public String impl1apdu2(Context context)throws Exception{
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try{
            IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = telephonyManager.iccOpenLogicalChannel("A000000151000000",0x00);
            int channelNo = iccOpenLogicalChannelResponse.getChannel();
            //# You may get something like this 6F1F8410A0000005591010FFFFFFFF8900000100A5049F6501FFE00582030202009000, & you can ignore it.
            telephonyManager.iccTransmitApduLogicalChannel(channelNo,0x00,0xA4,0x04,0x00,0x10,"A0000001249921F2300100014D4F5E00");// applet selection command


            String data = "01";
            String returnVal = telephonyManager.iccTransmitApduLogicalChannel(channelNo,0x00,0x43,0x00,0x00,0x01, data);

            boolean isIccLogicalChannelClosed = telephonyManager.iccCloseLogicalChannel(channelNo);
            return returnVal;

        } catch (NoSuchMethodError e){
            throw new Exception();
        }
    }


//    public String implapdu3(Context context)throws Exception{
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//        try {
//            IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = telephonyManager.iccOpenLogicalChannel("A000000151000000",0x00);
//            int channelNo = iccOpenLogicalChannelResponse.getChannel();
//
//            int data = 0x00;
//            String returnVal = telephonyManager.iccTransmitApduLogicalChannel(channelNo,0x00,0x43,0x00,0x00,0x01, Integer.toString(data));
//
//            boolean isIccLogicalChannelClosed = telephonyManager.iccCloseLogicalChannel(channelNo);
//            return returnVal;
//        } catch (NoSuchMethodError e){
//            throw new Exception();
//        }
//
//    }

    public String implapdu3(Context context)throws Exception{
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try{
            IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = telephonyManager.iccOpenLogicalChannel("A000000151000000",0x00);
            int channelNo = iccOpenLogicalChannelResponse.getChannel();
            //# You may get something like this 6F1F8410A0000005591010FFFFFFFF8900000100A5049F6501FFE00582030202009000, & you can ignore it.
            telephonyManager.iccTransmitApduLogicalChannel(channelNo,0x00,0xA4,0x04,0x00,0x10,"A0000001249921F2300100014D4F5E00");// applet selection command


            String data = "00";
            String returnVal = telephonyManager.iccTransmitApduLogicalChannel(channelNo,0x00,0x43,0x00,0x00,0x01, data);

            boolean isIccLogicalChannelClosed = telephonyManager.iccCloseLogicalChannel(channelNo);
            return returnVal;

        } catch (NoSuchMethodError e){
            throw new Exception();
        }
    }

//    public String impl2apdu1(Context context)throws Exception{
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//        try {
//            IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = telephonyManager.iccOpenLogicalChannel("A0000001249921F2300100014D4F5E00",0x00);
//            int channelNo = iccOpenLogicalChannelResponse.getChannel();
//
//            return telephonyManager.iccTransmitApduLogicalChannel(channelNo,0x00,0x42,0x00,0x00,0x00,"00");
//        } catch (NoSuchMethodError e){
//            throw new Exception();
//        }
//
//    }
//
//    public String impl2apdu2(Context context)throws Exception{
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//        try {
//            IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = telephonyManager.iccOpenLogicalChannel("A0000001249921F2300100014D4F5E00",0x00);
//            int channelNo = iccOpenLogicalChannelResponse.getChannel();
//
//            return telephonyManager.iccTransmitApduLogicalChannel(channelNo,0x00,0x43,0x00,0x00,0x01, "01");
//        } catch (NoSuchMethodError e){
//            throw new Exception();
//        }
//
//    }


//    public String customApdu(EditText claET, EditText insET ,EditText p1ET, EditText p2ET, EditText p3ET, EditText dataET, Context context) throws Exception {
//
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//        try{
//
//            int cla = getInt(claET);
//            int ins = getInt(insET);
//            int p1 = getInt(p1ET);
//            int p2 = getInt(p2ET);
//            int p3 = getInt(p3ET);
//            String data = String.valueOf(dataET.getText());
//
//            IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = telephonyManager.iccOpenLogicalChannel("A000000151000000",0x00);
//            int channelNo = iccOpenLogicalChannelResponse.getChannel();
//            //# You may get something like this 6F1F8410A0000005591010FFFFFFFF8900000100A5049F6501FFE00582030202009000, & you can ignore it.
//
//            // remove below one completely
//            telephonyManager.iccTransmitApduLogicalChannel(channelNo,0x00,0xA4,0x04,0x00,0x10,"A0000001249921F2300100014D4F5E00");// applet selection command
//
//
//
//            String returnVal = telephonyManager.iccTransmitApduLogicalChannel(channelNo,cla,ins,p1,p2,p3, data);
//
//            boolean isIccLogicalChannelClosed = telephonyManager.iccCloseLogicalChannel(channelNo);
//            return returnVal;
//
//        } catch (NoSuchMethodError e){
//            throw new Exception();
//        }
//
//    }

    int m_channelNo;
    TelephonyManager m_telephonyManager;
    public void initiateSession(Context context)
    {
        m_telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = m_telephonyManager.iccOpenLogicalChannel("A000000151000000",0x00);
        m_channelNo = iccOpenLogicalChannelResponse.getChannel();
        //# You may get something like this 6F1F8410A0000005591010FFFFFFFF8900000100A5049F6501FFE00582030202009000, & you can ignore it.
    }

    public Boolean closeSession()
    {
        if(m_telephonyManager == null) return true;
        boolean isIccLogicalChannelClosed = m_telephonyManager.iccCloseLogicalChannel(m_channelNo);
        return isIccLogicalChannelClosed;
    }


    public String customApdu(EditText claET, EditText insET ,EditText p1ET, EditText p2ET, EditText p3ET, EditText dataET, Context context) throws Exception {

        if(m_telephonyManager == null) initiateSession(context);

        try{

            int cla = getInt(claET);
            int ins = getInt(insET);
            int p1 = getInt(p1ET);
            int p2 = getInt(p2ET);
            int p3 = getInt(p3ET);
            String data = String.valueOf(dataET.getText());

            String returnVal = m_telephonyManager.iccTransmitApduLogicalChannel(m_channelNo,cla,ins,p1,p2,p3, data);
            return returnVal;

        } catch (NoSuchMethodError e){
            throw new Exception();
        }
    }

    public int getInt(EditText et){
        int retVal = 0;

        try {
            //retVal = Integer.parseInt(HexStringConverter.getHexStringConverterInstance().stringToHex(et.getText().toString()));
            String string = et.getText().toString();
            if(string.startsWith("0x") || string.startsWith("0X"))
                string = string.substring(2);
            Logger.i("Parsed input "+string);
            retVal = Integer.parseInt(string,16);
        }
        catch(NumberFormatException e){
            Logger.e("Error "+e.getMessage());
            et.setError("!");
//        } catch (UnsupportedEncodingException e) {
//            Logger.e(e.getMessage());
//            et.setError("!");
        }
        Logger.i("return Value getInt "+retVal);
        return retVal;
    }

}
