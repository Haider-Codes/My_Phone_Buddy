console.log("Javascript for viewing contact modal..");

// Grabbing modal
const modal = document.getElementById('modal');

// Grabbing modal component 
let modalName = document.getElementById('modal-name');
let modalEmail = document.getElementById('modal-email');
let modalPhoneNumber = document.getElementById('modal-phoneNumber');
let modalAddress = document.getElementById('modal-address');
let modalDescription = document.getElementById('modal-description');
let modalWebsite = document.getElementById('modal-website');
let modalLinkedIn = document.getElementById('modal-linkedIn');
let modalPicture = document.getElementById('modal-picture');
let isModalFavourite = document.getElementById('modal-favourite');

isModalFavourite.classList.add('hidden')

// options with default values
const options = {
    placement: 'center',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'modal',
  override: true
};

// Creating a modal object
const modalObj = new Modal(modal, options, instanceOptions);

// Defined a moddle show function
const showModal = () => {
    modalObj.show();
}

const loadContact = (id) => {
    console.log("modal contact selected");
    showModal();
    const result = async () => {
        try {
        let data = await((await fetch(`https://my-phone-buddy.onrender.com/api/contact?id=${id}`)).json());
        console.log(data);
        console.log("Email of contact is:", data.email);
        modalName.innerHTML = data.name;
        modalEmail.innerHTML = data.email;
        modalPhoneNumber.innerHTML = data.phoneNumber;
        modalAddress.innerHTML = data.address;
        modalDescription.innerHTML = data.description;
        modalWebsite.href = data.websiteLink;
        modalWebsite.innerHTML = data.websiteLink
        modalLinkedIn.href = data.linkedInLink;
        modalLinkedIn.innerHTML = data.linkedInLink;
        data.favourite ? isModalFavourite.classList.remove('hidden') : isModalFavourite.classList.add('hidden')
        data.picture != null ?modalPicture.setAttribute("src", data.picture): modalPicture.setAttribute("src", "/assets/default-profile-image-2.png");
        }
        catch(error) {
            console.log(error);
        }
    }
    return result();
}