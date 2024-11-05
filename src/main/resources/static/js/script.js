// Store the API key globally once generated
let currentApiKey = '';

// API Base URL
const API_BASE_URL = '/api/v1';

// Utility function to display API responses
function displayResponse(response) {
    const responseElement = document.getElementById('apiResponse');
    let displayText = '';

    if (response.error) {
        // Handle error messages from our catch blocks
        displayText = {
            status: 'error',
            message: response.error
        };
    } else if (response.timestamp && response.message) {
        // Handle Spring Boot error responses
        displayText = {
            status: 'error',
            timestamp: response.timestamp,
            message: response.message,
            details: response.error || []
        };
    } else {
        // Handle successful responses
        displayText = response;
    }

    responseElement.textContent = JSON.stringify(displayText, null, 2);

    // Add error styling if it's an error response
    if (response.error || (response.timestamp && response.message)) {
        responseElement.classList.add('text-danger');
    } else {
        responseElement.classList.remove('text-danger');
    }
}

function clearResponse() {
    document.getElementById('apiResponse').textContent = '// API responses will appear here';
}

// Add headers to fetch requests
function getHeaders() {
    const headers = {
        'Content-Type': 'application/json'
    };
    if (currentApiKey) {
        headers['X-API-KEY'] = currentApiKey;
    }
    return headers;
}

// API Key Management
document.getElementById('generateKeyForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const description = document.getElementById('keyDescription').value;
    if (description.length < 5 || description.length > 200) {
        displayResponse({
            error: 'Description must be between 5 and 200 characters'
        });
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/keys/generate`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                description: description,
                createdBy: document.getElementById('keyCreator').value,
                validityDays: parseInt(document.getElementById('validityDays').value) || null
            })
        });

        const data = await response.json();

        if (!response.ok) {
            // Handle server-side validation errors
            displayResponse(data);
            return;
        }

        currentApiKey = data.keyValue; // Store the API key
        displayResponse(data);
        await loadApiKeys(); // Refresh the keys list

        // Clear the form on success
        e.target.reset();
    } catch (error) {
        displayResponse({ error: error.message });
    }
});

async function loadApiKeys() {
    try {
        const response = await fetch(`${API_BASE_URL}/keys`, {
            headers: getHeaders()
        });
        const data = await response.json();
        const tbody = document.getElementById('apiKeysList');
        tbody.innerHTML = ''; // Clear existing rows

        data.forEach(key => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${key.id}</td>
                <td>${key.keyValue}</td>
                <td>${key.description}</td>
                <td>${key.createdBy}</td>
                <td>
                    <span class="badge ${key.active ? 'bg-success' : 'bg-danger'}">
                        ${key.active ? 'Active' : 'Inactive'}
                    </span>
                </td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="revokeKey(${key.id})">
                        Revoke
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        displayResponse({ error: error.message });
    }
}

async function revokeKey(id) {
    try {
        await fetch(`${API_BASE_URL}/keys/${id}`, {
            method: 'DELETE',
            headers: getHeaders()
        });
        await loadApiKeys(); // Refresh the list
        displayResponse({ message: `API key ${id} revoked successfully` });
    } catch (error) {
        displayResponse({ error: error.message });
    }
}

// Pet Management
document.getElementById('createPetForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
        const response = await fetch(`${API_BASE_URL}/pets`, {
            method: 'POST',
            headers: getHeaders(),
            body: JSON.stringify({
                Name: document.getElementById('petName').value,
                Species: document.getElementById('petSpecies').value,
                Breed: document.getElementById('petBreed').value,
                Age: parseInt(document.getElementById('petAge').value),
                Gender: document.getElementById('petGender').value,
                Weight: parseFloat(document.getElementById('petWeight').value),
                Color: document.getElementById('petColor').value,
                Temperament: document.getElementById('petTemperament').value,
                Adoption_Fee: parseFloat(document.getElementById('petAdoptionFee').value)
            })
        });
        const data = await response.json();
        displayResponse(data);
        await loadPets(); // Refresh the pets list
        e.target.reset(); // Reset the form
    } catch (error) {
        displayResponse({ error: error.message });
    }
});

async function loadPets(filter = 'all') {
    try {
        let url = `${API_BASE_URL}/pets`;
        if (filter === 'available') {
            url = `${API_BASE_URL}/pets/available`;
        } else if (filter === 'needs-foster') {
            url = `${API_BASE_URL}/pets/needs-foster`;
        }

        const response = await fetch(url, {
            headers: getHeaders()
        });
        const data = await response.json();
        const tbody = document.getElementById('petsList');
        tbody.innerHTML = ''; // Clear existing rows

        data.pets.forEach(pet => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${pet.id}</td>
                <td>${pet.Name}</td>
                <td>${pet.Species}</td>
                <td>${pet.Age}</td>
                <td>
                    <span class="badge bg-${getStatusColor(pet.Current_Status)}">
                        ${pet.Current_Status}
                    </span>
                </td>
                <td>${pet.Foster_Name || 'None'}</td>
                <td>
                    <div class="btn-group btn-group-sm">
                        <button class="btn btn-primary" onclick="viewPet(${pet.id})">View</button>
                        <button class="btn btn-danger" onclick="removePet(${pet.id})">Remove</button>
                    </div>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        displayResponse({ error: error.message });
    }
}

function getStatusColor(status) {
    switch (status) {
        case 'AVAILABLE': return 'success';
        case 'FOSTERED': return 'primary';
        case 'ADOPTED': return 'info';
        case 'REMOVED': return 'danger';
        default: return 'secondary';
    }
}

async function viewPet(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/pets/${id}`, {
            headers: getHeaders()
        });
        const data = await response.json();
        displayResponse(data);
    } catch (error) {
        displayResponse({ error: error.message });
    }
}

async function removePet(id) {
    if (!confirm('Are you sure you want to remove this pet?')) return;

    try {
        await fetch(`${API_BASE_URL}/pets/${id}`, {
            method: 'DELETE',
            headers: getHeaders()
        });
        await loadPets(); // Refresh the list
        displayResponse({ message: `Pet ${id} removed successfully` });
    } catch (error) {
        displayResponse({ error: error.message });
    }
}

// Foster Management
document.getElementById('createFosterForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
        const response = await fetch(`${API_BASE_URL}/fosters`, {
            method: 'POST',
            headers: getHeaders(),
            body: JSON.stringify({
                Name: document.getElementById('fosterName').value,
                'Last Name': document.getElementById('fosterLastName').value,
                Email: document.getElementById('fosterEmail').value,
                Phone: document.getElementById('fosterPhone').value,
                Address: document.getElementById('fosterAddress').value,
                MaxPets: parseInt(document.getElementById('fosterMaxPets').value)
            })
        });
        const data = await response.json();
        displayResponse(data);
        await loadFosters(); // Refresh the fosters list
        e.target.reset(); // Reset the form
    } catch (error) {
        displayResponse({ error: error.message });
    }
});



async function viewFoster(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/fosters/${id}`, {
            headers: getHeaders()
        });
        const data = await response.json();
        displayResponse(data);
    } catch (error) {
        displayResponse({ error: error.message });
    }
}

async function deactivateFoster(id) {
    if (!confirm('Are you sure you want to deactivate this foster?')) return;

    try {
        await fetch(`${API_BASE_URL}/fosters/${id}`, {
            method: 'DELETE',
            headers: getHeaders()
        });
        await loadFosters(); // Refresh the list
        displayResponse({ message: `Foster ${id} deactivated successfully` });
    } catch (error) {
        displayResponse({ error: error.message });
    }
}

// Initialize data on page load
document.addEventListener('DOMContentLoaded', () => {
    // Add click handlers for filter buttons
    document.querySelectorAll('#petsSection .btn-group button').forEach((button, index) => {
        button.addEventListener('click', () => {
            const filters = ['all', 'available', 'needs-foster'];
            loadPets(filters[index]);
        });
    });

    document.querySelectorAll('#fostersSection .btn-group button').forEach((button, index) => {
        button.addEventListener('click', () => {
            const filters = ['all', 'active', 'available'];
            loadFosters(filters[index]);
        });
    });

    // Load initial data
    if (currentApiKey) {
        loadApiKeys();
        loadPets();
        loadFosters();
    }
});

// Add these new functions to your existing script.js

async function openAssignPetModal(fosterId) {
    // Store the foster ID
    document.getElementById('selectedFosterId').value = fosterId;

    try {
        const response = await fetch(`${API_BASE_URL}/pets/available`, {
            headers: getHeaders()
        });
        const data = await response.json();

        // Populate the select dropdown
        const select = document.getElementById('petToAssign');
        select.innerHTML = ''; // Clear existing options

        data.pets.forEach(pet => {
            const option = document.createElement('option');
            option.value = pet.id;
            option.textContent = `${pet.Name} (${pet.Species} - ${pet.Breed || 'Unknown breed'})`;
            select.appendChild(option);
        });

        // Open the modal using Bootstrap
        const modal = new bootstrap.Modal(document.getElementById('assignPetModal'));
        modal.show();
    } catch (error) {
        displayResponse({ error: error.message });
    }
}

async function unassignPetFromFoster(fosterId, petId) {
    if (!confirm('Are you sure you want to remove this pet from the foster?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/fosters/${fosterId}/pets/${petId}`, {
            method: 'DELETE',
            headers: getHeaders()
        });
        const data = await response.json();
        await loadFosters();
        displayResponse(data);
    } catch (error) {
        displayResponse({ error: error.message });
    }
}

document.getElementById('assignPetForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const fosterId = document.getElementById('selectedFosterId').value;
    const petId = document.getElementById('petToAssign').value;

    try {
        const response = await fetch(`${API_BASE_URL}/fosters/${fosterId}/pets/${petId}`, {
            method: 'POST',
            headers: getHeaders()
        });

        const data = await response.json();

        // Hide the modal
        const modal = bootstrap.Modal.getInstance(document.getElementById('assignPetModal'));
        modal.hide();

        // Refresh the fosters list and display response
        await loadFosters();
        displayResponse(data);
    } catch (error) {
        displayResponse({ error: error.message });
    }
});

async function cleanupExpiredKeys() {
    try {
        await fetch(`${API_BASE_URL}/keys/cleanup`, {
            method: 'POST',
            headers: getHeaders()
        });
        await loadApiKeys();
        displayResponse({ message: 'Expired keys cleaned up successfully' });
    } catch (error) {
        displayResponse({ error: error.message });
    }
}

async function filterBySpecies() {
    const species = document.getElementById('speciesFilter').value;
    try {
        if (species) {
            const response = await fetch(`${API_BASE_URL}/pets/species/${species}`, {
                headers: getHeaders()
            });
            const data = await response.json();
            updatePetsList(data);
        } else {
            await loadPets();
        }
    } catch (error) {
        displayResponse({ error: error.message });
    }
}

// Update the loadFosters function to include the Assign Pet and Unassign Pet buttons
// Update the foster row rendering function with a better assigned pets display
function updateFosterRow(foster) {
    return `
        <td>${foster.id}</td>
        <td>${foster.Name} ${foster['Last Name']}</td>
        <td>${foster.Email}</td>
        <td>${foster.Phone}</td>
        <td>
            <div class="d-flex flex-column">
                <span class="mb-1">${foster.CurrentPetCount}/${foster.MaxPets}</span>
                ${foster.AssignedPets && foster.AssignedPets.length > 0 ? `
                    <div class="list-group list-group-flush small">
                        ${foster.AssignedPets.map(pet => `
                            <div class="list-group-item list-group-item-action p-2 d-flex justify-content-between align-items-center">
                                <span>
                                    <i class="fas fa-paw me-1"></i>
                                    ${pet.Name} 
                                    <small class="text-muted">(${pet.Species})</small>
                                </span>
                                <button class="btn btn-outline-danger btn-sm py-0 px-2" 
                                    onclick="unassignPetFromFoster(${foster.id}, ${pet.id})"
                                    title="Remove pet from foster">
                                    <i class="fas fa-times"></i>
                                </button>
                            </div>
                        `).join('')}
                    </div>
                ` : '<small class="text-muted">No pets assigned</small>'}
            </div>
        </td>
        <td>
            <span class="badge ${foster.Active ? 'bg-success' : 'bg-danger'}">
                ${foster.Active ? 'Active' : 'Inactive'}
            </span>
        </td>
        <td>
            <div class="btn-group btn-group-sm">
                <button class="btn btn-primary" onclick="viewFoster(${foster.id})">
                    <i class="fas fa-eye"></i> View
                </button>
                <button class="btn btn-success" onclick="openAssignPetModal(${foster.id})" 
                    ${!foster.Active || foster.CurrentPetCount >= foster.MaxPets ? 'disabled' : ''}>
                    <i class="fas fa-plus"></i> Assign
                </button>
                <button class="btn btn-danger" onclick="deactivateFoster(${foster.id})">
                    <i class="fas fa-ban"></i> Deactivate
                </button>
            </div>
        </td>
    `;
}

// Update the loadFosters function
async function loadFosters(filter = 'all') {
    try {
        let url = `${API_BASE_URL}/fosters`;
        if (filter === 'active') {
            url = `${API_BASE_URL}/fosters/active`;
        } else if (filter === 'available') {
            url = `${API_BASE_URL}/fosters/available`;
        }

        const response = await fetch(url, {
            headers: getHeaders()
        });
        const data = await response.json();
        const tbody = document.getElementById('fostersList');
        tbody.innerHTML = ''; // Clear existing rows

        data.Fosters.forEach(foster => {
            const row = document.createElement('tr');
            row.innerHTML = updateFosterRow(foster);
            tbody.appendChild(row);
        });
    } catch (error) {
        displayResponse({ error: error.message });
    }
}