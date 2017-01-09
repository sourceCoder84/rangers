package toast.test.android.bpyc.com.myapplication.Util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by xyzj on 2017/1/3.
 */
public class StorageUtil {

//    出自：http://blog.csdn.net/androidwifi/article/details/17725989/
//    1.内部存储
//    2.外部存储
//        最容易混淆的是外部存储,如果说pc上也要区分出外部存储和内部存储的话,
//        那么自带的硬盘算是内部存储,U盘或者移动硬盘算是外部存储,因此我们很
//        容易带着这样的理解去看待安卓手机,认为机身固有存储是内部存储,而扩展
//        的FT卡是外部存储.比如我们任务16GB版本的Nexus 4有16G的内部存储,
//        普通消费者可以这样理解,但是安卓的编程中不能,这16GB仍然是外部存储.
//        所有的安卓设备都有外部存储和内部存储,这两个名称来源于安卓的早期设备,
//        那个时候的设备内部存储确实是固定的,而外部存储确实是可以像U盘一样移
//        动的.但是在后来的设备中,很多中高端机器都将自己的机身存储扩展到了8G以上,
//        他们将存储在概念上分成了"内部internal" 和"外部external" 两部分,
//        但其实都在手机内部.所以不管安卓手机是否有可移动的sdcard,他们总是有
//        外部存储和内部存储.最关键的是,我们都是通过相同的api来访问可移动的
//        sdcard或者手机自带的存储（外部存储）.外部存储虽然概念上有点复杂,
//        但也很好区分,你把手机连接电脑,能被电脑识别的部分就一定是外部存储.
//
//       关于外部存储,我觉得api中在介绍Environment.getExternalStorageDirectory()方法的时候说得很清楚：
//
//       don't be confused by the word "external" here. This directory can better be thought
//       as media/shared storage. It is a filesystem that can hold a relatively large amount
//       of data and that is shared across all applications (does not enforce permissions).
//       Traditionally this is an SD card, but it may also be implemented as built-in storage
//       in a device that is distinct from the protected internal storage and can be mounted
//       as a filesystem on a computer.

//   也就是说机身里面有一个存储设备，将这个存储设备一分为二一部分是内部存储，一部分是外部存储，同时我们还可以添加
//   FT卡，这样就相当于我们有两个FT卡, 而且第一个外部存储一般都是出于挂载状态，不需要进行是否挂载的判断


    private static final String TAG = StorageUtil.class.getName();

    private Context mContext;

    private static StorageUtil storageUtil;

    private static String block = new String();

    private StorageUtil() {
    }

    public void init(Context context) {

        mContext = context;
    }


    public static StorageUtil getInstance() {

        //双重检查锁定
        if (storageUtil == null) {
            synchronized (block) {

                if (storageUtil == null) {
                    storageUtil = new StorageUtil();
                }
            }
        }


        return storageUtil;
    }


//    public boolean createFile(String parent, String name , boolean isDir, boolean isInternal){
//
//        if(isInternal){
//
//
//
//        }else{
//
//            String state  = getStorageState(getSecondaryStoragePath());
//
//
//
//        }
//
//
//
//
//    }


    public void checkStorageState(){


//        Log.d(TAG, "The primary storage state = "+getStorageState(getPrimaryStoragePath()));
//        Log.d(TAG, "The second storage state = "+getStorageState(getSecondaryStoragePath()));
//        Log.d(TAG, "The second storage state = "+getStorageState(getSecondaryStoragePath()));

        String[] paths = getStoragePaths();

        for(int i = 0; i < paths.length; i++){

            Log.d(TAG, "The "+i+" storage state = "+getStorageState(paths[i]));

        }

    }


    public boolean delete(File file, boolean isExternal){

        boolean result = false;

        if(isExternal){

            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

                result = file.delete();

            }else{

                Log.e(TAG, "external storage is not mounted path = "+file.getAbsolutePath());
            }
        }else{

            mContext.deleteFile(file.getName());
        }

        return result;
    }


    private String[] getStoragePaths(){
        String[] paths = null;
        StorageManager manager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        try {
            Method methodGetPaths = manager.getClass().getMethod("getVolumePaths");
            paths = (String[]) methodGetPaths.invoke(manager);
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public String getPrimaryStoragePath() {
        try {
            StorageManager sm = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", null);
            String[] paths = (String[]) getVolumePathsMethod.invoke(sm, null);
            // first element in paths[] is primary storage path
            Log.d(TAG, "primary sdcard = "+Arrays.toString(paths));
            return paths[0];
        } catch (Exception e) {
            Log.e(TAG, "getPrimaryStoragePath() failed", e);
        }
        return null;
    }

    public String getSecondaryStoragePath() {
        try {
            StorageManager sm = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", null);
            String[] paths = (String[]) getVolumePathsMethod.invoke(sm, null);
            // second element in paths[] is secondary storage path
            Log.d(TAG, "second sdcard = "+paths[1]);
            return paths[1];
        } catch (Exception e) {
            Log.e(TAG, "getSecondaryStoragePath() failed", e);
        }

        return null;
    }

    public String getStorageState(String path) {
        try {
            StorageManager sm = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
            Method getVolumeStateMethod = StorageManager.class.getMethod("getVolumeState", new Class[] {String.class});
            String state = (String) getVolumeStateMethod.invoke(sm, path);
            return state;
        } catch (Exception e) {
            Log.e(TAG, "getStorageState() failed", e);
        }
        return null;
    }




    public void printFileTree(File file){

        ReadDirectory readDirectory = new ReadDirectory();

//        readDirectory.printDir(file.getAbsolutePath());
        readDirectory.readFile(file.getAbsolutePath());


    }


    public void deleteFile(File file){


        if(file.exists()){


            boolean result = file.delete();

            if(result){

                Log.d(TAG, "delete file success "+file.getAbsolutePath());
            }else{

                Log.d(TAG, "delete file failed "+file.getAbsolutePath());
            }

        }else{

            Log.d(TAG, "the file to delete is not exist"+file.getAbsolutePath());
        }

    }




    public void createTest(String parentPath, String name){

        Log.d(TAG, "parentPath = "+parentPath);

        ReadDirectory readDirectory = new ReadDirectory();

        File parent = new File(parentPath);

        if(!parent.exists()){

            Log.e(TAG, "parent file is not exist parent = "+parentPath);
            boolean result = parent.mkdirs();
            if(!result){

                Log.e(TAG, "create parent directory error");
            }else{
                Log.d(TAG, "create parent success");
            }


        }

        File file = new File(parent, name);

        if(!file.exists()){

            boolean result = file.mkdir();
            if(result){

                Log.d(TAG, "create dir success " + file.getAbsolutePath());
            }else{
                Log.d(TAG, "create dir failed " + file.getAbsolutePath());
            }

            readDirectory.readFile(parentPath);

            Log.d(TAG, "File.list = "+Arrays.toString(mContext.fileList()));


            file = new File(file, name);

            try {

                result = file.createNewFile();

            }catch (IOException ex){

                result = false;
            }

            Log.d(TAG, result? "create file success ": "create file failed");
            readDirectory.readFile(parentPath);
            Log.d(TAG, "File.list = "+Arrays.toString(mContext.fileList()));
            Log.d(TAG, "file absolutePath = "+file.getAbsolutePath());



            result = file.delete();

            Log.d(TAG, result? "delete file success ": "delete file failed");

            readDirectory.readFile(parentPath);
            Log.d(TAG, "File.list = "+Arrays.toString(mContext.fileList()));


            file = new File(parent, name);

            result = file.delete();

            Log.d(TAG, result? "delete directory success ": "delete dir failed");


            readDirectory.readFile(parentPath);

        }else{

            Log.d(TAG, "create error the file is already exist!! "+file.getAbsolutePath());
        }


    }




    public void createFileInCacheDir(String name){

        File file = new File(new File(mContext.getCacheDir(), "tiantian"+File.separator+"xiangshang"), name);

        if(!file.exists()){

            boolean result = false;

            try {

                result = file.createNewFile();

            }catch (Exception ex){

                ex.printStackTrace();
                Log.e(TAG, ex.getMessage());
                result = false;
            }

            if(result){

                Log.d(TAG, "create file success "+file.getAbsolutePath());
            }else{

                Log.d(TAG, "create file failed "+file.getAbsolutePath());
            }

        }else{

            Log.e(TAG, name + "is exist in internal cache dir "+file.getAbsolutePath());
        }

    }

    public void createDirInCacheDir(String name){


        File dir = new File(mContext.getCacheDir(), name);

        if(!dir.exists()){

            boolean result = dir.mkdirs();

            if(!result){
                Log.e(TAG, "create dir error by mkdirs in internal cache dir");

                Log.d(TAG, "try to use mkdir instead of mkdirs to create dir");

                result = dir.mkdir();

                if(!result){

                    Log.e(TAG, "create dir error by mkdir in internal cache dir");
                }else{

                    Log.e(TAG, "create dir success by mkdir in internal cache dir");
                }

            }else{

                Log.e(TAG, "create dir success by mkdirs in internal cache dir");
            }

        }else{

            Log.e(TAG, name + "is exist in internal cache dir "+dir.getAbsolutePath());
        }


    }

    public void test() {

//        mContext.getCodeCacheDir(); 21
//        mContext.getNoBackupFilesDir(); 21
//        File[] mediaDirs = mContext.getExternalMediaDirs();
//        String stateSpec = Environment.getExternalStorageState(new File());

        // 是在/data/data/<package>/文件夹下创建文件夹并在文件夹的名称前面加上app_
        File makeDir = mContext.getDir("tiantian", Context.MODE_PRIVATE);

//        mContext.deleteFile("fileName");
        String[] fileList = mContext.fileList();


//        mContext.openFileInput("tiantian.txt");
//        mContext.openFileOutput("tiantian.txt", Context.MODE_APPEND);
//        mContext.getFileStreamPath("tiantian");


        File obbDir = mContext.getObbDir();
        File[] obbDirs = mContext.getObbDirs();

        String packagePath = mContext.getPackageCodePath();


        File cacheDir = mContext.getCacheDir();
        File externalCacheDir = mContext.getExternalCacheDir();
        File[] externalCacheDirs = mContext.getExternalCacheDirs();


        File fileDir = mContext.getFilesDir();
        File externalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File[] externalFileDirs = mContext.getExternalFilesDirs(Environment.DIRECTORY_ALARMS);

        Log.d("StorageUtil", "makeDir = " + makeDir);
        Log.d("StorageUtil", "fileList = " + Arrays.toString(fileList));

        Log.d("StorageUtil", "packagePath = " + packagePath);

        Log.d("StorageUtil", "obbDir = " + obbDir);
        Log.d("StorageUtil", "obbDirs = " + Arrays.toString(obbDirs));

        Log.d("StorageUtil", " cacheDir = " + cacheDir);
        Log.d("StorageUtil", " cacheDir.list() = " + Arrays.toString(cacheDir.list()));
        Log.d("StorageUtil", " externalCacheDir = " + externalCacheDir);
        Log.d("StorageUtil", " externalCacheDirs = " + Arrays.toString(externalCacheDirs));


        Log.d("StorageUtil", " fileDir = " + fileDir);
        Log.d("StorageUtil", " fileDir.list() emulate  = " + Arrays.toString(new File("/data/data/toast.test.android.bpyc.com.myapplication/").list()));
        Log.d("StorageUtil", " fileDir.list() real  = " + Arrays.toString(fileDir.list()));
        Log.d("StorageUtil", " mContext.fileList()  = " + Arrays.toString(mContext.fileList()));
        Log.d("StorageUtil", "  mContext.getFileStreamPath(\"instant-run\")  = " + mContext.getFileStreamPath("instant-run"));


        Log.d("StorageUtil", " externalFileDir = " + externalFileDir);
        Log.d("StorageUtil", " externalFileDirs = " + Arrays.toString(externalFileDirs));

        File externalDir = Environment.getExternalStorageDirectory();
        File publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
//        ：传入的类型参数不能是null，返回的目录路径有可能不存在，所以必须在使用之前确认一下，比如使用File.mkdirs创建该路径。
        String state = Environment.getExternalStorageState();
        File enRootDir = Environment.getRootDirectory();
        File enDataDir = Environment.getDataDirectory();

        File enDownloadDir = Environment.getDownloadCacheDirectory();

        boolean isEmulated = Environment.isExternalStorageEmulated();

        boolean isRemovable = Environment.isExternalStorageRemovable();

        Log.d("StorageUtil", " externalDir = " + externalDir);
        Log.d("StorageUtil", " publicDir = " + publicDir);
        Log.d("StorageUtil", " state = " + state);
        Log.d("StorageUtil", " enRootDir = " + enRootDir);
        Log.d("StorageUtil", " enRootDir.list() = " + Arrays.toString(enRootDir.list()));

        Log.d("StorageUtil", " enDataDir = " + enDataDir);
        Log.d("StorageUtil", " enDataDir.list() = " + Arrays.toString(enDataDir.list()));

        Log.d("StorageUtil", " enDownloadDir = " + enDownloadDir);
        Log.d("StorageUtil", " enDownloadDir.list() = " + Arrays.toString(enDownloadDir.list()));

        Log.d("StorageUtil", " isEmulated = " + isEmulated);
        Log.d("StorageUtil", " isRemovable = " + isRemovable);





//        18.982 D/StorageUtil: makeDir = /data/data/toast.test.android.bpyc.com.myapplication/app_tiantian
//        18.983 D/StorageUtil: fileList = [instant-run]
//        18.983 D/StorageUtil: packagePath = /data/app/toast.test.android.bpyc.com.myapplication-1/base.apk
//        18.983 D/StorageUtil: obbDir = /mnt/internal_sd/Android/obb/toast.test.android.bpyc.com.myapplication
//        18.983 D/StorageUtil: obbDirs = [/mnt/internal_sd/Android/obb/toast.test.android.bpyc.com.myapplication]
//        18.983 D/StorageUtil:  cacheDir = /data/data/toast.test.android.bpyc.com.myapplication/cache
//        18.984 D/StorageUtil:  cacheDir.list() = [slice-support-annotations-23.4.0_7056e6427d9100a7aa7cb301c7bcef0bfd789ae9-classes.dex, slice-slice_9-classes.dex, slice-slice_8-classes.dex, slice-slice_7-classes.dex, slice-slice_6-classes.dex, slice-slice_5-classes.dex, slice-slice_4-classes.dex, slice-slice_3-classes.dex, slice-slice_2-classes.dex, slice-slice_1-classes.dex, slice-slice_0-classes.dex, slice-internal_impl-23.4.0_10d746c6158cca7149203bc94dabc8b98fff0776-classes.dex, slice-com.android.support-support-vector-drawable-23.4.0_f3c13fd564952f832137d73cf330b93d1a030558-classes.dex, slice-com.android.support-support-v4-23.4.0_e0e0b6e9468b0aa586b7b672ccb99c2335e52d99-classes.dex, slice-com.android.support-appcompat-v7-23.4.0_5f25fc38569ec7f67b82ffce5474e4943f34dd7f-classes.dex, slice-com.android.support-animated-vector-drawable-23.4.0_e141cafedfd4c78969a208a0cf716cc4c59adc60-classes.dex, com.android.opengl.shaders_cache, reload0x0000.dex]
//        18.984 D/StorageUtil:  externalCacheDir = /mnt/internal_sd/Android/data/toast.test.android.bpyc.com.myapplication/cache
//        18.984 D/StorageUtil:  externalCacheDirs = [/mnt/internal_sd/Android/data/toast.test.android.bpyc.com.myapplication/cache]
//        18.984 D/StorageUtil:  fileDir = /data/data/toast.test.android.bpyc.com.myapplication/files
//        18.985 D/StorageUtil:  fileDir.list() emulate  = [lib, cache, files, app_tiantian]
//        18.985 D/StorageUtil:  fileDir.list() real  = [instant-run]
//        18.986 D/StorageUtil:  mContext.fileList()  = [instant-run]
//        18.987 D/StorageUtil:   mContext.getFileStreamPath("instant-run")  = /data/data/toast.test.android.bpyc.com.myapplication/files/instant-run
//        18.987 D/StorageUtil:  externalFileDir = /mnt/internal_sd/Android/data/toast.test.android.bpyc.com.myapplication/files/Music
//        18.987 D/StorageUtil:  externalFileDirs = [/mnt/internal_sd/Android/data/toast.test.android.bpyc.com.myapplication/files/Alarms]
//        18.998 D/StorageUtil:  externalDir = /mnt/internal_sd
//        18.998 D/StorageUtil:  publicDir = /mnt/internal_sd/Alarms
//        18.998 D/StorageUtil:  state = mounted
//        18.998 D/StorageUtil:  enRootDir = /system
//        18.999 D/StorageUtil:  enRootDir.list() = [app, bin, build.prop, etc, fonts, framework, lib, lost+found, manifest.xml, media, preinstall, priv-app, tts, usr, vendor, xbin]
//        18.999 D/StorageUtil:  enDataDir = /data
//        18.999 D/StorageUtil:  enDataDir.list() = null
//        18.999 D/StorageUtil:  enDownloadDir = /cache
//        19.000 D/StorageUtil:  enDownloadDir.list() = null
//        19.000 D/StorageUtil:  isEmulated = false
//        19.000 D/StorageUtil:  isRemovable = true


    }


//    //通过StorageManager来获取多个sdcard，比使用cat /proc/mounts要好：
//    public static String[] getStoragePaths(Context cxt) {
//        List<String> pathsList = new ArrayList<String>();
//        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.GINGERBREAD) {
//            StringBuilder sb = new StringBuilder();
//            try {
//                pathsList.addAll(new SdCardFetcher().getStoragePaths(new FileReader("/proc/mounts"), sb));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                File externalFolder = Environment.getExternalStorageDirectory();
//                if (externalFolder != null) {
//                    pathsList.add(externalFolder.getAbsolutePath());
//                }
//            }
//        } else {
//            StorageManager storageManager = (StorageManager) cxt.getSystemService(Context.STORAGE_SERVICE);
//            try {
//                Method method = StorageManager.class.getDeclaredMethod("getVolumePaths");
//                method.setAccessible(true);
//                Object result = method.invoke(storageManager);
//                if (result != null && result instanceof String[]) {
//                    String[] pathes = (String[]) result;
//                    StatFs statFs;
//                    for (String path : pathes) {
//                        if (!TextUtils.isEmpty(path) && new File(path).exists()) {
//                            statFs = new StatFs(path);
//                            if (statFs.getBlockCount() * statFs.getBlockSize() != 0) {
//                                pathsList.add(path);
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                File externalFolder = Environment.getExternalStorageDirectory();
//                if (externalFolder != null) {
//                    pathsList.add(externalFolder.getAbsolutePath());
//                }
//            }
//        }
//        return pathsList.toArray(new String[pathsList.size()]);
//    }





    public static long getAvailableInternalMemorySize(File path) {


        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSizeLong();

        long availableBlocks = stat.getAvailableBlocksLong();

        Log.d(TAG, "availableBlocks === "+availableBlocks+"blockSize = "+blockSize+" size = "+availableBlocks * blockSize);

        Log.d(TAG, "size = "+(availableBlocks * blockSize) / (1024 * 1024));

        return availableBlocks * blockSize;

    }


    private void readFun() {

        File externalCachedir = mContext.getExternalCacheDir();

        BufferedReader reader = null;
        File file = new File(externalCachedir, "tiantian.txt");
        if (externalCachedir.exists()) {

            Log.d("StorageUtil", externalCachedir + "  is exist !!");
        } else {
            Log.d("StorageUtil", externalCachedir + "  is not exist !!");

            return;
        }

        try {

            reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();

            Log.d("StorageUtil", "The result = " + line);

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            Log.d("StorageUtil", "e.getMessage() = " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();

            Log.d("StorageUtil", "e.getMessage() = " + e.getMessage());

        } finally {

            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    private void writeFun() {

        File externalCachedir = mContext.getExternalCacheDir();

        BufferedWriter writer = null;
        File file = new File(externalCachedir, "tiantian.txt");
        if (externalCachedir.exists()) {

            Log.d("StorageUtil", externalCachedir + "  is exist !!");
        } else {
            Log.d("StorageUtil", externalCachedir + "  is not exist !!");
            externalCachedir.mkdirs();
        }

        try {


            writer = new BufferedWriter(new FileWriter(file));
            writer.write("hao hao xue xi , tian tian xiang shang");

            writer.flush();
            writer.flush();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                if (writer != null)
                    writer.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
