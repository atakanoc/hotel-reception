# mysql official image
FROM mysql

# add a database
ENV MYSQL_DATABASE hotel

# container startup scripts
COPY create_ci_db.sql/ /docker-entrypoint-initdb.d/
