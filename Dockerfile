FROM postgres:17.2

# Copy the initialization script to Docker container and place it in a directory
COPY init.sql /docker-entrypoint-initdb.d/

# The entry point will automatically execute any scripts in the /docker-entrypoint-initdb.d directory

# Expose PostgreSQL default port
EXPOSE 5432
