<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="profile-content">

    <div class="profile-card">

        <h2>Thông tin tài khoản</h2>

        <div class="profile-info">

            <div class="info-row">
                <div class="label">Họ và tên</div>
                <div class="value">${user.fullName}</div>
            </div>

            <div class="info-row">
                <div class="label">Email</div>
                <div class="value">${user.email}</div>
            </div>

            <div class="info-row">
                <div class="label">Số điện thoại</div>
                <div class="value">${user.phoneNumber}</div>
            </div>

        </div>

        <div class="profile-actions">

            <button class="btn-primary" onclick="openEditModal()">
                Sửa tài khoản
            </button>

            <a href="${pageContext.request.contextPath}/customer/profile/delete"
               class="btn-delete">
                Xóa tài khoản
            </a>

        </div>

        <div class="modal" id="editProfileModal">

            <div class="modal-content">

                <span class="close" onclick="closeEditModal()">&times;</span>

                <h3>Edit Profile</h3>

                <form method="post"
                      action="${pageContext.request.contextPath}/customer/profile">

                    <div class="form-group">
                        <label>Full Name</label>
                        <input type="text"
                               name="fullName"
                               value="${user.fullName}"
                               required>
                    </div>

                    <div class="form-group">
                        <label>Phone</label>
                        <input type="text"
                               name="phone"
                               value="${user.phoneNumber}"
                               required>
                    </div>

                    <button type="submit" class="btn-primary">
                        Save Changes
                    </button>

                </form>

            </div>

        </div>

    </div>

</div>

<script>

    function openEditModal() {
        document.getElementById("editProfileModal").style.display = "flex";
    }

    function closeEditModal() {
        document.getElementById("editProfileModal").style.display = "none";
    }

    window.onclick = function(e) {
        let modal = document.getElementById("editProfileModal");

        if (e.target === modal) {
            modal.style.display = "none";
        }
    }

</script>