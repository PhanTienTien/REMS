<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Quản lý người dùng</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/user-dashboard.css">
</head>

<script src="${pageContext.request.contextPath}/assets/js/admin/users-dashboard.js"></script>

<body>
<div class="dashboard-container">
    <jsp:include page="components/sidebar.jsp"/>

    <div class="main-content">
        <jsp:include page="components/topbar.jsp"/>

        <div class="dashboard-content">
            <div class="page-title">Quản lý người dùng</div>

            <div class="user-toolbar">
                <form method="get"
                      action="${pageContext.request.contextPath}/admin/users"
                      class="search-form">

                    <input type="text"
                           name="keyword"
                           placeholder="Tìm ID, tên, email..."
                           value="${param.keyword}"
                           class="search-input"/>

                    <select name="role" class="search-select">
                        <option value="">Tất cả vai trò</option>
                        <option value="ADMIN"
                                <c:if test="${param.role == 'ADMIN'}">selected</c:if>>
                            ADMIN
                        </option>
                        <option value="STAFF"
                                <c:if test="${param.role == 'STAFF'}">selected</c:if>>
                            STAFF
                        </option>
                        <option value="CUSTOMER"
                                <c:if test="${param.role == 'CUSTOMER'}">selected</c:if>>
                            CUSTOMER
                        </option>
                    </select>

                    <button type="submit" class="btn-search">Tìm kiếm</button>

                    <a href="${pageContext.request.contextPath}/admin/users"
                       class="btn-refresh">Đặt lại</a>
                </form>

                <button class="btn-create"
                        onclick="openCreateModal()">
                    + Tạo người dùng
                </button>
            </div>

            <table class="user-table">
                <thead>
                <tr>
                    <th>STT</th>
                    <th>Họ tên</th>
                    <th>Email</th>
                    <th>Số điện thoại</th>
                    <th>Vai trò</th>
                    <th>Xác thực</th>
                    <th>Trạng thái</th>
                    <th>Ngày tạo</th>
                    <th>Cập nhật</th>
                    <th>Thao tác</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="u" items="${users}" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${u.fullName}</td>
                        <td>${u.email}</td>
                        <td>${u.phoneNumber}</td>

                        <td>
                            <c:choose>
                                <c:when test="${u.role == 'ADMIN'}">
                                    <span class="badge badge-admin">ADMIN</span>
                                </c:when>
                                <c:when test="${u.role == 'STAFF'}">
                                    <span class="badge badge-staff">STAFF</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-user">CUSTOMER</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${u.verified}">
                                    <span class="badge badge-yes">Đã xác thực</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-no">Chưa</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${u.deleted}">
                                    <span class="badge badge-no">Đã xóa</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-yes">Đang hoạt động</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>${u.createdAt}</td>
                        <td>${u.updatedAt}</td>

                        <td class="action-buttons">
                            <button class="btn-edit"
                                    onclick="openEditModal(
                                            '${u.id}',
                                            '${u.fullName}',
                                            '${u.email}',
                                            '${u.phoneNumber}',
                                            '${u.role}',
                                            '${u.verified}'
                                            )">
                                Sửa
                            </button>

                            <form method="post"
                                  action="${pageContext.request.contextPath}/admin/users"
                                  style="display:inline">
                                <input type="hidden" name="action" value="delete"/>
                                <input type="hidden" name="id" value="${u.id}"/>

                                <button type="submit"
                                        class="btn-delete"
                                        onclick="return confirm('Bạn có chắc muốn xóa người dùng này?')">
                                    Xóa
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <div id="createModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeCreateModal()">&times;</span>

                    <h2>Tạo người dùng</h2>

                    <form method="post"
                          action="${pageContext.request.contextPath}/admin/users">
                        <input type="hidden" name="action" value="create"/>

                        <label>Họ tên</label>
                        <input type="text" name="fullName" required>

                        <label>Mật khẩu</label>
                        <input type="password" name="password" required>

                        <label>Email</label>
                        <input type="email" name="email" required>

                        <label>Số điện thoại</label>
                        <input type="text" name="phoneNumber">

                        <label>Vai trò</label>
                        <select name="role">
                            <option value="CUSTOMER">CUSTOMER</option>
                            <option value="STAFF">STAFF</option>
                            <option value="ADMIN">ADMIN</option>
                        </select>

                        <label>Trạng thái</label>
                        <select name="status">
                            <option value="ACTIVE">ĐANG HOẠT ĐỘNG</option>
                            <option value="INACTIVE">NGỪNG HOẠT ĐỘNG</option>
                        </select>

                        <button type="submit" class="btn-save">Tạo mới</button>
                    </form>
                </div>
            </div>

            <div id="editModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeEditModal()">&times;</span>

                    <h2>Chỉnh sửa người dùng</h2>

                    <form method="post"
                          action="${pageContext.request.contextPath}/admin/users">
                        <input type="hidden" name="action" value="update"/>
                        <input type="hidden" name="id" id="editId"/>

                        <label>Họ tên</label>
                        <input type="text" name="fullName" id="editFullName" required>

                        <label>Email</label>
                        <input type="email" name="email" id="editEmail" required>

                        <label>Số điện thoại</label>
                        <input type="text" name="phoneNumber" id="editPhone">

                        <label>Vai trò</label>
                        <select name="role" id="editRole">
                            <option value="CUSTOMER">CUSTOMER</option>
                            <option value="ADMIN">ADMIN</option>
                        </select>

                        <label>Trạng thái</label>
                        <select name="status" id="editStatus">
                            <option value="ACTIVE">ĐANG HOẠT ĐỘNG</option>
                            <option value="INACTIVE">NGỪNG HOẠT ĐỘNG</option>
                        </select>

                        <button type="submit" class="btn-save">Cập nhật</button>
                    </form>
                </div>
            </div>

            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="?page=${currentPage - 1}&keyword=${param.keyword}">Trước</a>
                </c:if>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="?page=${i}&keyword=${param.keyword}"
                       class="${i == currentPage ? 'active' : ''}">
                            ${i}
                    </a>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="?page=${currentPage + 1}&keyword=${param.keyword}">Sau</a>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>
