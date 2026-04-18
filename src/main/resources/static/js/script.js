console.log("Loading main script!!");

let currentTheme = getTheme();

const theme_toggle = document.querySelector("#theme-toggler");
const dark_mode = document.querySelector("#dark-mode");
const light_mode = document.querySelector("#light-mode");

changeTheme();

// Toggling icon based on web page load.
function pageLoad() {
    return currentTheme === "dark" ? dark_mode.classList.add("hidden") : light_mode.classList.add("hidden");
}

// main change theme function
function changeTheme() {

    //Call Page Load
    pageLoad();

    // Applying the current Theme on web page.
    document.querySelector("html").classList.add(currentTheme);

    // set the event listener to button(theme-toggler) to change the theme based on events.
    theme_toggle.addEventListener("click", () => {
        
        console.log("Theme Toggle Button clicked!!");
        
        // Fetching old  page theme
        const oldTheme = currentTheme;
        
        // changing page theme
        currentTheme = changePageTheme(oldTheme, currentTheme);
        
        // Setting current Theme
        setPageTheme(oldTheme, currentTheme);
    })

}

// change page theme - icons
function changePageTheme(oldPageTheme, currentPageTheme) {
    if(oldPageTheme === "dark") {
        // change theme to light
        light_mode.classList.add("hidden");
        dark_mode.classList.remove("hidden");
        currentPageTheme = "light";
    }
    else {
        // change theme to dark
        dark_mode.classList.add("hidden");
        light_mode.classList.remove("hidden");
        currentPageTheme = "dark";
    }
    return currentPageTheme;
}

// setting page theme
function setPageTheme(oldPageTheme, currentPageTheme) {
    setTheme(currentTheme);
    document.querySelector("html").classList.remove(oldPageTheme);
    document.querySelector("html").classList.add(currentPageTheme);
}

// set theme to localStorage (default storage for browser)
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

// get theme from localStorage
function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light"; 
}