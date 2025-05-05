// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    console.log('Costume Rental System Frontend Initialized');
    
    // Set active nav item based on current page
    setActiveNavItem();
    
    // Initialize API connection
    initAPI();
});

// Set the active navigation item based on current page
function setActiveNavItem() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');
    
    navLinks.forEach(link => {
        link.classList.remove('active');
        const linkPath = link.getAttribute('href');
        if (currentPath.endsWith(linkPath)) {
            link.classList.add('active');
        }
    });
    
    // If no active item set (e.g., on index), set home as active
    if (!document.querySelector('.nav-link.active') && (currentPath.endsWith('/') || currentPath.endsWith('index.html'))) {
        document.querySelector('a[href="index.html"]').classList.add('active');
    }
}

// Initialize API connection to backend
function initAPI() {
    // Base API URL - would be replaced with actual API gateway URL in production
    window.apiBaseUrl = 'http://localhost:8080/api';
    
    // Test API connection
    fetch(window.apiBaseUrl + '/health', {
        method: 'GET'
    })
    .then(response => {
        if (response.ok) {
            console.log('API connection successful');
        } else {
            console.warn('API connection error');
        }
    })
    .catch(error => {
        console.error('API connection failed:', error);
    });
} 