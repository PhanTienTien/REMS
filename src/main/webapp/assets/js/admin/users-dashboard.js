document.addEventListener("DOMContentLoaded", function () {

    console.log("Admin Users page loaded");

});

function openCreateModal(){
    document.getElementById("createModal").style.display = "block";
}

function closeCreateModal(){
    document.getElementById("createModal").style.display = "none";
}

function openEditModal(id, fullName, email, phone, role, verified){

    document.getElementById("editModal").style.display = "block";

    document.getElementById("editId").value = id;
    document.getElementById("editFullName").value = fullName;
    document.getElementById("editEmail").value = email;
    document.getElementById("editPhone").value = phone;
    document.getElementById("editRole").value = role;
    document.getElementById("editVerified").value = verified;
}

function closeEditModal(){
    document.getElementById("editModal").style.display="none";
}