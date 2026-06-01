#!/bin/sh
set -eu

if [ -n "${DATABASE_URL:-}" ]; then
  url_without_scheme="${DATABASE_URL#postgresql://}"
  credentials="${url_without_scheme%@*}"
  host_and_database="${url_without_scheme#*@}"
  username="${credentials%%:*}"
  password="${credentials#*:}"
  host_and_port="${host_and_database%%/*}"
  database="${host_and_database#*/}"
  database="${database%%\?*}"

  export SPRING_DATASOURCE_URL="jdbc:postgresql://${host_and_port}/${database}"
  export SPRING_DATASOURCE_USERNAME="${username}"
  export SPRING_DATASOURCE_PASSWORD="${password}"
fi

exec java -jar /app/app.jar --spring.profiles.active=prod
