<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../../common/header.jsp" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/customer/profile.css">

<div class="profile-page">

    <div class="profile-wrapper">

        <!-- Sidebar -->
        <jsp:include page="components/profile-sidebar.jsp"/>

        <!-- Content -->
        <div class="profile-content">

            <div class="profile-card">

                <div class="profile-card-header">
                    <h2>Đổi mật khẩu</h2>
                    <p>Cập nhật mật khẩu để bảo vệ tài khoản của bạn</p>
                </div>

                <form method="post"
                      action="${pageContext.request.contextPath}/customer/profile"
                      class="password-form">

                    <input type="hidden"
                           name="action"
                           value="changePassword"/>

                    <div class="form-group">
                        <label>Mật khẩu hiện tại</label>
                        <input type="password"
                               name="currentPassword"
                               placeholder="Nhập mật khẩu hiện tại"
                               required>
                    </div>

                    <div class="form-group">
                        <label>Mật khẩu mới</label>
                        <input type="password"
                               name="newPassword"
                               placeholder="Nhập mật khẩu mới"
                               required>
                    </div>

                    <div class="form-group">
                        <label>Xác nhận mật khẩu mới</label>
                        <input type="password"
                               name="confirmPassword"
                               placeholder="Nhập lại mật khẩu mới"
                               required>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn-primary">
                            Lưu thay đổi
                        </button>

                        <a href="${pageContext.request.contextPath}/customer/profile"
                           class="btn-secondary">
                            Huỷ
                        </a>
                    </div>

                </form>

            </div>

        </div>

    </div>

</div>

<%@ include file="../../common/footer.jsp" %>