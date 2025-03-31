let currentPage = 0;
const pageSize = 10;

// Initialize page
document.addEventListener("DOMContentLoaded", function () {
  searchUsers();
  setupAvatarPreview();
});

// Setup avatar preview
function setupAvatarPreview() {
  const avatarInput = document.getElementById("avatarInput");
  const avatarPreview = document.getElementById("avatarPreview");

  avatarInput.addEventListener("change", function (e) {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = function (e) {
        avatarPreview.src = e.target.result;
        avatarPreview.classList.remove("d-none");
      };
      reader.readAsDataURL(file);
    }
  });
}

// Search users
function searchUsers(page = 0) {
  const code = document.getElementById("codeFilter").value;
  const phone = document.getElementById("phoneFilter").value;
  const createdDate = document.getElementById("createdDateFilter").value;

  const url = new URL("/api/user/search", window.location.origin);
  url.searchParams.append("code", code);
  url.searchParams.append("phone", phone);
  url.searchParams.append("createdDate", createdDate);
  url.searchParams.append("page", page);
  url.searchParams.append("size", pageSize);

  fetch(url)
    .then((response) => response.json())
    .then((data) => {
      displayUsers(data.content);
      updatePagination(data);
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("Có lỗi xảy ra khi tải dữ liệu");
    });
}

// Display users in table
function displayUsers(users) {
  const tbody = document.getElementById("userTableBody");
  tbody.innerHTML = "";

  users.forEach((user) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
            <td>${user.code}</td>
            <td>${user.username}</td>
            <td>${user.firstName} ${user.lastName}</td>
            <td>${user.phone}</td>
            <td>${user.createdDate}</td>
            <td>${user.address}</td>
        `;
    tbody.appendChild(tr);
  });
}

// Update pagination
function updatePagination(data) {
  const pagination = document.getElementById("pagination");
  pagination.innerHTML = "";

  const totalPages = data.totalPages;
  const currentPage = data.number;

  // Previous button
  const prevLi = document.createElement("li");
  prevLi.className = `page-item ${currentPage === 0 ? "disabled" : ""}`;
  prevLi.innerHTML = `
        <a class="page-link" href="#" onclick="searchUsers(${
          currentPage - 1
        })">Trước</a>
    `;
  pagination.appendChild(prevLi);

  // Page numbers
  for (let i = 0; i < totalPages; i++) {
    const li = document.createElement("li");
    li.className = `page-item ${i === currentPage ? "active" : ""}`;
    li.innerHTML = `
            <a class="page-link" href="#" onclick="searchUsers(${i})">${
      i + 1
    }</a>
        `;
    pagination.appendChild(li);
  }

  // Next button
  const nextLi = document.createElement("li");
  nextLi.className = `page-item ${
    currentPage === totalPages - 1 ? "disabled" : ""
  }`;
  nextLi.innerHTML = `
        <a class="page-link" href="#" onclick="searchUsers(${
          currentPage + 1
        })">Sau</a>
    `;
  pagination.appendChild(nextLi);
}

// Show create user modal
function showCreateUserModal() {
  const modal = new bootstrap.Modal(document.getElementById("createUserModal"));
  modal.show();
}

// Create user
function createUser() {
  const form = document.getElementById("createUserForm");
  const formData = new FormData(form);
  const userData = Object.fromEntries(formData.entries());

  // Get avatar base64
  const avatarPreview = document.getElementById("avatarPreview");
  const base64Image = avatarPreview.classList.contains("d-none")
    ? null
    : avatarPreview.src;

  // Create user request
  fetch("/api/user/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(userData),
  })
    .then((response) => response.json())
    .then((data) => {
      // Close modal and refresh table
      const modal = bootstrap.Modal.getInstance(
        document.getElementById("createUserModal")
      );
      modal.hide();
      form.reset();
      avatarPreview.classList.add("d-none");
      searchUsers();
      alert("Tạo user thành công!");
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("Có lỗi xảy ra khi tạo user");
    });
}
