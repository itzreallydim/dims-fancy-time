# Changelogs

# Version 1.0.0

**Added:**
- Initial release.

# Version 1.1.0

**Added:**
- 12/24h time format toggle.

**Changed:**
- Configuration menu now uses Yet Another Config Lib (YACL) for a cleaner interface!

# Version 1.2.0

**Added:**
- Color picker and remove/enable shadow button.

# Version 1.3.0

**Added:**
- Sleep reminder feature, which gently reminds you that you can sleep now! Can be set to Enabled, Disabled, or Only in Survival.
- Descriptions in the config menu.

# Version 1.4.0

**Added:**
- Text backdrop for the clock, custom sleep reminder text configuration.

**Fixed:**
- Sleep reminder now doesn't get tucked behind the armor, breathing bubble and horse health bars, and the clock doesn't get tucked behind effects.

**Technical:**
- Mod now (almost) immune to all NullPointerExceptions!

# Version 1.5.0

**Added:**
- On/off toggle for clock, toast/text toggle for the sleep reminder, and tidied up the YACL config menu!

**Technical:**
- Replaced e.printStackTrace() with LOGGER.error for more detailed error logs, and bumped minimum required YACL version to 3.9.7 to fix a bug in older versions that did not allow typing in the custom sleep reminder field.

# Version 1.5.1

**Fixed:**
- Long custom sleep reminder text in the toast now wraps onto a second line instead of overflowing, truncating with "..." if it's still too long.
- Wrapped toast text is now horizontally centered instead of left-aligned.
- Sleep reminder now triggers at the correct earlier time during rainy weather, matching vanilla's actual sleep window.