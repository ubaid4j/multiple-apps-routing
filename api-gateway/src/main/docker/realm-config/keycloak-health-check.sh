#!/bin/bash
exec 3<>/dev/tcp/localhost/38218

echo -e "GET /health/ready HTTP/1.1\nhost: localhost:38218\n" >&3

timeout --preserve-status 1 cat <&3 | grep -m 1 status | grep -m 1 UP
ERROR=$?

exec 3<&-
exec 3>&-

exit $ERROR
