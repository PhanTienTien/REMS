<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="author" content="Untree.co" />
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/images/favicon.png" />

    <meta name="description" content="" />
    <meta name="keywords" content="bootstrap, bootstrap5" />

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600;700&display=swap" rel="stylesheet" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/icomoon/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/flaticon/font/flaticon.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tiny-slider.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/aos.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />

    <title>Property Management System</title>
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

                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/views/customer/properties">Properties</a></li>
                    <li><a href="${pageContext.request.contextPath}/views/customer/services">Services</a></li>
                    <li><a href="${pageContext.request.contextPath}/views/customer/about">About</a></li>
                    <li><a href="${pageContext.request.contextPath}/views/customer/contact">Contact</a></li>

                    <c:choose>

                        <c:when test="${not empty sessionScope.user}">

                            <li class="has-children">
                                <a href="#">${sessionScope.user.userName}</a>
                                <ul class="dropdown">
                                    <li>
                                        <a href="${pageContext.request.contextPath}/user/profile">
                                            My Profile
                                        </a>
                                    </li>
                                    <li>
                                        <a href="${pageContext.request.contextPath}/logout">
                                            Logout
                                        </a>
                                    </li>
                                </ul>
                            </li>

                        </c:when>

                        <c:otherwise>

                            <li>
                                <a href="${pageContext.request.contextPath}/auth"
                                   class="btn btn-primary text-white px-3">
                                    Login
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