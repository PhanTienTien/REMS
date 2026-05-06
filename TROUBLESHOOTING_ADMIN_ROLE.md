# ADMIN Role Permission Troubleshooting Guide

## Issue: ADMIN users cannot perform "Accept" and "Complete" actions

## Possible Causes and Solutions

### 1. **Missing ADMIN User in Database**
**Problem**: No ADMIN user exists in the database
**Solution**: 
```sql
-- Run the create_admin_user.sql script
-- This creates a test admin user:
-- Email: admin@test.com
-- Password: admin123
```

### 2. **Incorrect Role Assignment**
**Problem**: User exists but role is not set to ADMIN
**Solution**: Check the users table
```sql
SELECT id, full_name, email, role FROM users WHERE email = 'admin@test.com';
```

### 3. **Session Issues**
**Problem**: User not properly logged in or session expired
**Solution**: 
- Log out and log back in
- Check session by visiting `/admin/debug`
- Verify user role is displayed correctly

### 4. **Authentication Filter Issues**
**Problem**: AuthFilter not allowing admin access
**Solution**: Check if `/admin/*` URLs are properly mapped in web.xml

### 5. **Property Approval Button Not Showing** 🔧 **FIXED**
**Problem**: ADMIN users cannot see "Duyệt" button for DRAFT properties
**Root Cause**: JSP only checked user role, not property status
**Solution Applied**: Updated condition to check both role AND status:
```jsp
<!-- BEFORE (only checked role) -->
<c:if test="${sessionScope.currentUser.role == 'ADMIN'}">

<!-- AFTER (checks role AND DRAFT status) -->
<c:if test="${sessionScope.currentUser.role == 'ADMIN' and p.status == 'DRAFT'}">
```

## Testing Steps

### Step 1: Verify ADMIN User Exists
1. Run the SQL script: `database/create_admin_user.sql`
2. Check that the user was created: `SELECT * FROM users WHERE role = 'ADMIN';`

### Step 2: Test Login
1. Go to the login page
2. Enter: admin@test.com / admin123
3. Should redirect to `/admin/dashboard`

### Step 3: Debug Session
1. Visit `/admin/debug`
2. Verify:
   - User is not null
   - Role shows as ADMIN
   - Is Admin/Staff shows as true
   - DRAFT properties count > 0

### Step 4: Test Property Approval
1. Go to `/admin/properties`
2. Find a property with status "DRAFT"
3. "Duyệt" button should be visible
4. Click "Duyệt" - should approve the property

### Step 5: Test Transactions
1. Go to `/admin/transactions`
2. Find a PENDING transaction
3. Click "Hoàn tất" - should complete the transaction

## Debug Information

### Check Console Logs
The controllers will log any issues to console. Look for:
- Role checking messages
- Error messages
- Redirect information

### Common Error Messages
- "User is not admin, redirecting to auth" - Role checking failed
- "Internal server error" - Service method failed
- "Invalid credentials" - Login failed

## Files Modified
- `AdminBookingController.java` - Simplified role checking
- `AdminTransactionController.java` - Simplified role checking
- `AdminPropertyController.java` - Fixed approve button condition ✅
- `AdminDebugController.java` - Enhanced with property debugging
- `create_admin_user.sql` - Test admin user creation

## Quick Fix
If still not working:
1. Create admin user using the SQL script
2. Restart the application server
3. Clear browser cache
4. Test with the debug endpoint
5. Check console logs for errors
6. Verify DRAFT properties exist in database

## Contact Support
If issue persists:
1. Provide console log output
2. Show debug endpoint output
3. Share database query results
4. Describe exact steps taken
