// Export JS Script
let contacts_table = document.getElementById('export-contacts');

// This line fixes the "p(...) is not a function" error
//window.html2canvas = html2canvas;
window.jsPDF = window.jspdf.jsPDF; // we need to map the global window object with jspdf in newer versions of the library.

// Function for exporting contacts to Excel Sheet (.xlsx)
const exportContactsToXlsx = () => {

    console.log("Running export contacts to XLSX script.");
    
    TableToExcel.convert(contacts_table, {
        // output file config
        name: "contacts.xlsx",
        sheet: {
            name: "Sheet 1"
        }
    });
}

// Function for exporting contacts to PDF (.pdf)
const exportContactsToPdf = () => {

    console.log("Running export contacts to PDF script.");

  const doc = new jsPDF()
  doc.autoTable({ 
    html: contacts_table,
    theme: 'striped',
       } 
    )
  doc.save('contacts.pdf')

}