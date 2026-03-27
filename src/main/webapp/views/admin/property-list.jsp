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

    <c:choose>

        <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
            <jsp:include page="/views/admin/components/sidebar.jsp"/>
        </c:when>

        <c:otherwise>
            <jsp:include page="/views/staff/components/sidebar.jsp"/>
        </c:otherwise>

    </c:choose>

    <div class="main-content">

        <c:choose>

            <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
                <jsp:include page="/views/admin/components/topbar.jsp"/>
            </c:when>

            <c:otherwise>
                <jsp:include page="/views/staff/components/topbar.jsp"/>
            </c:otherwise>

        </c:choose>

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
                        <th>STT</th>
                        <th>Title</th>
                        <th>Address</th>
                        <th>Description</th>
                        <th>Type</th>
                        <th>Price</th>
                        <th>Status</th>
                        <th>Created</th>
                        <th>Actions</th>
                    </tr>
                    </thead>

                    <tbody>

                    <c:forEach var="p" items="${properties}" varStatus="loop">

                        <tr>

                            <td>${loop.index + 1}</td>

                            <td>${p.title}</td>

                            <td>${p.address}</td>

                            <td>${p.description}</td>

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

                                    <c:choose>

                                        <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
                                            <button class="btn-approve">Approve</button>
                                        </c:when>

                                    </c:choose>

                                </form>

                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/properties/delete">

                                    <input type="hidden" name="id" value="${p.id}">

                                    <button class="btn-delete"
                                            onclick="return confirm('Delete this property?')">
                                        Delete
                                    </button>

                                </form>

                                <button class="btn-image"
                                        onclick="openImageModal('${p.id}')">
                                    Images
                                </button>

                            </td>

                        </tr>

                    </c:forEach>

                    </tbody>

                </table>

            </div>

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

<div id="createModal" class="modal">
    <div class="modal-content">
        <h3>Create Property</h3>
        <form method="post"
              enctype="multipart/form-data"
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

            <label>Property Images</label>

            <input type="file"
                   name="images"
                   multiple
                   accept="image/*">

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

<div id="imageModal" class="modal">
    <div class="modal-content">

        <h3>Manage Images</h3>

        <div id="imageList" class="image-grid">

        </div>

        <form id="uploadForm"
              method="post"
              enctype="multipart/form-data"
              action="${pageContext.request.contextPath}/admin/property-images?action=add">

            <input type="hidden" name="propertyId" id="imagePropertyId">

            <input type="file" name="images" multiple>

            <button>Add Images</button>
        </form>

        <button onclick="closeImageModal()" class="btn-cancel">
            Close
        </button>

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

<script>
    const contextPath = "${pageContext.request.contextPath}";
    const openModal = "${openModal}";
    const formData = {
        id: "${formId}",
        title: "${formTitle}",
        address: "${formAddress}",
        description: "${formDescription}",
        type: "${formType}",
        price: "${formPrice}"
    };
</script>

<script src="${pageContext.request.contextPath}/assets/js/admin/property-dashboard.js?v=1"></script>

</body>
</html>