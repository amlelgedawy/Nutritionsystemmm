// NutritionX JavaScript Functions

// Global variables
let currentUserId = null;

// Initialize when document is ready
document.addEventListener('DOMContentLoaded', function() {
    initializeEventListeners();
    initializeFoodSearch();
});

// Initialize event listeners
function initializeEventListeners() {
    // Registration form
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegistration);
        
        const userTypeSelect = document.getElementById('userType');
        if (userTypeSelect) {
            userTypeSelect.addEventListener('change', toggleCoachFields);
        }
    }
    
    // Food logging form
    const logFoodForm = document.getElementById('logFoodForm');
    if (logFoodForm) {
        logFoodForm.addEventListener('submit', handleFoodLogging);
    }
    
    // Progress form
    const progressForm = document.getElementById('progressForm');
    if (progressForm) {
        progressForm.addEventListener('submit', handleProgressLogging);
    }
    
    // External food search
    const searchExternalBtn = document.getElementById('searchExternalBtn');
    if (searchExternalBtn) {
        searchExternalBtn.addEventListener('click', searchExternalFood);
    }
}

// Toggle coach-specific fields in registration
function toggleCoachFields() {
    const userType = document.getElementById('userType').value;
    const coachFields = document.getElementById('coachFields');
    
    if (userType === 'coach') {
        coachFields.style.display = 'block';
        // Make coach fields required
        document.getElementById('specialization').required = true;
        document.getElementById('experienceYears').required = true;
        document.getElementById('certification').required = true;
    } else {
        coachFields.style.display = 'none';
        // Remove required attribute from coach fields
        document.getElementById('specialization').required = false;
        document.getElementById('experienceYears').required = false;
        document.getElementById('certification').required = false;
    }
}

// Handle user registration
async function handleRegistration(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const userType = formData.get('userType');
    
    let userData = {
        username: formData.get('username'),
        email: formData.get('email'),
        password: formData.get('password'),
        name: formData.get('name'),
        age: parseInt(formData.get('age')),
        height: parseFloat(formData.get('height')),
        weight: parseFloat(formData.get('weight')),
        gender: formData.get('gender'),
        activityLevel: parseFloat(formData.get('activityLevel'))
    };
    
    let endpoint;
    
    if (userType === 'coach') {
        userData.specialization = formData.get('specialization');
        userData.experienceYears = parseInt(formData.get('experienceYears'));
        userData.certification = formData.get('certification');
        endpoint = '/auth/register/coach';
    } else if (userType === 'premium') {
        endpoint = '/auth/register/premium-user';
    } else {
        endpoint = '/auth/register/user';
    }
    
    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        });
        
        if (response.ok) {
            showAlert('Registration successful! Please login.', 'success');
            setTimeout(() => {
                window.location.href = '/auth/login';
            }, 2000);
        } else {
            showAlert('Registration failed. Please try again.', 'danger');
        }
    } catch (error) {
        console.error('Registration error:', error);
        showAlert('An error occurred during registration.', 'danger');
    }
}

// Initialize food search functionality
function initializeFoodSearch() {
    const foodNameInput = document.getElementById('foodName');
    if (foodNameInput) {
        foodNameInput.addEventListener('input', debounce(searchFoods, 300));
    }
}

// Search for foods in the database
async function searchFoods(event) {
    const query = event.target.value;
    if (query.length < 2) {
        hideFoodSuggestions();
        return;
    }
    
    try {
        const response = await fetch(`/api/food/search?name=${encodeURIComponent(query)}`);
        if (response.ok) {
            const foods = await response.json();
            showFoodSuggestions(foods);
        }
    } catch (error) {
        console.error('Food search error:', error);
    }
}

// Show food suggestions dropdown
function showFoodSuggestions(foods) {
    const suggestionsDiv = document.getElementById('foodSuggestions');
    suggestionsDiv.innerHTML = '';
    
    if (foods.length === 0) {
        suggestionsDiv.style.display = 'none';
        return;
    }
    
    foods.forEach(food => {
        const suggestionItem = document.createElement('div');
        suggestionItem.className = 'list-group-item list-group-item-action food-suggestion-item';
        suggestionItem.innerHTML = `
            <strong>${food.name}</strong><br>
            <small>${food.calories} cal per 100g | P: ${food.protein}g | C: ${food.carbs}g | F: ${food.fat}g</small>
        `;
        suggestionItem.addEventListener('click', () => selectFood(food));
        suggestionsDiv.appendChild(suggestionItem);
    });
    
    suggestionsDiv.style.display = 'block';
}

// Hide food suggestions
function hideFoodSuggestions() {
    const suggestionsDiv = document.getElementById('foodSuggestions');
    suggestionsDiv.style.display = 'none';
}

// Select a food from suggestions
function selectFood(food) {
    document.getElementById('foodName').value = food.name;
    document.getElementById('calories').value = food.calories;
    document.getElementById('protein').value = food.protein;
    document.getElementById('carbs').value = food.carbs;
    document.getElementById('fat').value = food.fat;
    hideFoodSuggestions();
}

// Handle food logging
async function handleFoodLogging(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const intakeData = {
        foodName: formData.get('foodName'),
        quantity: parseFloat(formData.get('quantity')),
        unit: formData.get('unit'),
        mealType: formData.get('mealType'),
        calories: parseFloat(formData.get('calories')) || 0,
        protein: parseFloat(formData.get('protein')) || 0,
        carbs: parseFloat(formData.get('carbs')) || 0,
        fat: parseFloat(formData.get('fat')) || 0,
        loggedAt: new Date()
    };
    
    try {
        const response = await fetch(`/api/users/${userId}/intake-log`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(intakeData)
        });
        
        if (response.ok) {
            showAlert('Food logged successfully!', 'success');
            event.target.reset();
        } else {
            showAlert('Failed to log food. Please try again.', 'danger');
        }
    } catch (error) {
        console.error('Food logging error:', error);
        showAlert('An error occurred while logging food.', 'danger');
    }
}

// Handle progress logging
async function handleProgressLogging(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const progressData = {
        weight: parseFloat(formData.get('weight')),
        bodyFatPercentage: parseFloat(formData.get('bodyFatPercentage')) || null,
        muscleMass: parseFloat(formData.get('muscleMass')) || null,
        waistCircumference: parseFloat(formData.get('waistCircumference')) || null,
        chestCircumference: parseFloat(formData.get('chestCircumference')) || null,
        armCircumference: parseFloat(formData.get('armCircumference')) || null,
        thighCircumference: parseFloat(formData.get('thighCircumference')) || null,
        notes: formData.get('notes'),
        recordedAt: new Date()
    };
    
    try {
        const response = await fetch(`/api/users/${userId}/progress`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(progressData)
        });
        
        if (response.ok) {
            showAlert('Progress recorded successfully!', 'success');
            setTimeout(() => {
                location.reload();
            }, 1500);
        } else {
            showAlert('Failed to record progress. Please try again.', 'danger');
        }
    } catch (error) {
        console.error('Progress logging error:', error);
        showAlert('An error occurred while recording progress.', 'danger');
    }
}

// Search external food database
async function searchExternalFood() {
    const foodName = document.getElementById('foodName').value;
    if (!foodName) {
        showAlert('Please enter a food name first.', 'warning');
        return;
    }
    
    try {
        showLoading('searchExternalBtn');
        const response = await fetch(`/api/food/external/search?name=${encodeURIComponent(foodName)}`);
        
        if (response.ok) {
            const food = await response.json();
            selectFood(food);
            showAlert('Food data loaded from external database!', 'success');
        } else {
            showAlert('Food not found in external database.', 'warning');
        }
    } catch (error) {
        console.error('External search error:', error);
        showAlert('Error searching external database.', 'danger');
    } finally {
        hideLoading('searchExternalBtn');
    }
}

// Utility functions
function showAlert(message, type) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.firstChild);
    
    // Auto-dismiss after 5 seconds
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

function showLoading(buttonId) {
    const button = document.getElementById(buttonId);
    button.disabled = true;
    button.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Loading...';
}

function hideLoading(buttonId) {
    const button = document.getElementById(buttonId);
    button.disabled = false;
    button.innerHTML = 'Search External Database';
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Format date for display
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

// Calculate BMI
function calculateBMI(weight, height) {
    const heightInMeters = height / 100;
    return (weight / (heightInMeters * heightInMeters)).toFixed(1);
}

// Calculate daily calorie progress
function calculateCalorieProgress(consumed, target) {
    return Math.min((consumed / target) * 100, 100);
}

// Export functions for use in other scripts
window.NutritionX = {
    showAlert,
    formatDate,
    calculateBMI,
    calculateCalorieProgress
};