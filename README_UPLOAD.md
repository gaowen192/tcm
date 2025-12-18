# Video Upload Documentation

## Overview
This document provides information about the video upload functionality in the application.

## Storage Configuration
- **Video Storage Path**: Located in the project directory at `./uploads/videos`
- **Thumbnail Storage Path**: Located in the project directory at `./uploads/videos/thumbnails`
- **Date-based Organization**: Files are organized by date in the format `yyyyMMdd`

## Supported Formats
- **Videos**: mp4, avi, mov, wmv, flv, mkv, webm
- **Thumbnails**: jpg, jpeg, png, gif, webp

## Size Limitations
- **Maximum Video Size**: 100MB
- **Maximum Thumbnail Size**: 5MB

## Usage
Uploaded files are saved with unique UUID filenames to prevent conflicts.
The file paths are stored in the database for later retrieval.
