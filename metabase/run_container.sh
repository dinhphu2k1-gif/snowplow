docker run -d -p 4000:3000 \
  -e "MB_DB_TYPE=mysql" \
  -e "MB_DB_DBNAME=customer-data-platform" \
  -e "MB_DB_PORT=3306" \
  -e "MB_DB_USER=book_shop" \
  -e "MB_DB_PASS=package1107N" \
  -e "MB_DB_HOST=mysqldb" \
  --network customer-data-platform_snowplow \
  --name metabase metabase/metabase
