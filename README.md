# MST Visualizer: Prim's and Kruskal's Algorithms

Đây là một ứng dụng Desktop tương tác được xây dựng bằng **JavaFX**, giúp người dùng trực quan hóa đồ thị và thuật toán tìm **Cây khung nhỏ nhất (Minimum Spanning Tree - MST)**. Dự án hỗ trợ vẽ đồ thị tùy ý và minh họa từng bước hoạt động của hai thuật toán nổi tiếng: **Thuật toán Prim** và **Thuật toán Kruskal**.

Đây là đồ án môn học CT239 - Niên luận cơ sở ngành Kỹ Thuật Phần Mềm - CTU

## 🌟 Các tính năng chính

Ứng dụng cung cấp các công cụ tương tác trực tiếp trên giao diện đồ họa (GUI):

- **Thêm đỉnh (Add Vertex):** Nhấn chuột trên bản vẽ để tạo các đỉnh mới cho đồ thị.
- **Thêm cạnh (Add Edge):** Kéo thả giữa hai đỉnh để tạo cạnh nối, sau đó nhập trọng số cho cạnh.
- **Di chuyển (Move):** Có thể kéo thả để thay đổi vị trí của các đỉnh trên bản vẽ. Các cạnh kết nối cũng sẽ di chuyển theo.
- **Xóa (Delete):** Xóa đỉnh hoặc cạnh đã chọn khỏi đồ thị.
- **Kiểm tra liên thông (Connect):** Thuật toán Duyệt theo chiều sâu (DFS) được sử dụng để kiểm tra số lượng và bôi màu các thành phần liên thông của đồ thị.
- **Giải thuật Prim (Prim's Algorithm):** Trực quan hóa việc tìm cây khung nhỏ nhất khởi đầu từ một đỉnh bất kỳ do người dùng chọn. Các cạnh thuộc cây khung sẽ được highlight màu đỏ.
- **Giải thuật Kruskal (Kruskal's Algorithm):** Trực quan hóa việc tìm cây khung nhỏ nhất cho toàn bộ đồ thị theo trọng số cạnh (sử dụng cấu trúc dữ liệu Union-Find). Các cạnh thuộc cây khung sẽ được highlight màu cam.
- **Lưu và Mở đồ thị (Save / Open):** Hỗ trợ lưu cấu trúc đồ thị hiện tại xuống file và mở lại để tiếp tục thao tác.
- **Làm mới (Reset):** Xóa toàn bộ đồ thị và đưa bản vẽ về trạng thái ban đầu.

## 💻 Công nghệ sử dụng

- **Ngôn ngữ lập trình:** Java (Phiên bản 21)
- **Giao diện người dùng (GUI):** JavaFX 21 & FXML
- **Quản lý dự án / Build Tool:** Maven
- **Cấu trúc dữ liệu và giải thuật:**
  - Đồ thị (Graph), Đỉnh (Vertex), Cạnh (Edge)
  - Stack (cho DFS), Priority Queue (cho thuật toán Prim)
  - Disjoint Set / Union-Find (cho thuật toán Kruskal)

## 📋 Yêu cầu hệ thống

Để có thể biên dịch và chạy được ứng dụng, máy tính của bạn cần cài đặt:

- **Java Development Kit (JDK):** Phiên bản 21 trở lên.
- **Apache Maven:** Môi trường đã được thiết lập biến toàn cục cho `mvn`.

## 🚀 Hướng dẫn Cài đặt và Chạy ứng dụng

1. **Clone hoặc tải dự án về máy:**
   Đảm bảo bạn đã có toàn bộ mã nguồn tại thư mục.

2. **Chạy ứng dụng bằng Maven:**
   Mở Terminal (Command Prompt / PowerShell) tại thư mục gốc của dự án (nơi có chứa file `pom.xml`) và chạy lệnh sau:
   ```bash
   mvn clean javafx:run
   ```
   Lệnh này sẽ dọn dẹp, tải các thư viện JavaFX cần thiết và tự động khởi chạy ứng dụng.

## 📖 Hướng dẫn sử dụng

1. **Khởi tạo Đồ thị:**
   - Click vào nút **Thêm Đỉnh** và nhấp chuột lên màn hình trắng để tạo điểm.
   - Click vào nút **Thêm Cạnh**, sau đó nhấn giữ chuột từ đỉnh này kéo sang đỉnh khác để nối chúng lại, sau đó một hộp thoại sẽ hiện lên yêu cầu bạn nhập **Trọng số** cho cạnh (số nguyên dương).
2. **Chỉnh sửa:**
   - Để đổi trọng số: Nhấp đúp (Double-click) vào nhãn trọng số trên cạnh.
   - Để xóa: Chọn tính năng **Xóa** và click vào đỉnh hoặc cạnh cần xóa.
   - Để đổi vị trí: Chọn tính năng **Di chuyển** và kéo thả các đỉnh.
3. **Chạy Giải thuật:**
   - Click **Connect** để ứng dụng tự động nhận diện và đếm số lượng các bộ phận liên thông.
   - Click **MST (Prim)**, hộp thoại sẽ yêu cầu bạn chọn đỉnh bắt đầu. Ứng dụng sẽ tìm và làm nổi bật bề dầy của Cây Khung Nhỏ Nhất dựa trên thuật toán Prim.
   - Click **MST (Kruskal)**, ứng dụng sẽ tự động chọn các cạnh tối ưu làm nổi bật để tạo thành Cây Khung Nhỏ Nhất theo thuật toán Kruskal. _(Lưu ý: Để chạy giải thuật, đồ thị phải là đồ thị liên thông)._
4. **Lưu trữ:**
   - Click **Save** để lưu lại đồ thị đang thao tác vào máy tính.
   - Click **Open** để duyệt file và mở những đồ thị đã lưu trước đó.

## 📂 Thực thể mã nguồn chính (Source Code Structure)

- `Main.java`: Điểm bắt đầu (Entry point) khởi chạy ứng dụng JavaFX.
- `models/Graph.java`: Chứa toàn bộ logic thuật toán (DFS, Prim, Kruskal, Union Find).
- `models/Vertex.java` & `models/Edge.java`: Lớp đại diện cho điểm và cạnh của đồ thị.
- `controllers/HomeController.java`: Quản lý các sự kiện tương tác giao diện người dùng (Click, Drag, Xử lý giao diện Canvas v.v.).
- `services/FileService.java`: Dịch vụ xử lý đọc/ghi đồ thị ra file.
