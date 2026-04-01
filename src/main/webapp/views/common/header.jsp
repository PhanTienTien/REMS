<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="author" content="Untree.co" />
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/images/favicon.png" />

    <meta name="description" content="" />
    <meta name="keywords" content="bat dong san, quan ly, giao dich" />

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600;700&display=swap" rel="stylesheet" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/icomoon/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/flaticon/font/flaticon.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tiny-slider.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/aos.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />

    <title>Hệ thống quản lý bất động sản</title>
</head>
<body>

<div class="site-mobile-menu site-navbar-target">
    <div class="site-mobile-menu-header">
        <div class="site-mobile-menu-close">
            <span class="icofont-close js-menu-toggle"></span>
        </div>
    </div>
    <div class="site-mobile-menu-body"></div>
</div>

<nav class="site-nav">
    <div class="container">
        <div class="menu-bg-wrap">
            <div class="site-navigation">
                <a href="${pageContext.request.contextPath}/" class="logo m-0 float-start">
                    REMS
                </a>

                <ul class="js-clone-nav d-none d-lg-inline-block text-start site-menu float-end">
                    <li><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/about">Giới thiệu</a></li>
                    <li><a href="${pageContext.request.contextPath}/customer/properties">Bất động sản</a></li>
                    <li><a href="${pageContext.request.contextPath}/customer/contact">Liên hệ</a></li>

                    <c:choose>
                        <c:when test="${not empty sessionScope.currentUser}">
                            <li class="has-children">
                                <a href="#">${sessionScope.currentUser.fullName}</a>
                                <ul class="dropdown">
                                    <li>
                                        <a href="${pageContext.request.contextPath}/customer/profile">
                                            Hồ sơ của tôi
                                        </a>
                                    </li>
                                    <li>
                                        <form action="${pageContext.request.contextPath}/logout"
                                              method="post"
                                              style="display:inline;">
                                            <button type="submit" class="logout-link">
                                                Đăng xuất
                                            </button>
                                        </form>
                                    </li>
                                </ul>
                            </li>
                        </c:when>

                        <c:otherwise>
                            <li>
                                <a href="${pageContext.request.contextPath}/auth"
                                   class="btn btn-primary text-white px-3">
                                    Đăng nhập
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>

                <a href="#" class="burger light me-auto float-end mt-1 site-menu-toggle js-menu-toggle d-inline-block d-lg-none">
                    <span></span>
                </a>
            </div>
        </div>
    </div>
</nav>
