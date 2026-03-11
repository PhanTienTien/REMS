function openCreateModal(){
    document.getElementById("createModal").style.display="flex";
}

function closeCreateModal(){
    document.getElementById("createModal").style.display="none";
}

function openEditModal(id,title,address,description,type,price){

    document.getElementById("editId").value=id;
    document.getElementById("editTitle").value=title;
    document.getElementById("editAddress").value=address;
    document.getElementById("editDescription").value=description;
    document.getElementById("editType").value=type;
    document.getElementById("editPrice").value=price;

    document.getElementById("editModal").style.display="flex";

}

function closeEditModal(){
    document.getElementById("editModal").style.display="none";
}

window.onclick=function(event){

    const createModal=document.getElementById("createModal");
    const editModal=document.getElementById("editModal");

    if(event.target===createModal){
        createModal.style.display="none";
    }

    if(event.target===editModal){
        editModal.style.display="none";
    }

}

function showLoading(){
    document.getElementById("loadingSpinner").style.display="flex";
}

function hideLoading(){
    document.getElementById("loadingSpinner").style.display="none";
}

function showToast(message){

    const toast=document.getElementById("toast");

    toast.innerText=message;

    toast.style.display="block";

    setTimeout(()=>{
        toast.style.display="none";
    },3000);

}

let confirmCallback=null;

function confirmAction(message,callback){

    document.getElementById("confirmMessage").innerText=message;

    confirmCallback=callback;

    document.getElementById("confirmDialog").style.display="flex";

}

function confirmYes(){

    if(confirmCallback){
        confirmCallback();
    }

    closeConfirm();

}

function closeConfirm(){
    document.getElementById("confirmDialog").style.display="none";
}

document.querySelectorAll(".btn-delete").forEach(btn=>{

    btn.addEventListener("click",(e)=>{

        e.preventDefault();

        const form=btn.closest("form");

        confirmAction("Delete this property?",()=>{

            showLoading();

            form.submit();

        });

    });

});

document.querySelectorAll(".property-table th").forEach((header,i)=>{

    header.addEventListener("click",()=>{

        const table=header.closest("table");

        const rows=Array.from(table.querySelectorAll("tbody tr"));

        const asc=header.classList.toggle("asc");

        rows.sort((a,b)=>{

            const A=a.children[i].innerText;
            const B=b.children[i].innerText;

            return asc?A.localeCompare(B):B.localeCompare(A);

        });

        rows.forEach(r=>table.querySelector("tbody").appendChild(r));

    });

});

const searchInput=document.querySelector("input[name='address']");

if(searchInput){

    searchInput.addEventListener("keyup",()=>{

        const value=searchInput.value.toLowerCase();

        document.querySelectorAll(".property-table tbody tr").forEach(row=>{

            const text=row.innerText.toLowerCase();

            row.style.display=text.includes(value)?"":"none";

        });

    });

}

document.querySelectorAll("form").forEach(f=>{

    f.addEventListener("submit",()=>{

        showLoading();

    });

});

window.onload = function(){

    const modal = "${openModal}";

    if(modal === "create"){
        openCreateModal();
    }

    if(modal === "edit"){
        openEditModal(
            "${formId}",
            "${formTitle}",
            "${formAddress}",
            "${formDescription}",
            "${formType}",
            "${formPrice}"
        );
    }

}