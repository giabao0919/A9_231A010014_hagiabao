package vn.edu.vhu.ltdd.a9listview;

import android.os.Parcel;
import android.os.Parcelable;

/** Lớp mô tả một món ăn. Tách dữ liệu ra khỏi giao diện. */
public class MonAn implements Parcelable {

    private String ten;
    private String moTa;
    private int gia;        // đơn vị đồng — dùng int cho tiền tệ, KHÔNG dùng double
    private int anhResId;   // R.drawable.xxx

    public MonAn(String ten, String moTa, int gia, int anhResId) {
        this.ten = ten;
        this.moTa = moTa;
        this.gia = gia;
        this.anhResId = anhResId;
    }

    protected MonAn(Parcel in) {
        ten = in.readString();
        moTa = in.readString();
        gia = in.readInt();
        anhResId = in.readInt();
    }

    public static final Creator<MonAn> CREATOR = new Creator<MonAn>() {
        @Override
        public MonAn createFromParcel(Parcel in) {
            return new MonAn(in);
        }

        @Override
        public MonAn[] newArray(int size) {
            return new MonAn[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ten);
        dest.writeString(moTa);
        dest.writeInt(gia);
        dest.writeInt(anhResId);
    }

    public String getTen() { return ten; }
    public String getMoTa() { return moTa; }
    public int getGia() { return gia; }
    public int getAnhResId() { return anhResId; }

    public void setTen(String ten) { this.ten = ten; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public void setGia(int gia) { this.gia = gia; }

    /** 45000 → "45.000 đ". Không cần thư viện ngoài. */
    public static String dinhDangGia(int gia) {
        String s = String.valueOf(gia);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (s.length() - i) % 3 == 0) sb.append('.');
            sb.append(s.charAt(i));
        }
        return sb + " đ";
    }
}
