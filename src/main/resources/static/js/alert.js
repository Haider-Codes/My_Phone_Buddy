console.log("Alert Js Script");

const triggerAlert = (id) => {
    Swal.fire({
    title: `Do you want to delete this contact?`,
    showDenyButton: true,
    showCancelButton: true,
    confirmButtonText: "Yes, Delete it",
    denyButtonText: `Don't Delete it`
    }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed){
        const url = "/user/contacts/delete?contactId="+id;
        Swal.fire("Contact successfully deleted!", "", "success");
        window.location.replace(url);
    } 
    else if  (result.isDenied) {
        Swal.fire("Some error ocurred while deletion!", "", "info");
    } 
    });
}