-- find all requests made by a given  ip
  select * from logline where ip='192.168.11.231';

-- find IPs that made more than a certain number of requests for a given time period
  SELECT
    ip,
    count(id) as requests
  FROM
    logline
  WHERE
    date BETWEEN '2017-01-01.00:00:00' AND '2017-01-01.23:59:59'
  GROUP BY
    ip
  HAVING
    requests>500
  ORDER BY requests DESC;