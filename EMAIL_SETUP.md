# Email Configuration Setup

This document explains how to configure the email settings for the REMS system.

## Configuration File

Email settings are configured in `src/main/resources/config.properties`:

```properties
# Email Configuration
email.smtp.host=smtp.gmail.com
email.smtp.port=587
email.smtp.auth=true
email.smtp.starttls=true
email.username=your-email@gmail.com
email.password=your-app-password
email.from=your-email@gmail.com
```

## Gmail Setup Instructions

### 1. Enable 2-Factor Authentication
1. Go to your Google Account settings
2. Enable 2-Factor Authentication (2FA)

### 2. Generate App Password
1. Go to Google Account settings
2. Navigate to "Security"
3. Select "App passwords" (you may need to search for it)
4. Generate a new app password for "Mail" on "Other (custom name)"
5. Copy the generated password (it will be shown only once)

### 3. Update Configuration
Replace the placeholder values in `config.properties`:

- `email.username`: Your Gmail address
- `email.password`: The app password you generated (NOT your regular password)
- `email.from`: Your Gmail address (usually same as username)

## Alternative SMTP Providers

For other email providers, update these settings:

### Outlook/Hotmail
```properties
email.smtp.host=smtp-mail.outlook.com
email.smtp.port=587
```

### Yahoo Mail
```properties
email.smtp.host=smtp.mail.yahoo.com
email.smtp.port=587
```

## Testing

After configuration, the system will send emails for:
- OTP verification during registration
- Transaction creation notifications
- Transaction completion notifications

## Troubleshooting

### Common Issues:
1. **"Skip sending email due to missing config"** - Check that all required properties are set
2. **Authentication failed** - Use app password for Gmail, not regular password
3. **Connection refused** - Check SMTP host and port settings
4. **SSL/TLS issues** - Ensure `email.smtp.starttls=true`

### Debug Mode
To enable email debug logging, add this to your JVM startup:
```
-Dmail.debug=true
```

## Security Notes

- Never commit real email credentials to version control
- Use app passwords instead of regular passwords
- Consider using environment variables for production deployments
