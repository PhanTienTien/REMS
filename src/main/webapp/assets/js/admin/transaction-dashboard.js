document.addEventListener("DOMContentLoaded", function(){

    const forms = document.querySelectorAll(".complete-form");

    forms.forEach(form => {

        form.addEventListener("submit", function(e){

            const ok = confirm("Complete this transaction?");

            if(!ok){
                e.preventDefault();
            }

        });

    });

});

const modal = document.getElementById("transactionModal");
const closeBtn = document.querySelector(".close-btn");

document.querySelectorAll(".view-btn").forEach(btn => {

    btn.addEventListener("click", () => {

        document.getElementById("m-id").innerText = btn.dataset.id;
        document.getElementById("m-property").innerText = btn.dataset.property;
        document.getElementById("m-customer").innerText = btn.dataset.customer;
        document.getElementById("m-amount").innerText = btn.dataset.amount;
        document.getElementById("m-status").innerText = btn.dataset.status;
        document.getElementById("m-type").innerText = btn.dataset.type;
        document.getElementById("m-date").innerText = btn.dataset.date;
        document.getElementById("m-processed").innerText = btn.dataset.processed;

        modal.style.display = "block";
    });

});

closeBtn.onclick = () => modal.style.display = "none";

window.onclick = (e) => {
    if (e.target === modal) {
        modal.style.display = "none";
    }
};