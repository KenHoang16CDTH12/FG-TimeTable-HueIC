package app.it.hueic.nghiencuukhoahochueic.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.karan.churi.PermissionManager.PermissionManager;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.adapter.ListFileAdapter;
import app.it.hueic.nghiencuukhoahochueic.database.SQLiteController;
import app.it.hueic.nghiencuukhoahochueic.model.FileModel;
import app.it.hueic.nghiencuukhoahochueic.model.Para;
import app.it.hueic.nghiencuukhoahochueic.util.Config;

public class ParseExcelActivity extends AppCompatActivity {
    // Declare variables
    //private String[] FilePathStrings;
    //private String[] FileNameStrings;
    private File[] listFile;
    public static int count = 0;
    public static ArrayList<String> pathHistory;
    public static String lastDirectory = "";
    File file;
    //Button
    //private ImageView btnUpDirectory;
    //Progress
    private ProgressBar progressBar;
    //Toolbar
    private Toolbar toolbar;

    private ListView lvInternalStorage;
    private SQLiteController sqLiteController;
    PermissionManager permissionManager;
    ListFileAdapter adapter;
    //Update
    List<FileModel> fileModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_file_excel);
        //Permission manager
        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);
        lvInternalStorage = findViewById(R.id.lvInternalStorage);
        //btnUpDirectory =  findViewById(R.id.btnUpDirectory);
        //btnSDCard = (Button) findViewById(R.id.btnViewSDCard);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.tbList);
        toolbar.setTitle(R.string.import_file);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.outbox);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == 0){
                    Log.d(Config.TAG, "btnUpDirectory: You have reached the highest level directory.");
                } else {
                    pathHistory.remove(count);
                    count--;
                    checkInternalStorage();
                    Log.d(Config.TAG, "btnUpDirectory: " + pathHistory.get(count));
                }
            }
        });

        //Create arrPara
        //need to check the permissions
        //checkFilePermissions();

        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                lastDirectory = pathHistory.get(count);
                if(lastDirectory.equals(adapterView.getItemAtPosition(position).toString())){
                    Log.d(Config.TAG, "lvInternalStorage: Selected a file for upload: " + lastDirectory);
                    //Delete data para
                    sqLiteController = new SQLiteController();
                    sqLiteController.removeData();
                    //Execute method for reading the excel data.
                    lvInternalStorage.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    new AsyncInstallDatabase().execute(lastDirectory);
                } else {
                    count++;
                    pathHistory.add(count, adapterView.getItemAtPosition(position).toString());
                    checkInternalStorage();
                    Log.d(Config.TAG, "lvInternalStorage: " + pathHistory.get(count));
                }
            }
        });


    }


    private void checkInternalStorage() {
        Log.d(Config.TAG, "checkInternalStorage: Started.");
        try{
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                //toastMessage("No SD card found.");
            }
            else{
                // Locate the image folder in your SD Card
                file = new File(pathHistory.get(count));
                Log.d(Config.TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));
            }
            if (count == 0) {
                listFile = file.listFiles();
            } else {
                listFile = file.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return ( name.endsWith(".xls") || name.endsWith(".XLS"));
                    }
                });
            }

            fileModels = new ArrayList<>();
            FileModel fileModel = new FileModel();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].getName().endsWith(".xls") || listFile[i].getName().endsWith(".XLS")) {
                    fileModel = new FileModel(R.drawable.excel, listFile[i].getName(), listFile[i].getAbsolutePath());
                } else {
                    fileModel = new FileModel(R.drawable.folder, listFile[i].getName(), listFile[i].getAbsolutePath());
                }
                fileModels.add(fileModel);
            }
            for (int i = 0; i < listFile.length; i++)
            {
                Log.d("Files", "FileName:" + listFile[i].getName());
            }

            adapter = new ListFileAdapter(this, R.layout.item_folder, fileModels);
            adapter.notifyDataSetChanged();
            lvInternalStorage.setAdapter(adapter);

        } catch(NullPointerException e) {
            Log.e(Config.TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage() );
        }
    }


    public void readExcel(String pathFile) throws IOException {

        boolean isBeginDay = false;
        int currentCell = 0;
        int currentThu = 0;
        int nrows = 0;
        // Đọc một file XSL.
        FileInputStream inputStream = new FileInputStream(new File(pathFile));

        // Đối tượng workbook cho file XSL.
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

        // Lấy ra sheet đầu tiên từ workbook
        HSSFSheet sheet = workbook.getSheetAt(0);


        // Lấy ra Iterator cho tất cả các dòng của sheet hiện tại.
        Iterator<Row> rowIterator = sheet.iterator();

        Calendar calBegin = null;
        Calendar calEnd = null;
        int tietbatdau = 0;
        int tietkethuc = 0;
        String tenmonhoc = "";
        String tengiaovien = "";
        String phonghoc = "";

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            nrows++;
            if(nrows > Config.BEGIN_ROW){
                // Lấy Iterator cho tất cả các cell của dòng hiện tại.
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (currentCell > Config.END_COL)
                        currentCell = 0;

                    // Đổi thành getCellType() nếu sử dụng POI 4.x
                    CellType cellType = cell.getCellTypeEnum();

                    if(cellType == CellType.STRING){
                        String value = cell.getStringCellValue();
                        if(currentCell == 0)
                            currentThu = getThu(value);

                        if(currentCell == CellReference.convertColStringToIndex("A")){

                        }
                        if(currentCell == CellReference.convertColStringToIndex("B")){
                            //Xu li tiet hoc
                            String[] stiet = value.split("->");
                            tietbatdau = Integer.parseInt(stiet[0].trim());
                            tietkethuc = Integer.parseInt(stiet[1].trim());
                        }
                        if(currentCell == CellReference.convertColStringToIndex("D")){
                            //Lay ngay o day
                            String stringDate[] = value.split("->");
                            calBegin = getCalendar(stringDate[0]);
                            calEnd = getCalendar(stringDate[1]);
                        }
                        if(currentCell == CellReference.convertColStringToIndex("I")){
                            tenmonhoc = value.trim();
                        }
                        if(currentCell == CellReference.convertColStringToIndex("R")){

                        }
                        if(currentCell == CellReference.convertColStringToIndex("S")){
                            phonghoc = value.trim();
                        }
                        if(currentCell == CellReference.convertColStringToIndex("Y")){
                            tengiaovien = value.trim();
                            //Chen toan bo para co thoi gian calBegin den calEnd vao paraList
                            insertToList(calBegin, calEnd,
                                    currentThu, tietbatdau, tietkethuc,
                                    tenmonhoc, tengiaovien, phonghoc);
                        }
                    }
                    currentCell++;
                }
            }
        }
    }
    private  void insertToList(Calendar calBegin, Calendar calEnd, int dayWeek,
                               int tietbatdau, int tietketthuc, String tenmonhoc,
                               String tengiaovien, String phonghoc){
        int count = 0;
        int DAYS_COUNT = elapsed(calBegin, calEnd, Calendar.DAY_OF_MONTH);
        Calendar calendar = (Calendar)calBegin.clone();

        while(count < DAYS_COUNT){
            count++;
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek == dayWeek){
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                Para para = new Para(tietbatdau, tietketthuc,
                        tenmonhoc, tengiaovien, phonghoc,
                        year, month, day);
                para.save();
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public  int elapsed(Calendar before, Calendar after, int field) {
        Calendar clone = (Calendar) before.clone(); // Otherwise changes are been reflected.
        int elapsed = -1;
        while (!clone.after(after)) {
            clone.add(field, 1);
            elapsed++;
        }
        return elapsed;
    }
    private  int getThu(String value){
        switch (value){
            case Config.T2:
                return Calendar.MONDAY;
            case Config.T3:
                return Calendar.TUESDAY;
            case Config.T4:
                return Calendar.WEDNESDAY;
            case Config.T5:
                return Calendar.THURSDAY;
            case Config.T6:
                return Calendar.FRIDAY;
            case Config.T7:
                return Calendar.SATURDAY;
        }
        return 0;
    }

    private  Calendar getCalendar(String strDate){
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = simpleDateFormat.parse(strDate);
            cal.setTime(date);
        }catch (Exception ex){
            ex.toString();
        }

        return cal;
    }
    private class AsyncInstallDatabase extends AsyncTask<String, Void, Boolean>{


        @Override
        protected Boolean doInBackground(String... params) {
            String pathFile = params[0];
            try {
                readExcel(pathFile);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    MDToast.makeText(getApplicationContext(), getString(R.string.success_import_excel), MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                    Intent intent = new Intent(ParseExcelActivity.this, TimeTableActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    /**
     * Check permission
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);

        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;
        ArrayList<String> denied = permissionManager.getStatus().get(0).denied;

        //Update
        //Opens the SDCard or phone memory
        count = 0;
        pathHistory = new ArrayList<String>();
        pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
        Log.d(Config.TAG, "btnSDCard: " + pathHistory.get(count));
        checkInternalStorage();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("NewApi")
    private void checkFilePermissions() {
        System.out.println(Build.VERSION.SDK_INT + " && " + Build.VERSION_CODES.LOLLIPOP);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        }else{
            Log.d(Config.TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Opens the SDCard or phone memory
        count = 0;
        pathHistory = new ArrayList<String>();
        pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
        Log.d(Config.TAG, "btnSDCard: " + pathHistory.get(count));
        checkInternalStorage();
    }

}
