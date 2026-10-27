package vn.edu.vhu.ltdd.a9listview;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * CÔNG DỤNG BÀI NÂNG CAO NC3: Màn hình hiển thị chi tiết món ăn.
 * Khi chạm vào một món ăn ở MainActivity, màn hình này sẽ nhận đối tượng MonAn (Parcelable)
 * qua Intent và hiển thị hình ảnh, tên món, giá tiền cùng mô tả chi tiết.
 */
public class ChiTietActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainChiTiet), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // Ánh xạ các View từ layout activity_chi_tiet.xml
        ImageView imgAnh = findViewById(R.id.imgAnhChiTiet);
        TextView tvTen = findViewById(R.id.tvTenChiTiet);
        TextView tvGia = findViewById(R.id.tvGiaChiTiet);
        TextView tvMoTa = findViewById(R.id.tvMoTaChiTiet);
        Button btnQuayLai = findViewById(R.id.btnQuayLai);

        // CÔNG DỤNG NC3: Trích xuất đối tượng MonAn được truyền từ MainActivity qua Intent
        MonAn monAn;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            monAn = getIntent().getParcelableExtra("mon_an", MonAn.class);
        } else {
            monAn = getIntent().getParcelableExtra("mon_an");
        }

        // Đổ dữ liệu từ đối tượng MonAn lên giao diện màn hình chi tiết
        if (monAn != null) {
            imgAnh.setImageResource(monAn.getAnhResId());
            tvTen.setText(monAn.getTen());
            tvGia.setText(MonAn.dinhDangGia(monAn.getGia()));
            tvMoTa.setText(monAn.getMoTa());
        }

        // Bấm nút Quay lại để đóng màn hình chi tiết và trở về thực đơn
        btnQuayLai.setOnClickListener(v -> finish());
    }
}
