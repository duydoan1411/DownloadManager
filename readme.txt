Phần mền được phát triển và hoạt động ổn định trên linux (đã test trên ubuntu 20.04, chưa test đối với windows).
Yêu cầu: (đã bao gồm trong gradle)
	- java: 15
	- javafx: 15.0.1
	- mysql (đã test trên mysql 8)
	
Các bước để chạy app:
	1. Tạo database theo file database.sql.
	2. Cấu hình lại thông tin database trong file /src/main/resources/hibernate.cfg.xml.
	3. Đồng bộ các dependencies của gradle.
	4. Đối với Intellij IDEA chạy app bằng cách mở Gradle tool -> DownloadManager -> Tasks -> application -> run.
	 
Các bước để tải file:
	1. Add URL -> cửa sổ nhập url hiện lên.
	2. Nhập url -> bấm Add -> cửa sổ File Information hiện lên.
	3. Chọn đường dẫn (nếu cần) -> bấm Download để tải -> cửa sổ giám sát thông tin tải hiện lên.
	4. File đang bắt đầu tải.
	
