# Project Snapshot Automation

## Overview

This workflow automatically updates a screenshot of the Babelup project in the README whenever a pull request is merged to the `main` or `master` branch.

## What Happens

1. **Trigger**: The workflow runs on every `push` to `main` or `master` branches
2. **Build & Start**: Docker containers are started (MySQL, Backend, Frontend)
3. **Health Checks**: The workflow waits for both backend and frontend services to be ready
4. **Screenshot**: A screenshot of the running application is captured using Playwright
5. **Commit**: If the screenshot changed, it's automatically committed and pushed to the repository

## Workflow File

- **Location**: `.github/workflows/update-snapshot.yml`
- **Trigger Event**: `push` to `main` or `master` branches
- **Runtime**: ~5-10 minutes (depending on Docker startup times)

## How to Update Manually

If you want to manually trigger the snapshot update:

1. Make a change to the main branch (e.g., merge a PR)
2. The GitHub Actions workflow will automatically run
3. Check the workflow status in the **Actions** tab of the repository

## Troubleshooting

### Workflow Fails

If the workflow fails, check:

1. **Docker startup times**: Increase the wait times in the workflow if services take longer to start
2. **Application errors**: Check the backend logs for errors
3. **Playwright issues**: Ensure Playwright is properly installed and chromium is available

### Screenshot Not Updating

1. Verify the application loads correctly at `http://localhost:5173`
2. Check if there are actual visual changes to capture
3. Review the workflow logs for errors

## Customization

### Change Screenshot Size

Edit the Playwright script in the workflow:
```javascript
await page.screenshot({ 
  path: filename, 
  fullPage: true,  // Set to false for viewport only
  type: 'png'
});
```

### Wait for Specific Elements

Add waits for specific elements before taking the screenshot:
```javascript
await page.waitForSelector('.your-element-selector', { timeout: 5000 });
```

### Add Multiple Screenshots

Modify the script to take screenshots of different pages or states:
```javascript
// Home page
await page.screenshot({ path: 'screenshots/home.png' });

// Dashboard page
await page.goto('http://localhost:5173/dashboard');
await page.screenshot({ path: 'screenshots/dashboard.png' });
```

## Security

- The workflow uses `github-actions[bot]` which is GitHub's built-in bot user
- Commits are created with `[skip ci]` to prevent infinite workflow loops on updates
- All sensitive information (database credentials) comes from environment variables

## Performance Notes

- Workflow timeout: 30 minutes
- Screenshot capture timeout: 5 minutes
- Docker startup wait: Up to 40 attempts × 3 seconds each
- This doesn't run on pull requests, only on merges to main/master

## Related Files

- README.md: Contains the snapshot section with the image
- docker-compose.yml: Defines the services that are started
- .github/workflows/update-snapshot.yml: The main workflow file
