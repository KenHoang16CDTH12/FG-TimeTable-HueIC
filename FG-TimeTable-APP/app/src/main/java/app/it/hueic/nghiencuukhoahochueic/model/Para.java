package app.it.hueic.nghiencuukhoahochueic.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by kenhoang on 5/18/17.
 */

@Table(name = "para")
public class Para extends Model {

    @Column(name = "tietbatdau")
    public int tietbatdau;

    @Column(name = "tietketthuc")
    public int tietkethuc;

    @Column(name = "tenmonhoc")
    public String tenmonhoc;

    @Column(name = "phonghoc")
    public String phonghoc;

    @Column(name = "tengiaovien")
    public String tengiaovien;

    @Column(name = "day")
    public int day;

    @Column(name = "month")
    public int month;

    @Column(name = "year")
    public int year;

    public int tongTiet() {
        return tietkethuc - tietbatdau + 1;
    }

    public Para(int tietbatdau, int tietkethuc, String tenmonhoc,
                String phonghoc, String tengiaovien,
                int year, int month, int day) {
        this.tietbatdau = tietbatdau;
        this.tietkethuc = tietkethuc;
        this.tenmonhoc = tenmonhoc;
        this.phonghoc = phonghoc;
        this.tengiaovien = tengiaovien;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Para() {
    }
}
