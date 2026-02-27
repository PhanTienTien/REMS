let currentEmail = "";
let countdownInterval = null;
let resendInterval = null;

// ==============================
// REGISTER FORM SUBMIT
// ==============================
const registerForm = document.getElementById("registerForm");

if (registerForm) {
    registerForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const body = new URLSearchParams(new FormData(registerForm));
        const res = await fetch(contextPath() + "/auth", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8" },
            body
        });

        const data = await res.json();

        if (data.status === "success") {
            currentEmail = body.get("email"); // URLSearchParams có get()
            openModal();
        } else {
            document.getElementById("registerError").innerText = data.message;
        }
    });
}

function openModal() {

    const hiddenEmail = document.getElementById("otpEmail");

    if (hiddenEmail && hiddenEmail.value) {
        currentEmail = hiddenEmail.value;
    }

    document.getElementById("otpModal").classList.add("active");

    setupOtpInputs();
    startCountdown(300);
    startResendTimer();
}

// ==============================
// OTP INPUT AUTO MOVE
// ==============================
function setupOtpInputs() {

    const inputs = document.querySelectorAll(".otp-inputs input[type='text']");

    inputs.forEach((input, index) => {

        input.value = "";

        // chỉ cho nhập số
        input.addEventListener("input", function () {
            this.value = this.value.replace(/[^0-9]/g, '');

            if (this.value.length === 1 && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }
        });

        input.addEventListener("keydown", function (e) {
            if (e.key === "Backspace" && this.value === "" && index > 0) {
                inputs[index - 1].focus();
            }
        });

    });

    if (inputs.length > 0) {
        inputs[0].focus();
    }
}

// ==============================
// GET OTP VALUE
// ==============================
function getOtpValue() {
    const inputs = document.querySelectorAll(".otp-inputs input[type='text']");
    return Array.from(inputs).map(i => i.value).join("");
}

// ==============================
// COUNTDOWN 5 MINUTES
// ==============================
function startCountdown(seconds) {

    const countdown = document.getElementById("countdown");

    if (countdownInterval) {
        clearInterval(countdownInterval);
    }

    countdownInterval = setInterval(() => {

        let min = Math.floor(seconds / 60);
        let sec = seconds % 60;

        countdown.innerText =
            `${String(min).padStart(2, '0')}:${String(sec).padStart(2, '0')}`;

        seconds--;

        if (seconds < 0) {
            clearInterval(countdownInterval);
            countdown.innerText = "Hết hạn OTP";
        }

    }, 1000);
}

// ==============================
// RESEND TIMER 60s
// ==============================
function startResendTimer() {

    const btn = document.getElementById("resendBtn");

    if (resendInterval) {
        clearInterval(resendInterval);
    }

    btn.disabled = true;
    let seconds = 60;

    resendInterval = setInterval(() => {

        btn.innerText = `Gửi lại OTP (${seconds}s)`;
        seconds--;

        if (seconds < 0) {
            clearInterval(resendInterval);
            btn.disabled = false;
            btn.innerText = "Gửi lại OTP";
        }

    }, 1000);
}

// ==============================
// RESEND OTP
// ==============================
async function resendOtp() {

    const body = new URLSearchParams();
    body.append("action", "resend");
    body.append("email", currentEmail);

    const res = await fetch(contextPath() + "/auth", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
        },
        body
    });

    const data = await res.json();

    if (data.status === "success") {
        startCountdown(300);
        startResendTimer();
    } else {
        document.getElementById("otpError").innerText = data.message;
    }
}

// ==============================
// VERIFY OTP
// ==============================
async function verifyOtp() {

    const otp = getOtpValue();

    if (otp.length !== 6) {
        document.getElementById("otpError").innerText = "Vui lòng nhập đủ 6 số OTP";
        return;
    }

    const body = new URLSearchParams();
    body.append("action", "verify");
    body.append("email", currentEmail);
    body.append("otp", otp);

    const res = await fetch(contextPath() + "/auth", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
        },
        body
    });

    const data = await res.json();

    if (data.status === "success") {

        document.getElementById("otpError").innerText = "";
        alert("Xác thực thành công!");

        setTimeout(() => {
            window.location.href = contextPath() + "/auth?action=login";
        }, 1000);

    } else {
        document.getElementById("otpError").innerText = data.message;
    }
}

// ==============================
// GET CONTEXT PATH SAFE
// ==============================
function contextPath() {
    return document.body.getAttribute("data-context") || "";
}

document.addEventListener("DOMContentLoaded", function () {
    const otpModal = document.getElementById("otpModal");

    if (otpModal && otpModal.classList.contains("active")) {
        openModal();
    }
});