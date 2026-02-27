<%@ include file="views/common/header.jsp" %>

<div class="hero">
    <div class="hero-slide">
        <div class="img overlay"
             style="background-image: url('${pageContext.request.contextPath}/assets/images/hero_bg_3.jpg')"></div>
        <div class="img overlay"
             style="background-image: url('${pageContext.request.contextPath}/assets/images/hero_bg_2.jpg')"></div>
        <div class="img overlay"
             style="background-image: url('${pageContext.request.contextPath}/assets/images/hero_bg_1.jpg')"></div>
    </div>

    <div class="container">
        <div class="row justify-content-center align-items-center">
            <div class="col-lg-9 text-center">
                <h1 class="heading">
                    Easiest way to find your dream home
                </h1>

                <form action="${pageContext.request.contextPath}/search"
                      method="get"
                      class="narrow-w form-search d-flex align-items-stretch mb-3">

                    <input type="text"
                           class="form-control px-4"
                           placeholder="Search property..."
                           name="q" />

                    <button type="submit" class="btn btn-primary">
                        Search
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="section">
    <div class="container">
        <div class="row mb-5 align-items-center">
            <div class="col-lg-6">
                <h2 class="font-weight-bold text-primary heading">
                    Popular Properties
                </h2>
            </div>
            <div class="col-lg-6 text-lg-end">
                <a href="${pageContext.request.contextPath}/properties"
                   class="btn btn-primary text-white py-3 px-4">
                    View all properties
                </a>
            </div>
        </div>

        <!-- Sau này sẽ thay bằng c:forEach -->
        <div class="row">
            <div class="col-lg-4">
                <div class="property-item">
                    <img src="${pageContext.request.contextPath}/assets/images/img_1.jpg"
                         class="img-fluid"/>
                    <div class="property-content">
                        <div class="price mb-2">
                            <span>$1,291,000</span>
                        </div>
                        <span class="d-block mb-2 text-black-50">
                            5232 California Fake, Ave. 21BC
                        </span>
                        <span class="city d-block mb-3">
                            California, USA
                        </span>
                        <a href="#"
                           class="btn btn-primary py-2 px-3">
                            See details
                        </a>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<%@ include file="views/common/footer.jsp" %>