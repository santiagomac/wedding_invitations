# Skeleton basic Spring Security

This skeleton provides you the basic configuration to implements
login with spring security and JWT.

---

## Initial Steps

1. Delete H2 dependency from **build.gradle**. (If is necessary)
2. Put your environment variables in application.yml
3. Change the database dependency from postgres to your choose

---

## Features

- 🗝️ Generation from JWT.
- 🙍‍♂️ Sign in and Sign up.
- 🔒 Protection to your endpoints.

---

## Project Structure

```plaintext
└── 📁src
    ├── 📁main
    │   ├── 📁java
    │   │   └── 📁com
    │   │       └── 📁{project-name}
    │   │           ├── 📁application
    │   │           │    ├── 📁dto
    │   │           ├── 📁domain
    │   │           │   ├── 📁repository
    │   │           │   └── 📁service
    │   │           ├── 📁infrastructure
    │   │           │   ├── 📁client
    │   │           │   ├── 📁dto
    │   │           │   ├── 📁exception
    │   │           │   ├── 📁persistence
    │   │           │   ├── 📁rest
    │   │           │   └── 📁security
    │   └── 📁resources
    │       └── application.yml
```

## Contributors or 

If you have any issue o your have some feature request let me know
in the issue page.