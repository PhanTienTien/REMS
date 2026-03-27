<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="../common/header.jsp" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/icomoon/style.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/flaticon/font/flaticon.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tiny-slider.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/aos.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />

<!-- HERO -->
<div class="hero page-inner overlay"
     style="background-image: url('${pageContext.request.contextPath}/assets/images/hero_bg_2.jpg');">

  <div class="container">
    <div class="row justify-content-center align-items-center">

      <div class="col-lg-8 text-center">
        <h1 class="heading text-white" data-aos="fade-up">
          Contact Us
        </h1>
        <p class="text-white" data-aos="fade-up" data-aos-delay="100">
          Liên hệ với chúng tôi để được hỗ trợ nhanh nhất
        </p>
      </div>

    </div>
  </div>
</div>


<!-- CONTACT -->
<div class="section">
  <div class="container">

    <div class="row">

      <!-- FORM -->
      <div class="col-lg-7">

        <div class="contact-form-box" data-aos="fade-up">

          <h3 class="mb-4">Gửi tin nhắn</h3>

          <form method="post"
                action="${pageContext.request.contextPath}/contact">

            <div class="row">
              <div class="col-md-6 form-group">
                <input type="text"
                       class="form-control"
                       name="name"
                       placeholder="Họ tên"
                       required>
              </div>

              <div class="col-md-6 form-group">
                <input type="email"
                       class="form-control"
                       name="email"
                       placeholder="Email"
                       required>
              </div>
            </div>

            <div class="form-group mt-3">
              <input type="text"
                     class="form-control"
                     name="subject"
                     placeholder="Tiêu đề"
                     required>
            </div>

            <div class="form-group mt-3">
                            <textarea name="message"
                                      class="form-control"
                                      rows="6"
                                      placeholder="Nội dung..."
                                      required></textarea>
            </div>

            <div class="mt-4">
              <button type="submit"
                      class="btn btn-primary px-4">
                Gửi liên hệ
              </button>
            </div>

          </form>

        </div>

      </div>


      <!-- INFO -->
      <div class="col-lg-5">

        <div class="contact-info-box" data-aos="fade-up" data-aos-delay="100">

          <h3>Thông tin liên hệ</h3>

          <ul class="list-unstyled mt-4">

            <li class="mb-3">
              📍 <strong>Địa chỉ:</strong><br>
              Hanoi, Vietnam
            </li>

            <li class="mb-3">
              📞 <strong>Điện thoại:</strong><br>
              +84 123 456 789
            </li>

            <li class="mb-3">
              ✉️ <strong>Email:</strong><br>
              support@rems.com
            </li>

          </ul>

        </div>

      </div>

    </div>

  </div>
</div>


<!-- MAP -->
<div class="section pt-0">
  <div class="container">

    <div class="map-box" data-aos="fade-up">
      <iframe
              src="https://www.google.com/maps?q=Hanoi&output=embed"
              width="100%"
              height="350"
              style="border:0; border-radius:10px;"
              allowfullscreen=""
              loading="lazy">
      </iframe>
    </div>

  </div>
</div>


<%@ include file="../common/footer.jsp" %>