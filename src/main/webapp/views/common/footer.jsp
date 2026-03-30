<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/icomoon/style.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/flaticon/font/flaticon.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tiny-slider.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/aos.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />

<div class="site-footer">
    <div class="container">
        <div class="row">
            <div class="col-lg-4">
                <div class="widget">
                    <h3 class="footer-heading">REMS</h3>
                    <p>
                        Nền tảng quản lý và giao dịch bất động sản hiện đại.
                        Tìm kiếm, đặt lịch và quản lý tài sản của bạn một cách dễ dàng.
                    </p>
                </div>
            </div>

            <div class="col-lg-2 col-md-6">
                <div class="widget">
                    <h3 class="footer-heading">Liên kết nhanh</h3>
                    <ul class="list-unstyled links">
                        <li><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
                        <li><a href="${pageContext.request.contextPath}/about">Giới thiệu</a></li>
                        <li><a href="${pageContext.request.contextPath}/customer/properties">Bất động sản</a></li>
                        <li><a href="${pageContext.request.contextPath}/contact">Liên hệ</a></li>
                    </ul>
                </div>
            </div>

            <div class="col-lg-3 col-md-6">
                <div class="widget">
                    <h3 class="footer-heading">Tài khoản</h3>
                    <ul class="list-unstyled links">
                        <c:choose>
                            <c:when test="${not empty sessionScope.currentUser}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/customer/profile">
                                        Hồ sơ của tôi
                                    </a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/customer/profile/bookings">
                                        Lịch đặt của tôi
                                    </a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/customer/profile/favorites">
                                        Yêu thích
                                    </a>
                                </li>
                            </c:when>

                            <c:otherwise>
                                <li>
                                    <a href="${pageContext.request.contextPath}/auth">
                                        Đăng nhập
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </div>

            <div class="col-lg-3">
                <div class="widget">
                    <h3 class="footer-heading">Liên hệ</h3>
                    <ul class="list-unstyled links">
                        <li>Email: support@rems.com</li>
                        <li>Điện thoại: +84 123 456 789</li>
                        <li>Địa chỉ: Hà Nội, Việt Nam</li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="row mt-5">
            <div class="col-12 text-center">
                <div class="border-top pt-4">
                    <p>
                        © <script>document.write(new Date().getFullYear());</script>
                        REMS. Bảo lưu mọi quyền.
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="overlayer"></div>

<div class="loader">
    <div class="spinner-border" role="status">
        <span class="visually-hidden">Đang tải...</span>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/tiny-slider.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/aos.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/navbar.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/counter.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/custom.js"></script>
