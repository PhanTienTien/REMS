<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Quản lý bất động sản</title>

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
                <div class="error-box">${error}</div>
            </c:if>

            <div class="page-header">
                <h2>Quản lý bất động sản</h2>
                <button class="btn-primary" onclick="openCreateModal()">
                    + Tạo bất động sản
                </button>
            </div>

            <form class="search-bar" method="get"
                  action="${pageContext.request.contextPath}/admin/properties">

                <input type="text" name="address"
                       value="${keyword}"
                       placeholder="Tìm theo địa chỉ">

                <select name="type">
                    <option value="">Tất cả loại</option>
                    <option value="SALE" ${type == 'SALE' ? 'selected' : ''}>Bán</option>
                    <option value="RENT" ${type == 'RENT' ? 'selected' : ''}>Cho thuê</option>
                </select>

                <input type="number" name="minPrice"
                       value="${minPrice}" placeholder="Giá từ">

                <input type="number" name="maxPrice"
                       value="${maxPrice}" placeholder="Giá đến">

                <select name="sort">
                    <option value="">Mới nhất</option>
                    <option value="price_asc" ${sort == 'price_asc' ? 'selected' : ''}>Giá tăng dần</option>
                    <option value="price_desc" ${sort == 'price_desc' ? 'selected' : ''}>Giá giảm dần</option>
                    <option value="oldest" ${sort == 'oldest' ? 'selected' : ''}>Cũ nhất</option>
                </select>

                <select name="size">
                    <option value="5" ${result.size == 5 ? 'selected' : ''}>5</option>
                    <option value="10" ${result.size == 10 ? 'selected' : ''}>10</option>
                    <option value="20" ${result.size == 20 ? 'selected' : ''}>20</option>
                </select>

                <button class="btn-search">Tìm kiếm</button>
            </form>

            <c:url var="baseUrl" value="/admin/properties">
                <c:param name="address" value="${keyword}" />
                <c:param name="type" value="${type}" />
                <c:param name="minPrice" value="${minPrice}" />
                <c:param name="maxPrice" value="${maxPrice}" />
                <c:param name="sort" value="${sort}" />
                <c:param name="size" value="${result.size}" />
            </c:url>

            <div class="table-wrapper">
                <table class="property-table">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Ảnh</th>
                        <th>Tiêu đề</th>
                        <th>Địa chỉ</th>
                        <th>Mô tả</th>
                        <th>Loại</th>
                        <th>Giá</th>
                        <th>Trạng thái</th>
                        <th>Ngày tạo</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach var="p" items="${result.data}" varStatus="loop">
                        <tr>
                            <td>${(result.page - 1) * result.size + loop.index + 1}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty thumbnails[p.id]}">
                                        <img src="${pageContext.request.contextPath}${thumbnails[p.id]}"
                                             alt="${p.title}"
                                             style="width:72px;height:56px;object-fit:cover;border-radius:8px;">
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Chưa có ảnh</span>
                                    </c:otherwise>
                                </c:choose>
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
                                    Sửa
                                </button>

                                <c:if test="${sessionScope.currentUser.role == 'ADMIN' and p.status == 'DRAFT'}">
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/properties/approve">
                                        <input type="hidden" name="_csrf" value="${sessionScope.CSRF_TOKEN}"/>
                                        <input type="hidden" name="id" value="${p.id}">
                                        <button class="btn-approve">Duyệt</button>
                                    </form>
                                </c:if>

                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/properties/delete">
                                    <input type="hidden" name="_csrf" value="${sessionScope.CSRF_TOKEN}"/>
                                    <input type="hidden" name="id" value="${p.id}">
                                    <button class="btn-delete"
                                            onclick="return confirm('Bạn có chắc muốn xóa bất động sản này?')">
                                        Xóa
                                    </button>
                                </form>

                                <button class="btn-image"
                                        onclick="openImageModal('${p.id}')">
                                    Ảnh
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="pagination">
                <c:if test="${result.page > 1}">
                    <a href="${baseUrl}&page=${result.page - 1}">Trước</a>
                </c:if>

                <c:forEach begin="1" end="${result.totalPages}" var="i">
                    <a href="${baseUrl}&page=${i}"
                       class="${i == result.page ? 'active' : ''}">
                            ${i}
                    </a>
                </c:forEach>

                <c:if test="${result.page < result.totalPages}">
                    <a href="${baseUrl}&page=${result.page + 1}">Sau</a>
                </c:if>
            </div>
        </div>
    </div>
</div>

<div id="createModal" class="modal">
    <div class="modal-content">
        <h3>Tạo bất động sản</h3>
        <form method="post"
              enctype="multipart/form-data"
              action="${pageContext.request.contextPath}/admin/properties/create">
            <input type="hidden" name="_csrf" value="${sessionScope.CSRF_TOKEN}"/>

            <input type="text" name="title" placeholder="Tiêu đề"
                   value="${formTitle}" required>

            <input type="text" name="address" placeholder="Địa chỉ"
                   value="${formAddress}" required>

            <textarea name="description"
                      placeholder="Mô tả">${formDescription}</textarea>

            <select name="type">
                <option value="SALE"
                        <c:if test="${formType == 'SALE'}">selected</c:if>>
                    Bán
                </option>

                <option value="RENT"
                        <c:if test="${formType == 'RENT'}">selected</c:if>>
                    Cho thuê
                </option>
            </select>

            <input type="number" name="price"
                   value="${formPrice}"
                   placeholder="Giá" required>

            <label>Ảnh bất động sản</label>

            <input type="file"
                   name="images"
                   multiple
                   accept="image/*">

            <div class="modal-actions">
                <button class="btn-primary">Tạo mới</button>

                <button type="button" onclick="closeCreateModal()" class="btn-cancel">
                    Hủy
                </button>
            </div>
        </form>
    </div>
</div>

<div id="editModal" class="modal">
    <div class="modal-content">
        <h3>Chỉnh sửa bất động sản</h3>
        <form method="post"
              action="${pageContext.request.contextPath}/admin/properties/edit">
            <input type="hidden" name="_csrf" value="${sessionScope.CSRF_TOKEN}"/>

            <input type="hidden" name="id" id="editId">
            <input type="text" name="title" id="editTitle">
            <input type="text" name="address" id="editAddress">
            <textarea name="description" id="editDescription"></textarea>

            <select name="type" id="editType">
                <option value="SALE">Bán</option>
                <option value="RENT">Cho thuê</option>
            </select>

            <input type="number" name="price" id="editPrice">

            <div class="modal-actions">
                <button class="btn-primary">Cập nhật</button>

                <button type="button" onclick="closeEditModal()" class="btn-cancel">
                    Hủy
                </button>
            </div>
        </form>
    </div>
</div>

<div id="imageModal" class="modal">
    <div class="modal-content">
        <h3>Quản lý ảnh</h3>

        <div id="imageList" class="image-grid"></div>

        <form id="uploadForm"
              method="post"
              enctype="multipart/form-data"
              action="${pageContext.request.contextPath}/admin/property-images?action=add">

            <input type="hidden" name="_csrf" value="${sessionScope.CSRF_TOKEN}"/>
            <input type="hidden" name="propertyId" id="imagePropertyId">
            <input type="file" name="images" multiple>
            <button>Thêm ảnh</button>
        </form>

        <button onclick="closeImageModal()" class="btn-cancel">
            Đóng
        </button>
    </div>
</div>

<div id="loadingSpinner" class="loading-spinner">
    <div class="spinner"></div>
</div>

<div id="toast" class="toast"></div>

<div id="confirmDialog" class="confirm-dialog">
    <div class="confirm-box">
        <p id="confirmMessage">Bạn có chắc không?</p>

        <div class="confirm-actions">
            <button onclick="confirmYes()" class="btn-danger">Có</button>
            <button onclick="closeConfirm()" class="btn-cancel">Hủy</button>
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
