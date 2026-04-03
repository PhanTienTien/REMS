<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Static Resource Test</title>
</head>
<body>
    <h1>Static Resource Test</h1>
    
    <h2>Testing Images</h2>
    <img src="${pageContext.request.contextPath}/assets/images/img_1.jpg" alt="Test Image 1" style="width: 200px;">
    <img src="${pageContext.request.contextPath}/assets/images/favicon.png" alt="Favicon" style="width: 50px;">
    
    <h2>Testing Uploads</h2>
    <img src="${pageContext.request.contextPath}/uploads/properties/property1.jpg" alt="Property 1" style="width: 200px;">
    
    <h2>Testing CSS</h2>
    <div style="background-color: #007bff; color: white; padding: 10px;">
        If this is blue, CSS is working
    </div>
    
    <h2>Context Path Info</h2>
    <p>Context Path: ${pageContext.request.contextPath}</p>
    <p>Request URI: ${pageContext.request.requestURI}</p>
    
    <h2>Resource Links</h2>
    <ul>
        <li><a href="${pageContext.request.contextPath}/assets/images/">Images Directory</a></li>
        <li><a href="${pageContext.request.contextPath}/uploads/properties/">Uploads Directory</a></li>
        <li><a href="${pageContext.request.contextPath}/assets/css/style.css">Style.css</a></li>
    </ul>
</body>
</html>
