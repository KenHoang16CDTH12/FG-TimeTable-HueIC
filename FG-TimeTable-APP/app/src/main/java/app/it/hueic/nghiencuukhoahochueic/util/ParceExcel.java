package app.it.hueic.nghiencuukhoahochueic.util;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import app.it.hueic.nghiencuukhoahochueic.model.Para;

/**
 * Created by kenhoang on 11/01/2018.
 */

public class ParceExcel {
    /**
     * Handle parse Excel
     * @param pathFile
     * @throws IOException
     */
    public static void readExcel(String pathFile) throws IOException {

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
    public static void insertToList(Calendar calBegin, Calendar calEnd, int dayWeek,
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

    public static int elapsed(Calendar before, Calendar after, int field) {
        Calendar clone = (Calendar) before.clone(); // Otherwise changes are been reflected.
        int elapsed = -1;
        while (!clone.after(after)) {
            clone.add(field, 1);
            elapsed++;
        }
        return elapsed;
    }
    public static int getThu(String value){
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

    public static Calendar getCalendar(String strDate){
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


}
