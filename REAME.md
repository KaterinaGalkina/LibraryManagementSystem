# Library Management System

### To compile

```Shell
mkdir -p bin
javac -cp "lib/sqlite-jdbc-3.50.3.0.jar" -d bin $(find src -name "*.java")
```

### To run

```Shell
java -cp "bin:lib/sqlite-jdbc-3.50.3.0.jar" MainApplication
```

### Database

Database structure is described in this [first file](database/database_structure.txt).
And the database itself is in this [second file](database/library_info.db).
