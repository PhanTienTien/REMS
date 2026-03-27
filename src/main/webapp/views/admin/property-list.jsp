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

    <!-- SIDEBAR -->
    <c:choose>
        <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
            <jsp:include page="/views/admin/components/sidebar.jsp"/>
        </c:when>
        <c:otherwise>
            <jsp:include page="/views/staff/components/sidebar.jsp"/>
        </c:otherwise>
    </c:choose>

    <div class="main-content">

        <!-- TOPBAR -->
        <c:choose>
            <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
                <jsp:include page="/views/admin/components/topbar.jsp"/>
            </c:when>
            <c:otherwise>
                <jsp:include page="/views/staff/components/topbar.jsp"/>
            </c:otherwise>
        </c:choose>

        <div class="content">

            <!-- ERROR -->
            <c:if test="${not empty error}">
                <div class="error-box">${error}</div>
            </c:if>

            <!-- HEADER -->
            <div class="page-header">
                <h2>Property Management</h2>
                <button class="btn-primary" onclick="openCreateModal()">
                    + Create Property
                </button>
            </div>

            <!-- SEARCH + FILTER -->
            <form class="search-bar" method="get"
                  action="${pageContext.request.contextPath}/admin/properties">

                <input type="text" name="address"
                       value="${keyword}"
                       placeholder="Search address">

                <select name="type">
                    <option value="">All Type</option>
                    <option value="SALE" ${type == 'SALE' ? 'selected' : ''}>Sale</option>
                    <option value="RENT" ${type == 'RENT' ? 'selected' : ''}>Rent</option>
                </select>

                <input type="number" name="minPrice"
                       value="${minPrice}" placeholder="Min price">

                <input type="number" name="maxPrice"
                       value="${maxPrice}" placeholder="Max price">

                <select name="sort">
                    <option value="">Newest</option>
                    <option value="price_asc" ${sort == 'price_asc' ? 'selected' : ''}>Price ↑</option>
                    <option value="price_desc" ${sort == 'price_desc' ? 'selected' : ''}>Price ↓</option>
                    <option value="oldest" ${sort == 'oldest' ? 'selected' : ''}>Oldest</option>
                </select>

                <select name="size">
                    <option value="5" ${result.size == 5 ? 'selected' : ''}>5</option>
                    <option value="10" ${result.size == 10 ? 'selected' : ''}>10</option>
                    <option value="20" ${result.size == 20 ? 'selected' : ''}>20</option>
                </select>

                <button class="btn-search">Search</button>
            </form>

            <!-- BASE URL (GIỮ FILTER) -->
            <c:url var="baseUrl" value="/admin/properties">
                <c:param name="address" value="${keyword}" />
                <c:param name="type" value="${type}" />
                <c:param name="minPrice" value="${minPrice}" />
                <c:param name="maxPrice" value="${maxPrice}" />
                <c:param name="sort" value="${sort}" />
                <c:param name="size" value="${result.size}" />
            </c:url>

            <!-- TABLE -->
            <div class="table-wrapper">
                <table class="property-table">

                    <thead>
                    <tr>
                        <th>#</th>
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

                    <c:forEach var="p" items="${result.data}" varStatus="loop">

                        <tr>
                            <!-- FIX STT -->
                            <td>
                                    ${(result.page - 1) * result.size + loop.index + 1}
                            </td>

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

                                <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/properties/approve">
                                        <input type="hidden" name="id" value="${p.id}">
                                        <button class="btn-approve">Approve</button>
                                    </form>
                                </c:if>

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

            <!-- PAGINATION -->
            <div class="pagination">

                <c:if test="${result.page > 1}">
                    <a href="${baseUrl}&page=${result.page - 1}">Previous</a>
                </c:if>

                <c:forEach begin="1" end="${result.totalPages}" var="i">
                    <a href="${baseUrl}&page=${i}"
                       class="${i == result.page ? 'active' : ''}">
                            ${i}
                    </a>
                </c:forEach>

                <c:if test="${result.page < result.totalPages}">
                    <a href="${baseUrl}&page=${result.page + 1}">Next</a>
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

<script src="${pageContext.request.contextPath}/assets/js/admin/property-dashboard.js?v=2"></script>

</body>
</html>