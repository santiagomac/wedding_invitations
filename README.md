# Wedding Invitations Backend

Backend para el sitio de invitaciones de boda. El frontend es un sitio estático que
renderiza siempre el mismo HTML; lo único que cambia por invitado es el **nombre a
mostrar** y la **cantidad de cupos** de esa invitación, ambos obtenidos desde este
backend a partir de un **código corto** que viaja en la URL (`/invite/{code}`).

El backend expone dos módulos independientes (arquitectura hexagonal, cada uno con
`domain / application / infrastructure`):

- **`auth`**: login/registro de administradores vía JWT (cookies httpOnly).
- **`invitation`**: generación de invitaciones y flujo público de confirmación/rechazo
  de asistencia (RSVP).

---

## Stack

- Java 21 + Spring Boot 3.4
- Spring Security + JWT (`jjwt` / Nimbus, resource server)
- Spring Data JPA + PostgreSQL (H2 disponible para pruebas locales)

---

## Variables de entorno

| Variable          | Descripción                                   |
|-------------------|------------------------------------------------|
| `APPLICATION_NAME` | Nombre de la app (Spring)                      |
| `DATABASE`         | Nombre de la base de datos Postgres            |
| `DB_USERNAME`      | Usuario de la base de datos                    |
| `DB_PASSWORD`      | Password de la base de datos                   |
| `SECRET_KEY`       | Clave HMAC usada para firmar/verificar los JWT |

El esquema se crea con `src/main/resources/init.sql` (tablas `roles`, `users`,
`sessions`, `invitations`) y Hibernate corre en modo `ddl-auto: update` sobre ese
esquema.

---

## Modelo de datos de una invitación

Tabla `invitations`:

| Campo               | Descripción                                                        |
|---------------------|---------------------------------------------------------------------|
| `id`                 | UUID interno (PK)                                                   |
| `code`               | Código corto único (6 caracteres, ej. `G7K2QX`) — es el ID público que usa el frontend |
| `display_name`       | Nombre a mostrar en la invitación (ej. "Familia Pérez", "Juan y María") |
| `max_guests`         | Cupos totales de esa invitación (cuántas personas puede llevar)     |
| `status`             | `PENDING` \| `CONFIRMED` \| `DECLINED`                               |
| `confirmed_guests`   | Cuántos de esos cupos confirmaron asistencia (0 si rechazó, null si aún no responde) |
| `responded_at`       | Fecha/hora de la respuesta                                          |
| `created_at` / `updated_at` | Auditoría                                                     |

### Reglas de negocio del RSVP

- `PENDING → CONFIRMED`: permitido, requiere `1 ≤ confirmedGuests ≤ maxGuests`.
- `PENDING → DECLINED`: permitido.
- `CONFIRMED → DECLINED`: permitido (el invitado puede arrepentirse y bajarse).
- `CONFIRMED → CONFIRMED`: permitido (puede corregir la cantidad de acompañantes antes del cierre).
- `DECLINED → CONFIRMED`: **bloqueado** (`409 Conflict`). Una vez que alguien rechazó, no puede volver a confirmar por este medio.

---

## Flujo end-to-end

1. **Administrador (ustedes)** se loguea (`/api/v1/auth/login`) y con ese token
   crea una invitación por cada familia/grupo indicando `displayName` y `maxGuests`
   (`POST /api/v1/admin/invitations` o `/bulk` para cargar varias de una vez).
2. El backend genera un `code` único de 6 caracteres para cada invitación. Ese
   código es el que arman en el link que comparten por WhatsApp/email:
   `https://tusitio.com/invite/G7K2QX`.
3. **El invitado** entra a ese link. El frontend estático toma el `code` de la URL
   y llama `GET /api/v1/invitations/{code}` para obtener el nombre y la cantidad
   de cupos, y renderiza el HTML con esos datos.
4. El invitado confirma o rechaza desde el frontend, que llama
   `POST /api/v1/invitations/{code}/rsvp` indicando si asiste y, si asiste,
   cuántos de los cupos confirman.
5. **Ustedes** consultan en cualquier momento `GET /api/v1/admin/invitations` (listado
   completo) o `GET /api/v1/admin/invitations/stats` (resumen agregado) para ver
   cuántos confirmaron, rechazaron o siguen pendientes.

---

## API Reference

### Auth (módulo `auth`)

Base path: `/api/v1/auth` — público.

#### `POST /api/v1/auth/signup`
Crea un usuario administrador (uso puntual, para dar de alta a quien va a gestionar
las invitaciones).

```json
// Request
{
  "email": "pareja@boda.com",
  "password": "unaClaveSegura123",
  "roleId": "<uuid-del-rol-ADMIN-en-la-tabla-roles>"
}
```

```json
// Response 200
{
  "id": "3f2a1b9c-...-e0d1",
  "email": "pareja@boda.com"
}
```

#### `POST /api/v1/auth/login`
Autentica y devuelve el `access_token`/`refresh_token` como cookies **httpOnly**
(no vienen en el body). El `access_token` dura 15 minutos, el `refresh_token` 7 días.

```json
// Request
{
  "email": "pareja@boda.com",
  "password": "unaClaveSegura123"
}
```

Response `200` sin body, con headers `Set-Cookie: access_token=...` y
`Set-Cookie: refresh_token=...`. Todas las llamadas a `/api/v1/admin/**` deben
incluir esa cookie (el navegador la manda solo si se usó `fetch(..., { credentials: "include" })`).

#### `POST /api/v1/auth/refreshToken`
Renueva el `access_token` usando la cookie `refresh_token`. No requiere body.

---

### Invitaciones públicas (módulo `invitation`, sin autenticación)

Base path: `/api/v1/invitations` — usado por el frontend estático, cualquiera con
el `code` puede consultarlo (es el mismo modelo de seguridad que un link "secreto").

#### `GET /api/v1/invitations/{code}`
Devuelve los datos para renderizar la invitación.

```json
// Response 200
{
  "code": "G7K2QX",
  "displayName": "Familia Pérez",
  "maxGuests": 4,
  "status": "PENDING",
  "confirmedGuests": null,
  "respondedAt": null
}
```

`404` si el código no existe (`{"error": "Invitation not found: G7K2QX"}`).

#### `POST /api/v1/invitations/{code}/rsvp`
Confirma o rechaza asistencia.

```json
// Request (confirma con 3 de los 4 cupos)
{
  "attending": true,
  "confirmedGuests": 3
}
```

```json
// Request (rechaza)
{
  "attending": false
}
```

```json
// Response 200
{
  "code": "G7K2QX",
  "displayName": "Familia Pérez",
  "maxGuests": 4,
  "status": "CONFIRMED",
  "confirmedGuests": 3,
  "respondedAt": "2026-09-18T20:15:00"
}
```

Errores:
- `404` — código inexistente.
- `400` — `confirmedGuests` fuera de rango (`< 1` o `> maxGuests`) al confirmar.
- `409` — se intentó confirmar (`attending: true`) una invitación que ya estaba `DECLINED`.

---

### Invitaciones — administración (módulo `invitation`, requiere rol `ADMIN`)

Base path: `/api/v1/admin/invitations` — requiere la cookie `access_token` de un
usuario con rol `ADMIN` (`401` sin cookie/token inválido, `403` si el rol no es `ADMIN`).

#### `POST /api/v1/admin/invitations`
Crea una invitación individual.

```json
// Request
{ "displayName": "Familia Pérez", "maxGuests": 4 }
```

```json
// Response 200
{
  "id": "3f2a1b9c-...-e0d1",
  "code": "G7K2QX",
  "displayName": "Familia Pérez",
  "maxGuests": 4
}
```

#### `POST /api/v1/admin/invitations/bulk`
Crea varias invitaciones de una sola vez (carga masiva de la lista de invitados).

```json
// Request
{
  "invitations": [
    { "displayName": "Familia Pérez", "maxGuests": 4 },
    { "displayName": "Juan y María", "maxGuests": 2 }
  ]
}
```

```json
// Response 200
[
  { "id": "...", "code": "G7K2QX", "displayName": "Familia Pérez", "maxGuests": 4 },
  { "id": "...", "code": "9H3RTM", "displayName": "Juan y María", "maxGuests": 2 }
]
```

#### `GET /api/v1/admin/invitations`
Lista todas las invitaciones con su estado actual (para armar una tabla de
seguimiento).

```json
// Response 200
[
  {
    "id": "...",
    "code": "G7K2QX",
    "displayName": "Familia Pérez",
    "maxGuests": 4,
    "status": "CONFIRMED",
    "confirmedGuests": 3,
    "respondedAt": "2026-09-18T20:15:00",
    "createdAt": "2026-09-01T10:00:00"
  }
]
```

#### `GET /api/v1/admin/invitations/stats`
Resumen agregado para un dashboard simple.

```json
// Response 200
{
  "totalInvitations": 40,
  "totalInvitedGuests": 120,
  "pendingInvitations": 10,
  "confirmedInvitations": 25,
  "declinedInvitations": 5,
  "confirmedGuests": 78
}
```

---

## Project Structure

```plaintext
└── 📁src/main/java/com/santiagomac
    ├── 📁auth              (login/registro admin, JWT, seguridad)
    │   ├── 📁application/{dto,ports,usecases}
    │   ├── 📁domain/model/{user,session,exceptions}
    │   └── 📁infrastructure/{config,driven_adapter,entry_points}
    └── 📁invitation        (invitaciones + RSVP)
        ├── 📁application/{dto,ports,usecases}
        ├── 📁domain/model/{invitation,exceptions}
        └── 📁infrastructure/{driven_adapter,entry_points}
```

`AuthApplication` (en `com.santiagomac.auth`) sigue siendo la clase `@SpringBootApplication`,
con el escaneo de componentes/entidades/repositorios ampliado explícitamente a
`com.santiagomac` para que recoja también el paquete `invitation`.

---

## Contributors or

If you have any issue o your have some feature request let me know
in the issue page.
