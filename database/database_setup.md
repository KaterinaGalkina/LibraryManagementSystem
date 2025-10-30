# Database Structure & Refresh

You can check the database structure in the `refresh_database.sql` file in the current folder.

To refresh the database with the initial data, execute the following commands:

Remove the old database:

```Shell
rm library_info.db
```

Create a new database from the SQL file:

```Shell
sqlite3 library_info.db < refresh_database.sql
```

After running these commands, a fresh library_info.db will be created with all tables and sample data.