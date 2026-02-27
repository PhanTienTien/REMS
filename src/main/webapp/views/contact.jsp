<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Contact</title>

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
    <a href="<%=path%>/home.jsp" class="logo">REMS</a>
  </div>
</nav>

<!-- HERO -->
<div class="hero page-inner overlay"
     style="background-image: url('<%=path%>/images/hero_bg_1.jpg')">
  <div class="container text-center">
    <h1>Contact Us</h1>
  </div>
</div>

<% if (request.getAttribute("success") != null) { %>
<p style="color:green;">
  <%= request.getAttribute("success") %>
</p>
<% } %>

<% if (request.getAttribute("error") != null) { %>
<p style="color:red;">
  <%= request.getAttribute("error") %>
</p>
<% } %>

<!-- CONTACT FORM -->
<div class="section">
  <div class="container">
    <form action="contact" method="post">
      <input type="text" name="name" placeholder="Your Name" class="form-control"/>
      <input type="email" name="email" placeholder="Your Email" class="form-control"/>
      <input type="text" name="subject" placeholder="Subject" class="form-control"/>
      <textarea name="message" rows="5" placeholder="Message" class="form-control"></textarea>
      <input type="submit" value="Send Message" class="btn btn-primary"/>
    </form>
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

<script src="<%=path%>/js/bootstrap.bundle.min.js"></script>
<script src="<%=path%>/js/tiny-slider.js"></script>
<script src="<%=path%>/js/aos.js"></script>
<script src="<%=path%>/js/navbar.js"></script>
<script src="<%=path%>/js/counter.js"></script>
<script src="<%=path%>/js/custom.js"></script>

</body>
</html>