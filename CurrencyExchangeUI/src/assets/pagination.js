  const table = document.getElementById("mytable");
  const data = [
      ];

      for (let i = 1; i < table.rows.length; i++) 
      {
        const row = table.rows[i];
        const username = row.cells[0].textContent;
        const email = row.cells[1].textContent;
        const phNumber = parseInt(row.cells[2].textContent,);
        const Gender = row.cells[3].textContent;
        
        const rowData =
        {
          username: username,
          email: email,
          phNumber: phNumber,
          Gender: Gender
        };
        console.log(rowData);
        data.push(rowData);
      }
  // Function to populate the table with data
  function populateTable(pageNumber) {
      const perPage = 5; // Number of rows per page
      const startIndex = (pageNumber - 1) * perPage;
      const endIndex = startIndex + perPage;

      const tbody = document.querySelector("tbody");
      tbody.innerHTML = ""; // Clear previous data

      for (let i = startIndex; i < endIndex && i < data.length; i++) {
          const row = data[i];
          const tr = document.createElement("tr");
          tr.innerHTML = `
              <td>${row.username}</td>
              <td>${row.email}</td>
              <td>${row.phNumber}</td>
              <td>${row.Gender}</td>
          `;
          tbody.appendChild(tr);
      }
  }
  populateTable(1);
  function handlePaginationClick(pageNumber) {
        populateTable(pageNumber);
    }

    const paginationLinks = document.querySelectorAll(".page-link");
    paginationLinks.forEach((link) => {
        link.addEventListener("click", (e) => {
            e.preventDefault();
            const pageNumber = parseInt(link.getAttribute("data-page"), 5);
            handlePaginationClick(pageNumber);
        });
    });
