```md
# 📦 BinaryBrigdeMouldAds

## 📌 1. Thêm token vào `gradle.properties`  

Mở file **`gradle.properties`** trong thư mục **`$HOME/.gradle/gradle.properties`** (nếu chưa có, hãy tạo mới) và thêm:  

```properties
--warning-mode all
authToken=jp_2o576u0l0g910mltrtmijps8tp
```

📌 **Lưu ý:**  
- **Windows**: File nằm ở `C:\Users\your-username\.gradle\gradle.properties`  
- **macOS/Linux**: File nằm ở `~/.gradle/gradle.properties`  

---

## 🛠 2. Cấu hình `build.gradle (Project)`  

Mở file **`build.gradle`** (cấp Project) và thêm đoạn sau vào phần `dependencyResolutionManagement`:  

```gradle
        maven {
            url "https://jitpack.io"
            credentials {
                username = authToken
                password = ""
            }
        }
```

📌 **Giải thích:**  
- **`username = authToken`**: Lấy từ `gradle.properties` (JitPack yêu cầu token để truy cập repo private).  
- **Không cần `password`**, để trống là được.  

---

## 📥 3. Import thư viện vào `build.gradle (Module)`  

Trong file **`build.gradle (Module: app)`**, thêm:  

```gradle
dependencies {
    implementation 'com.github.Binary-Bridge-Labs:BinaryBrigdeMouldAds:1.0.1'
}
```

📌 **Thay thế** `1.0.1` bằng phiên bản mới nhất nếu có.  

---

## 🔄 4. Cập nhật & build lại  

Sau khi thêm xong, chạy lệnh sau để cập nhật và kiểm tra:  

```sh
./gradlew clean
./gradlew build
```

Nếu gặp lỗi quyền truy cập, kiểm tra xem **JitPack đã được cấp quyền trên GitHub chưa**:  
👉 **[https://jitpack.io/private](https://jitpack.io/private)**  

---

## 🚀 Tóm tắt  

✅ **Thêm token vào `gradle.properties`**  
✅ **Cấu hình `build.gradle (Project)` với JitPack + token**  
✅ **Thêm thư viện vào `build.gradle (Module)`**  
✅ **Chạy `./gradlew build` để kiểm tra**  

