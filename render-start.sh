#!/bin/sh
set -eu

if [ -z "${DATABASE_URL:-}" ]; then
  echo "DATABASE_URL is not set. Starting with temporary H2 storage." >&2
  exec java -jar /app/app.jar \
    --spring.datasource.url="jdbc:h2:file:/tmp/productdb" \
    --spring.datasource.username="sa" \
    --spring.datasource.password="" \
    --spring.datasource.driver-class-name="org.h2.Driver"
fi

url_without_scheme="${DATABASE_URL#postgresql://}"
credentials="${url_without_scheme%@*}"
host_and_database="${url_without_scheme#*@}"
username="${credentials%%:*}"
password="${credentials#*:}"
host_and_port="${host_and_database%%/*}"
database="${host_and_database#*/}"
database="${database%%\?*}"

exec java -jar /app/app.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url="jdbc:postgresql://${host_and_port}/${database}" \
  --spring.datasource.username="${username}" \
  --spring.datasource.password="${password}"
