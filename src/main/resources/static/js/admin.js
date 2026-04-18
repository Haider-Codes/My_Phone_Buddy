console.log("Loading admin script for preview images.");

// event listener which will trigger once the state is 'changed' for file input field
document.querySelector("#file_input").addEventListener('change', (event) => {
    
    // Grabbin the file input 
    let file = event.target.files[0];
    console.log(event)
    // Initializing the file reader, which is an inbuilt browser tool to read file asynchronously
    let fileReader = new FileReader();

    // event-listener which get trigger once the file is successfully read and converted into a data string/stream,
    // and set's it into src
    fileReader.onload = () => {
        document.getElementById('image-preview').setAttribute("src", fileReader.result);
    }

    // start the file read process
    fileReader.readAsDataURL(file);

})

// Incase of a reset event
document.querySelector("form").addEventListener('reset', (event) => {
    document.getElementById('image-preview').removeAttribute("src");
})