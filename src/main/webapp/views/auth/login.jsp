<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - REMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<script>
    document.addEventListener("DOMContentLoaded", function () {

        const modal = document.getElementById("forgotModal");
        const link = document.getElementById("forgotPasswordLink");
        const closeBtn = document.querySelector(".close");

        link.addEventListener("click", function () {
            modal.style.display = "block";
        });

        closeBtn.addEventListener("click", function () {
            modal.style.display = "none";
        });

        window.addEventListener("click", function (event) {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        });

    });
</script>
<body>
<div class="auth-container">
    <div class="auth-box">
        <h2>Đăng nhập</h2>

        <c:if test="${not empty error}">
            <div class="error-message">
                    ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth" method="post">
            <input type="hidden" name="action" value="login"/>

            <div class="auth-form">
                <label>Email</label>
                <input type="email"
                       name="email"
                       value="${param.email}"
                       required>
            </div>

            <div class="auth-form">
                <label>Mật khẩu</label>
                <input type="password"
                       name="password"
                       required>
            </div>

            <p class="auth-link-1">
                <a href="javascript:void(0);" id="forgotPasswordLink">
                    Quên mật khẩu
                </a>
            </p>

            <div class="auth-form"><button type="submit">Đăng nhập</button></div>

        </form>

        <p class="auth-link">
            Chưa có tài khoản?
            <a href="${pageContext.request.contextPath}/auth?action=register">
                Đăng ký
            </a>
        </p>
    </div>
</div>

<div id="forgotModal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <h2>Khôi phục mật khẩu</h2>

        <form action="${pageContext.request.contextPath}/auth" method="post">
            <input type="hidden" name="action" value="resetPassword"/>

            <input type="text" name="otp" placeholder="Nhập mã xác thực (OTP)" required>

            <input type="password" name="newPassword" placeholder="Mật khẩu mới" required>

            <input type="password" name="confirmPassword" placeholder="Xác nhận mật khẩu mới" required>

            <button type="submit">Đổi mật khẩu</button>
        </form>
    </div>
</div>
</body>
</html>