Lưu ý khi chạy project:
- Để chạy được backend: 
+ Cần cài XAMPP. Đưa thư mục backend vào folder htdocs.
+ CMD: php artisan serve để chạy backend

- Để chạy được app:
Vì phía backend chưa deploy lên nền tảng online nào nên chỉ chạy ở phía local.
Nên có 2 cách để chạy được api:
Cách 1:
+ Cần replace địa chỉ ipv4 của máy tính đang chạy trong 2 project CustomerApp và DeliveryApp.
B1: CMD: gõ ipconfig -> copy dòng IPv4 Address
B2: vào project ấn tổ hợp Ctrl+Shift+R. Tìm 172.20.10.5 và thay thế bằng IPV4 máy đang chạy.

Cách 2:
+ Chạy song song project backend bằng câu lệnh : php artisan serve
+ Ở project app android, replace all "172.20.10.5/h_and_h-food-delivery-app/public" thành "127.0.0.1:8000"