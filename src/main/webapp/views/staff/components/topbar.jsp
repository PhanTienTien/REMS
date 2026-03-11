<div class="topbar">

    <div class="topbar-left">
        <h3>Staff Dashboard</h3>
    </div>

    <div class="topbar-right">

    <span>
      Welcome, ${sessionScope.currentUser.fullName}
    </span>

        <form action="${pageContext.request.contextPath}/logout"
              method="post"
              onsubmit="return confirm('Bạn có chắc chắn muốn đăng xuất?')"
              style="display:inline;">
            <button type="submit" class="logout-btn">
                Logout
            </button>
        </form>

    </div>

</div>