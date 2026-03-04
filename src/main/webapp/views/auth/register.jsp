<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Register - REMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>

<c:if test="${showOtp}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            openModal();
        });
    </script>
</c:if>

<body data-context="${pageContext.request.contextPath}">
<div class="auth-container">
    <div class="auth-box">
        <h2>Đăng ký tài khoản</h2>

        <div id="registerError" class="error"></div>

        <form id="registerForm"
              action="${pageContext.request.contextPath}/auth"
              method="post"
              class="auth-form">
            <input type="hidden" name="action" value="register"/>

            <input type="text" name="userName" placeholder="Họ tên" required>
            <input type="email" name="email" placeholder="Email" required>
            <input type="text" name="phoneNumber" placeholder="Số điện thoại" required>
            <input type="password" name="password" placeholder="Mật khẩu" required>
            <input type="password" name="confirmPassword" placeholder="Xác nhận mật khẩu" required>

            <label>
                <input style="width: 10%" type="checkbox" name="agreeTerms" required>
                Đồng ý điều khoản
            </label>

            <button type="submit">Đăng ký</button>

            <p class="auth-link">
                Bạn đã có tài khoản?
                <a href="${pageContext.request.contextPath}/auth?action=login">
                    Đăng nhập
                </a>
            </p>
        </form>
    </div>
</div>

<div class="otp-modal" id="otpModal">
    <div class="otp-content">

        <h3>Xác thực OTP</h3>

        <p>Mã có hiệu lực trong <b>5 phút</b></p>

        <div class="otp-inputs">
            <input type="hidden" id="otpEmail" name="email" value="${registeredEmail}">
            <input maxlength="1" type="text">
            <input maxlength="1" type="text">
            <input maxlength="1" type="text">
            <input maxlength="1" type="text">
            <input maxlength="1" type="text">
            <input maxlength="1" type="text">
        </div>

        <div class="countdown" id="countdown">
            05:00
        </div>

        <div id="otpError" style="color:red;"></div>

        <button onclick="verifyOtp()">Xác thực</button>

        <button class="resend-btn"
                id="resendBtn"
                onclick="resendOtp()"
                disabled>
            Gửi lại OTP (60s)
        </button>

    </div>
</div>
<script src="${pageContext.request.contextPath}/assets/js/auth.js"></script>
</body>
</html>