const container = document.querySelector('.container');
const bookBtn = document.getElementById('bookBtn');
const movieSelect = document.getElementById('movie');
const message = document.getElementById('message');
const allSeats = document.querySelectorAll('.row .seat');

// 1. Hàm gọi xuống Database hỏi xem phim này đã bán ghế nào
async function fetchBookedSeats() {
    const movieName = movieSelect.value;
    try {
        const response = await fetch(`http://localhost:8080/api/booked-seats?movie=${encodeURIComponent(movieName)}`);
        
        if (!response.ok) {
            throw new Error("Chưa kết nối được API lấy ghế");
        }
        
        const bookedSeats = await response.json(); // Nhận mảng ghế, VD: ["A1", "B2"]

        // Reset: Xóa màu của tất cả các ghế trên màn hình
        allSeats.forEach(seat => {
            seat.classList.remove('selected', 'occupied');
        });

        // Tô màu xám (occupied) cho những ghế server báo là đã bán
        allSeats.forEach(seat => {
            const seatId = seat.getAttribute('data-seat');
            if (bookedSeats.includes(seatId)) {
                seat.classList.add('occupied');
            }
        });
        
        message.innerText = ""; 
    } catch (error) {
        console.error("Lỗi tải dữ liệu ghế:", error);
    }
}

// 2. Chạy hàm lấy ghế ngay khi vừa mở web
fetchBookedSeats();

// 3. Chạy lại hàm lấy ghế mỗi khi người dùng chọn phim khác
movieSelect.addEventListener('change', fetchBookedSeats);

// 4. Xử lý click chọn ghế (chỉ cho chọn ghế chưa ai đặt)
container.addEventListener('click', (e) => {
    if (e.target.classList.contains('seat') && !e.target.classList.contains('occupied')) {
        e.target.classList.toggle('selected');
    }
});

// 5. Nút Đặt Vé gửi dữ liệu xuống Database
bookBtn.addEventListener('click', async () => {
    const selectedSeats = document.querySelectorAll('.row .seat.selected');
    const seatsArray = [...selectedSeats].map(seat => seat.getAttribute('data-seat'));
    
    if (seatsArray.length === 0) {
        alert("Vui lòng chọn ít nhất 1 ghế!");
        return;
    }

    const bookingData = {
        movieName: movieSelect.value,
        seats: seatsArray
    };

    try {
        const response = await fetch('http://localhost:8080/api/book', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bookingData)
        });
        
        const result = await response.text();
        message.innerText = result; 
        
        // Cập nhật giao diện: biến ghế vừa đặt thành màu xám luôn
        selectedSeats.forEach(seat => {
            seat.classList.remove('selected');
            seat.classList.add('occupied');
        });
    } catch (error) {
        message.innerText = "Lỗi kết nối đến server.";
    }

        // ==========================================
    // PHẦN BẢO MẬT & ĐĂNG XUẤT CHO KHÁCH HÀNG
    // ==========================================

    // 1. Kiểm tra xem người này đã đăng nhập chưa
    const currentUser = localStorage.getItem('currentUser');
    const currentRole = localStorage.getItem('currentRole');

    if (!currentUser) {
        // Nếu chưa đăng nhập (không có tên trong bộ nhớ), đá văng về trang login
        alert("Bạn cần đăng nhập để đặt vé xem phim!");
        window.location.href = 'login.html';
    } else {
        // Nếu đã đăng nhập, hiển thị tên lên góc phải màn hình
        document.getElementById('userName').innerText = currentUser;
    }

    // 2. Chức năng Đăng xuất
    const logoutUserBtn = document.getElementById('logoutUserBtn');
    if (logoutUserBtn) {
        logoutUserBtn.addEventListener('click', () => {
            // Xóa sạch trí nhớ và quay về Login
            localStorage.removeItem('currentUser');
            localStorage.removeItem('currentRole');
            window.location.href = 'login.html';
        });
    }
});