package toast.test.android.bpyc.com.myapplication.Util;

import android.content.Context;

/**
 * Created by xyzj on 2017/1/4.
 */
public class FileUtil {



    private static FileUtil fileUtil = null;

    private static String block = new String();


    public Context mContext;

    private FileUtil(){}


    public static FileUtil getInstance(){

        if(fileUtil == null){

            synchronized (block){

                if(fileUtil == null){

                    fileUtil = new FileUtil();
                }
            }
        }

        return fileUtil;
    }

    public void init(Context context){
        this.mContext = context;
    }










}
