# IT Asset Console

A Java (Jakarta EE) servlet web application for looking up IT support records. A technician logs in, lands on a command menu, and queries **users**, **technicians**, or **workstations** by ID — each lookup returns the record plus the related workstation assignments, rendered as HTML and backed by a MySQL database.

## What it is / What it does

The app is a small server-rendered web application built on the Jakarta Servlet API with MySQL persistence. The flow is:

1. **Login** (`index.html` → `Login` servlet) – the user submits an ID and password, which are checked against the `TechLogin` table with a prepared statement. On success they are redirected to the command menu; on failure a "Login Failed" page is shown.
2. **Command menu** (`Console.html`) – links to the three lookup pages.
3. **Lookups** – each page posts an ID to its servlet, which calls a static method on the `Queryable` interface to run the query and return an HTML fragment:
   - **Technicians** (`Techs.html` → `Techs` servlet → `Queryable.queryTechnicians`) – shows the technician's ID/name/role and every workstation assigned to them.
   - **Users** (`Users.html` → `Users` servlet → `Queryable.queryUsers`) – shows the user's ID/name/role and every workstation assigned to them (including the assigned technician).
   - **Workstations** (`Workstations.html` → `Workstations` servlet → `Queryable.queryComputers`) – shows a single computer's ID, OS, floor location, status, and assigned technician.

All database access uses JDBC `PreparedStatement`s. The query logic lives in `Queryable`, which uses Java interface `static` methods as a lightweight utility layer shared by the servlets.

### Database

The schema (`src/java/pashynskyi/DBSPT/schema.sql`) creates the `itassetdb` database with five tables:

- `Techs` / `TechLogin` – technicians (with a generated `FullName` column and a `TechRole` enum) and their login credentials.
- `Users` / `UserLogin` – end users (generated `FullName`, `UserRole` enum) and their login credentials.
- `Workstations` – computers with OS, floor location, an `Online`/`Offline` status enum, and foreign keys to the assigned user (`AssignedUser`) and technician (`AssignedTech`).

`src/java/pashynskyi/DBSPT/data.sql` seeds the tables with sample technicians, users, logins, and workstations so the lookups return data out of the box.

## Tech stack

- **Language:** Java 17
- **Web layer:** Jakarta Servlet 6.0 (annotation-based `@WebServlet` mappings; runs on Tomcat 10/11 or another Jakarta EE 10 web container)
- **Database:** MySQL, accessed via JDBC (MySQL Connector/J 9.4.0)
- **Build:** Apache Ant (NetBeans web project; `build.xml` imports `nbproject/build-impl.xml`)
- **Frontend:** Static HTML pages plus servlet-generated HTML, styled by a single `styles.css`

## Project structure

```
it-asset-console/
├── build.xml                          # Ant build script (NetBeans-generated)
├── nbproject/                         # NetBeans project configuration
│   ├── project.xml                    # Project name, libraries, source roots
│   └── project.properties             # Build settings, Java 17, WAR name, classpath
├── src/
│   ├── conf/MANIFEST.MF
│   └── java/pashynskyi/
│       ├── DBSPT/
│       │   ├── DBConnection.java       # JDBC connection helper (selects itassetdb)
│       │   ├── schema.sql              # Database + table definitions
│       │   └── data.sql                # Sample seed data
│       ├── INTERFACE/
│       │   └── Queryable.java           # Static query methods (login + 3 lookups)
│       └── SERVLETS/
│           ├── Login.java               # /Login  – authenticate against TechLogin
│           ├── Techs.java               # /Techs  – technician lookup
│           ├── Users.java               # /Users  – user lookup
│           └── Workstations.java        # /Workstations – workstation lookup
├── web/
│   ├── index.html                     # Login page
│   ├── Console.html                   # Command menu
│   ├── Techs.html / Users.html / Workstations.html   # Lookup forms
│   ├── styles.css                     # Shared styling
│   ├── META-INF/context.xml           # Tomcat context path (/it-asset-console)
│   └── WEB-INF/web.xml                # Servlet registrations and URL mappings
└── test/                              # (empty test source root)
```

> The servlets are mapped both by `@WebServlet` annotations and in `web.xml`; both point at the same `/Techs`, `/Users`, `/Workstations`, and `/Login` URLs.

## Prerequisites

- JDK 17
- Apache Ant
- A Jakarta EE 10 servlet container (e.g. Apache Tomcat 10 or 11)
- A running MySQL server
- MySQL Connector/J 9.4.0 (`mysql-connector-j-9.4.0.jar`)

## Setup and run

1. **Create and seed the database.** Run the schema, then the data:

   ```bash
   mysql -u root -p < src/java/pashynskyi/DBSPT/schema.sql
   mysql -u root -p < src/java/pashynskyi/DBSPT/data.sql
   ```

   This creates the `itassetdb` database, its tables, and the sample rows.

2. **Configure the database credentials.** Edit `src/java/pashynskyi/DBSPT/DBConnection.java` so `DB_USER` / `DB_PASS` match your MySQL server (defaults are `root` / `root`, host `localhost:3306`, database `itassetdb`).

3. **Provide the JDBC driver.** The Ant classpath references `mysql-connector-j-9.4.0.jar`. Update the `file.reference.mysql-connector-j-9.4.0.jar` path in `nbproject/project.properties` to point at your local copy of the connector.

4. **Build the WAR.** From the project root:

   ```bash
   ant dist
   ```

   This produces `dist/it-asset-console.war`. (Opening the project in NetBeans and using Clean and Build does the same thing.)

5. **Deploy.** Drop the WAR into your servlet container (e.g. Tomcat's `webapps/`). The app deploys under the context path `/it-asset-console`.

6. **Open** `http://localhost:8080/it-asset-console/` and log in with a technician's credentials from `TechLogin` (see `data.sql`).

## Notes

- Login validates the technician ID and password against the `TechLogin` table using a prepared statement; passwords are stored in plain text in the sample data. This is a learning/demo project, not hardened for production use.
- The `test/` source root is present but contains no tests.
