# ITI API Testing Project

This is the API testing project of ITI Software Testing internship.
It tests various scenarios on an instance of [PocketBase](https://pocketbase.io).

## The backend

**PocketBase** is an open-source backend solution that runs as a single executable file.

It provides an instant backend with:
- Built-in SQLite database
- Realtime APIs (REST & WebSocket)
- User authentication & file storage out of the box
- Web-based admin dashboard for managing data
- Extendable with custom Go code

On a local environment, it usually runs on `http://localhost:8090/api` as a REST API,
along with the dashboard on `http://localhost:8090/_`.

## Built with

The project uses the following technologies:
- `Maven`: a build tool for Java.
- `TestNG`: a test runner.
- `RestAssured`: for actual API testing.
- `Allure`: for test reporting.
- `Logback`: for logging.

## How to run

Install [Go](https://go.dev) and start the PocketBase server using the following commands:

```
cd pocketbase
go mod tidy
go run . serve
```


After it starts, the Java tests can be run using `mvn clean test` or by using your IDE's test runner.

For `newman`, ensure [Node.js](https://nodejs.org) is installed and use the following command:

```sh
npx newman@latest run project.postman_collection.json
```

### Reports

The generated allure reports can be served as static html files from `target/allure-results`.

## Presentation

The `presentation` directory contains a web-based slide deck showcasing the project’s features and test results.

To view it in a local server, run:

```sh
cd presentation
npm install
npm run dev
```

To export it as a PDF, use the following command:

```sh
npm run export
```
