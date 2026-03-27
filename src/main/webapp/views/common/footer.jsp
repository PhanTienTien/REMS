<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/icomoon/style.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/flaticon/font/flaticon.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tiny-slider.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/aos.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />

<div class="site-footer">

    <div class="container">

        <div class="row">

            <!-- LOGO + ABOUT -->
            <div class="col-lg-4">
                <div class="widget">
                    <h3 class="footer-heading">REMS</h3>
                    <p>
                        Nền tảng quản lý và giao dịch bất động sản hiện đại.
                        Tìm kiếm, đặt lịch và quản lý tài sản của bạn một cách dễ dàng.
                    </p>
                </div>
            </div>

            <!-- QUICK LINKS -->
            <div class="col-lg-2 col-md-6">
                <div class="widget">
                    <h3 class="footer-heading">Quick Links</h3>
                    <ul class="list-unstyled links">
                        <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/about">About</a></li>
                        <li><a href="${pageContext.request.contextPath}/customer/properties">Properties</a></li>
                        <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                    </ul>
                </div>
            </div>

            <!-- ACCOUNT -->
            <div class="col-lg-3 col-md-6">
                <div class="widget">
                    <h3 class="footer-heading">Account</h3>
                    <ul class="list-unstyled links">

                        <c:choose>

                            <c:when test="${not empty sessionScope.currentUser}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/customer/profile">
                                        My Profile
                                    </a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/customer/profile/bookings">
                                        My Bookings
                                    </a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/customer/profile/favorites">
                                        Favorites
                                    </a>
                                </li>
                            </c:when>

                            <c:otherwise>
                                <li>
                                    <a href="${pageContext.request.contextPath}/auth">
                                        Login
                                    </a>
                                </li>
                            </c:otherwise>

                        </c:choose>

                    </ul>
                </div>
            </div>

            <!-- CONTACT -->
            <div class="col-lg-3">
                <div class="widget">
                    <h3 class="footer-heading">Contact</h3>
                    <ul class="list-unstyled links">
                        <li>Email: support@rems.com</li>
                        <li>Phone: +84 123 456 789</li>
                        <li>Address: Hanoi, Vietnam</li>
                    </ul>
                </div>
            </div>

        </div>

        <!-- LINE -->
        <div class="row mt-5">
            <div class="col-12 text-center">

                <div class="border-top pt-4">

                    <p>
                        © <script>document.write(new Date().getFullYear());</script>
                        REMS. All rights reserved.
                    </p>

                </div>

            </div>
        </div>

    </div>
</div>

<!-- LOADER + SCRIPTS -->

<div id="overlayer"></div>

<div class="loader">
    <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/tiny-slider.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/aos.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/navbar.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/counter.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/custom.js"></script>
