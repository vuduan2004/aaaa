package com.DuAn1.techstore.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DuAn1.techstore.Adapter.Adapter_ChiTietHoaDon;
import com.DuAn1.techstore.DAO.Server;
import com.DuAn1.techstore.Model.ChiTietHoaDon;
import com.DuAn1.techstore.Model.HoaDon;
import com.DuAn1.techstore.Model.Loading;
import com.DuAn1.techstore.Model.SanPham;
import com.DuAn1.techstore.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_ChiTietHoaDon extends AppCompatActivity {
    private Toolbar toolbar;
    private HoaDon hoaDon;
    private ChiTietHoaDon chiTietHoaDon;
    private SanPham sanPham;
    private ArrayList<SanPham> lstSP;
    private ArrayList<ChiTietHoaDon> lstCTHD;
    private TextView tvMaHD;
    private TextView tvNgayMua;
    private TextView tvSlSP;
    private TextView tvTongTien;
    private RecyclerView rcv;
    private static DecimalFormat format = new DecimalFormat("###,###,###");
    //
    private Adapter_ChiTietHoaDon adapter_chiTietHoaDon;
    private Loading loading;
    private int tongSL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don);
        AnhXa();
        ActionBar();
        XuLiDuLieu();
        getChiTietHoaDon();
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar);
        tvMaHD = findViewById(R.id.tvMaHD);
        tvNgayMua = findViewById(R.id.tvNgayMua);
        tvSlSP = findViewById(R.id.tvSlSP);
        tvTongTien = findViewById(R.id.tvTongTien);
        rcv = findViewById(R.id.rcv);
        lstCTHD = new ArrayList<>();
        lstSP = new ArrayList<>();
        loading = new Loading(this);
        loading.LoadingDialog();
        //
        adapter_chiTietHoaDon = new Adapter_ChiTietHoaDon(getApplicationContext(), lstSP, lstCTHD);
        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcv.setAdapter(adapter_chiTietHoaDon);

    }

    @SuppressLint("RestrictedApi")
    private void ActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Chi tiết hóa đơn");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

    }

    private void XuLiDuLieu() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        hoaDon = (HoaDon) bundle.get("hoaDon");
        tvMaHD.setText("Mã hóa đơn: " + hoaDon.getMaHoaDon());
        tvNgayMua.setText(hoaDon.getNgayBan());

        tvTongTien.setText("Tổng tiền: " + format.format(hoaDon.getTongTien()) + "đ");
    }

    private void getChiTietHoaDon() {
        StringRequest request = new StringRequest(Request.Method.POST, Server.getCTHoaDon,
                response -> {
                    if (response.equals("failure")) {
                        Toast.makeText(getApplicationContext(), "Hóa đơn trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                chiTietHoaDon = new ChiTietHoaDon();
                                sanPham = new SanPham();
                                chiTietHoaDon.setMaHoaDon(jsonObject.getInt("maHD"));
                                chiTietHoaDon.setMaSanPham(jsonObject.getInt("maSP"));
                                chiTietHoaDon.setSoLuongMua(jsonObject.getInt("soLuongMua"));
                                chiTietHoaDon.setDonGia(jsonObject.getInt("donGia"));
                                //
                                sanPham.setMaLoai(jsonObject.getInt("maLoai"));
                                sanPham.setMaSanPham(jsonObject.getInt("maSP"));
                                sanPham.setTenSanPham(jsonObject.getString("tenSP"));
                                sanPham.setSoLuongNhap(jsonObject.getInt("soLuongNhap"));
                                sanPham.setHinhAnh(jsonObject.getString("hinhAnh"));
                                sanPham.setGiaTien(jsonObject.getInt("giaTien"));
                                sanPham.setGiaCu(jsonObject.getInt("giaCu"));
                                sanPham.setNgayNhap(jsonObject.getString("ngayNhap"));
                                sanPham.setThongTinSanPham(jsonObject.getString("thongTinSP"));
                                lstCTHD.add(chiTietHoaDon);
                                lstSP.add(sanPham);
                                tongSL += chiTietHoaDon.getSoLuongMua();
                                adapter_chiTietHoaDon.notifyDataSetChanged();
                            }
                            tvSlSP.setText("Tổng số lượng: " + tongSL);
                            loading.DimissDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> {
            Toast.makeText(getApplicationContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
        }) {
            // gửi dl
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("maHD", String.valueOf(hoaDon.getMaHoaDon()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter_chiTietHoaDon.fixMemoryLeak();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}