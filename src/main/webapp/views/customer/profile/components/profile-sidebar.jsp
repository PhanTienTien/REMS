<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="profile-sidebar">

    <div class="profile-user">

        <img src="${pageContext.request.contextPath}/assets/images/person_1-min.jpg"
             class="avatar" alt="Avatar">

        <div class="user-name">
            ${user.fullName}
        </div>

        <div class="user-email">
            ${user.email}
        </div>

    </div>

    <c:set var="uri" value="${pageContext.request.requestURI}" />

    <ul class="profile-menu">

        <li class="${uri.contains('/customer/profile') && !uri.contains('bookings') && !uri.contains('favorites') && !uri.contains('transactions') ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/customer/profile">
                Tổng quan tài khoản
            </a>
        </li>

        <li class="${uri.contains('change-password') ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/customer/profile/change-password">
                Đổi mật khẩu
            </a>
        </li>

        <li class="${uri.contains('bookings') ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/customer/profile/bookings">
                BĐS đã đặt
            </a>
        </li>

        <li class="${uri.contains('favorites') ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/customer/profile/favorites">
                Bất động sản đã lưu
            </a>
        </li>

        <li class="${uri.contains('transactions') ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/customer/profile/transactions">
                Giao dịch của bạn
            </a>
        </li>

        <li>
            <form action="${pageContext.request.contextPath}/logout"
                  method="post"
                  onsubmit="return confirm('Bạn có chắc chắn muốn đăng xuất?')">
                <button type="submit" class="logout-btn">
                    Logout
                </button>
            </form>
        </li>

    </ul>

</div>