#!/bin/sh

curl http://insurance.test.thread-pilot.int.mindphaser.se/v1/insurances/123456789012 -o result

echo -n '[{"id":1,"type":"PET","monthlyCost":10},{"id":2,"type":"HEALTH","monthlyCost":20},{"id":3,"type":"CAR","registrationNumber":"XYZ123","vehicle":{"id":1,"registrationNumber":"XYZ123","make":"Toyota","model":"Corolla","year":2020},"monthlyCost":30}]' > expected

diff -u expected result
