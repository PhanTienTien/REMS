<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>

    <title>Property Management</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin/property-dashboard.css">

</head>

<body>

<div class="dashboard-container">

    <jsp:include page="/views/admin/components/sidebar.jsp"/>

    <div class="main-content">

        <jsp:include page="/views/admin/components/topbar.jsp"/>

        <div class="content">
            <c:if test="${not empty error}">
                <div class="error-box">
                        ${error}
                </div>
            </c:if>

            <div class="page-header">

                <h2>Property Management</h2>

                <button class="btn-primary" onclick="openCreateModal()">
                    + Create Property
                </button>

            </div>

            <form class="search-bar"
                  method="get"
                  action="${pageContext.request.contextPath}/admin/properties/search">

                <input type="text" name="address" placeholder="Search address">

                <select name="type">
                    <option value="">All Type</option>
                    <option value="SALE">Sale</option>
                    <option value="RENT">Rent</option>
                </select>

                <input type="number" name="minPrice" placeholder="Min price">

                <input type="number" name="maxPrice" placeholder="Max price">

                <button class="btn-search" type="submit">
                    Search
                </button>

            </form>

            <div class="table-wrapper">

                <table class="property-table">

                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Address</th>
                        <th>Type</th>
                        <th>Price</th>
                        <th>Status</th>
                        <th>Created</th>
                        <th>Actions</th>
                    </tr>
                    </thead>

                    <tbody>

                    <c:forEach var="p" items="${properties}">

                        <tr>

                            <td>${p.id}</td>

                            <td>${p.title}</td>

                            <td>${p.address}</td>

                            <td>${p.type}</td>

                            <td>$${p.price}</td>

                            <td>

                                <span class="status-badge status-${p.status}">
                                        ${p.status}
                                </span>

                            </td>

                            <td>${p.createdAt}</td>

                            <td class="actions">

                                <button class="btn-edit"
                                        onclick="openEditModal(
                                                '${p.id}',
                                                '${p.title}',
                                                '${p.address}',
                                                '${p.description}',
                                                '${p.type}',
                                                '${p.price}'
                                                )">
                                    Edit
                                </button>

                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/properties/approve">

                                    <input type="hidden" name="id" value="${p.id}">

                                    <button class="btn-approve">Approve</button>

                                </form>

                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/properties/delete">

                                    <input type="hidden" name="id" value="${p.id}">

                                    <button class="btn-delete"
                                            onclick="return confirm('Delete this property?')">
                                        Delete
                                    </button>

                                </form>

                            </td>

                        </tr>

                    </c:forEach>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>

<div id="createModal" class="modal">
    <div class="modal-content">
        <h3>Create Property</h3>
        <form method="post"
              action="${pageContext.request.contextPath}/admin/properties/create">

            <input type="text" name="title" placeholder="Title"
                   value="${formTitle}" required>

            <input type="text" name="address" placeholder="Address"
                   value="${formAddress}" required>

            <textarea name="description"
                      placeholder="Description">${formDescription}</textarea>

            <select name="type">

                <option value="SALE"
                        <c:if test="${formType == 'SALE'}">selected</c:if>>
                    Sale
                </option>

                <option value="RENT"
                        <c:if test="${formType == 'RENT'}">selected</c:if>>
                    Rent
                </option>

            </select>

            <input type="number" name="price"
                   value="${formPrice}"
                   placeholder="Price" required>

            <div class="modal-actions">

                <button class="btn-primary">Create</button>

                <button type="button" onclick="closeCreateModal()" class="btn-cancel">
                    Cancel
                </button>
            </div>
        </form>
    </div>
</div>

<div id="editModal" class="modal">
    <div class="modal-content">
        <h3>Edit Property</h3>
        <form method="post"
              action="${pageContext.request.contextPath}/admin/properties/edit">

            <input type="hidden" name="id" id="editId">

            <input type="text" name="title" id="editTitle">

            <input type="text" name="address" id="editAddress">

            <textarea name="description" id="editDescription"></textarea>

            <select name="type" id="editType">
                <option value="SALE">Sale</option>
                <option value="RENT">Rent</option>
            </select>

            <input type="number" name="price" id="editPrice">

            <div class="modal-actions">

                <button class="btn-primary">Update</button>

                <button type="button" onclick="closeEditModal()" class="btn-cancel">
                    Cancel
                </button>

            </div>

        </form>

    </div>

</div>

<div id="loadingSpinner" class="loading-spinner">
    <div class="spinner"></div>
</div>

<div id="toast" class="toast"></div>

<div id="confirmDialog" class="confirm-dialog">
    <div class="confirm-box">

        <p id="confirmMessage">Are you sure?</p>

        <div class="confirm-actions">
            <button onclick="confirmYes()" class="btn-danger">Yes</button>
            <button onclick="closeConfirm()" class="btn-cancel">Cancel</button>
        </div>

    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/admin/property-dashboard.js"></script>

</body>
</html>