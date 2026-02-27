<%@ include file="common/header.jsp" %>

<div class="hero page-inner overlay"
     style="background-image: url('${pageContext.request.contextPath}/assets/images/hero_bg_3.jpg')">

    <div class="container">
        <div class="row justify-content-center align-items-center">
            <div class="col-lg-9 text-center mt-5">
                <h1 class="heading">About</h1>

                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb text-center justify-content-center">
                        <li class="breadcrumb-item">
                            <a href="${pageContext.request.contextPath}/">Home</a>
                        </li>
                        <li class="breadcrumb-item active text-white-50">
                            About
                        </li>
                    </ol>
                </nav>
            </div>
        </div>
    </div>
</div>

<div class="section">
    <div class="container">
        <div class="row text-left mb-5">
            <div class="col-12">
                <h2 class="font-weight-bold heading text-primary mb-4">About REMS</h2>
            </div>

            <div class="col-lg-6">
                <p class="text-black-50">
                    Lorem ipsum dolor sit amet consectetur adipisicing elit.
                </p>
                <p class="text-black-50">
                    Lorem ipsum dolor sit amet consectetur adipisicing elit.
                </p>
            </div>

            <div class="col-lg-6">
                <p class="text-black-50">
                    Lorem ipsum dolor sit amet consectetur adipisicing elit.
                </p>
            </div>
        </div>
    </div>
</div>

<div class="section pt-0">
    <div class="container">
        <div class="row justify-content-between mb-5">
            <div class="col-lg-7 order-lg-2">
                <div class="img-about dots">
                    <img src="${pageContext.request.contextPath}/assets/images/hero_bg_3.jpg"
                         class="img-fluid" />
                </div>
            </div>

            <div class="col-lg-4">
                <div class="d-flex feature-h">
          <span class="wrap-icon me-3">
            <span class="icon-home2"></span>
          </span>
                    <div class="feature-text">
                        <h3 class="heading">Quality properties</h3>
                        <p class="text-black-50">High quality listings.</p>
                    </div>
                </div>

                <div class="d-flex feature-h">
          <span class="wrap-icon me-3">
            <span class="icon-person"></span>
          </span>
                    <div class="feature-text">
                        <h3 class="heading">Top rated agents</h3>
                        <p class="text-black-50">Professional agents.</p>
                    </div>
                </div>

                <div class="d-flex feature-h">
          <span class="wrap-icon me-3">
            <span class="icon-security"></span>
          </span>
                    <div class="feature-text">
                        <h3 class="heading">Easy and safe</h3>
                        <p class="text-black-50">Secure transactions.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="section">
    <div class="container">
        <div class="row">
            <div class="col-md-4">
                <img src="${pageContext.request.contextPath}/assets/images/img_1.jpg"
                     class="img-fluid" />
            </div>
            <div class="col-md-4 mt-lg-5">
                <img src="${pageContext.request.contextPath}/assets/images/img_3.jpg"
                     class="img-fluid" />
            </div>
            <div class="col-md-4">
                <img src="${pageContext.request.contextPath}/assets/images/img_2.jpg"
                     class="img-fluid" />
            </div>
        </div>
    </div>
</div>

<%@ include file="common/footer.jsp" %>