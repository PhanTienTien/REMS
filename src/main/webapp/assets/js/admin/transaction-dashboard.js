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