package app.it.hueic.nghiencuukhoahochueic.database;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import app.it.hueic.nghiencuukhoahochueic.model.Para;

/**
 * Created by kenhoang on 5/19/17.
 */

public class SQLiteController {
    public List<Para> getParaByMonth(int year, int month){
        List<Para> rs = new Select()
                .from(Para.class)
                .where("year = ?", year)
                .and("month = ?", month).execute();
        return rs;
    }
    public List<Para> getParaByDate(int year, int month, int day){
        return new Select().from(Para.class).where("year = ?",year).and("month = ?", month).and("day = ?", day).execute();
    }
    public List<Para> removeData(){
        return new Delete().from(Para.class).execute();
    }
    public List<Para> getParaAll(){
        return new Select().from(Para.class).execute();
    }

}
