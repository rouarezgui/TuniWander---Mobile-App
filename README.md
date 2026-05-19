# TuniWander Mobile App

TuniWander is an Android travel application for discovering Tunisian destinations, booking tours, managing agencies and guides, and allowing an admin to verify professional users.

## Main Features

- Splash screen with Tunisia video.
- Public destination list with search and category filters.
- Firebase Authentication for sign in, sign up, and password reset.
- Role-based navigation:
  - Admin
  - Touriste
  - Guide
  - Agence
- Admin dashboard with statistics.
- Admin user verification with `Verify` and `Refuse` actions.
- Destination management with add/edit support.
- Reservation creation and reservation status management.
- Guide profile with availability toggle.
- Agency profile with guide hiring/releasing.
- Notifications by target role.
- Rating system for destinations.

## Admin Account

The unique admin account is:

```text
roua@gmail.com
```

When this email signs in, the app synchronizes the Firestore profile with:

```text
role = Admin
isVerified = true
```

This allows Roua to open the admin dashboard and verify or refuse pending Guide/Agence accounts.

## Verification Flow

1. A Touriste account is verified automatically.
2. A Guide or Agence account is created with `isVerified = false`.
3. The pending user cannot access the app until admin approval.
4. Admin opens `Verify Users`.
5. Admin can press:
   - `Verify`: updates `isVerified` to `true`.
   - `Refuse`: removes the pending Firestore user profile from the admin list.

Note: deleting another user from Firebase Authentication requires secure server-side Firebase Admin SDK permissions. From the Android client, the app refuses by deleting the Firestore profile only.

## Firebase Collections

- `users`: user profile, role, verification state, guide/agency fields.
- `lieux`: destinations, images, category, rating, agency/guide assignment.
- `reservations`: booking data and status.
- `ratings`: user ratings.
- `notifications`: admin messages by target role.

## Project Structure

```text
app/src/main/java/com/example/miniprojet/
```

Important files:

- `SplashActivity.java`: intro video and swipe navigation.
- `MainActivity.java`: first menu after splash.
- `SignInActivity.java`: login, verification check, role redirect.
- `SignUpActivity.java`: account creation and role selection.
- `AdminDashboardActivity.java`: admin home and statistics.
- `ManageUsersActivity.java`: admin user list.
- `UserAdapter.java`: user card, Verify and Refuse actions.
- `ListeLieuxActivity.java`: destination list.
- `DetailActivity.java`: destination details.
- `ReservationActivity.java`: booking form.
- `TouristeProfilActivity.java`: tourist profile.
- `GuideProfilActivity.java`: guide profile.
- `AgenceProfilActivity.java`: agency profile.

Full French code explanation is available in:

```text
EXPLICATION_CODE.md
```

## Build

Use Android Studio or run:

```bash
./gradlew assembleDebug
```

The latest checked build passed successfully.

## Run Checklist

Before presenting the project:

1. Run the app from Android Studio.
2. Sign in as `roua@gmail.com`.
3. Create a test Guide account.
4. Confirm the Guide is blocked before verification.
5. Sign in as admin and open `Verify Users`.
6. Test `Verify`.
7. Create another Guide/Agence test account.
8. Test `Refuse`.
9. Test destination list, details, reservation, and profile pages.

## Cleanup Notes

The following unused Java files were removed:

- `AdminProfilActivity.java`: empty and unused.
- `RoleManager.java`: unused and used old role names.

