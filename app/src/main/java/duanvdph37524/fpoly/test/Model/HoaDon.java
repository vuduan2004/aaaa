package duanvdph37524.fpoly.test.Model;

import java.io.Serializable;

public class HoaDon implements Serializable {

    private int maHoaDon;
    private int maKhachHang;
    private String ngayBan;
    private int tongTien;

    public HoaDon(int maHoaDon, int maKhachHang, String ngayBan, int tongTien) {
        this.maHoaDon = maHoaDon;
        this.maKhachHang = maKhachHang;
        this.ngayBan = ngayBan;
        this.tongTien = tongTien;
    }

    public HoaDon() {
    }

    public String getNgayBan() {
        return ngayBan;
    }

    public void setNgayBan(String ngayBan) {
        this.ngayBan = ngayBan;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }


    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

}
