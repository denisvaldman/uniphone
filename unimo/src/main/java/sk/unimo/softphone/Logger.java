package sk.unimo.softphone;

import android.util.Log;



/**
 * Created by denis.valdman on 4. 12. 2015.
 */
public class Logger {
    private static boolean isDebugEnabled = BuildConfig.DEBUG;
    private static final String TAG = "AppSuite";
    private static int bufferSize = 4055;

    public  static void initialize(boolean isDebugEnabled){
        Logger.isDebugEnabled = isDebugEnabled;
    }

    public static void i(String tag, String msg){
        if(isDebugEnabled) {
            do {
                Log.i(tag, msg.substring(0, msg.length() > bufferSize ? bufferSize : msg.length()));
                msg = msg.substring(msg.length() > bufferSize?bufferSize:msg.length(),msg.length());
            }while(msg.length() > 0);
        }
    }

    public static void i(String msg){
        i(TAG, msg);
    }

    public static void e(String tag, String msg){
        try{
//            errors will be logged always to make it easier to debug released apps
//            if(isDebugEnabled) {
                do {
                    Log.e(tag, msg.substring(0, msg.length() > bufferSize ? bufferSize : msg.length()));
                    msg = msg.substring(msg.length() > bufferSize?bufferSize:msg.length(),msg.length());
                }while(msg.length() > 0);
//            }
        }catch (Exception e){
        }
    }

    public static void e(String msg){
        e(TAG, msg);
    }

    public static void e(Throwable e){
        if (null != e) {
            e(TAG, e.getClass()+": "+e.getMessage());
            e.printStackTrace();
        } else {
            e(TAG, "Exception was null!");
        }
    }

    public static void e(String message, Throwable e) {
        Log.e(TAG, message, e);
    }

    public static void d(String tag, String msg){
        if(isDebugEnabled) {
            //add class and method name if possible
            try {
                StackTraceElement element = Thread.currentThread().getStackTrace()[4];
                msg = element.getClassName() + "." + element.getMethodName() + " : " + msg;
            } catch (Exception e) {}
            do {
                Log.d(tag, msg.substring(0, msg.length() > bufferSize ? bufferSize : msg.length()));
                msg = msg.substring(msg.length() > bufferSize?bufferSize:msg.length(),msg.length());
            }while(msg.length() > 0);
        }
    }

    public static void d(String msg){
        d(TAG, msg);
    }
}

