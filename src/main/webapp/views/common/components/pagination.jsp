<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%-- Pagination Component --%>
<c:if test="${pagination.totalPages > 1}">
    <div class="pagination-wrapper">
        <div class="pagination-info">
            <span>Hiển thị ${pagination.startItem}-${pagination.endItem} của ${pagination.totalItems} kết quả</span>
        </div>
        
        <nav class="pagination-nav">
            <ul class="pagination">
                <c:if test="${pagination.hasPreviousPage}">
                    <li>
                        <a href="?page=${pagination.previousPage}&size=${pagination.size}" class="page-link">
                            <i class="fas fa-chevron-left"></i> Trước
                        </a>
                    </li>
                </c:if>
                
                <c:forEach begin="1" end="${pagination.totalPages}" var="pageNum">
                    <c:choose>
                        <c:when test="${pageNum == pagination.page}">
                            <li class="active">
                                <span class="page-link">${pageNum}</span>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li>
                                <a href="?page=${pageNum}&size=${pagination.size}" class="page-link">${pageNum}</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                
                <c:if test="${pagination.hasNextPage}">
                    <li>
                        <a href="?page=${pagination.nextPage}&size=${pagination.size}" class="page-link">
                            Sau <i class="fas fa-chevron-right"></i>
                        </a>
                    </li>
                </c:if>
            </ul>
        </nav>
        
        <div class="page-size-selector">
            <label for="pageSize">Hiển thị:</label>
            <select id="pageSize" onchange="changePageSize(this.value)">
                <option value="10" ${pagination.size == 10 ? 'selected' : ''}>10</option>
                <option value="20" ${pagination.size == 20 ? 'selected' : ''}>20</option>
                <option value="50" ${pagination.size == 50 ? 'selected' : ''}>50</option>
            </select>
        </div>
    </div>
    
    <script>
        function changePageSize(size) {
            const url = new URL(window.location);
            url.searchParams.set('size', size);
            url.searchParams.set('page', '1');
            window.location.href = url.toString();
        }
    </script>
</c:if>

<style>
.pagination-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 24px;
    padding: 20px;
    background: white;
    border-radius: 12px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    flex-wrap: wrap;
    gap: 16px;
}

.pagination-info {
    color: #6b7280;
    font-size: 0.875rem;
}

.pagination-nav {
    flex: 1;
    display: flex;
    justify-content: center;
}

.pagination {
    display: flex;
    list-style: none;
    margin: 0;
    padding: 0;
    gap: 4px;
}

.pagination li {
    display: flex;
}

.page-link {
    display: flex;
    align-items: center;
    padding: 8px 12px;
    border: 1px solid #e5e7eb;
    background: white;
    color: #374151;
    text-decoration: none;
    border-radius: 6px;
    font-size: 0.875rem;
    font-weight: 500;
    transition: all 0.2s ease;
    min-width: 40px;
    justify-content: center;
}

.page-link:hover {
    background: #f9fafb;
    border-color: #d1d5db;
    color: #1f2937;
}

.pagination li.active .page-link {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-color: #667eea;
    color: white;
}

.page-size-selector {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 0.875rem;
    color: #6b7280;
}

.page-size-selector select {
    padding: 6px 8px;
    border: 1px solid #e5e7eb;
    border-radius: 6px;
    font-size: 0.875rem;
    background: white;
    cursor: pointer;
}

@media (max-width: 768px) {
    .pagination-wrapper {
        flex-direction: column;
        text-align: center;
    }
    
    .pagination-nav {
        order: -1;
    }
    
    .pagination {
        flex-wrap: wrap;
        justify-content: center;
    }
    
    .page-link {
        padding: 6px 10px;
        font-size: 0.8rem;
        min-width: 32px;
    }
}
</style>
