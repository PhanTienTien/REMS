<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="topbar">
    <div class="topbar-left">
        <h3>Bảng điều khiển quản trị</h3>
    </div>

    <div class="topbar-right">
        <span>
          Xin chào, ${sessionScope.currentUser.fullName}
        </span>

        <form action="${pageContext.request.contextPath}/logout"
              method="post"
              onsubmit="return confirm('Bạn có chắc chắn muốn đăng xuất?')"
              style="display:inline;">
            <button type="submit" class="logout-btn">
                Đăng xuất
            </button>
        </form>
    </div>
</div>
