<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Services</title>

    <link rel="stylesheet" href="<%=path%>/assets/fonts/icomoon/style.css">
    <link rel="stylesheet" href="<%=path%>/assets/fonts/flaticon/font/flaticon.css">
    <link rel="stylesheet" href="<%=path%>/assets/css/tiny-slider.css">
    <link rel="stylesheet" href="<%=path%>/assets/css/aos.css">
    <link rel="stylesheet" href="<%=path%>/assets/css/style.css">
</head>
<body>

<nav class="site-nav">
    <div class="container">
        <div class="menu-bg-wrap">
            <div class="site-navigation">
                <a href="<%=path%>/home.jsp" class="logo m-0 float-start">REMS</a>

                <ul class="js-clone-nav d-none d-lg-inline-block text-start site-menu float-end">
                    <li><a href="<%=path%>/home.jsp">Home</a></li>
                    <li><a href="<%=path%>/views/user/properties.jsps.jsp">Properties</a></li>
                    <li class="active"><a href="<%=path%>/views/user/services.jsp">Services</a></li>
                    <li><a href="<%=path%>/views/user/about.jspt.jsp">About</a></li>
                    <li><a href="<%=path%>/views/user/contact.jspt.jsp">Contact</a></li>
                </ul>
            </div>
        </div>
    </div>
</nav>

<div class="hero page-inner overlay"
     style="background-image: url('<%=path%>/images/hero_bg_1.jpg')">

    <div class="container">
        <div class="row justify-content-center align-items-center">
            <div class="col-lg-9 text-center mt-5">
                <h1 class="heading">Services</h1>
            </div>
        </div>
    </div>
</div>

<div class="section bg-light">
    <div class="container">
        <div class="row">

            <div class="col-6 col-lg-3">
                <div class="box-feature mb-4">
                    <span class="flaticon-house mb-4 d-block"></span>
                    <h3>Quality Properties</h3>
                    <p>Far far away, behind the word mountains...</p>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="site-footer">
    <div class="container text-center">
        <p>
            Copyright &copy;
            <%= java.time.Year.now() %>
            - Property
        </p>
    </div>
</div>

<script src="<%=path%>/js/bootstrap.bundle.min.js"></script>
<script src="<%=path%>/js/tiny-slider.js"></script>
<script src="<%=path%>/js/aos.js"></script>
<script src="<%=path%>/js/navbar.js"></script>
<script src="<%=path%>/js/counter.js"></script>
<script src="<%=path%>/js/custom.js"></script>

</body>
</html>