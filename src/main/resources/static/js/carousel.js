console.log('Welcome to carousel control script..');

let carousel_image_1 = document.getElementById('carousel-image-1');
let carousel_image_2 = document.getElementById('carousel-image-2');
let carousel_image_3 = document.getElementById('carousel-image-3');

const carouselChange = () => {
    if(document.querySelector('html').classList.contains('dark')) {
        carousel_image_1.setAttribute('src', '/assets/sample_dashboard.png');
        carousel_image_2.setAttribute('src', '/assets/sample_modal.png');
        carousel_image_3.setAttribute('src', '/assets/sample_ViewOfContactsWithSearch.png');
    }
    else {
        carousel_image_1.setAttribute('src', '/assets/sample_dashboard_light.png');
        carousel_image_2.setAttribute('src', '/assets/sample_modal_light.png');
        carousel_image_3.setAttribute('src', '/assets/sample_ViewOfContactsWithSearch_light.png');
    }
}

carouselChange()