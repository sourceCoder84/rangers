package toast.test.android.bpyc.com.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;

import toast.test.android.bpyc.com.myapplication.Util.MemoryStatus;
import toast.test.android.bpyc.com.myapplication.Util.StorageUtil;


public class MainActivity extends AppCompatActivity {

    private TextView tiantian;
    private ListView listView;

    private LayoutInflater inflater;

    private String[] array = new String[]{"getCacheDir", "getDir", "deleteFile", "fileList"};

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private int selectedIndex;

    private static String dialogResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        StorageUtil.getInstance().createDirInCacheDir("tiantian/xiangshang");

//        StorageUtil.getInstance().createFileInCacheDir("haohaoxuexi.txt");
//        StorageUtil.getInstance().printFileTree(this.getCacheDir());


//        StorageUtil.getInstance().deleteFile(new File(new File(getCacheDir(), "tiantian"+File.separator+"xiangshang"), "haohaoxuexi.txt"));
//        StorageUtil.getInstance().printFileTree(new File(StorageUtil.getInstance().getSecondaryStoragePath()));
//        StorageUtil.getInstance().printFileTree(new File(StorageUtil.getInstance().getPrimaryStoragePath()));

//        Log.d("MainActivity", new File(StorageUtil.getInstance().getSecondaryStoragePath()).list().toString());

//        Log.d("MainActivity", Arrays.toString(new File(StorageUtil.getInstance().getSecondaryStoragePath()).list()));

//        StorageUtil.getInstance().checkStorageState();

//        StorageUtil.getInstance().getAvailableInternalMemorySize(getFilesDir());
//
//        Log.d("StorageUtil", "--------------------------------------Envirenment.getDataDirectory() "+Environment.getDataDirectory());
//        StorageUtil.getInstance().getAvailableInternalMemorySize(Environment.getDataDirectory());

        MemoryStatus.test();;

        StorageUtil.getInstance().createTest(getFilesDir().getAbsolutePath(), "women");


//        StorageUtil.getInstance().printFileTree(this.getCacheDir());

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        tiantian = (TextView)findViewById(R.id.tiantian);
        listView = (ListView)findViewById(R.id.left_drawer);

        listView.setAdapter(new MyAdapter());

        inflater = LayoutInflater.from(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switchToFunction(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name,
                R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);



    }

//        mContext.getCodeCacheDir(); 21
//        mContext.getNoBackupFilesDir(); 21


//    File cacheFile = mContext.getCacheDir();
//
//    File tiantian = mContext.getDir("tiantian", Context.MODE_PRIVATE);
//
//    mContext.deleteFile("fileName");
//    String[] fileList = mContext.fileList();
//
//    File fileDir = mContext.getFilesDir();
//    Log.d("StorageUtil", fileDir.getAbsolutePath());
//
//
//    mContext.openFileInput("tiantian.txt");
//    mContext.openFileOutput("tiantian.txt", Context.MODE_APPEND);
//
//    mContext.getObbDir();
//    mContext.getObbDirs();
//
//    String packagePath = mContext.getPackageCodePath();
//
//
//    File externalCacheDir = mContext.getExternalCacheDir();
//    File[] cacheFiles = mContext.getExternalCacheDirs();
//
//
//    File externalFile = mContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//    File[] externalFiles = mContext.getExternalFilesDirs(Environment.DIRECTORY_ALARMS);
//
//    File[] mediaDirs = mContext.getExternalMediaDirs();
//
//
//    File externalStorage = Environment.getExternalStorageDirectory();
//
//
//    mContext.getFileStreamPath("tiantian");
//
//    Log.d("StorageUtil", " cacheFile = " + cacheFile);
//    Log.d("StorageUtil", " tiantian = " + tiantian);
//    Log.d("StorageUtil", "fileList = " + Arrays.toString(fileList));
//    Log.d("StorageUtil", "externalCachedir = " + externalCacheDir);
//    Log.d("StorageUtil", "externalFile = " + externalFile);
//
//    Log.d("StorageUtil", "externalStorage = " + externalStorage);

    private void switchToFunction(int position){

        switch (position){

            case 1:
//                getCacheDir
                File cacheDir = getCacheDir();
                Log.d("MainActivity", "cacheDir = "+cacheDir.getAbsolutePath());
                break;
            case 2:
//                getDir
                showDialog("请输入文件夹名称");
                try{

                    File file = getDir(dialogResult, Context.MODE_PRIVATE);
                    if(file != null){
                        if(file.exists()){

                            Log.d("TAG", "File is exist!!!"+file.getAbsolutePath());
                        }else{

                            Log.d("TAG", "File is not exist!!!"+file.getAbsolutePath());
                        }

                    }else{

                        Log.d("TAG", "File  == null");
                    }

                }catch (Exception e){

                    e.printStackTrace();
                }


                break;
            case 3:
//                deleteFile

                break;
            case 4:
//                fileList

                break;
            case 5:

                break;



        }
    }


    private void showMessage(String message){

        tiantian.setText(message);
    }

    private void showDialog(String message){

        final EditText text = new EditText(MainActivity.this);

        new AlertDialog.Builder(this).setTitle(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(text).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialogResult = text.getText().toString();

            }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();

    }








    private class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public Object getItem(int position) {
            return array[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if(convertView == null){

                convertView = inflater.inflate(R.layout.list_item, null);
                TextView view = (TextView) convertView.findViewById(R.id.item);
                view.setText(array[position]);
                convertView.setTag(view);

            }else{

                TextView view = (TextView)convertView.getTag();
                view.setText(array[position]);
            }

            return convertView;
        }
    }

}
