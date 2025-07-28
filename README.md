# Vintage Timepiece Evaluation and Trading Platform

> **Vietnamese:** Hệ thống hỗ trợ thẩm định, mua bán đồng hồ cũ  
> Một nền tảng trực tuyến toàn diện giúp kết nối người mua, người bán và chuyên gia thẩm định trong lĩnh vực đồng hồ cổ.

## Giới thiệu

**Vintage Timepiece Evaluation and Trading Platform** là hệ thống web được thiết kế để hỗ trợ việc **đăng bán**, **thẩm định**, và **giao dịch an toàn** các mẫu đồng hồ cổ.  
Nền tảng cung cấp một giao diện thân thiện với người dùng, hỗ trợ nhiều vai trò khác nhau nhằm mang lại trải nghiệm thương mại hiệu quả, minh bạch và chuyên nghiệp.

---

## Các vai trò trong hệ thống

### Sellers – Người bán
- Đăng sản phẩm: Tải lên hình ảnh và mô tả chi tiết đồng hồ cổ.
- Tương tác với chuyên gia thẩm định: Nhận đánh giá và điều chỉnh giá bán.
- Quản lý bài đăng: Cập nhật thông tin, thay đổi giá hoặc xóa bài.
- Hoàn tất giao dịch: Bán hàng thông qua hệ thống thanh toán bảo mật.

### Buyers – Người mua
- Duyệt sản phẩm: Tìm kiếm đồng hồ thông qua bộ lọc và từ khóa.
- Xem thẩm định: Truy cập báo cáo về tính xác thực và giá trị.
- Mua hàng: Thanh toán online qua cổng thanh toán hiện đại, thuận tiện, nhanh chóng: VNPay hoặc Momo.
- Đánh giá: Gửi nhận xét và đánh giá người bán/thẩm định viên.
- Đăng ký trở thành người bán hàng: Cung cấp thông tin liên quan để nhanh chóng trở thành người bán hàng.

### Appraisers – Chuyên gia thẩm định
- Đánh giá sản phẩm: Kiểm định tình trạng, tính xác thực và định giá.
- Tạo báo cáo: Cung cấp báo cáo chi tiết về sản phẩm.
- Giao tiếp với người bán: Đưa ra tư vấn và đề xuất điều chỉnh giá.

### Administrators – Quản trị viên
- Quản lý người dùng: Phê duyệt tài khoản, xử lý tranh chấp và hỗ trợ chung.
- Giám sát giao dịch: Theo dõi và can thiệp khi có tranh chấp.
- Bảo mật hệ thống: Cập nhật chính sách bảo mật, xử lý rủi ro an ninh.
- Cập nhật nền tảng: Quản lý hệ thống backend, triển khai cập nhật.

### Customer Support Agents – Hỗ trợ khách hàng
- Giải đáp thắc mắc: Hỗ trợ người dùng gặp sự cố với hệ thống hoặc tài khoản.
- Thu thập phản hồi: Ghi nhận góp ý để cải thiện hệ thống trong tương lai.

---

## Tính năng chính

- Đăng và duyệt sản phẩm đồng hồ cổ.
- Thẩm định chất lượng và xác thực sản phẩm bởi chuyên gia.
- Giao dịch an toàn, thanh toán tiện lợi qua cổng thanh toán hiện đại, nhanh chóng: VNPay hoặc Momo.
- Đánh giá và phản hồi giữa người dùng.
- Giao diện phân quyền theo từng vai trò.

---

## Công nghệ sử dụng
- **Frontend**: React, Vite
- **Backend**: Java (Spring Boot 3, Spring Security với Authenticate, JWT Token)
- **Cơ sở dữ liệu**: MySQL
- **Thanh toán**: VNPay, Momo
- **Security**: JWT Token, phân quyền người dùng
- **Quản lý dự án**: maven, npm
- **Giao thức**: HTTP/HTTPS - REST API với các method GET, POST, PUT, DELETE
- **Định dạng dữ liệu**: JSON 

## Công nghệ hỗ trợ khác
- **VS Code**
- **IntelliJ**
- **Postman**: Kiểm thử REST API
- **CORS**: Quản lý truy cập khác domain
- **Tài liệu tham khảo**: Swagger, OpenAPI, ...

## Đặc điểm của API

- **Tuân theo nguyên tắc REST**  
  Thiết kế rõ ràng, dễ mở rộng và không lưu trạng thái (**stateless communication**)

- **Phản hồi định dạng JSON**  
  Tất cả dữ liệu phản hồi từ server đều ở dạng JSON để dễ dàng thao tác ở frontend

- **Hỗ trợ CORS**  
  Cho phép frontend và backend giao tiếp dù ở trên các domain khác nhau

- **Bảo mật với JWT & Spring Security**  
  - Xác thực người dùng thông qua **JWT (JSON Web Token)**
  - Phân quyền truy cập thông qua **Spring Security**
  - Bảo vệ API khỏi truy cập trái phép
 
## Logo công nghệ
 | Java (Spring Boot) | Spring Security | React | MySQL |
 |--------------------|-----------------|--------------|-------|
 ![Java](https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg) | ![Spring Security](https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg) | ![React](https://cdn.jsdelivr.net/gh/devicons/devicon/icons/react/react-original.svg) | ![MySQL](https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg) |

## Hướng dẫn cài đặt
### Yêu cầu
- Node.js (v16 trở lên)
- Java 17 (cho Spring Boot 3)
- MySQL (v8 trở lên)
- Yarn hoặc npm, maven

### Các bước cài đặt
1. **Clone dự án**:
   ```bash
   git clone https://github.com/QuocHuyLearningCode/bus-ticket-system.git
   cd bus-ticket-system
   ```

2. **Cài đặt**:
   - Frontend: dự án chạy trên [localhost:5173](http://localhost:5173/)
      ```bash
     cd frontend
     npm install
     npm run dev
     
     ```
   - Backend: [localhost:8080](http://localhost:8080/)
     ```bash
     cd backend
     down pom.xml
     
Kiến trúc dữ án: 
