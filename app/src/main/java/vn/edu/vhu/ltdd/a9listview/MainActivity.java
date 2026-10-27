package vn.edu.vhu.ltdd.a9listview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "A9_231A010014";

    private final List<MonAn> danhSach = new ArrayList<>();
    private MonAnAdapter adapter;

    private ListView lvMonAn;
    private TextView tvTrong, tvThongKe;
    private EditText edtTen, edtGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // Ánh xạ các View giao diện
        lvMonAn = findViewById(R.id.lvMonAn);
        tvTrong = findViewById(R.id.tvTrong);
        tvThongKe = findViewById(R.id.tvThongKe);
        edtTen = findViewById(R.id.edtTen);
        edtGia = findViewById(R.id.edtGia);
        Button btnThem = findViewById(R.id.btnThem);

        // Tạo dữ liệu thực đơn mẫu 5 món
        taoDuLieuMau();

        // Gắn adapter dữ liệu vào ListView
        adapter = new MonAnAdapter(this, danhSach);
        lvMonAn.setAdapter(adapter);

        // Tự động hiện View tvTrong khi danh sách thực đơn rỗng
        lvMonAn.setEmptyView(tvTrong);

        // CÔNG DỤNG BÀI NÂNG CAO NC4: Hiển thị thống kê tổng số món và tổng số tiền ban đầu
        capNhatThongKe();

        // =========================================================================
        // CÔNG DỤNG BÀI NÂNG CAO NC3: Chạm vào 1 dòng -> Mở màn hình chi tiết món ăn
        // Sử dụng Intent truyền đối tượng MonAn (Parcelable) sang ChiTietActivity
        // =========================================================================
        lvMonAn.setOnItemClickListener((parent, view, position, id) -> {
            MonAn mon = (MonAn) parent.getItemAtPosition(position);

            // Khởi tạo Intent chuyển sang màn hình chi tiết ChiTietActivity
            Intent intent = new Intent(MainActivity.this, ChiTietActivity.class);
            // Đóng gói đối tượng MonAn vào Intent
            intent.putExtra("mon_an", mon);
            startActivity(intent);

            Log.d(TAG, "Mở chi tiết món " + position + ": " + mon.getTen());
        });

        // Xử lý sự kiện giữ lâu 1 dòng để xóa món ăn khỏi thực đơn
        lvMonAn.setOnItemLongClickListener((parent, view, position, id) -> {
            MonAn mon = danhSach.remove(position);
            adapter.notifyDataSetChanged();     // Báo adapter cập nhật lại giao diện

            // CÔNG DỤNG BÀI NÂNG CAO NC4: Cập nhật lại thống kê sau khi xóa món
            capNhatThongKe();

            Toast.makeText(this, getString(R.string.da_xoa, mon.getTen()), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Đã xóa món: " + mon.getTen());
            return true;
        });

        // Sự kiện bấm nút Thêm món
        btnThem.setOnClickListener(v -> themMon());
    }

    /** Tạo danh sách 5 món ăn mẫu ban đầu */
    private void taoDuLieuMau() {
        danhSach.add(new MonAn("Phở bò tái", "Nước dùng ninh xương 8 giờ", 45000, R.drawable.mon1));
        danhSach.add(new MonAn("Bún chả Hà Nội", "Chả nướng than hoa", 50000, R.drawable.mon2));
        danhSach.add(new MonAn("Cơm tấm sườn", "Sườn nướng mật ong", 40000, R.drawable.mon3));
        danhSach.add(new MonAn("Bánh mì thịt nguội", "Pate nhà làm", 25000, R.drawable.mon4));
        danhSach.add(new MonAn("Trà đào cam sả", "Mát lạnh, ít đường", 30000, R.drawable.mon5));
    }

    /** Đọc dữ liệu nhập từ ô tên và giá, kiểm tra hợp lệ rồi thêm vào danh sách */
    private void themMon() {
        String ten = edtTen.getText().toString().trim();
        String giaStr = edtGia.getText().toString().trim();

        if (ten.isEmpty()) {
            edtTen.setError(getString(R.string.err_ten));
            edtTen.requestFocus();
            return;
        }
        int gia;
        try {
            gia = Integer.parseInt(giaStr);
        } catch (NumberFormatException e) {
            edtGia.setError(getString(R.string.err_gia));
            edtGia.requestFocus();
            return;
        }
        if (gia <= 0) {
            edtGia.setError(getString(R.string.err_gia_duong));
            edtGia.requestFocus();
            return;
        }

        danhSach.add(0, new MonAn(ten, getString(R.string.moi_them), gia, R.drawable.mon1));
        adapter.notifyDataSetChanged();
        lvMonAn.smoothScrollToPosition(0);

        // CÔNG DỤNG BÀI NÂNG CAO NC4: Cập nhật lại thống kê sau khi thêm món mới
        capNhatThongKe();

        edtTen.setText("");
        edtGia.setText("");
        edtTen.requestFocus();
        Log.d(TAG, "Đã thêm: " + ten + " — tổng " + danhSach.size() + " món");
    }

    // =========================================================================
    // CÔNG DỤNG BÀI NÂNG CAO NC4: Hàm cập nhật thống kê tổng số món và tổng giá trị thực đơn
    // Tính tổng số món = danhSach.size(), cộng dồn tổng tiền và hiển thị lên TextView tvThongKe
    // =========================================================================
    private void capNhatThongKe() {
        int tongSoMon = danhSach.size();
        int tongGiaTri = 0;
        for (MonAn mon : danhSach) {
            tongGiaTri += mon.getGia();
        }
        tvThongKe.setText("📊 Thống kê: " + tongSoMon + " món | Tổng tiền: " + MonAn.dinhDangGia(tongGiaTri));
    }
}
