<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>User Management</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin-users.css">
</head>

<script src="${pageContext.request.contextPath}/assets/js/admin-users.js"></script>

<body>

<div class="dashboard-container">

    <!-- SIDEBAR -->
    <jsp:include page="../components/sidebar.jsp"/>

    <div class="main-content">

        <!-- TOPBAR -->
        <jsp:include page="../components/topbar.jsp"/>

        <div class="dashboard-content">

            <div class="page-title">
                User Management
            </div>

            <!-- SEARCH + CREATE -->
            <div class="user-toolbar">

                <!-- LEFT: SEARCH + FILTER -->
                <form method="get"
                      action="${pageContext.request.contextPath}/admin/users"
                      class="search-form">

                    <input type="text"
                           name="keyword"
                           placeholder="Search ID, name, email..."
                           value="${param.keyword}"
                           class="search-input"/>

                    <select name="role" class="search-select">
                        <option value="">All Roles</option>
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

                    <button type="submit" class="btn-search">
                        Search
                    </button>

                    <a href="${pageContext.request.contextPath}/admin/users"
                       class="btn-refresh">
                        Reset
                    </a>

                </form>

                <button class="btn-create"
                        onclick="openCreateModal()">
                    + Create User
                </button>

            </div>


            <!-- USER TABLE -->
            <table class="user-table">

                <thead>
                <tr>
                    <th>ID</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Role</th>
                    <th>Verified</th>
                    <th>Status</th>
                    <th>Created</th>
                    <th>Updated</th>
                    <th>Action</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach var="u" items="${users}">
                    <tr>

                        <td>${u.id}</td>
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
                                    <span class="badge badge-yes">Verified</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-no">No</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${u.deleted}">
                                    <span class="badge badge-no">Deleted</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-yes">Active</span>
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
                                Edit
                            </button>

                            <form method="post"
                                  action="${pageContext.request.contextPath}/admin/users"
                                  style="display:inline">

                                <input type="hidden" name="action" value="delete"/>
                                <input type="hidden" name="id" value="${u.id}"/>

                                <button type="submit"
                                        class="btn-delete"
                                        onclick="return confirm('Delete this user?')">
                                    Delete
                                </button>

                            </form>

                        </td>

                    </tr>
                </c:forEach>

                </tbody>

            </table>

            <!-- CREATE USER MODAL -->
            <div id="createModal" class="modal">

                <div class="modal-content">

                    <span class="close" onclick="closeCreateModal()">&times;</span>

                    <h2>Create User</h2>

                    <form method="post"
                          action="${pageContext.request.contextPath}/admin/users">

                        <input type="hidden" name="action" value="create"/>

                        <label>Full Name</label>
                        <input type="text" name="fullName" required>

                        <label>Password</label>
                        <input type="password" name="password" required>

                        <label>Email</label>
                        <input type="email" name="email" required>

                        <label>Phone</label>
                        <input type="text" name="phoneNumber">

                        <label>Role</label>
                        <select name="role">
                            <option value="CUSTOMER">CUSTOMER</option>
                            <option value="STAFF">STAFF</option>
                            <option value="ADMIN">ADMIN</option>
                        </select>

                        <label>Status</label>
                        <select name="status">
                            <option value="ACTIVE">ACTIVE</option>
                            <option value="INACTIVE">INACTIVE</option>
                        </select>

                        <button type="submit" class="btn-save">
                            Create
                        </button>

                    </form>

                </div>

            </div>

            <!-- EDIT USER MODAL -->
            <div id="editModal" class="modal">

                <div class="modal-content">

                    <span class="close" onclick="closeEditModal()">&times;</span>

                    <h2>Edit User</h2>

                    <form method="post"
                          action="${pageContext.request.contextPath}/admin/users">

                        <input type="hidden" name="action" value="update"/>
                        <input type="hidden" name="id" id="editId"/>

                        <label>Full Name</label>
                        <input type="text" name="fullName" id="editFullName" required>

                        <label>Email</label>
                        <input type="email" name="email" id="editEmail" required>

                        <label>Phone</label>
                        <input type="text" name="phoneNumber" id="editPhone">

                        <label>Role</label>
                        <select name="role" id="editRole">
                            <option value="CUSTOMER">CUSTOMER</option>
                            <option value="ADMIN">ADMIN</option>
                        </select>

                        <label>Status</label>
                        <select name="status" id="editStatus">
                            <option value="ACTIVE">ACTIVE</option>
                            <option value="INACTIVE">INACTIVE</option>
                        </select>

                        <button type="submit" class="btn-save">
                            Update
                        </button>

                    </form>

                </div>

            </div>


            <!-- PAGINATION -->
            <div class="pagination">

                <c:if test="${currentPage > 1}">
                    <a href="?page=${currentPage - 1}&keyword=${param.keyword}">
                        Previous
                    </a>
                </c:if>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="?page=${i}&keyword=${param.keyword}"
                       class="${i == currentPage ? 'active' : ''}">
                            ${i}
                    </a>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="?page=${currentPage + 1}&keyword=${param.keyword}">
                        Next
                    </a>
                </c:if>

            </div>

        </div>

    </div>

</div>

</body>
</html>