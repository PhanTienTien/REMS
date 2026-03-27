<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="../common/header.jsp" %>


<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/icomoon/style.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/flaticon/font/flaticon.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tiny-slider.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/aos.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />

<div class="hero page-inner overlay"
     style="background-image: url('${pageContext.request.contextPath}/assets/images/hero_bg_1.jpg');">

    <div class="container">
        <div class="row justify-content-center align-items-center">

            <div class="col-lg-8 text-center">
                <h1 class="heading" data-aos="fade-up">
                    About REMS
                </h1>
                <p class="text-white mb-4" data-aos="fade-up" data-aos-delay="100">
                    Nền tảng quản lý & giao dịch bất động sản hiện đại
                </p>
            </div>

        </div>
    </div>
</div>


<!-- ABOUT -->
<div class="section">
    <div class="container">

        <div class="row justify-content-between align-items-center">

            <div class="col-lg-6" data-aos="fade-up">
                <h2 class="section-title">Chúng tôi là ai?</h2>

                <p>
                    REMS là hệ thống quản lý bất động sản giúp kết nối người mua,
                    người thuê và nhà cung cấp một cách nhanh chóng, minh bạch.
                </p>

                <p>
                    Với công nghệ hiện đại, REMS giúp bạn tìm kiếm, đặt lịch,
                    quản lý giao dịch và theo dõi tài sản chỉ trong vài bước đơn giản.
                </p>

            </div>

            <div class="col-lg-5" data-aos="fade-up" data-aos-delay="100">
                <img src="${pageContext.request.contextPath}/assets/images/img_1.jpg"
                     alt="Image"
                     class="img-fluid rounded">
            </div>

        </div>

    </div>
</div>


<!-- FEATURES -->
<div class="section bg-light">
    <div class="container">

        <div class="row mb-5 text-center">
            <div class="col-lg-6 mx-auto">
                <h2 class="section-title">Tại sao chọn REMS?</h2>
                <p>Chúng tôi mang đến trải nghiệm tốt nhất cho người dùng</p>
            </div>
        </div>

        <div class="row">

            <div class="col-lg-4" data-aos="fade-up">
                <div class="feature-box">
                    <h3>Tìm kiếm nhanh</h3>
                    <p>Dễ dàng tìm bất động sản phù hợp chỉ trong vài giây.</p>
                </div>
            </div>

            <div class="col-lg-4" data-aos="fade-up" data-aos-delay="100">
                <div class="feature-box">
                    <h3>Đặt lịch dễ dàng</h3>
                    <p>Booking nhanh chóng, xác nhận minh bạch.</p>
                </div>
            </div>

            <div class="col-lg-4" data-aos="fade-up" data-aos-delay="200">
                <div class="feature-box">
                    <h3>Quản lý thông minh</h3>
                    <p>Theo dõi giao dịch và tài sản ngay trong dashboard.</p>
                </div>
            </div>

        </div>

    </div>
</div>


<!-- STATS -->
<div class="section">
    <div class="container">

        <div class="row text-center">

            <div class="col-lg-3 col-6" data-aos="fade-up">
                <h2 class="counter">1000+</h2>
                <p>Properties</p>
            </div>

            <div class="col-lg-3 col-6" data-aos="fade-up" data-aos-delay="100">
                <h2 class="counter">500+</h2>
                <p>Customers</p>
            </div>

            <div class="col-lg-3 col-6" data-aos="fade-up" data-aos-delay="200">
                <h2 class="counter">300+</h2>
                <p>Transactions</p>
            </div>

            <div class="col-lg-3 col-6" data-aos="fade-up" data-aos-delay="300">
                <h2 class="counter">99%</h2>
                <p>Satisfaction</p>
            </div>

        </div>

    </div>
</div>


<!-- TEAM -->
<div class="section bg-light">
    <div class="container">

        <div class="row mb-5 text-center">
            <div class="col-lg-6 mx-auto">
                <h2 class="section-title">Our Team</h2>
                <p>Những người đứng sau REMS</p>
            </div>
        </div>

        <div class="row">

            <div class="col-lg-4 text-center" data-aos="fade-up">
                <img src="${pageContext.request.contextPath}/assets/images/person_1.jpg"
                     class="img-fluid rounded-circle mb-3"
                     width="120">
                <h5>Founder</h5>
                <p>System Architect</p>
            </div>

            <div class="col-lg-4 text-center" data-aos="fade-up" data-aos-delay="100">
                <img src="${pageContext.request.contextPath}/assets/images/person_2.jpg"
                     class="img-fluid rounded-circle mb-3"
                     width="120">
                <h5>Backend Dev</h5>
                <p>Java Specialist</p>
            </div>

            <div class="col-lg-4 text-center" data-aos="fade-up" data-aos-delay="200">
                <img src="${pageContext.request.contextPath}/assets/images/person_3.jpg"
                     class="img-fluid rounded-circle mb-3"
                     width="120">
                <h5>Frontend Dev</h5>
                <p>UI/UX Designer</p>
            </div>

        </div>

    </div>
</div>


<%@ include file="../common/footer.jsp" %>