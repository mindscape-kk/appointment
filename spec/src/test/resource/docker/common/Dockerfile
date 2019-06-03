FROM mysql:5.7.25

RUN mkdir /init-data-files
ADD bin/load.sh /docker-entrypoint-initdb.d
ADD data /data/
ADD my.cnf /etc/mysql

EXPOSE 3306