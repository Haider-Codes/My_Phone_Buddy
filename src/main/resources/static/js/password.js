console.log('Handling password using JS...')

let password = document.getElementById('password');
let mode_on = document.getElementById('mode-on');
let mode_off = document.getElementById('mode-off');

// Making password.js field generic to be used in multiple views.
let check_match = document.getElementById('check-match') ? document.getElementById('check-match') : null;
let confirm_password = document.getElementById('confirm-password') ? document.getElementById('confirm-password') : null;

mode_off.classList.add('hidden');

if(check_match !== null) {
    check_match.classList.add('hidden');
}

mode_on.addEventListener('click', (event) => {
    password.type = "text";
    mode_off.classList.remove('hidden');
    mode_on.classList.add('hidden');
});

mode_off.addEventListener('click', (event) => {
    password.type = 'password';
    mode_on.classList.remove('hidden');
    mode_off.classList.add('hidden');
});

if(password !== null && confirm_password !== null) {

    password.addEventListener('input', (event) => {
        confirm_password.value = "";
        check_match.classList.add('hidden');
    })

    confirm_password.addEventListener('input', (event) => {
        if(confirm_password.value === password.value)
            check_match.classList.remove('hidden');
        else
            check_match.classList.add('hidden');
    })

}
