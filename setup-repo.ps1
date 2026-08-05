# PowerShell script to initialise the Git repository for the Smart Nutrition project
# Run this script from the root of the backend folder.

# Initialise git repository
if (-not (Test-Path .git)) {
    git init
    Write-Host "Initialized empty Git repository."
}

# Add all files and commit
git add .
if ((git status --porcelain).Length -gt 0) {
    git commit -m "Initial commit – backend skeleton, DB dump, README, .gitignore, .env.example"
    Write-Host "Committed initial project files."
} else {
    Write-Host "No changes to commit."
}

# Show user the next steps (remote creation and push)
Write-Host "\n==== NEXT STEPS ====\n"
Write-Host "1️⃣  Create a new empty repository on GitHub (https://github.com/new)"
Write-Host "2️⃣  Run the following commands:"
Write-Host "   git remote add origin https://github.com/SanjeetSan/smart-nutrition.git"
Write-Host "   git push -u origin main"
Write-Host "3️⃣  Invite your teammate as a collaborator via Settings → Manage access"
Write-Host "4️⃣  Your teammate can now clone the repo and follow the README for setup."
