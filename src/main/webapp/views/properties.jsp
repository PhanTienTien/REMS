<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <title>Properties</title>

    <link rel="stylesheet" href="<%=path%>/assets/fonts/icomoon/style.css" />
    <link rel="stylesheet" href="<%=path%>/assets/fonts/flaticon/font/flaticon.css" />
    <link rel="stylesheet" href="<%=path%>/assets/css/tiny-slider.css" />
    <link rel="stylesheet" href="<%=path%>/assets/css/aos.css" />
    <link rel="stylesheet" href="<%=path%>/assets/css/style.css" />
</head>

<body>
<nav class="site-nav">
    <div class="container">
        <div class="site-navigation">
            <a href="<%=path%>/home.jsp" class="logo">Property</a>

            <ul class="site-menu float-end">
                <li><a href="<%=path%>/home.jsp">Home</a></li>
                <li class="active"><a href="<%=path%>/views/properties.jsp">Properties</a></li>
                <li><a href="<%=path%>/views/services.jsp">Services</a></li>
                <li><a href="<%=path%>/views/about.jsp">About</a></li>
                <li><a href="<%=path%>/views/contact.jsp">Contact</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="hero page-inner overlay"
     style="background-image: url('<%=path%>/images/hero_bg_1.jpg')">
    <div class="container text-center">
        <h1>Properties</h1>
    </div>
</div>

<div class="section section-properties">
    <div class="container">
        <div class="row">

            <div class="col-lg-4">
                <div class="property-item mb-30">
                    <a href="<%=path%>/property-single.jsp" class="img">
                        <img src="<%=path%>/images/img_1.jpg" class="img-fluid" />
                    </a>

                    <div class="property-content">
                        <div class="price mb-2"><span>$1,291,000</span></div>
                        <span class="d-block mb-2 text-black-50">
              5232 California Fake, Ave. 21BC
            </span>
                        <span class="city d-block mb-3">California, USA</span>

                        <a href="<%=path%>/property-single.jsp"
                           class="btn btn-primary">
                            See details
                        </a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="site-footer text-center">
    <p>
        Copyright &copy;
        <%= java.time.Year.now() %>
        All Rights Reserved.
    </p>
</div>

<script src="<%=path%>/js/bootstrap.bundle.min.js"></script>
<script src="<%=path%>/js/tiny-slider.js"></script>
<script src="<%=path%>/js/aos.js"></script>
<script src="<%=path%>/js/navbar.js"></script>
<script src="<%=path%>/js/counter.js"></script>
<script src="<%=path%>/js/custom.js"></script>

</body>
</html>