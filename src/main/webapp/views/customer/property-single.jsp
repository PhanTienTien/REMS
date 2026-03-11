<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <title>Property Detail</title>

    <!-- CSS -->
    <link rel="stylesheet" href="<%=path%>/assets/fonts/icomoon/style.css" />
    <link rel="stylesheet" href="<%=path%>/assets/fonts/flaticon/font/flaticon.css" />
    <link rel="stylesheet" href="<%=path%>/assets/css/tiny-slider.css" />
    <link rel="stylesheet" href="<%=path%>/assets/css/aos.css" />
    <link rel="stylesheet" href="<%=path%>/assets/css/style.css" />
</head>

<body>

<!-- NAV -->
<nav class="site-nav">
    <div class="container">
        <div class="site-navigation">
            <a href="<%=path%>/index.jsp" class="logo">Property</a>
            <ul class="site-menu float-end">
                <li><a href="<%=path%>/index.jsp">Home</a></li>
                <li><a href="<%=path%>/properties.jsp">Properties</a></li>
                <li><a href="<%=path%>/services.jsp">Services</a></li>
                <li><a href="<%=path%>/about.jsp">About</a></li>
                <li><a href="<%=path%>/contact.jsp">Contact</a></li>
            </ul>
        </div>
    </div>
</nav>

<!-- HERO -->
<div class="hero page-inner overlay"
     style="background-image: url('<%=path%>/images/hero_bg_3.jpg')">
    <div class="container text-center">
        <h1>5232 California AVE. 21BC</h1>
    </div>
</div>

<!-- PROPERTY DETAIL -->
<div class="section">
    <div class="container">
        <div class="row justify-content-between">

            <!-- Image Slider -->
            <div class="col-lg-7">
                <div class="img-property-slide">
                    <img src="<%=path%>/images/img_1.jpg" class="img-fluid" />
                    <img src="<%=path%>/images/img_2.jpg" class="img-fluid" />
                    <img src="<%=path%>/images/img_3.jpg" class="img-fluid" />
                </div>
            </div>

            <!-- Property Info -->
            <div class="col-lg-4">
                <h2 class="heading text-primary">5232 California Ave. 21BC</h2>
                <p class="meta">California, United States</p>

                <p class="text-black-50">
                    Lorem ipsum dolor sit amet consectetur adipisicing elit.
                </p>

                <!-- Agent -->
                <div class="agent-box p-4">
                    <img src="<%=path%>/images/person_2-min.jpg"
                         class="img-fluid mb-3"/>

                    <h3>Alicia Huston</h3>
                    <div class="meta mb-2">Real Estate</div>
                </div>

            </div>

        </div>
    </div>
</div>

<!-- FOOTER -->
<div class="site-footer text-center">
    <p>
        Copyright &copy;
        <%= java.time.Year.now() %>
        All Rights Reserved.
    </p>
</div>

<!-- JS -->
<script src="<%=path%>/js/bootstrap.bundle.min.js"></script>
<script src="<%=path%>/js/tiny-slider.js"></script>
<script src="<%=path%>/js/aos.js"></script>
<script src="<%=path%>/js/navbar.js"></script>
<script src="<%=path%>/js/counter.js"></script>
<script src="<%=path%>/js/custom.js"></script>

</body>
</html>